package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by kelvinchan on 2016-04-30.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
