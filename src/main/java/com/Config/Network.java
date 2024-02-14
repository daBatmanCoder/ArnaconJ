package com.Config;

public abstract class Network {

    String ENTRY_POINT_URL;
    int CHAIN_ID;
    
    public abstract String getRPC();
    public abstract int getChainID();
}
