package com.example.bluetoothex04.models.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalRepository {
    private final String PREFERENCE_NAME = "com.example.bluetoothex04.bluetoothex04_preferences";
    private final String DEFAULT_VALUE_STRING = "";
    private SharedPreferences sharedPreferences;

    private LocalRepository() {
    }

    public LocalRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setLatLon(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latlon", value);
        editor.commit();
    }

    public String[] getLatLon() {
        String latlon = sharedPreferences.getString("latlon", null);
        if (latlon != null) {
            return latlon.split(",");
        } else {
            return null;
        }
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, DEFAULT_VALUE_STRING);
    }
}
