package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class Utilities {

	// Password SECTION
	public static String password = "bettercare";
	public static String authorizedUser = "authorization";

	// Keys passing data SECTION
	public static String EXTRA_INFO = "com.lowermainlandpharmacyservices.MainActivity.SEARCHINPUT";
	public static String EXTRA_STRENGTHS = "com.lowermainlandpharmacyservices.MainActivity.STRENGTHS";
	public static String EXTRA_GENERICNAME = "com.lowermainlandpharmacyservices.MainActivity.GENERICNAME";
	public static String EXTRA_BRANDNAME = "com.lowermainlandpharmacyservices.MainActivity.BRANDNAME";
	public static String EXTRA_RESTRICTIONS = "com.lowermainlandpharmacyservices.MainActivity.RESTRICTIONS";
	public static String EXTRA_EXCLUDED_REASON = "com.lowermainlandpharmacyservices.MainActivity.EXCLUDED_REASON";
	public static String EXTRA_TYPE = "TYPE";

	//Firebase
	public static final String firebaseRoot = "https://bcha-formulary.firebaseio.com/";

	//network connection
	public static boolean isConnectedInternet(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


}
