package com.cellact;


import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;


import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;


public class CreateWallet {

    String NAME_HASH_ADDRESS = "0x198AB4FcDD811f693Ebb08D1f67Baf3c214f1969";

    String mnemonic;
    String privateKey;

    // Connect to Ethereum node
    Mumbai mumbai = new Mumbai();
    Web3j web3j = Web3j.build(new HttpService(mumbai.getRPC()));

    private static final int[] DERIVATION_PATH = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0};

    public CreateWallet() {
        this.mnemonic = generateMnemonic();
        this.privateKey = getPrivateKeyFromMnemonic(this.mnemonic);
    }

    public CreateWallet(String privateKey){
        this.mnemonic = generateMnemonic();
        this.privateKey = privateKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    static String generatePrivateKey(){
        return getPrivateKeyFromMnemonic(generateMnemonic());
    }

    static boolean isValidMnemonic(String mnemonic) {
        return MnemonicUtils.validateMnemonic(mnemonic);
    }

    static String generateMnemonic() {
        byte[] entropy = new byte[16]; // 128 bits
        new SecureRandom().nextBytes(entropy);
        return MnemonicUtils.generateMnemonic(entropy);
    }

    static String getPrivateKeyFromMnemonic(String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, DERIVATION_PATH);
        return Numeric.toHexStringNoPrefix(derivedKeyPair.getPrivateKey());
    }

    public static String getPublicKeyFromPrivateKey(String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        return credentials.getAddress();
    }

    public static Credentials getCredentials(String privateKey) {
        return Credentials.create(privateKey);
    }


    // Takes a message and signs it with the private key of the current wallet
    public String signMessage(String Message){

        Credentials credentials = getCredentials(this.getPrivateKey());

        byte[] messageBytes = Message.getBytes();
        byte[] messageHash = Hash.sha3(messageBytes);

        Sign.SignatureData signature = Sign.signPrefixedMessage(messageHash, credentials.getEcKeyPair());
        String sigHex = Numeric.toHexString(signature.getR()) 
                + Numeric.toHexStringNoPrefix(signature.getS()) 
                + Numeric.toHexStringNoPrefix(new byte[]{signature.getV()[0]});

        return sigHex;
    }

    private String encodeFunction(String functionName, List<Type> inputParameters, List<TypeReference<?>> outputParameters) {
        
        Function function = new Function(
                functionName,
                inputParameters,
                outputParameters
        );

        return FunctionEncoder.encode(function);
        
    }

    public String mintNFT(String _appWAddress, String _userENS) throws IOException {

        // Load your Ethereum wallet credentials
        Credentials credentials = Credentials.create(this.privateKey);

        // Raw Transaction 
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials,
                80001, // Chain ID for Polygon Mumbai Testnet
                new PollingTransactionReceiptProcessor(web3j, 1000, 60)
        );

        List<Type> inputParameters = Arrays.asList(new Utf8String(_appWAddress), new Utf8String(_userENS));
        
        String encodedFunction = encodeFunction(
            "safeMint", // Function name
            inputParameters, // Function input parameters
            Collections.emptyList() // Function return types (empty for a transaction)
        );

        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger valueInWei = new BigInteger("1"); 
        BigInteger estimatedGasLimit = getEstimatedGasForStateChanging(credentials, encodedFunction, valueInWei, gasPrice);

        EthSendTransaction response = transactionManager.sendTransaction(
                gasPrice,
                estimatedGasLimit,
                NAME_HASH_ADDRESS,
                encodedFunction,
                valueInWei 
        );
        
        return response.getTransactionHash();
    }


    BigInteger getEstimatedGasForStateChanging(Credentials credentials, String encodedFunction, BigInteger valueInWei, BigInteger gasPrice) throws IOException {

        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();

        //  BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimitEstimation = BigInteger.valueOf(20000000);        

        // Create the transaction object for the state-changing function
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimitEstimation,
                NAME_HASH_ADDRESS,
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

}
