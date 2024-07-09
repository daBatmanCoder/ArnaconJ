package com.Web3ServiceBase;

import com.Arnacon.Wallet;

abstract public class AWeb3AJ {

    protected ADataSaveHelper   dataSaveHelper;
    protected ALogger           logger;
    protected ANetwork          network;
    protected Wallet            wallet;

    public AWeb3AJ(
        ADataSaveHelper dataSaveHelper, 
        ALogger logger
    ) {
        this.dataSaveHelper = dataSaveHelper;
        this.logger = logger;
    }

    protected void updateWallet(
        Wallet wallet
    ) {
        this.wallet = wallet;
    }

    public abstract String[]    getServiceProviderList();
    public abstract void        setServiceProvider(String provider);

    public abstract String      fetchStore() throws Exception;
    
    public abstract String      getPaymentURL(String packageNum, String successURL, String cancelURL);
    public abstract void        sendFCM(String fcmToken);
    public abstract String      getXData();
    public abstract String      getXSign(String XData);

    public abstract void        saveENSItem(String item);
    public abstract void        saveENSItem(String item, String serviceProviderName);
    public abstract String      updateNewProduct(String password, String ciphertextHex);

}
