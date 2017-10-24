package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.R;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SqlHelper;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
	SqlHelper mSqlHelper;
	
	AutoCompleteTextView autocompletetextview;
	List<String> drugNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSqlHelper = new SqlHelper(this);
		drugNames = mSqlHelper.getAllDrugNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item,  drugNames);
		autocompletetextview = (AutoCompleteTextView) findViewById(R.id.search_input);
		autocompletetextview.setAdapter(adapter);

		//disable keyboard on autocomplete
		autocompletetextview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Log.d(TAG, "Clicked");
				// Check if no view has focus:
				View v = MainActivity.this.getCurrentFocus();
				if (v != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});

		//disable keyboard on non edit text tap
		autocompletetextview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
//					hideKeyboard(v);
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.hide();
	}

	@Override
	public void onStop() {
		super.onStop();
		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.show();
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

	// Display the drug result being searched for when Search button is pressed
	public void displayResult(View view) throws Exception {
		EditText editText = (EditText) findViewById(R.id.search_input);
		String searchInput = editText.getText().toString().toUpperCase().trim();

		if (searchInput.isEmpty()) {
			return;
		}

		//TODO check if search input is valid drug;
		DrugBase drugResult = mSqlHelper.queryDrug(searchInput);
		if (drugResult != null) {
			Log.d(TAG, "Drug found " + drugResult);
			Intent intent = new Intent(this, ResultsActivity.class);
			Gson gson = new Gson();
			String drugJson = gson.toJson(drugResult);
			intent.putExtra(ResultsActivity.DRUG_INTENT, drugJson);
			startActivity(intent);
		} else {
			Log.e(TAG, "Drug not found: " + searchInput);
			Intent intent = new Intent(this, DisplayOtherResult.class);
			intent.putExtra(DisplayOtherResult.DRUG_NOT_FOUND, searchInput);
			startActivity(intent);
		}
	}

	public void displayHelp(View view) throws Exception {
		Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
	}
}
