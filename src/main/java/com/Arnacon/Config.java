package com.Arnacon;

public class Config {

    private static final String DEFAULT_NETWORK_NAME    = "amoy";
    private static final String DEFAULT_ENTRY_POINT_URL = "https://rpc-amoy.polygon.technology";
    private static final String DEFAULT_FUNCTIONS_URL   = "https://us-central1-arnacon-nl.cloudfunctions.net/Functions";
    private static final String PAYMENT_DEEPLINK_OK     = "https://success-java.vercel.app/";
    private static final String PAYMENT_DEEPLINK_NOK    = "https://failure-java.vercel.app/";
    private static final String REDIRECT_URL            = "https://redirect-generation.vercel.app?redirect=";
    private static final int    DEFAULT_CHAIN_ID        = 80002;

    private static final String IN_SHOP_URL_WITH_PARAMS = "https://arnacon-shop.vercel.app/?user_address=";
    
    public static String getDefaultNetworkName(){
        return DEFAULT_NETWORK_NAME;
    }

    public static String getDefaultEntryPointUrl() {
        return DEFAULT_ENTRY_POINT_URL;
    }

    public static int getDefaultChainId() {
        return DEFAULT_CHAIN_ID;
    }
    
    public static String getDefaultFunctionsURL(){
        return DEFAULT_FUNCTIONS_URL;
    }

    public static String getPaymentDLOK(){
        return PAYMENT_DEEPLINK_OK;
    }

    public static String getPaymentDLNotOK(){
        return PAYMENT_DEEPLINK_NOK;
    }

    public static String getRedirectURL(){
        return REDIRECT_URL;
    }

    public static String getRedirectInShopURL(){
        return IN_SHOP_URL_WITH_PARAMS;
    }
}
