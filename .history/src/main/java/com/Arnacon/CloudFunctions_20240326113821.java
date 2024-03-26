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

public class CloudFunctions {

    private String MasterURL = "https://us-central1-arnacon-nl.cloudfunctions.net/Functions";

    private String ens_url;
    private String get_service_provider_url;
    private String get_networks_url;
    private String get_contracts_url;
    public String send_stripe_url;
    private String send_fcm_url;


    public CloudFunctions() {

        String urls = RequestGetFromCloud(MasterURL, false);
        JSONObject urlsObject = new JSONObject(urls);

        this.ens_url = urlsObject.getString(                    "ens_url");
        this.get_service_provider_url = urlsObject.getString(   "get_service_provider_url");
        this.get_networks_url = urlsObject.getString(           "get_networks_url");
        this.get_contracts_url = urlsObject.getString(          "get_contracts_url");
        this.send_stripe_url = urlsObject.getString(            "send_stripe");
        this.send_fcm_url = urlsObject.getString(               "send_secure_fcmToken");
    }

    private String RequestGetFromCloud(String RequestURL,boolean lowerCase) {
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
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private String RequestPostToCloud(String RequestURL, String jsonInputString) {
        String result = "";

        try {
            URL obj = new URL(RequestURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    result = response.toString();
                }
            } else {
                System.out.println("POST request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String[] getServiceProviderList(){
        String serviceProviders = RequestGetFromCloud(get_service_provider_url, false);
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
    
    public String getUserENS(String userAddress) {
        try{
            String jsonInputString = "{\"user_address\": \"" + URLEncoder.encode(userAddress, "UTF-8") + "\"}";
            return RequestPostToCloud(ens_url, jsonInputString);
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return "Error"; 
        }
    }

    public String getShopCID(String serviceProvider) {
        String result = RequestGetFromCloud(get_service_provider_url, false);
        
        JSONObject jsonObject = new JSONObject(result);

        for (String key : jsonObject.keySet()) {
            JSONObject serviceProviderObject = jsonObject.getJSONObject(key);
            if (serviceProviderObject.getString("name").equals(serviceProvider)) {
                return serviceProviderObject.getString("cid");
            }
        }

        return result;
    }

    public JSONObject getNetwork(String InetworkName) {

        String result = RequestGetFromCloud(get_networks_url, true);

        String networkName = InetworkName.toLowerCase();

        JSONObject config = new JSONObject(result);
        JSONObject networkConfig = config.getJSONObject(networkName);

        return networkConfig;
    }

    public String getContractAddress(String contractName) {

        String result = RequestGetFromCloud(get_contracts_url, false);

        JSONObject config = new JSONObject(result);
        String contractAddress = config.getString(contractName);

        return contractAddress;
    }

    public void sendFCM(String fcm_token, String fcm_signed, String ens) {
        String jsonInputString = "{\"tokens\": " + fcm_token + ", \"tokens_signed\": \"" + fcm_signed + "\", \"ens\": \"" + ens + "\"}";
        RequestPostToCloud(send_fcm_url, jsonInputString);
    }

}
