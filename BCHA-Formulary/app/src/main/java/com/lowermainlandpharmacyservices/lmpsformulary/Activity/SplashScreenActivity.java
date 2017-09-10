package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.BrandDrugList;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.GenericDrugList;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.CSVparser;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SharedPrefManager;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SplashScreenActivity extends Activity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

	SharedPreferences settings;
	SharedPreferences.Editor editor;

	CSVparser masterList = null;
	GenericDrugList genericList;
	BrandDrugList brandList;

	int PAUSE_MILLISECONDS = 500;

	public AssetManager assetManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash_screen);
//		getActionBar().hide();

//		settings = getApplicationContext().getSharedPreferences("foo", 0);

		//Check internet status
        if(!Utilities.isConnectedInternet(getApplicationContext())) {
            //TODO make dialog message
            Toast.makeText(this, "Internet connection is required for most up to date formulary information", Toast.LENGTH_LONG).show();
        } else {

        }

		//pause to see the pretty splash screen
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeApp();
                finish();
            }
        }, PAUSE_MILLISECONDS);
	}

	private void initializeApp(){
//        String lastUpdated;
////		assetManager = getAssets();
//		//TODO start progress bar here
//        try {
//            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance();
//            lastUpdated = sharedPrefManager.getString(SharedPrefManager.Key.LAST_UPDATED, "");
//        } catch (Exception e) {
//            Log.d(TAG, "Could not get last update from pref");
//            lastUpdated = "";
//        }
        if(Utilities.isConnectedInternet(this)) {
            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

            ValueEventListener updateEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String firebaseTime = dataSnapshot.getValue().toString();
                    String lastUpdated;
                    try {
                        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance();
                        lastUpdated = sharedPrefManager.getString(SharedPrefManager.Key.LAST_UPDATED, "");
                    } catch (Exception e) {
                        Log.d(TAG, "Could not get last update from pref");
                        lastUpdated = "";
                    }
//                    if (lastUpdated.isEmpty() || !lastUpdated.equals(firebaseTime)){
//                        performUpdate(firebaseTime);
//                    } else {
                        //firebase is up to date
                        Intent searchActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(searchActivity);
//                    }
//                    Long time = Long.parseLong(firebaseTime);
//                    Date lastUpdate = new Date(time);
//                    Date lastDeviceUpdate = SplashScreenActivity.this.getCurrentFileVersion();
//                    if(lastUpdate.equals(lastDeviceUpdate))

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            firebase.child("Update").addListenerForSingleValueEvent(updateEvent);
        } else {

        }

//
//		//TODO stop progress bar here
//		System.out.println("Initializing app in splash screen");
//
//		Intent searchActivity = new Intent(this, MainActivity.class);
//		startActivity(searchActivity);
//		System.out.println("startedmainactivity");

	}

	private Date getCurrentFileVersion() {
        String lastDeviceUpdate = settings.getString("LAST_UPDATE", "0");
        return new Date(Long.parseLong(lastDeviceUpdate));
	}

	private void performUpdate(String updateVersion) {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();

		// if network is on
		if (isConnected) {
        //Firebase
            final List<FormularyDrug> formularyDrugList = new ArrayList<FormularyDrug>();
            final List<ExcludedDrug> excludedDrugList= new ArrayList<ExcludedDrug>();
            final List<RestrictedDrug> restrictedDrugList = new ArrayList<RestrictedDrug>();

            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

            firebase.child("Formulary").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FormularyDrug formularyDrug = dataSnapshot.getValue(FormularyDrug.class);
                    formularyDrugList.add(formularyDrug);
                    Log.d("Formulary Drug", "Drug: " + formularyDrug.getPrimaryName() + "Count: " + formularyDrugList.size());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            firebase.child("Excluded").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ExcludedDrug excludedDrug = dataSnapshot.getValue(ExcludedDrug.class);
                    excludedDrugList.add(excludedDrug);
                    Log.d("Excluded Drug", "Drug: " + excludedDrug.getPrimaryName() + "Count: " + excludedDrugList.size());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            firebase.child("Restricted").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    RestrictedDrug restrictedDrug = dataSnapshot.getValue(RestrictedDrug.class);
                    restrictedDrugList.add(restrictedDrug);
                    Log.d("Restricted Drug", "Drug: " + restrictedDrug.getPrimaryName() + "Count: " + restrictedDrugList.size());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //TODO remove child listeners on view hiding
            //TODO make sql tables
            //TODO refactor all keys to string utils file
            editor.putString("LAST_UPDATE", updateVersion);
            editor.commit();
		} else { // if network is off
			Toast.makeText(
					this,
					"A version update is available, please connect to wi-fi "
							+ "and restart to app to update",
					Toast.LENGTH_LONG).show();
		}

	}

}
