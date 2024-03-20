package com.Arnacon;

public interface DataSaveHelper {

    void setPreference(String key, T value);

    String getPreference(String key, T defaultValue);
}
