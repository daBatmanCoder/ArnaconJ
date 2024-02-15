package com.Arnacon;

import com.Config.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;


public class Web3AJ {

    public Wallet wallet;
    Network network; // Ethereum / Polygon / Binance Smart Chain
    Web3j web3j;
    Contracts contracts;


    // Constructor with no private key
    public Web3AJ(Network _network) {
        this.wallet = new Wallet();
        commonConstructor(_network);
    }

    public Web3AJ(Network _network, String privateKey) {
        this.wallet = new Wallet(privateKey);
        commonConstructor(_network);
    }

    private void commonConstructor(Network _network){
        this.network = _network;
        this.web3j = Web3j.build(new HttpService(this.network.getRPC()));
        contracts = new Contracts();
    }

    // Takes a message and signs it with the private key of the current wallet
    public String signMessage(String Message){

        Credentials credentials = wallet.getCredentials();

        byte[] messageBytes = Message.getBytes();
        byte[] messageHash = Hash.sha3(messageBytes);

        Sign.SignatureData signature = Sign.signPrefixedMessage(messageHash, credentials.getEcKeyPair());
        String sigHex = Numeric.toHexString(signature.getR()) 
                + Numeric.toHexStringNoPrefix(signature.getS()) 
                + Numeric.toHexStringNoPrefix(new byte[]{signature.getV()[0]});

        return sigHex;
    }

    // Encodes a function call to be used in a transaction
    private String encodeFunction(String functionName, List<Type> inputParameters, List<TypeReference<?>> outputParameters) {
        
        Function function = new Function(
                functionName,
                inputParameters,
                outputParameters
        );

        return FunctionEncoder.encode(function);
        
    }

    // Mint an NFT - for the user wallet and ENS
    public String mintNFT(String _userENS) throws IOException {

        // Load your Ethereum wallet credentials
        Credentials credentials = Credentials.create(this.wallet.getPrivateKey());

        // Raw Transaction 
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials,
                this.network.getChainID(), // Chain ID for Polygon Mumbai Testnet
                new PollingTransactionReceiptProcessor(web3j, 1000, 60)
        );

        List<Type> inputParameters = Arrays.asList(new Utf8String(this.wallet.getPublicKey()), new Utf8String(_userENS));
        
        String encodedFunction = encodeFunction(
            "safeMint", // Function name
            inputParameters, // Function input parameters
            Collections.emptyList() // Function return types (empty for a transaction)
        );

        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger valueInWei = new BigInteger("0"); 
        BigInteger estimatedGasLimit = getEstimatedGasForStateChanging(credentials, encodedFunction, valueInWei, gasPrice, contracts.NAME_HASH_ADDRESS);

        EthSendTransaction response = transactionManager.sendTransaction(
                gasPrice,
                estimatedGasLimit,
                contracts.NAME_HASH_ADDRESS,
                encodedFunction,
                valueInWei
        );
        
        return response.getTransactionHash();
    }

    // Buys our ENS (for web2 users)
    public String buyENS(String _userENS) throws IOException {

            // Load your Ethereum wallet credentials
            Credentials credentials = Credentials.create(this.wallet.getPrivateKey());

            // Raw Transaction 
            TransactionManager transactionManager = new RawTransactionManager(
                    web3j,
                    credentials,
                    this.network.getChainID(), // Chain ID for Polygon Mumbai Testnet
                    new PollingTransactionReceiptProcessor(web3j, 1000, 60)
            );

            List<Type> inputParameters = Arrays.asList(new Address(this.wallet.getPublicKey()), new Utf8String(_userENS));
            
            String encodedFunction = encodeFunction(
                "safeMint", // Function name
                inputParameters, // Function input parameters
                Collections.emptyList() // Function return types (empty for a transaction)
            );

            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();

            // Need to change this to MATIC (network currency let's say.)
            BigInteger valueInWei = new BigInteger("0"); 
            
            BigInteger estimatedGasLimit = getEstimatedGasForStateChanging(credentials, encodedFunction, valueInWei, gasPrice,contracts.W_ENS_ADDRESS);

            EthSendTransaction response = transactionManager.sendTransaction(
                    gasPrice,
                    estimatedGasLimit,
                    contracts.W_ENS_ADDRESS,
                    encodedFunction,
                    valueInWei 
            );
            
            return response.getTransactionHash();
        }
    
    // Helper function to estimate the gas limit for a state-changing function
    BigInteger getEstimatedGasForStateChanging(Credentials credentials, String encodedFunction, BigInteger valueInWei, BigInteger gasPrice, String contractAddress) throws IOException {

        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();

        //  BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimitEstimation = BigInteger.valueOf(20000000);        

        // Create the transaction object for the state-changing function
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimitEstimation,
                contractAddress,
                valueInWei,//BigInteger.ZERO, // Value in wei to send with the transaction
                encodedFunction
        );

        Transaction transaction = Transaction.createFunctionCallTransaction(
                credentials.getAddress(),
                rawTransaction.getNonce(),
                rawTransaction.getGasPrice(),
                rawTransaction.getGasLimit(),
                rawTransaction.getTo(),
                rawTransaction.getValue(),
                rawTransaction.getData()
        );


        // Estimate the gas limit for the transaction
        EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();

        BigInteger estimatedGasLimit;

        if (ethEstimateGas.hasError()) {
            estimatedGasLimit = BigInteger.valueOf(2000000); // Fallback gas limit
        } else {
            estimatedGasLimit = ethEstimateGas.getAmountUsed();
        }
        
        return estimatedGasLimit;
    }

    public BigDecimal checkBalance(String publicKey) throws IOException {
        
        EthGetBalance balance = web3j.ethGetBalance(publicKey, DefaultBlockParameterName.LATEST).send();
        BigInteger big_wei = balance.getBalance();
        // Optionally, convert the balance to Ether
        BigDecimal wei = new BigDecimal(big_wei);
        BigDecimal precise_balance = wei.divide(new BigDecimal("1000000000000000000")); 
        
        return precise_balance;
    }

    public static String fetchStoreFromIPFS(String cid) throws Exception {
        // Use a public IPFS gateway to fetch the content. You can also use a local IPFS node if you have one running.
        String ipfsGateway = "https://ipfs.io/ipfs/";
        String ipfsLink = ipfsGateway + cid;
        URL url = new URL(ipfsLink);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check for successful response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            return content.toString(); // Returns the content of the file
        } else {
            throw new Exception("Failed to fetch content from IPFS. Response code: " + responseCode);
        }
    }

}
