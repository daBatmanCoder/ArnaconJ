package com.Arnacon.initialization;

import java.util.Scanner;

import com.Arnacon.ConfigServiceProvider;
import com.Arnacon.Utils;
import com.Arnacon.Web3AJ;
import com.Arnacon.Network;

public class InitAppWeb2 {

    Web3AJ Web3Service;
    ConfigServiceProvider serviceProvider;

    public InitAppWeb2() throws Exception {

        // Make Web3 Instance
        this.Web3Service = new Web3AJ(new Network("mumbai"));
        serviceProvider = new ConfigServiceProvider("test2.cellact.nl");

        // Fetch store
        String ipfsContent = Web3AJ.fetchStore(serviceProvider.getServiceProviderName());
        System.out.println("Store: " + ipfsContent);
        
        // Choose a product
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter package you want: ");
        String packageNum = scanner.nextLine();

        String success_url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String failure_url = "https://www.google.com";
        // Payment URL
        String url = Utils.getPaymentURL(this.Web3Service.wallet.getPublicKey(), packageNum, ipfsContent,success_url,failure_url);
        System.out.println("Payment URL: " + url);

        scanner.nextLine();
        scanner.close();

        String fcm_token = "9c71ab46-370b-40f6-8235-bf1b03da1867";
        Web3Service.sendFCM(fcm_token);

    }
    
}
