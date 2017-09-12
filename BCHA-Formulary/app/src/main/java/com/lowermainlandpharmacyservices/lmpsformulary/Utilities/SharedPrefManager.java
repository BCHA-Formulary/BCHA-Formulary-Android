package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kelvinchan on 2017-09-09.
 */

public class SharedPrefManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPrefManager sharedPrefManager = new SharedPrefManager();
    private Context context;

    private SharedPrefManager(){}

    public static void initalizeSharedPrefManager(Context appContext) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext.getApplicationContext());
        }
        sharedPrefManager.context = appContext;
    }

    public static SharedPrefManager getInstance() throws Exception{
        if (sharedPrefManager == null) {
            throw new Exception("Shared Pref not inialized at launch!");
        }
        return sharedPrefManager;
    }

    public String getString(Key key, String defaultVal) {
        return sharedPreferences.getString(key.name(), defaultVal);
    }

    public void putString(Key key, String val) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key.name(), val);
        editor.apply();
    }

    public enum Key {
        LAST_UPDATED
    }
}
