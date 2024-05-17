package com.Arnacon;

import org.json.JSONObject;

import com.Web3ServiceBase.ANetwork;

public class Network implements ANetwork{
    
    private String ENTRY_POINT_URL;
    private int CHAIN_ID;
    public String networkName;

    public Network() {
        this("amoy");
    }
    
    public Network(String _networkName) {
        try {
            this.networkName = _networkName;

            JSONObject networkConfig = Utils.CloudFunctions.getNetwork(networkName);
            
            this.ENTRY_POINT_URL = networkConfig.getString("entry_point_url");
            this.CHAIN_ID = networkConfig.getInt("chain_id");

        } catch (Exception e) {
            this.ENTRY_POINT_URL = "https://polygon-amoy-bor-rpc.publicnode.com";
            this.CHAIN_ID = 80002;
        }
    }

    public String getRPC() {
        return ENTRY_POINT_URL;
    }

    public int getChainID() {
        return CHAIN_ID;
    }

}
