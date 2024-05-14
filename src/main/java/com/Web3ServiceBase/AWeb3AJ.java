package com.Web3ServiceBase;

abstract public class AWeb3AJ {

    protected ADataSaveHelper dataSaveHelper;
    protected ALogger logger;

    public AWeb3AJ(ADataSaveHelper dataSaveHelper, ALogger logger) {
        this.dataSaveHelper = dataSaveHelper;
        this.logger = logger;
    }

    public abstract String[] getServiceProviderList();
    public abstract void setServiceProvider(String provider);
    public abstract String fetchStore() throws Exception;
    public abstract String getPaymentURL(String packageNum, String successURL, String cancelURL);
    public abstract void sendFCM(String fcmToken);
    public abstract String getXData();
    public abstract String getXSign(String XData);

}
