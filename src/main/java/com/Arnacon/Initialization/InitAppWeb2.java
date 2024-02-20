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

        this.Web3Service = new Web3AJ(new Network("mumbai"));
        serviceProvider = new ConfigServiceProvider("test2.cellact.nl");

        String ipfsContent = Web3AJ.fetchStoreFromIPFS(serviceProvider.getServiceProviderName());

        System.out.println("IPFS Content: " + ipfsContent);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter package you want: ");

        String packageNum = scanner.nextLine();

        String url = Utils.getPaymentURL(this.Web3Service.wallet.getPublicKey(), packageNum, ipfsContent);
        System.out.println("Payment URL: " + url);

        scanner.nextLine();
        scanner.close();

        String ens = Utils.CloudFunctions.getUserENS(this.Web3Service.wallet.getPublicKey());
        System.out.println("ENS: " + ens);

        String message = "9c71ab46-370b-40f6-8235-bf1b03da1867";
        String signed = Web3Service.signMessage(message);
        System.out.println("Signature: " + signed);

    }
    
}
