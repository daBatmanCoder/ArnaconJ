package com.Arnacon;

import com.Config.Contract;

public class Contracts extends Contract{
    
    String NAME_HASH_ADDRESS = "0xeC367aD331049234efcb6936542C83c510fD1473";
    String W_ENS_ADDRESS = "0x4aD58190BBF1e3196B611B4333552d2026440B16";

    public Contracts() {}

    public String getNameHashAddress() {
        return this.NAME_HASH_ADDRESS;
    }

    public String getWENSAddress() {
        return this.W_ENS_ADDRESS;
    }

}
