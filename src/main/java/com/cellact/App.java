package com.cellact;

import java.io.IOException;

public class App 
{   
    public static void main( String[] args ) throws IOException
    {

        String privateKeys = "1cf3bacf75f3c8580aabf395ddb3eb5bf2943ce44cc9907a60802a305c3f4e09";
        CreateWallet wallet = new CreateWallet(privateKeys);
        String public_key = wallet.getPublicKeyFromPrivateKey(wallet.getPrivateKey());
        System.out.println(public_key);
        String tx_hash = wallet.mintNFT("0xADaAf2160f7E8717FF67131E5AA00BfD73e377d5", "user1.eth");
        System.out.println(tx_hash);

        String message = "9c71ab46-370b-40f6-8235-bf1b03da1867";
        String signed = wallet.signMessage(message);

        System.out.println("Signature: " + signed);
    }
}