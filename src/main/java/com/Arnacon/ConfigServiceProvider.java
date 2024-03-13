package com.Arnacon;

public class ConfigServiceProvider {

    String serviceProviderName;
    DataSaveHelper dataSaveHelper;

    public ConfigServiceProvider(String _serviceProviderName, DataSaveHelper dataSaveHelper) {
        this.dataSaveHelper = dataSaveHelper;
        dataSaveHelper.setPreference("serviceProviderName", _serviceProviderName);
    }

    public String getServiceProviderName() {
        return dataSaveHelper.getPreference("serviceProviderName", "default");
    }


}
