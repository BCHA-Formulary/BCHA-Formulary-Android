package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lowermainlandpharmacyservices.lmpsformulary.GenericCallback;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.Status;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SharedPrefManager;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SqlHelper;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplashScreenActivity extends Activity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

	SharedPreferences settings;
	SharedPreferences.Editor editor;

//	CSVparser masterList = null;
//	GenericDrugList genericList;
//	BrandDrugList brandList;

//	int PAUSE_MILLISECONDS = 500;
//
//    int updatedListsFetched = 0;
//    List<Drug> updatedDrugList;

    ChildEventListener formularyListener;
    ChildEventListener excludedListener;
    ChildEventListener restrictedListener;
    ValueEventListener completedListener;

    MaterialDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        initializeApp();
	}

	private void initializeApp(){
        if(Utilities.isConnectedInternet(this)) {
            dialog = new MaterialDialog.Builder(this)
                    .title("Updating")
                    .content("Please wait, app is downloading the latest Formulary list")
                    .progress(true, 0)
                    .show();

            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

            ValueEventListener updateEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String firebaseTime = dataSnapshot.getValue().toString();
                    String lastUpdated;
                    try {
                        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance();
                        lastUpdated = sharedPrefManager.getString(SharedPrefManager.Key.LAST_UPDATED, "");
                    } catch (Exception e) {
                        Log.d(TAG, "Could not get last update from pref");
                        lastUpdated = "";
                        if (dialog != null)
                            dialog.dismiss();
                    }
                    if (lastUpdated.isEmpty() || !lastUpdated.equals(firebaseTime)){
                        fetchUpdatedDrugs(firebaseTime, new GenericCallback<List<DrugBase>, Throwable>() {
                            @Override
                            public void onSuccess(List<DrugBase> drugList) {
                                Log.d(TAG, "Drug count: " + drugList.size());
                                SqlHelper sqlHelper = new SqlHelper(SplashScreenActivity.this);
                                sqlHelper.clearAllTables();
                                sqlHelper.addAllDrug(drugList);
                                try {
                                    SharedPrefManager sharedPrefs = SharedPrefManager.getInstance();
                                    sharedPrefs.putString(SharedPrefManager.Key.LAST_UPDATED, firebaseTime);
                                } catch (Exception e) {
                                    Log.e(TAG, "Could not save update: " + e.getMessage());
                                }
                                if (dialog != null)
                                    dialog.dismiss();
                                Intent searchActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(searchActivity);
                                finish();
                            }
                            @Override
                            public void onFailure(Throwable object) {
                                Log.e(TAG, "Could not get firebase updates: " + object.getMessage());
                                new MaterialDialog.Builder(SplashScreenActivity.this)
                                        .title("Update Error")
                                        .content("There was an error retrieving updates. Please restart the app to get the latest updates.")
                                        .positiveText("Enter offline mode")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                                Intent searchActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                                                startActivity(searchActivity);
                                            }
                                        })
                                        .negativeText("Quit app")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).show();
                            }
                        });
                    }
                    else {
                        //firebase is up to date
                        if (dialog != null)
                            dialog.dismiss();
                        Intent searchActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(searchActivity);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            firebase.child("Update").addListenerForSingleValueEvent(updateEvent);
        } else {
            //Internet connection is not available
            new MaterialDialog.Builder(this)
                    .title("Network Error")
                    .content("Internet connection is required to ensure the Formulary list is most up to date. Please restart the app to check for latest updates.")
                    .positiveText("Enter offline mode")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            Intent searchActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(searchActivity);
                        }
                    })
                    .negativeText("Quit app")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
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

                    FormularyDrug fDrug = new FormularyDrug(primaryName.toUpperCase().trim(), nameType, altNames, drugClasses, Status.FORMULARY, strengths);
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

                    ExcludedDrug eDrug = new ExcludedDrug(primaryName.toUpperCase().trim(), nameType, altNames, drugClasses, Status.EXCLUDED, criteria);
                    excludedDrugList.add(eDrug);
                    allDrugList.add(eDrug);
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

                    RestrictedDrug rDrug = new RestrictedDrug(primaryName.toUpperCase().trim(), nameType, altNames, drugClasses, Status.RESTRICTED, criteria);
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
