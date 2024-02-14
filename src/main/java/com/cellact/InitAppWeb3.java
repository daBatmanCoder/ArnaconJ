package com.cellact;

import java.io.IOException;
import java.math.BigDecimal;

public class InitAppWeb3 {

    Web3AJ Web3Service;

    public InitAppWeb3() throws IOException {
        String privateKeys = "1cf3bacf75f3c8580aabf395ddb3eb5bf2943ce44cc9907a60802a305c3f4e09";
        this.Web3Service = new Web3AJ(new Mumbai(),privateKeys);
        
        // String tx_hash_ens = this.Web3Service.buyENS("joniBoy");
        // System.out.println(tx_hash_ens);

        // Thread.sleep(5000);
        // String tx_hash = Web3Service.mintNFT( "amir22222.web3");
        // System.out.println(tx_hash);
        BigDecimal balance = Web3Service.checkBalance(this.Web3Service.wallet.getPublicKey());
        System.out.println("Balance: " + balance);

        String message = "9c71ab46-370b-40f6-8235-bf1b03da1867";
        String signed = Web3Service.signMessage(message);

        System.out.println("Signature: " + signed);
    }
}
