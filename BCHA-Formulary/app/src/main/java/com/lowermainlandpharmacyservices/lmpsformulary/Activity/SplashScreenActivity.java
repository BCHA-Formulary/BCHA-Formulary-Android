package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.lowermainlandpharmacyservices.lmpsformulary.GenericCallback;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Drug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.Status;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SharedPrefManager;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SplashScreenActivity extends Activity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

	SharedPreferences settings;
	SharedPreferences.Editor editor;

//	CSVparser masterList = null;
//	GenericDrugList genericList;
//	BrandDrugList brandList;

	int PAUSE_MILLISECONDS = 500;

    int updatedListsFetched = 0;
    List<Drug> updatedDrugList;

    ChildEventListener formularyListener;
    ChildEventListener excludedListener;
    ChildEventListener restrictedListener;
    ValueEventListener completedListener;


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
                    if (lastUpdated.isEmpty() || !lastUpdated.equals(firebaseTime)){
                        fetchUpdatedDrugs(firebaseTime, new GenericCallback<List<DrugBase>, Throwable>() {
                            @Override
                            public void onSuccess(List<DrugBase> drugList) {
                                Log.d(TAG, "Drug count: " + drugList.size());
                            }

                            @Override
                            public void onFailure(Throwable object) {

                            }
                        });
                    }
//                    else {
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
            //Internet connection is not available

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

	private void fetchUpdatedDrugs(String updateVersion, final GenericCallback<List<DrugBase>, Throwable> callback) {
        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

        final List<FormularyDrug> formularyDrugList = new ArrayList<>();
        final List<ExcludedDrug> excludedDrugList= new ArrayList<>();
        final List<RestrictedDrug> restrictedDrugList = new ArrayList<>();
        final List<DrugBase> allDrugList = new ArrayList<>();

        formularyListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    String primaryName = (String) dataSnapshot.child("primaryName").getValue();

                    NameType nameType = NameType.getNameType((String) dataSnapshot.child("nameType").getValue());

                    List<String> drugClasses;
                    Object drugClassObj = dataSnapshot.child("drugClass").getValue();
                    if (drugClassObj instanceof List) {
                        drugClasses = (List<String>) drugClassObj;
                    } else {
                        HashMap<String, String> classHash = (HashMap<String, String>) drugClassObj;
                        drugClasses = new ArrayList<>(classHash.values());
                    }

                    List<String> altNames;
                    Object altObj = dataSnapshot.child("alternateName").getValue();
                    if (altObj instanceof List) {
                        altNames = (List<String>) altObj;
                    } else {
                        HashMap<String, String> altHash = (HashMap<String, String>) altObj;
                        altNames = new ArrayList<>(altHash.values());
                    }

                    List<String> strengths;
                    Object strObj = dataSnapshot.child("strengths").getValue();
                    if (strObj instanceof List) {
                        strengths = (List<String>) strObj;
                    } else {
                        HashMap<String, String> strHash = (HashMap<String, String>) strObj;
                        strengths = new ArrayList<>(strHash.values());
                    }

                    FormularyDrug fDrug = new FormularyDrug(primaryName, nameType, altNames, drugClasses, Status.FORMULARY, strengths);
                    formularyDrugList.add(fDrug);
                    allDrugList.add(fDrug);
                }
                catch (Exception e) {
                    Log.e(TAG, "Problem drug " + dataSnapshot.child("primaryName").getValue());
                }
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
        };

        excludedListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    ExcludedDrug excludedDrug = dataSnapshot.getValue(ExcludedDrug.class);
//                    excludedDrugList.add(excludedDrug);
                try {
                    String primaryName = (String) dataSnapshot.child("primaryName").getValue();
                    NameType nameType = NameType.getNameType((String) dataSnapshot.child("nameType").getValue());

                    List<String> drugClasses;
                    Object classObj = dataSnapshot.child("drugClass").getValue();
                    if (classObj instanceof List) {
                        drugClasses = (List<String>) classObj;
                    } else {
                        HashMap<String, String> classHash = (HashMap<String, String>) classObj;
                        drugClasses = new ArrayList<>(classHash.values());
                    }

                    List<String> altNames;
                    Object altObj = dataSnapshot.child("alternateName").getValue();
                    if (altObj instanceof List) {
                        altNames = (List<String>) altObj;
                    } else {
                        HashMap<String, String> altHash = (HashMap<String, String>) altObj;
                        altNames = new ArrayList<>(altHash.values());
                    }

                    String criteria = (String) dataSnapshot.child("criteria").getValue();

                    ExcludedDrug eDrug = new ExcludedDrug(primaryName, nameType, altNames, drugClasses, Status.EXCLUDED, criteria);
                    excludedDrugList.add(eDrug);
                    allDrugList.add(eDrug);
//                    Log.d("Excluded Drug", "Drug: " + excludedDrug.getPrimaryName() + "Count: " + excludedDrugList.size());
                }
                catch (Exception e) {
                    Log.e(TAG, "Problem drug " + dataSnapshot.child("primaryName").getValue());
                }
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
        };

        restrictedListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    RestrictedDrug restrictedDrug = dataSnapshot.getValue(RestrictedDrug.class);
//                    restrictedDrugList.add(restrictedDrug);
//                    Log.d("Restricted Drug", "Drug: " + restrictedDrug.getPrimaryName() + "Count: " + restrictedDrugList.size());
                try {
                    String primaryName = (String) dataSnapshot.child("primaryName").getValue();
                    NameType nameType = NameType.getNameType((String) dataSnapshot.child("nameType").getValue());

                    List<String> drugClasses;
                    Object classObj = dataSnapshot.child("drugClass").getValue();
                    if (classObj instanceof List) {
                        drugClasses = (List<String>) classObj;
                    } else {
                        HashMap<String, String> classHash = (HashMap<String, String>) classObj;
                        drugClasses = new ArrayList<>(classHash.values());
                    }

                    List<String> altNames;
                    Object altObj = dataSnapshot.child("alternateName").getValue();
                    if (altObj instanceof List) {
                        altNames = (List<String>) altObj;
                    } else {
                        HashMap<String, String> altHash = (HashMap<String, String>) altObj;
                        altNames = new ArrayList<>(altHash.values());
                    }

                    String criteria = (String) dataSnapshot.child("criteria").getValue();

                    RestrictedDrug rDrug = new RestrictedDrug(primaryName, nameType, altNames, drugClasses, Status.RESTRICTED, criteria);
                    restrictedDrugList.add(rDrug);
                    allDrugList.add(rDrug);
                }
                catch (Exception e) {
                    Log.e(TAG, "Problem drug " + dataSnapshot.child("primaryName").getValue());
                }
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
        };

        firebase.child("Formulary").addChildEventListener(formularyListener);

        firebase.child("Excluded").addChildEventListener(excludedListener);

        firebase.child("Restricted").addChildEventListener(restrictedListener);
            //TODO remove child listeners on view hiding
            //TODO make sql tables
            //TODO refactor all keys to string utils file
//            editor.putString("LAST_UPDATE", updateVersion);
//            editor.commit();
//		} else { // if network is off
//			Toast.makeText(
//					this,
//					"A version update is available, please connect to wi-fi "
//							+ "and restart to app to update",
//					Toast.LENGTH_LONG).show();
//		}

        completedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Formulary Count: " + formularyDrugList.size());
                Log.d(TAG, "Excluded Count: " + excludedDrugList.size());
                Log.d(TAG, "Restricted Count: " + restrictedDrugList.size());
                firebase.removeEventListener(formularyListener);
                firebase.removeEventListener(excludedListener);
                firebase.removeEventListener(restrictedListener);
                firebase.removeEventListener(completedListener);
                callback.onSuccess(allDrugList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        };
        firebase.child("Update").addListenerForSingleValueEvent(completedListener);
	}

}
