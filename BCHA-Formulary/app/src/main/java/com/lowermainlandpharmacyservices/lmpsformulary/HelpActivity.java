package com.lowermainlandpharmacyservices.lmpsformulary;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getActionBar().hide();

		String tosearch = "To search for the formulary status of a drug, enter the generic or brand name of the drug"
				+ "\n" + "\n";
		String searchresults = "Search results will depend on formulary status:";
		String resultscont = "\t"
				+ "- Formulary drugs: all strengths and formulations available in FH"
				+ "\n";
		resultscont += "\t"
				+ "- Restricted drugs: restriction criteria for approved indications, populations, patient care areas, and prescribers in FH"
				+ "\n";
		resultscont += "\t"
				+ "- Excluded drugs: reason for BCHA provincial formulary exclusion";

		String update = "The app will auto-update every time it is open and connected to the Internet."
				+ "\n" + "\n";
		String searchtips = "Search tips:";
		String tipscont = "\t"
				+ "- Combination drug products can be found by searching for any individual component alone or for the brand name of the entire product."
				+ "\n";
		tipscont += "\t"
				+ "- A full list of all vaccines will appear when you search for \" vaccine\". Similarly for \"multivitamins\" and \"lipid\""
				+ "\n";
		tipscont += "\t"
				+ "- If you are unable to find a medication, use the \"Download Formulary\" button to browse a list of all formulary, restricted, and excluded drugs";
		String createdby = "The BCHA Formulary App was created by Jessica Yeung and Kelvin Chan, with support from FH Medication  Use Evaluation"
				+ "\n" + "\n";

		String disclaimer = "Disclaimer:"
				+ "\n"
				+ "The information contained in this document, and as amended from time to time, was created expressly for use by the Lower Mainland Pharmacy Services and persons acting on behalf of the Lower Mainland Pharmacy Services for guiding actions and decisions taken on behalf of Lower Mainland Pharmacy Services. Any adoption/use/modification of this document are done so at the risk of the adopting organization. Lower Mainland Pharmacy Services accepts no responsibility for any modification and/or redistribution and is not liable in any way for any actions taken by individuals based on the information herein, or for any inaccuracies, errors, or omissions in the information in this document. Local health authorities may have additional restrictions to the use of this drug.";

		String questions = "Questions and comments:";

		TextView helpTextView = (TextView) findViewById(R.id.help_text);
		helpTextView.setText(tosearch + searchresults);
		helpTextView.setTypeface(null, Typeface.BOLD);

		TextView two = (TextView) findViewById(R.id.help_two);
		two.setText(resultscont);

		TextView three = (TextView) findViewById(R.id.help_three);
		three.setText(update + searchtips);
		three.setTypeface(null, Typeface.BOLD);

		TextView four = (TextView) findViewById(R.id.help_four);
		four.setText(tipscont);

		TextView five = (TextView) findViewById(R.id.help_five);
		five.setText(questions);
		five.setTypeface(null, Typeface.BOLD);

		TextView mailLink = (TextView) findViewById(R.id.mail_link);
		mailLink.setMovementMethod(LinkMovementMethod.getInstance());

		TextView footer = (TextView) findViewById(R.id.footer);
		footer.setText(createdby + disclaimer);
		footer.setTypeface(null, Typeface.BOLD);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
		// Questions and comments: <a
		// href="mailto:anthony.tung@fraserhealth.ca?subject=formulary app question">anthony.tung@fraserhealth.ca</a>

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