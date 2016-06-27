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
import com.lowermainlandpharmacyservices.lmpsformulary.R;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.CSVparser;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SplashScreenActivity extends Activity {

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
		setContentView(R.layout.activity_splash_screen);
		getActionBar().hide();

		settings = getApplicationContext().getSharedPreferences("foo", 0);

		//Check internet status
        if(!Utilities.isConnectedInternet(getApplicationContext()))
            Toast.makeText(this, "Internet connection is required for most up to date formulary information", Toast.LENGTH_LONG).show();

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
		assetManager = getAssets();
		//TODO start progress bar here
        if(Utilities.isConnectedInternet(this)) {
            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
            ValueEventListener updateEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String timeVal = dataSnapshot.getValue().toString();
                    Long time = Long.parseLong(timeVal.trim());
                    Date lastUpdate = new Date(time);
                    Date lastDeviceUpdate = SplashScreenActivity.this.getCurrentFileVersion();
                    if(lastUpdate.equals(lastDeviceUpdate))
                        performUpdate();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            firebase.child("Update").addListenerForSingleValueEvent(updateEvent);
        }


		//TODO stop progress bar here
		System.out.println("Initializing app in splash screen");

		Intent searchActivity = new Intent(this, MainActivity.class);
		startActivity(searchActivity);
		System.out.println("startedmainactivity");

	}

	private Date getCurrentFileVersion() {
        String lastDeviceUpdate = settings.getString("LAST_UPDATE", "0");
        return new Date(Long.parseLong(lastDeviceUpdate));
	}

//	private String getLatestFileVersion() {
//
//		FileInputStream fis = null;
//		String newVersion;
//		BufferedReader reader;
//		String line;
//
//		DownloadTask fileVersion = new DownloadTask(SplashScreenActivity.this,
//				"fileVersion.txt");
//
//		try {
//
//			fileVersion
//					.execute(
//							"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AAD2BXYQ0oB-i1RLnCYAnA7na/update.txt?dl=1").get();
//
//			fis = openFileInput("fileVersion.txt");
//			reader = new BufferedReader(new InputStreamReader(fis));
//			line = reader.readLine();
//			newVersion = line;
//
//		} catch (Exception e) {
//			newVersion = "-3";
//		} finally {
//			try {
//				if (fis != null)
//					fis.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//
//		return newVersion;
//	}


	private void performUpdate() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();


//		System.out.println("We need an update!");
//		Toast.makeText(this, "File update in progress",
//				Toast.LENGTH_LONG).show();


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


// The old method of grabbing dropbox
//			try{
//				final DownloadTask downloadFormulary = new DownloadTask(
//						SplashScreenActivity.this, "formularyUpdated.csv");
//				downloadFormulary
//						.execute(
//								"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AABotiW6CP_-JrGAh0mw1nkma/formulary.csv?dl=1").get();
//				final DownloadTask downloadExcluded = new DownloadTask(
//						SplashScreenActivity.this, "excludedUpdated.csv");
//				downloadExcluded
//						.execute(
//								"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AAAh2jkw2watr9KpopeH_JUsa/excluded.csv?dl=1").get();
//				final DownloadTask downloadRestricted = new DownloadTask(
//						SplashScreenActivity.this, "restrictedUpdated.csv");
//				downloadRestricted
//						.execute(
//								"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AACa_xqMx2PZWMoWKe5tJoRda/restricted.csv?dl=1").get();
//
//				// We need an Editor object to make preference changes.
//				// All objects are from android.context.Context
//
//				editor.putBoolean("filesDownloaded", true);
//				editor.commit();
//				Toast.makeText(this, "Update completed", Toast.LENGTH_LONG)
//						.show();
//			}
//			catch(Exception e)
//			{
//
//			}
		} else { // if network is off
			Toast.makeText(
					this,
					"A version update is available, please connect to wi-fi "
							+ "and restart to app to update",
					Toast.LENGTH_LONG).show();
		}

	}

}
