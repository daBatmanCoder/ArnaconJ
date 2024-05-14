package com.Web3ServiceBase;

public interface ADataSaveHelper {

    void setPreference(String key, String value);

    String getPreference(String key, String defaultValue);
}
