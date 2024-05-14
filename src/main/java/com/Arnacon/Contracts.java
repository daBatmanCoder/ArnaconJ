package com.Arnacon;

import com.Web3ServiceBase.AContract;

public class Contracts implements AContract{
    
    String NAME_HASH_ADDRESS;
    String W_ENS_ADDRESS;

    public Contracts() {
        try {
            CloudFunctions cloudFunctions = new CloudFunctions();
            this.NAME_HASH_ADDRESS = cloudFunctions.getContractAddress("NAME_HASH_ADDRESS");
            this.W_ENS_ADDRESS = cloudFunctions.getContractAddress("W_ENS_ADDRESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNameHashAddress() {
        return this.NAME_HASH_ADDRESS;
    }

    public String getWENSAddress() {
        return this.W_ENS_ADDRESS;
    }

}
