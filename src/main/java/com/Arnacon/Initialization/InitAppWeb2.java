package com.Arnacon.initialization;

import java.util.Scanner;

import com.Arnacon.CloudFunctions;
import com.Arnacon.ConfigServiceProvider;
import com.Arnacon.Utils;
import com.Arnacon.Web3AJ;
import com.Arnacon.Network;

public class InitAppWeb2 {

    Web3AJ Web3Service;
    CloudFunctions cloudFunctions;
    ConfigServiceProvider configServiceProvider;

    public InitAppWeb2() throws Exception {
        

        this.Web3Service = new Web3AJ(new Network("mumbai"));
        configServiceProvider = new ConfigServiceProvider("test2.cellact.nl");
        cloudFunctions = new CloudFunctions();

        System.out.println("Public Key: " + this.Web3Service.wallet.getPublicKey());
        String shopCID = cloudFunctions.getShopCID(configServiceProvider.getServiceProviderName());
        System.out.println("Shop CID: " + shopCID);

        String ipfsContent = Web3AJ.fetchStoreFromIPFS(shopCID);

        System.out.println("IPFS Content: " + ipfsContent);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter package you want: ");

        String packageNum = scanner.nextLine();

        boolean isValid = Utils.isValidPackage(packageNum, ipfsContent);
        System.out.println("Is the package valid? " + isValid);

        String url = Utils.getPaymentURL(this.Web3Service.wallet.getPublicKey(), packageNum, ipfsContent);
        System.out.println("Payment URL: " + url);

        scanner.nextLine();
        scanner.close();

        String ens = cloudFunctions.getUserENS(this.Web3Service.wallet.getPublicKey());
        System.out.println("ENS: " + ens);

        String message = "9c71ab46-370b-40f6-8235-bf1b03da1867";
        String signed = Web3Service.signMessage(message);
        System.out.println("Signature: " + signed);

    }

    
}
