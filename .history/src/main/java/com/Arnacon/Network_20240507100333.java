package com.Arnacon;

import org.json.JSONObject;

import com.Config.ANetwork;

public class Network implements ANetwork{
    
    private String ENTRY_POINT_URL;
    private int CHAIN_ID;
    public String networkName;

    public Network() {
        this("amoy");
    }
    
    public Network(String _networkName) {
        try {
            CloudFunctions cloudFunctions = new CloudFunctions();
            this.networkName = _networkName;
            JSONObject networkConfig = cloudFunctions.getNetwork(networkName);
            this.ENTRY_POINT_URL = networkConfig.getString("entry_point_url");
            this.CHAIN_ID = networkConfig.getInt("chain_id");

        } catch (Exception e) {
            e.printStackTrace();
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
