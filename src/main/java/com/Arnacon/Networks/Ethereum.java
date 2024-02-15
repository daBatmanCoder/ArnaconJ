package com.Arnacon.Networks;

import com.Config.Network;

public class Ethereum extends Network{
    
    String ENTRY_POINT_URL = "https://ethereum.publicnode.com";
    int CHAIN_ID = 1;

    public Ethereum() {}

    public String getRPC() {
        return this.ENTRY_POINT_URL;
    }

    public int getChainID() {
        return this.CHAIN_ID;
    }
}
