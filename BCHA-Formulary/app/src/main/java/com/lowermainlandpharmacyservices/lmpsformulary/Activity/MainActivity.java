package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.BrandDrugList;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Drug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.GenericDrugList;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.R;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.CSVparser;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SqlHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

	SharedPreferences settings;
	GenericDrugList genericList;
	BrandDrugList brandList;

	CSVparser masterList = null;
	public AssetManager assetManager;
	SqlHelper mSqlHelper;
	
	AutoCompleteTextView autocompletetextview;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSqlHelper = new SqlHelper(this);
		List<String> drugNames = mSqlHelper.getAllDrugNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item,  drugNames);
		autocompletetextview = (AutoCompleteTextView) findViewById(R.id.search_input);
		autocompletetextview.setAdapter(adapter);

		autocompletetextview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Log.d(TAG, "Clicked");
			}
		});
//		System.out.println("search activity started");

		// Write a message to the database
//		FirebaseDatabase database = FirebaseDatabase.getInstance();
//		DatabaseReference myRef = database.getReference("message");
//
//		myRef.setValue("Hello, World!");
		
//		beginParsing();
		
//		ArrayList<String> autoCompleteDrugNameList = preparePredictiveText();
//		setUpAutoComplete(autoCompleteDrugNameList);

	}

	@Override
	public void onStart() {
		super.onStart();
		getActionBar().hide();
	}

	@Override
	public void onPause() {
		super.onPause();
		getActionBar().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	private void beginParsing() {
//
//		settings = getApplicationContext().getSharedPreferences("foo", 0);
//		masterList = new CSVparser();
//		System.out.println("initparser");
//
//		try {
//			// get value of filesDownloaded, false if not found
//			if (settings.getBoolean("filesDownloaded", false)) {
//				// use downloaded files to parse
//				System.out.println("parser from updated files");
//				masterList
//				.parseFormulary(openFileInput("formularyUpdated.csv"));
//				System.out.println("formularyparsed");
//				masterList
//				.parseExcluded(openFileInput("excludedUpdated.csv"));
//				System.out.println("excludedparsed");
//				masterList
//				.parseRestricted(openFileInput("restrictedUpdated.csv"));
//			} else {
//				// use asset files to parse if no files were downloaded
//				System.out.println("parser from default files");
//				masterList.parseFormulary(assetManager
//						.open("formulary.csv"));
//				System.out.println("formularyparsed");
//				masterList.parseExcluded(assetManager.open("excluded.csv"));
//				System.out.println("excludedparsed");
//				masterList.parseRestricted(assetManager
//						.open("restricted.csv"));
//			}
//			System.out.println("parsingdidntbreak");
//		} catch (IOException e) {
//			Toast.makeText(
//					this,
//					"An error has caused this app to malfunction. "
//							+ "Please ensure there is enough memory on the phone, network is "
//							+ "present or re-install the app",
//							Toast.LENGTH_LONG).show();
//			e.printStackTrace();
//		}
//
//		genericList = masterList.getListByGeneric();
//		brandList = masterList.getListByBrand();
//
//	}

	private ArrayList<String> preparePredictiveText() {

		// make master nameList
		ArrayList<String> masterDrugNameList = genericList.getGenericNameList(); // add all the generic names
		ArrayList<String> brandNameList = brandList.getBrandNameList();

		for (String brandName : brandNameList) {
			// only add brand names if they don't already appear
			if (!(masterDrugNameList.contains(brandName))) { 
				masterDrugNameList.add(brandName);
			}
		}
		
		Collections.sort(masterDrugNameList);
		
		return masterDrugNameList;

	}
	
	private void setUpAutoComplete(ArrayList<String> autoCompleteDrugNameList) {
		autocompletetextview = (AutoCompleteTextView) findViewById(R.id.search_input);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, autoCompleteDrugNameList);
		autocompletetextview.setThreshold(1);
		autocompletetextview.setAdapter(adapter);

		// hide keyboard after selection
		autocompletetextview
		.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.toggleSoftInput(
                        InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }

        });
		
	}
	
	
	// Display the drug result being searched for when Search button is pressed
	public void displayResult(View view) throws Exception {
		EditText editText = (EditText) findViewById(R.id.search_input);
		String searchInput = editText.getText().toString().toUpperCase().trim();
		Drug drug = null;
		String type = null;

		//TODO check if search input is valid drug;

//		SqlHelper sqlHelper = new SqlHelper(this);
		DrugBase drugResult = mSqlHelper.queryDrug(searchInput);
		Log.d(TAG, "Drug found " + drugResult);
//
//		if (genericList.containsGenericName(searchInput)) {
//			drug = genericList.getGenericDrug(searchInput);
//			type = "Generic";
//			System.out.println("checkdrug");
//
//			if (drug.getStatus().equals("Formulary")) {
//				Intent formularyResult = new Intent(this,
//						DisplayFormularyResult.class);
//				GenericFormularyDrug fdrug = (GenericFormularyDrug) drug;
//				formularyResult.putExtra(Utilities.EXTRA_GENERICNAME,
//						fdrug.getGenericName());
//				formularyResult.putStringArrayListExtra(
//						Utilities.EXTRA_BRANDNAME, fdrug.getBrandNames());
//				formularyResult.putStringArrayListExtra(
//						Utilities.EXTRA_STRENGTHS, fdrug.getStrengths());
//				formularyResult.putExtra(Utilities.EXTRA_TYPE, type);
//				startActivity(formularyResult);
//				System.out.println("startedactivity");
//
//			} else if (drug.getStatus().equals("Restricted")) {
//				Intent restrictedResult = new Intent(this,
//						DisplayRestrictedResult.class);
//				GenericRestrictedDrug rdrug = (GenericRestrictedDrug) drug;
//				restrictedResult.putExtra(Utilities.EXTRA_GENERICNAME,
//						rdrug.getGenericName());
//				restrictedResult.putStringArrayListExtra(
//						Utilities.EXTRA_BRANDNAME, rdrug.getBrandNames());
//				restrictedResult.putExtra(Utilities.EXTRA_RESTRICTIONS,
//						rdrug.getCriteria());
//				restrictedResult.putExtra(Utilities.EXTRA_TYPE, type);
//				startActivity(restrictedResult);
//
//			} else if (drug.getStatus().equals("Excluded")) {
//				Intent excludedResult = new Intent(this,
//						DisplayExcludedResult.class);
//				GenericExcludedDrug edrug = (GenericExcludedDrug) drug;
//				excludedResult.putExtra(Utilities.EXTRA_GENERICNAME,
//						edrug.getGenericName());
//				excludedResult.putStringArrayListExtra(
//						Utilities.EXTRA_BRANDNAME, edrug.getBrandNames());
//				excludedResult.putExtra(Utilities.EXTRA_EXCLUDED_REASON,
//						edrug.getCriteria());
//				excludedResult.putExtra(Utilities.EXTRA_TYPE, type);
//				startActivity(excludedResult);
//			}
//
//		} else if (brandList.containsBrandName(searchInput)) {
//			drug = brandList.getBrandDrug(searchInput);
//			type = "Brand";
//
//			if (drug.getStatus().equals("Formulary")) {
//				Intent formularyResult = new Intent(this,
//						DisplayFormularyResult.class);
//				BrandFormularyDrug fdrug = (BrandFormularyDrug) drug;
//				formularyResult.putExtra(Utilities.EXTRA_GENERICNAME,
//						fdrug.getGenericNames());
//				formularyResult.putExtra(Utilities.EXTRA_BRANDNAME,
//						fdrug.getBrandName());
//				formularyResult.putStringArrayListExtra(
//						Utilities.EXTRA_STRENGTHS, fdrug.getStrengths());
//				formularyResult.putExtra(Utilities.EXTRA_TYPE, type);
//				startActivity(formularyResult);
//
//			} else if (drug.getStatus().equals("Restricted")) {
//				Intent restrictedResult = new Intent(this,
//						DisplayRestrictedResult.class);
//				BrandRestrictedDrug rdrug = (BrandRestrictedDrug) drug;
//				restrictedResult.putStringArrayListExtra(
//						Utilities.EXTRA_GENERICNAME, rdrug.getGenericNames());
//				restrictedResult.putExtra(Utilities.EXTRA_BRANDNAME,
//						rdrug.getBrandName());
//				restrictedResult.putExtra(Utilities.EXTRA_RESTRICTIONS,
//						rdrug.getCriteria());
//				restrictedResult.putExtra(Utilities.EXTRA_TYPE, type);
//				startActivity(restrictedResult);
//
//			} else if (drug.getStatus().equals("Excluded")) {
//				Intent excludedResult = new Intent(this,
//						DisplayExcludedResult.class);
//				BrandExcludedDrug edrug = (BrandExcludedDrug) drug;
//				excludedResult.putStringArrayListExtra(
//						Utilities.EXTRA_GENERICNAME, edrug.getGenericNames());
//				excludedResult.putExtra(Utilities.EXTRA_BRANDNAME,
//						edrug.getBrandName());
//				excludedResult.putExtra(Utilities.EXTRA_EXCLUDED_REASON,
//						edrug.getCriteria());
//				excludedResult.putExtra(Utilities.EXTRA_TYPE, type);
//				startActivity(excludedResult);
//			}
//
//		} else {
//			Intent otherResult = new Intent(this, DisplayOtherResult.class);
//			otherResult.putExtra(Utilities.EXTRA_INFO, searchInput);
//			startActivity(otherResult);
//		}

	}

	public void displayHelp(View view) throws Exception {
		final ArrayList<ExcludedDrug> drugList = new ArrayList<ExcludedDrug>();
		DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
		firebase.child("Excluded").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				System.out.println(dataSnapshot.toString());
				ExcludedDrug drug = dataSnapshot.getValue(ExcludedDrug.class);
				drugList.add(drug);
				System.out.println(drugList.size() + ":" + drug.primaryName);
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
	}

    public void firebase(View view){
//        RefactoredParser masterList = new RefactoredParser();
//        try {
//			//formulary===============================================================================
////			InputStream file = getAssets().open("formulary class 31Mar2016.csv");
//
////            masterList.parseFormulary(file);
////			System.out.println("Forumarly drug count: " + masterList.getFormularyList().size());
////
////			DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
////			ArrayList<FormularyDrug> formularyList = new ArrayList<FormularyDrug>(masterList.getFormularyList().values());
////			int multiplier = 1;
////			for(int i = 0; i < formularyList.size(); i++){
////				FormularyDrug drug = formularyList.get(i);
////
////				// only 75 items could be added to firebase using setValue alone
////				// HACK - pause adding drugs to firebase every 50 items to allow for mass adding
////				if(i * multiplier % 50 == 0){
////					try {
////						Thread.sleep(1000);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////				}
////				try {
////					firebase.child("Formulary").child(String.valueOf(i)).setValue(drug);
////				}
////				catch (Exception e){
////						System.out.println(drug.primaryName + " was not added.");
////						System.out.println(e.getMessage());
////				}
////			}
//			//excluded===============================================================================
////			InputStream file = getAssets().open("excluded class 31Mar2016.csv");
////			masterList.parseExcluded(file);
////			System.out.println("Excluded drug count: " + masterList.getExcludedList().size());
////
////			DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
////			ArrayList<ExcludedDrug> excludedList = new ArrayList<ExcludedDrug>(masterList.getExcludedList().values());
////			int multiplier = 1;
////			for(int i = 0; i < excludedList.size(); i++){
////				ExcludedDrug drug = excludedList.get(i);
////
////				// only 75 items could be added to firebase using setValue alone
////				// HACK - pause adding drugs to firebase every 50 items to allow for mass adding
////				if(i * multiplier % 50 == 0){
////					try {
////						Thread.sleep(1000);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////				}
////				try {
////					firebase.child("Excluded").child(String.valueOf(i)).setValue(drug);
////				}
////				catch (Exception e){
////						System.out.println(drug.primaryName + " was not added.");
////						System.out.println(e.getMessage());
////				}
////			}
////        }
////        catch (IOException e){
////            Log.d("Formulary parse error", e.getMessage());
////        }
//			//restricted============================================================================
//			InputStream file = getAssets().open("restricted class 31Mar2016.csv");
//			masterList.parseRestricted(file);
//			System.out.println("Restricted drug count: " + masterList.getRestrictedList().size());
//
//			DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
//			ArrayList<RestrictedDrug> restrictedList = new ArrayList<RestrictedDrug>(masterList.getRestrictedList().values());
//			int multiplier = 1;
//			for(int i = 0; i < restrictedList.size(); i++){
//				RestrictedDrug drug = restrictedList.get(i);
//
//				// only 75 items could be added to firebase using setValue alone
//				// HACK - pause adding drugs to firebase every 50 items to allow for mass adding
//				if(i * multiplier % 50 == 0){
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				try {
//					firebase.child("Restricted").child(String.valueOf(i)).setValue(drug);
//				}
//				catch (Exception e){
//					System.out.println(drug.primaryName + " was not added.");
//					System.out.println(e.getMessage());
//				}
//			}
//		}
//		catch (IOException e){
//			Log.d("Formulary parse error", e.getMessage());
//		}
//
//
    }

}
