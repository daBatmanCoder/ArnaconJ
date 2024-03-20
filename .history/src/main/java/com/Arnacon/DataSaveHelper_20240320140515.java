package com.Arnacon;

public interface DataSaveHelper {

    <T> void setPreference(String key, T value);

    <T> String getPreference(String key, T defaultValue);
}
