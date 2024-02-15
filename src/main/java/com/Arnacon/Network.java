package com.Arnacon;

import org.json.JSONObject;

import com.Config.ANetwork;

public class Network extends ANetwork{
    private String ENTRY_POINT_URL;
    private int CHAIN_ID;

    public Network(String networkName) {
        try {
            CloudFunctions cloudFunctions = new CloudFunctions();
            JSONObject networkConfig = cloudFunctions.getNetwork(networkName);
            this.ENTRY_POINT_URL = networkConfig.getString("entry_point_url");
            this.CHAIN_ID = networkConfig.getInt("chain_id");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRPC() {
        return ENTRY_POINT_URL;
    }

    public int getChainID() {
        return CHAIN_ID;
    }

}
