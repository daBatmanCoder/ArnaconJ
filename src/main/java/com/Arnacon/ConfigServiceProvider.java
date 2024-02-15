package com.Arnacon;

public class ConfigServiceProvider {

    String serviceProviderName;

    public ConfigServiceProvider(String _serviceProviderName) {
        this.serviceProviderName = _serviceProviderName;
    }

    public String getServiceProviderName() {
        return this.serviceProviderName;
    }

}
