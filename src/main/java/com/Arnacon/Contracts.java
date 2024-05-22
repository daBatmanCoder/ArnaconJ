package com.Arnacon;

import com.Web3ServiceBase.AContract;
import com.Web3ServiceBase.ALogger;

public class Contracts implements AContract{
    
    String NAME_HASH_ADDRESS;
    String W_ENS_ADDRESS;

    private static Contracts instance;

    private Contracts(ALogger logger) {
        try {
            this.NAME_HASH_ADDRESS = Utils.getCloudFunctions(logger).getContractAddress("NAME_HASH_ADDRESS");
            this.W_ENS_ADDRESS = Utils.getCloudFunctions(logger).getContractAddress("W_ENS_ADDRESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Contracts getContracts(ALogger logger) {
        if (instance == null) {  // First check (no locking)
            synchronized (Contracts.class) {
                if (instance == null) {  // Second check (with locking)
                    instance = new Contracts(logger);
                }
            }
        }
        return instance;
    }

    public String getNameHashAddress() {
        return this.NAME_HASH_ADDRESS;
    }

    public String getWENSAddress() {
        return this.W_ENS_ADDRESS;
    }

}
