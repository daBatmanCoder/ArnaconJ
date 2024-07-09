package com.Arnacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.Web3ServiceBase.ALogger;

import io.reactivex.annotations.Nullable;

public class CloudFunctions {

    private static final    String          MASTER_URL = Config.getDefaultFunctionsURL();
    private static volatile CloudFunctions  CloudFunctions; 

    // Immutable service URLs
    private final String    ensUrl;
    private final String    getServiceProviderUrl;
    private final String    getNetworksUrl;
    private final String    getContractsUrl;
    private final String    sendFcmUrl;
    private final String    sendDirectFcmUrl;
    private final String    sendRegisterAyalaUrl;
    private final String    getCalleeDomainUrl;
    private final String    registerNewProductUrl;
    public  final String    sendStripeUrl;
    private final ALogger   logger;

    private CloudFunctions(ALogger logger) {

        this.logger                     = logger;
        
        String     urls                 = requestGetFromCloud(MASTER_URL, false);
        JSONObject urlsObject           = new JSONObject(urls);

        this.ensUrl                     = urlsObject.getString("ens_url");
        this.getServiceProviderUrl      = urlsObject.getString("get_service_provider_url");
        this.getNetworksUrl             = urlsObject.getString("get_networks_url");
        this.getContractsUrl            = urlsObject.getString("get_contracts_url");
        this.sendStripeUrl              = urlsObject.getString("send_stripe");
        this.sendFcmUrl                 = urlsObject.getString("send_secure_fcmToken");
        this.sendDirectFcmUrl           = urlsObject.getString("send_fcmToken");
        this.sendRegisterAyalaUrl       = urlsObject.getString("register_ayala");
        this.getCalleeDomainUrl         = urlsObject.getString("get_callee_domain");
        this.registerNewProductUrl      = urlsObject.getString("register_new_product");
    }


    public static CloudFunctions getCloudFunctions( 
        ALogger logger
    ) {

        if(CloudFunctions == null){
            CloudFunctions = new CloudFunctions(logger);
        }

        return CloudFunctions;
    }

    private String requestGetFromCloud(
        String RequestURL,
        boolean lowerCase
    ) {

        String result = "";

        try {
            
            URL url = new URL(RequestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty(
                "Accept", 
            "application/json"
            );

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                        con.getInputStream(), 
                        "utf-8")
                        )
                    ) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    if(lowerCase){
                        String result1 = response.toString();
                        result = result1.toLowerCase();
                    }else{
                        result = response.toString();
                    }
                    
                }
            } else {
                logger.debug("GET request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private String requestPostToCloud(
        String RequestURL, 
        String jsonInputString
    ) {

        String result = "";

        try {
            URL obj = new URL(RequestURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                        con.getInputStream(), 
                        "UTF-8")
                        )
                    ) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    result = response.toString();
                }
            } else {
                logger.debug("POST request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public String[] getServiceProviderList(){

        String serviceProviders = requestGetFromCloud(
            getServiceProviderUrl, 
            false
        );

        JSONObject serviceProvidersObject = new JSONObject(serviceProviders);
        String[] serviceProvidersArray = new String[serviceProvidersObject.length()];
        int i = 0;

        for (String key : serviceProvidersObject.keySet()) {
            JSONObject serviceProviderObject = serviceProvidersObject.getJSONObject(key);

            serviceProvidersArray[i] = serviceProviderObject.getString("name");
            i++;
        }
        
        return serviceProvidersArray;
    }
     
    public String getUserENS(
        String userAddress, 
        @Nullable String customerId
    ) {
        try {

            // Start constructing the JSON input string with the user address
            StringBuilder jsonBuilder = new StringBuilder("{\"user_address\": \"" + URLEncoder.encode(userAddress, "UTF-8") + "\"");
    
            // If a customer_id is provided, add it to the JSON input string
            if (customerId != null && !customerId.isEmpty()) {
                jsonBuilder.append(", \"customer_id\": \"").append(URLEncoder.encode(customerId, "UTF-8")).append("\"");
            }
    
            // Close the JSON input string
            jsonBuilder.append("}");
    
            // Send the POST request with the constructed JSON input string
            return requestPostToCloud(ensUrl, jsonBuilder.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String getShopCID(
        String serviceProvider
    ) {

        String result = requestGetFromCloud(
            getServiceProviderUrl, 
            false
        );
        
        JSONObject jsonObject = new JSONObject(result);

        for (String key : jsonObject.keySet()) {
            JSONObject serviceProviderObject = jsonObject.getJSONObject(key);
            if (serviceProviderObject.getString("name").equals(serviceProvider)) {
                return serviceProviderObject.getString("cid");
            }
        }

        return result;
    }

    public JSONObject getNetwork(
        String InetworkName
    ) {

        String result = requestGetFromCloud(getNetworksUrl, true);

        String networkName = InetworkName.toLowerCase();

        JSONObject config = new JSONObject(result);
        JSONObject networkConfig = config.getJSONObject(networkName);

        return networkConfig;
    }

    public String getContractAddress(
        String contractName
    ) {

        String result = requestGetFromCloud(getContractsUrl, false);
        JSONObject config = new JSONObject(result);
        String contractAddress = config.getString(contractName);

        return contractAddress;
    }

    public String sendFCM(
        String fcm_token, 
        String fcm_signed, 
        String ens
    ) {

        String jsonInputString = "{\"tokens\": " + fcm_token + ", \"tokens_signed\": \"" + fcm_signed + "\", \"ens\": \"" + ens + "\"}";
        return requestPostToCloud(sendFcmUrl, jsonInputString);
    }

    public void sendDirectFCM(
        String fcmTokenJson, 
        String fcm_signed
    ) {

        String jsonInputString = "{\"tokens\": " + fcmTokenJson + ", \"tokens_signed\": \"" + fcm_signed + "\"}";
        requestPostToCloud(sendDirectFcmUrl, jsonInputString);
    }

    public void registerAyala(
        String data, 
        String signedData, 
        String ens
    ) {

        String jsonInputString = "{\"data\": \"" + data + "\", \"signedData\": \"" + signedData + "\", \"ens\": \"" + ens + "\"}";
        requestPostToCloud(sendRegisterAyalaUrl, jsonInputString);
    }

    public void registerAyala(
        String data, 
        String signedData, 
        String ens, 
        String serviceProviderName
    ) {

        String jsonInputString = "{\"data\": \"" + data + "\", \"signedData\": \"" + signedData + "\", \"ens\": \"" + ens + "\", \"sp\": \"" + serviceProviderName + "\"}";
        requestPostToCloud(sendRegisterAyalaUrl, jsonInputString);
    }

    public String getCalleeDomain(
        String callee
    ) {

        String jsonInputString = "{\"ens\": \"" + callee + "\"}";
        return requestPostToCloud(getCalleeDomainUrl, jsonInputString);
    }

    public void registerNewProduct(
        String data_to_sign, 
        String data_signed, 
        String publicKey, 
        String owner_sign
    ) {

        String jsonInputString = "{\"data\": \"" + data_to_sign + "\", \"data_signed\": \"" + data_signed + "\", \"address\": \"" + publicKey + "\", \"owner_sign\": \"" + owner_sign + "\"}";
        requestPostToCloud(registerNewProductUrl, jsonInputString);
    }
}
