package com.Config;

public abstract class ANetwork {

    String ENTRY_POINT_URL;
    int CHAIN_ID;
    
    public abstract String getRPC();
    public abstract int getChainID();
}
