package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.BrandDrugList;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.BrandExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.BrandFormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.BrandRestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Drug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.GenericDrugList;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.GenericExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.GenericFormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.GenericRestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.R;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.CSVparser;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends Activity {

	SharedPreferences settings;
	GenericDrugList genericList;
	BrandDrugList brandList;

	CSVparser masterList = null;
	public AssetManager assetManager;
	
	AutoCompleteTextView autocompletetextview;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		
		System.out.println("search activity started");

		// Write a message to the database
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference("message");

		myRef.setValue("Hello, World!");
		
//		beginParsing();
		
//		ArrayList<String> autoCompleteDrugNameList = preparePredictiveText();
//		setUpAutoComplete(autoCompleteDrugNameList);

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

	private void beginParsing() {

		settings = getApplicationContext().getSharedPreferences("foo", 0);
		masterList = new CSVparser();
		System.out.println("initparser");

		try {
			// get value of filesDownloaded, false if not found
			if (settings.getBoolean("filesDownloaded", false)) {
				// use downloaded files to parse
				System.out.println("parser from updated files");
				masterList
				.parseFormulary(openFileInput("formularyUpdated.csv"));
				System.out.println("formularyparsed");
				masterList
				.parseExcluded(openFileInput("excludedUpdated.csv"));
				System.out.println("excludedparsed");
				masterList
				.parseRestricted(openFileInput("restrictedUpdated.csv"));
			} else {
				// use asset files to parse if no files were downloaded
				System.out.println("parser from default files");
				masterList.parseFormulary(assetManager
						.open("formulary.csv"));
				System.out.println("formularyparsed");
				masterList.parseExcluded(assetManager.open("excluded.csv"));
				System.out.println("excludedparsed");
				masterList.parseRestricted(assetManager
						.open("restricted.csv"));
			}
			System.out.println("parsingdidntbreak");
		} catch (IOException e) {
			Toast.makeText(
					this,
					"An error has caused this app to malfunction. "
							+ "Please ensure there is enough memory on the phone, network is "
							+ "present or re-install the app",
							Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		genericList = masterList.getListByGeneric();
		brandList = masterList.getListByBrand();

	}

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

		if (genericList.containsGenericName(searchInput)) {
			drug = genericList.getGenericDrug(searchInput);
			type = "Generic";
			System.out.println("checkdrug");

			if (drug.getStatus().equals("Formulary")) {
				Intent formularyResult = new Intent(this,
						DisplayFormularyResult.class);
				GenericFormularyDrug fdrug = (GenericFormularyDrug) drug;
				formularyResult.putExtra(Utilities.EXTRA_GENERICNAME,
						fdrug.getGenericName());
				formularyResult.putStringArrayListExtra(
						Utilities.EXTRA_BRANDNAME, fdrug.getBrandNames());
				formularyResult.putStringArrayListExtra(
						Utilities.EXTRA_STRENGTHS, fdrug.getStrengths());
				formularyResult.putExtra(Utilities.EXTRA_TYPE, type);
				startActivity(formularyResult);
				System.out.println("startedactivity");

			} else if (drug.getStatus().equals("Restricted")) {
				Intent restrictedResult = new Intent(this,
						DisplayRestrictedResult.class);
				GenericRestrictedDrug rdrug = (GenericRestrictedDrug) drug;
				restrictedResult.putExtra(Utilities.EXTRA_GENERICNAME,
						rdrug.getGenericName());
				restrictedResult.putStringArrayListExtra(
						Utilities.EXTRA_BRANDNAME, rdrug.getBrandNames());
				restrictedResult.putExtra(Utilities.EXTRA_RESTRICTIONS,
						rdrug.getCriteria());
				restrictedResult.putExtra(Utilities.EXTRA_TYPE, type);
				startActivity(restrictedResult);

			} else if (drug.getStatus().equals("Excluded")) {
				Intent excludedResult = new Intent(this,
						DisplayExcludedResult.class);
				GenericExcludedDrug edrug = (GenericExcludedDrug) drug;
				excludedResult.putExtra(Utilities.EXTRA_GENERICNAME,
						edrug.getGenericName());
				excludedResult.putStringArrayListExtra(
						Utilities.EXTRA_BRANDNAME, edrug.getBrandNames());
				excludedResult.putExtra(Utilities.EXTRA_EXCLUDED_REASON,
						edrug.getCriteria());
				excludedResult.putExtra(Utilities.EXTRA_TYPE, type);
				startActivity(excludedResult);
			}

		} else if (brandList.containsBrandName(searchInput)) {
			drug = brandList.getBrandDrug(searchInput);
			type = "Brand";

			if (drug.getStatus().equals("Formulary")) {
				Intent formularyResult = new Intent(this,
						DisplayFormularyResult.class);
				BrandFormularyDrug fdrug = (BrandFormularyDrug) drug;
				formularyResult.putExtra(Utilities.EXTRA_GENERICNAME,
						fdrug.getGenericNames());
				formularyResult.putExtra(Utilities.EXTRA_BRANDNAME,
						fdrug.getBrandName());
				formularyResult.putStringArrayListExtra(
						Utilities.EXTRA_STRENGTHS, fdrug.getStrengths());
				formularyResult.putExtra(Utilities.EXTRA_TYPE, type);
				startActivity(formularyResult);

			} else if (drug.getStatus().equals("Restricted")) {
				Intent restrictedResult = new Intent(this,
						DisplayRestrictedResult.class);
				BrandRestrictedDrug rdrug = (BrandRestrictedDrug) drug;
				restrictedResult.putStringArrayListExtra(
						Utilities.EXTRA_GENERICNAME, rdrug.getGenericNames());
				restrictedResult.putExtra(Utilities.EXTRA_BRANDNAME,
						rdrug.getBrandName());
				restrictedResult.putExtra(Utilities.EXTRA_RESTRICTIONS,
						rdrug.getCriteria());
				restrictedResult.putExtra(Utilities.EXTRA_TYPE, type);
				startActivity(restrictedResult);

			} else if (drug.getStatus().equals("Excluded")) {
				Intent excludedResult = new Intent(this,
						DisplayExcludedResult.class);
				BrandExcludedDrug edrug = (BrandExcludedDrug) drug;
				excludedResult.putStringArrayListExtra(
						Utilities.EXTRA_GENERICNAME, edrug.getGenericNames());
				excludedResult.putExtra(Utilities.EXTRA_BRANDNAME,
						edrug.getBrandName());
				excludedResult.putExtra(Utilities.EXTRA_EXCLUDED_REASON,
						edrug.getCriteria());
				excludedResult.putExtra(Utilities.EXTRA_TYPE, type);
				startActivity(excludedResult);
			}

		} else {
			Intent otherResult = new Intent(this, DisplayOtherResult.class);
			otherResult.putExtra(Utilities.EXTRA_INFO, searchInput);
			startActivity(otherResult);
		}

	}

	public void displayHelp(View view) throws Exception {
		Intent helpResult = new Intent(this, HelpActivity.class);
		startActivity(helpResult);
	}

    public void firebase(View view){
        CSVparser masterList = new CSVparser();
        try {
//            masterList.parseFormulary(getAssets().open("formulary class 31Mar2016.csv"));
            masterList.parseExcluded(getAssets().open("excluded class 31Mar2016.csv"));
//            masterList.parseRestricted(getAssets().open("restricted class 31Mar2016.csv"));

            Log.d("Formuarly parsed", "Count: " + masterList.getListByGeneric().getDrugListSize());

            HashMap<String, Drug> formularyList = new HashMap<String, Drug>();
            HashMap<String, Drug> excludedList = new HashMap<String, Drug>();
            HashMap<String, Drug> restrictedList = new HashMap<String, Drug>();

            for(Map.Entry<String, Drug> drug :masterList.getListByGeneric().getGenericDrugList().entrySet()){
                String drugName = drug.getKey();
                Drug drugObj = drug.getValue();

                if(drugObj.getStatus().equals("Formulary") && !drugName.equals("")){
                    formularyList.put(UUID.randomUUID().toString(), drugObj);
//                    formularyList.put(drugName, drugObj);
                }
                if(drugObj.getStatus().equals("Excluded") && !drugName.equals("")){
                    excludedList.put(UUID.randomUUID().toString(), drugObj);
//                    excludedList.put(drugName, drugObj);
                }
                if(drugObj.getStatus().equals("Restricted") && !drugName.equals("")){
                    restrictedList.put(UUID.randomUUID().toString(), drugObj);
//                    restrictedList.put(drugName, drugObj);
                }
            }

            for(Map.Entry<String, Drug> drug :masterList.getListByBrand().getBrandDrugList().entrySet()){
                String drugName = drug.getKey();
                Drug drugObj = drug.getValue();

                if(drugObj.getStatus().equals("Formulary") && !drugName.equals("")){
                    formularyList.put(UUID.randomUUID().toString(), drugObj);
//                    formularyList.put(drugName, drugObj);
                }
                if(drugObj.getStatus().equals("Excluded") && !drugName.equals("")){
                    excludedList.put(UUID.randomUUID().toString(), drugObj);
//                    excludedList.put(drugName, drugObj);
                }
                if(drugObj.getStatus().equals("Restricted") && !drugName.equals("")){
                    restrictedList.put(UUID.randomUUID().toString(), drugObj);
//                    restrictedList.put(drugName, drugObj);
                }
            }

            Log.d("Formulary", "Count: " + formularyList.size());
            Log.d("Excluded", "Count: " + excludedList.size());
            Log.d("Restricted", "Count: " + restrictedList.size());

            Firebase firebase = new Firebase(Utilities.firebaseRoot);
            firebase.child("Formulary").setValue(formularyList);
            firebase.child("Excluded").setValue(excludedList);
            firebase.child("Restricted").setValue(restrictedList);
            firebase.child("Update").setValue(new Date());
        }
        catch (IOException e){
            Log.d("Formulary parse error", e.getMessage());
        }

    }

}
