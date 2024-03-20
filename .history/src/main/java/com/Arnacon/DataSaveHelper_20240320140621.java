package com.Arnacon;

public interface DataSaveHelper {

    void setPreference(String key, String value);

    String getPreference(String key, String defaultValue);
}
