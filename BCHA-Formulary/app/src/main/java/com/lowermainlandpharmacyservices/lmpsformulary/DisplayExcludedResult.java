package com.lowermainlandpharmacyservices.lmpsformulary;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayExcludedResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_excluded_result);
		getActionBar().hide();

		Intent intent = getIntent();
		String type = intent.getStringExtra(Utilities.EXTRA_TYPE);
		String name = "";
		ArrayList<String> otherNames;
		String otherNamesAsString = "";
		String genericbrandtitle = "";

		if (type.equals("Generic")) {
			genericbrandtitle = "Brand Names:";
			name = intent.getStringExtra(Utilities.EXTRA_GENERICNAME);
			otherNames = intent
					.getStringArrayListExtra(Utilities.EXTRA_BRANDNAME);
			System.out.println("gothere");

//			for (String s : otherNames) {
//				otherNamesAsString += "\t" + "\t" + "- " + s + "\n";
//			}

			if(!otherNames.isEmpty() && !otherNames.get(0).trim().equals("")){
				for (String s : otherNames) {
					otherNamesAsString += "\t" + "\t" + "- " + s + "\n";
				}
			}
			else {
				otherNamesAsString += "\t" + "\t" + "\t" + "No alternative names" + "\n";
			}
		} else if (type.equals("Brand")) {
			genericbrandtitle = "Generic Names:";
			name = intent.getStringExtra(Utilities.EXTRA_BRANDNAME);
			otherNames = intent
					.getStringArrayListExtra(Utilities.EXTRA_GENERICNAME);

//			for (String s : otherNames) {
//				otherNamesAsString += "\t" + "\t" + "- " + s + "\n";
//			}

			if(!otherNames.isEmpty() && !otherNames.get(0).trim().equals("")){
				for (String s : otherNames) {
					otherNamesAsString += "\t" + "\t" + "- " + s + "\n";
				}
			}
			else {
				otherNamesAsString += "\t" + "\t" + "\t" + "No alternative names" + "\n";
			}
		}

		// Search Name
		TextView genericNameTextView = (TextView) findViewById(R.id.excluded_drug_genericname);
		genericNameTextView.setText("\t" + name);
		genericNameTextView.setTypeface(null, Typeface.BOLD);

		TextView statusTextView = (TextView) findViewById(R.id.excluded);
		statusTextView.setText("\t" + "\t" + "EXCLUDED");
		statusTextView.setTextSize(20);
		statusTextView.setTextColor(Color.parseColor("#CC0000"));
		statusTextView.setTypeface(null, Typeface.BOLD);

		// Other Names
		TextView nameHeaderTextView = (TextView) findViewById(R.id.generic_or_brand);
		nameHeaderTextView.setText(genericbrandtitle);

		TextView brandNameTextView = (TextView) findViewById(R.id.excluded_brandnames);
		brandNameTextView.setText(otherNamesAsString);
		brandNameTextView.setTextSize(20);

		// Restriction Criteria
		String criteria = intent
				.getStringExtra(Utilities.EXTRA_EXCLUDED_REASON);
		TextView criteriaTextView = (TextView) findViewById(R.id.exclusion_criteria);
		criteriaTextView.setText(criteria);
		criteriaTextView.setTextSize(15);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_excluded_result, menu);
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
}
