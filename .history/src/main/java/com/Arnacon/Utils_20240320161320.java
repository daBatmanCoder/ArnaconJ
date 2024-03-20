package com.Arnacon;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject; // Make sure to include the JSON library in your project

public class Utils {

    public static CloudFunctions CloudFunctions = new CloudFunctions();
    public static Contracts Contracts = new Contracts();

    static String PAYMENT_DEEPLINK_OK = "https://success-java.vercel.app/";
    static String PAYMENT_DEEPLINK_NOK = "https://failure-java.vercel.app/";

    static String redirectURL = "https://redirect-generation.vercel.app?redirect=";

    static boolean isValidPackage(String packageNum, String jsonStore) {
        // Parse the JSON data
        JSONObject json = new JSONObject(jsonStore);
        
        try {
            // Attempt to convert the user's input to an integer
            int packageNumber = Integer.parseInt(packageNum);
            
            // Check if the JSON data contains a key corresponding to the user's input
            if (json.has(String.valueOf(packageNumber))) {
                return true; // The input is valid
            } else {
                System.out.println("Package number is not in the range of available packages.");
                return false; // The input number does not correspond to any package index
            }
        } catch (NumberFormatException e) {
            System.out.println("User input is not a valid number.");
            return false; // The input is not a valid number
        }
    }


    static void openShop(String publicKey) {
        String url = "https://arnacon-shop.vercel.app/?user_address=" + publicKey;
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    static String getPaymentURL(String userID, String packageNum, String jsonStore, String successURL, String cancelURL, DataSaveHelper dataSaveHelper) {
        boolean isValid = Utils.isValidPackage(packageNum, jsonStore);
        if (!isValid) {
            System.out.println("The package number is not valid.");
            return null;
        }

        try {

            JSONObject json = new JSONObject(jsonStore);
            JSONObject selectedPackage = json.getJSONObject(packageNum);
            String packageName = selectedPackage.getString("name");
            double transactionPrice = 0.0;
            double subscriptionPrice = 0.0;
            String currency = "";

            JSONArray attributes = selectedPackage.getJSONArray("attributes");
            for (int i = 0; i < attributes.length(); i++) {
                JSONObject attribute = attributes.getJSONObject(i);
                switch (attribute.getString("trait_type")) {
                    case "InitP":
                        transactionPrice = attribute.getDouble("value");
                        break;
                    case "Price":
                        subscriptionPrice = attribute.getDouble("value");
                        break;
                    case "Currency":
                        currency = attribute.getString("value");
                        break;
                }
            }

            // Prepare the URL and open connection
            URL url = new URL(CloudFunctions.send_stripe_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Create the JSON object to send
            JSONObject requestJson = new JSONObject();
            requestJson.put("provider", dataSaveHelper.getPreference("serviceProviderName", "")); // Hardcoded provider
            requestJson.put("userId", userID); // User's address
            requestJson.put("packageId", packageNum); // Package ID or number
            requestJson.put("packageName", packageName); // Package name
            requestJson.put("transactionPrice", transactionPrice); // One-time price
            requestJson.put("subscriptionPrice", subscriptionPrice); // Subscription price
            requestJson.put("currency", currency); // Currency
            requestJson.put("success_url", redirectURL + successURL); // Success URL
            requestJson.put("failure_url", redirectURL + cancelURL); // Failure URL


            // Send the JSON as request body
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Read the response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                // Extract checkout URL from the response
                JSONObject jsonResponse = new JSONObject(response.toString());
                String checkoutUrl = jsonResponse.getString("url");
                return checkoutUrl;
            }

        } catch (Exception e) {
            System.out.println("There was a problem with the fetch operation: " + e.getMessage());
        }

        return null;
    }
}
