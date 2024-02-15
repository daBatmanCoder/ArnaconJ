package com.Arnacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

public class CloudFunctions {

    String ens_url = "https://us-central1-arnacon-nl.cloudfunctions.net/get_user_new_ens";
    String get_service_provider_url = "https://us-central1-arnacon-nl.cloudfunctions.net/get_service_providers";

    public CloudFunctions() {}
    
    public String getUserENS(String userAddress) {
        
        String result = "";
    
        try {
            // Prepare JSON data
            String jsonInputString = "{\"user_address\": \"" + URLEncoder.encode(userAddress, "UTF-8") + "\"}";
    
            URL obj = new URL(ens_url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
    
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }
    
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
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

    public String getShopCID(String serviceProvider) {
        String result = "";

        try {
            URL url = new URL(get_service_provider_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    result = response.toString();
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(result);
        for (String key : jsonObject.keySet()) {
            JSONObject serviceProviderObject = jsonObject.getJSONObject(key);
            if (serviceProviderObject.getString("name").equals(serviceProvider)) {
                return serviceProviderObject.getString("cid");
            }
        }

        return result;
    }
}
