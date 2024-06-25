package com.Arnacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Random;
import java.util.Base64;

import java.time.Instant;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import com.Web3ServiceBase.ADataSaveHelper;
import com.Web3ServiceBase.ALogger;
import com.Web3ServiceBase.ANetwork;
import com.ABI.HLUI;
// import com.Web3ServiceBase.ANetwork;
import com.Web3ServiceBase.AWeb3AJ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;


public class Web3AJ extends AWeb3AJ{
    
    Web3j           web3j;
    String          freeName = "ANONYMOUS";
    ANetwork        network; // Ethereum / Polygon / Binance Smart Chain


    public Web3AJ(
        ADataSaveHelper dataSaveHelper, 
        ALogger logger
    ) {
        
        super(dataSaveHelper, logger);
        network = new Network(logger);
        
        String privateKey = dataSaveHelper.getPreference("privateKey", null);

        if (privateKey != null){
            updateWallet(new Wallet(privateKey));
        }
        else{
            updateWallet(new Wallet());
            dataSaveHelper.setPreference("privateKey", this.wallet.getPrivateKey());
        }

        getFreeProduct();
    }

    public String getXData(){

        String uuid = UUID.randomUUID().toString();
        long timestamp = Instant.now().toEpochMilli();
        String xdata = uuid + ":" + timestamp;
        return xdata;
    }

    public String getXSign(String data){
        return compressXSign(signMessage(data));
    }

    public String compressXSign(String hexString) {

        if (hexString.startsWith("0x")) {
            hexString = hexString.substring(2);
        }
        
        // Convert the hexadecimal string to bytes
        byte[] bytes = hexToBytes(hexString);
        
        // Base64 encode the bytes
        String base64String = Base64.getEncoder().encodeToString(bytes);
        
        return base64String;
    }
    
    private static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public String getPublicKey(){
        return this.wallet.getPublicKey();
    }

    // Takes a message and signs it with the private key of the current wallet
    public String signMessage(
        String Message
    ){
        String prefix = "\u0019Ethereum Signed Message:\n" + Message.length();
        String prefixedMessage = prefix + Message;

        Credentials credentials = wallet.getCredentials();

        byte[] messageBytes = prefixedMessage.getBytes();

        Sign.SignatureData signature = Sign.signMessage(messageBytes, credentials.getEcKeyPair());
        logger.debug("Signature: " + Numeric.toHexString(signature.getR()));
        String sigHex = Numeric.toHexString(signature.getR()) 
                + Numeric.toHexStringNoPrefix(signature.getS()) 
                + Numeric.toHexStringNoPrefix(new byte[]{signature.getV()[0]});

        return sigHex;
    }

    private String signMessageWithNewWallet(
        String Message,
        String privateKey
    ){
        String prefix = "\u0019Ethereum Signed Message:\n" + Message.length();
        String prefixedMessage = prefix + Message;

        Wallet wallet = new Wallet(privateKey);
        Credentials credentials = wallet.getCredentials();

        byte[] messageBytes = prefixedMessage.getBytes();

        Sign.SignatureData signature = Sign.signMessage(messageBytes, credentials.getEcKeyPair());
        // logger.debug("Signature: " + Numeric.toHexString(signature.getR()));
        String sigHex = Numeric.toHexString(signature.getR()) 
                + Numeric.toHexStringNoPrefix(signature.getS()) 
                + Numeric.toHexStringNoPrefix(new byte[]{signature.getV()[0]});

        return sigHex;
    }

    // Encodes a function call to be used in a transaction
    private String encodeFunction(
        String functionName,
        @SuppressWarnings("rawtypes") List<Type> inputParameters,
        List<TypeReference<?>> outputParameters
    ) {
        
        Function function = new Function(
                functionName,
                inputParameters,
                outputParameters
        );

        return FunctionEncoder.encode(function);
        
    }

    // Mint an NFT - for the user wallet and ENS
    String mintNFT(
        String _userENS
    ) throws IOException {

        // Load your Ethereum wallet credentials
        Credentials credentials = Credentials.create(this.wallet.getPrivateKey());

        // Raw Transaction 
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials,
                network.getChainID(),
                new PollingTransactionReceiptProcessor(web3j, 1000, 60)
        );

        @SuppressWarnings("rawtypes")
        List<Type> inputParameters = Arrays.asList(new Utf8String(this.wallet.getPublicKey()), new Utf8String(_userENS));
        
        String encodedFunction = encodeFunction(
            "safeMint", // Function name
            inputParameters, // Function input parameters
            Collections.emptyList() // Function return types (empty for a transaction)
        );

        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger valueInWei = new BigInteger("0"); 
        BigInteger estimatedGasLimit = getEstimatedGasForStateChanging(
            credentials, 
            encodedFunction, 
            valueInWei, 
            gasPrice, 
            Utils.getContracts(logger).NAME_HASH_ADDRESS
        );

        EthSendTransaction response = transactionManager.sendTransaction(
                gasPrice,
                estimatedGasLimit,
                Utils.getContracts(logger).NAME_HASH_ADDRESS,
                encodedFunction,
                valueInWei
        );
        
        return response.getTransactionHash();
    }

    String redeemGiftcard(
        String _userENS
    ) throws IOException {

        // Load your Ethereum wallet credentials
        Credentials credentials = Credentials.create(this.wallet.getPrivateKey());

        // Raw Transaction 
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials,
                network.getChainID(), 
                new PollingTransactionReceiptProcessor(web3j, 1000, 60)
        );

        @SuppressWarnings("rawtypes")
        List<Type> inputParameters = Arrays.asList(new Utf8String(this.wallet.getPublicKey()), new Utf8String(_userENS));
        
        String encodedFunction = encodeFunction(
            "safeMint", // Function name
            inputParameters, // Function input parameters
            Collections.emptyList() // Function return types (empty for a transaction)
        );

        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger valueInWei = new BigInteger("0"); 
        BigInteger estimatedGasLimit = getEstimatedGasForStateChanging(
            credentials, 
            encodedFunction, 
            valueInWei, 
            gasPrice, 
            Utils.getContracts(logger).NAME_HASH_ADDRESS
        );

        EthSendTransaction response = transactionManager.sendTransaction(
                gasPrice,
                estimatedGasLimit,
                Utils.getContracts(logger).NAME_HASH_ADDRESS,
                encodedFunction,
                valueInWei
        );
        
        return response.getTransactionHash();
    }

    // Buys our ENS (for web2 users)
    String buyENS(
        String _userENS
    ) throws IOException {

            // Load your Ethereum wallet credentials
            Credentials credentials = Credentials.create(this.wallet.getPrivateKey());

            // Raw Transaction 
            TransactionManager transactionManager = new RawTransactionManager(
                    web3j,
                    credentials,
                    network.getChainID(), // Chain ID for Polygon Mumbai Testnet
                    new PollingTransactionReceiptProcessor(web3j, 1000, 60)
            );

            @SuppressWarnings("rawtypes")
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
            
            BigInteger estimatedGasLimit = getEstimatedGasForStateChanging(
                credentials,
                encodedFunction, 
                valueInWei, 
                gasPrice,
                Utils.getContracts(logger).W_ENS_ADDRESS
            );

            EthSendTransaction response = transactionManager.sendTransaction(
                    gasPrice,
                    estimatedGasLimit,
                    Utils.getContracts(logger).W_ENS_ADDRESS,
                    encodedFunction,
                    valueInWei 
            );
            
            return response.getTransactionHash();
        }
    
    // Helper function to estimate the gas limit for a state-changing function
    BigInteger getEstimatedGasForStateChanging(
        Credentials credentials,
        String encodedFunction, 
        BigInteger valueInWei,
        BigInteger gasPrice,
        String contractAddress
    ) throws IOException {

        BigInteger nonce = web3j.ethGetTransactionCount(
            credentials.getAddress(), 
            DefaultBlockParameterName.LATEST
        ).send().getTransactionCount();

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

    public BigDecimal checkBalance(
        String publicKey
    ) throws IOException {
        
        EthGetBalance balance = web3j.ethGetBalance(publicKey, DefaultBlockParameterName.LATEST).send();
        BigInteger big_wei = balance.getBalance();
        // Optionally, convert the balance to Ether
        BigDecimal wei = new BigDecimal(big_wei);
        BigDecimal precise_balance = wei.divide(new BigDecimal("1000000000000000000")); 
        
        return precise_balance;
    }


    public String fetchStore() throws Exception { // This implementation can be anything but for us it's going to be an IPFS json

        String serviceProviderName = dataSaveHelper.getPreference("serviceProviderName", null);
        logger.debug("Service Provider: " + serviceProviderName);

        String cid = Utils.getCloudFunctions(logger).getShopCID(serviceProviderName);

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

            dataSaveHelper.setPreference("store", content.toString());

            return content.toString(); // Returns the content of the file
        } else {
            throw new Exception("Failed to fetch content from IPFS. Response code: " + responseCode);
        }
    }

    public String getPaymentURL(
        String packageNum,
        String successDP,
         String cancelDP
    ) {
        return Utils.getPaymentURL(
            this.wallet.getPublicKey(),
            packageNum,
            dataSaveHelper.getPreference("store", packageNum), 
            successDP, 
            cancelDP,
            dataSaveHelper,
            logger
        );
    }

    public void sendDirectFCM(
        String fcm_token
    ){
        try {
            String sendTokensFlag = dataSaveHelper.getPreference("tokensSent", "false");
            if (sendTokensFlag == "true"){
                throw new RuntimeException("Error: Tokens already sent");
            }
            String fcmTokenJson = "{\"fcm_token\": \"" + fcm_token + "\"}";
            String fcm_signed = signMessage(fcmTokenJson);
            Utils.getCloudFunctions(logger).sendDirectFCM(fcmTokenJson, fcm_signed);
            dataSaveHelper.setPreference("tokensSent", "true");
        } catch(Exception e){
            throw new RuntimeException("Error: " + e);
        }
        
    }

    public void sendFCMToken(String fcm_token){
        try{

            String sendTokensFlag = dataSaveHelper.getPreference("tokensSent", "false");
            if (sendTokensFlag == "true"){
                throw new RuntimeException("Error: Tokens already sent");
            }

            String ensJsonString = "";
            String ens = "";
            
            while (ensJsonString.isEmpty()) {

                ensJsonString = getENS();
                if (ensJsonString == null){
                    ensJsonString = "";
                }
            }

            // Parse the JSON string to a JSONArray
            JSONArray ensArray = new JSONArray(ensJsonString);

            if (ensArray != null && ensArray.length() > 0) {
                // Select a random ENS from the array
                int randomIndex = new Random().nextInt(ensArray.length()); // Get a random index
                ens = ensArray.getString(randomIndex); // Use the random index to select an ENS
                dataSaveHelper.setPreference("randomENS", ens);
            }

            String fcmTokenJson = "{\"fcm_token\": \"" + fcm_token + "\"}";
            String fcm_signed = signMessage(fcmTokenJson);

            String result = Utils.getCloudFunctions(logger).sendFCM(fcmTokenJson, fcm_signed, ens);
            if (result.equals("False")){
                throw new RuntimeException("Error: FCM not sent");
            }
            dataSaveHelper.setPreference("tokensSent", "true");

        }
        catch(Exception e) {
            throw new RuntimeException("Error: " + e);
        }
    }

    public void sendFCM(
        String fcm_token
    ) {
        sendFCMToken(fcm_token);
        String ens = dataSaveHelper.getPreference("randomENS", null);
        
        if (ens == null){
            throw new RuntimeException("Error: ENS not found");
        }
        logger.debug("ENS: " + ens);

        registerAyala(ens);
    }
    
    public void registerAyala(String userENS) {
        try{
            
            String randomData = getXData();
            String signedData = signMessage(randomData);

            Utils.getCloudFunctions(logger).registerAyala(randomData, signedData, userENS);
        }
        catch(Exception e) {
            throw new RuntimeException("Error: " + e);
        }
    }

    public void registerAyala(String userENS, String serviceProviderName) {
        try{

            String someData = getXData();
            String signedData = signMessage(someData);
            
            Utils.getCloudFunctions(logger).registerAyala(someData, signedData, userENS, serviceProviderName);
        }
        catch(Exception e) {
            throw new RuntimeException("Error: " + e);
        }
    }

    public String[] getServiceProviderList(){

        return Utils.getCloudFunctions(logger).getServiceProviderList();
    }

    public void setServiceProvider(
        String serviceProviderName
    ) {
        dataSaveHelper.setPreference("serviceProviderName", serviceProviderName);
    }

    public String getENS() {

        String ensList = getSavedENSList();

        if ( ensList != null && !ensList.isEmpty()){
            return ensList;
        }

        String ens = Utils.getCloudFunctions(logger).getUserENS(this.wallet.getPublicKey(), null);
        if (ens.equals("Error")){
            return null;
        }
        else{
            dataSaveHelper.setPreference("ensList", ens);
        }
        return ens;
    }

    public String getENS(String customerID) {

        String ens = Utils.getCloudFunctions(logger).getUserENS(this.wallet.getPublicKey(),customerID);
        if (ens.equals("Error")){
            return null;
        }
        return ens;
    }

    public String getSavedENSList(){
        return dataSaveHelper.getPreference("ensList", null);
    }

    public String getCalleeDomainCloud(String callee) {

        return Utils.getCloudFunctions(logger).getCalleeDomain(callee);
    }

    public String getCalleeDomain(String callee) throws Exception {

        boolean isNumeric = callee.matches("^\\d+$");
        
        if(isNumeric){
            return getGSMDomain(callee);
        }
        

        Web3j web3j = Web3j.build(new HttpService(this.network.getRPC()));

        HLUI contractHLUI = HLUI.load(
                Contracts.getContracts(logger).getHLUI(),
                web3j,
                new ReadonlyTransactionManager(web3j, this.wallet.getPublicKey()),  // For view functions, no credentials are needed
                new DefaultGasProvider()
        );

        String domain;

        try{
            domain = contractHLUI.getServiceProviderDomain(callee).send();
        } catch (Exception e){
            throw new RuntimeException("Error: " + e);
        }

        return domain;
    }

    public void setGSMDomain(String gsm, String domain){

        if (!gsm.matches("\\d+")) {
            throw new RuntimeException("Error: Invalid GSM");
        } 

        String gsmValue = dataSaveHelper.getPreference(gsm, "rickrolled");

        if (!gsmValue.equals("rickrolled")){
            throw new RuntimeException("GSM Already setted up.");
        }

        dataSaveHelper.setPreference(gsm, domain);
    }

    public String getGSMDomain(String gsm) {

        if (!gsm.matches("\\d+")) {
            throw new RuntimeException("Error: Invalid GSM");
        } 

        String serviceProviderOfGsm = dataSaveHelper.getPreference(gsm, null);
        
        if (serviceProviderOfGsm == null){
            throw new RuntimeException("Error: GSM not found");
        }
        return serviceProviderOfGsm;
    }

    public String getCurrentProduct() {
        return dataSaveHelper.getPreference("currentProduct", null);
    }

    public void setCurrentProduct(String currentProductChoosed) {

        // Check if the currentProductChoosed is valid- meaning if it one of the ens in the ensList
        String ensList = getSavedENSList();
        if (ensList != null && !ensList.isEmpty()){
            if (!ensList.contains(currentProductChoosed)){
                throw new RuntimeException("Error: Invalid ENS");
            }
        }
        else{
            throw new RuntimeException("Error: ENS List not found");
        }
        dataSaveHelper.setPreference("currentProduct", currentProductChoosed);
    }

    private byte[] decrypt(byte[] ciphertext, String password) throws Exception {
        SecretKeySpec secretKey = deriveKeyFromPassword(password);
        logger.debug(secretKey.toString());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(ciphertext);
    }

    private SecretKeySpec deriveKeyFromPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] key = sha256.digest(password.getBytes(StandardCharsets.UTF_8));
        key = Arrays.copyOf(key, 16); // Use only the first 128 bits
        return new SecretKeySpec(key, "AES");
    }

    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    // Decrypts the data, registers the new product and returns the item
    public String updateNewProduct(String password, String ciphertextHex) {

        try {
            byte[] ciphertext = hexStringToByteArray(ciphertextHex);
            byte[] decryptedData = decrypt(ciphertext, password);

            String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
            logger.debug(decryptedString);
            JSONObject jsonObject = new JSONObject(decryptedString);
            String private_key = jsonObject.getString("private_key");
            String item;
            String data_to_sign;
            Boolean isCellENS = false;
            String serviceProviderOfENS = "";
            long timestamp = Instant.now().toEpochMilli();

            if (jsonObject.has("ens")){
                item = jsonObject.getString("ens");
                if (jsonObject.has("sp")){
                    isCellENS = true;
                    logger.debug("ajshdjasdkasdhjakshdka");
                    serviceProviderOfENS = jsonObject.getString("sp");
                }

                data_to_sign = item + ":" + timestamp;

            }else {
                item = jsonObject.getString("gsm");
                String serviceProviderOfGsm = jsonObject.getString("SP");
                data_to_sign = item + ":" + serviceProviderOfGsm + ":" + timestamp;
                dataSaveHelper.setPreference(item, serviceProviderOfGsm);
            }

            String owner_signed = signMessage(data_to_sign);
            
            String data_signed = signMessageWithNewWallet(data_to_sign ,private_key);

            Utils.getCloudFunctions(logger).registerNewProduct(data_to_sign, data_signed, this.wallet.getPublicKey(), owner_signed);
            
            if (isCellENS){
                saveENSItem(item, serviceProviderOfENS);
            } else{
                saveENSItem(item);
            }

            return item;

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e);
        }
    }

    // Save the ENS item to the list of ENS items - in reverse order
    public void saveENSItem(String item) {

        boolean isNumeric = item.matches("^\\d+$");
        String ensListJsonStr = getSavedENSList();
        JSONArray ensListArray;
        
        try {

            if (!isNumeric && !item.equals(freeName)){
                registerAyala(item);
            }

            if (ensListJsonStr != null && !ensListJsonStr.isEmpty()) {
                ensListArray = new JSONArray(ensListJsonStr);
            } else {
                ensListArray = new JSONArray();
            }

            if(item.equals(freeName)){
                String alreadyCalled = dataSaveHelper.getPreference("free", "nope");
                if (alreadyCalled.equals("nope")){
                    item = freeName;
                    dataSaveHelper.setPreference("free", freeName);
                }
                else {
                    logger.debug("Already have a free product.");
                    return;
                }
            } 

            Utils.addItem(ensListArray, item);

            dataSaveHelper.setPreference("ensList", ensListArray.toString());

            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveENSItem(String item, String serviceProviderName) {

        boolean isNumeric = item.matches("^\\d+$");
        String ensListJsonStr = getSavedENSList();
        JSONArray ensListArray;
        
        try {

            if (!isNumeric && !item.equals(freeName)){
                registerAyala(item, serviceProviderName);
            }

            if (ensListJsonStr != null && !ensListJsonStr.isEmpty()) {
                ensListArray = new JSONArray(ensListJsonStr);
            } else {
                ensListArray = new JSONArray();
            }

            if(item.equals(freeName)){
                String alreadyCalled = dataSaveHelper.getPreference("free", "nope");
                if (alreadyCalled.equals("nope")){
                    item = freeName;
                    dataSaveHelper.setPreference("free", freeName);
                }
                else {
                    logger.debug("Already have a free product.");
                    return;
                }
            } 

            if(isNumeric){
                setGSMDomain(item, serviceProviderName);
            }

            Utils.addItem(ensListArray, item);

            dataSaveHelper.setPreference("ensList", ensListArray.toString());

            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFreeProduct(){
        saveENSItem(freeName);
    }

}

