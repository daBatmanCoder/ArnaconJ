package com.cellact;

import com.Config.config_file;

public class Mumbai extends config_file{

    String ENTRY_POINT_URL = "https://polygon-mumbai-bor.publicnode.com";
    int CHAIN_ID = 80001;

    public Mumbai() {}

    public String getRPC() {
        return this.ENTRY_POINT_URL;
    }

    public int getChainID() {
        return this.CHAIN_ID;
    }
}
