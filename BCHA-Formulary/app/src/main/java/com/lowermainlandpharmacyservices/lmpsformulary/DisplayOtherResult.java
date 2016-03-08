package com.lowermainlandpharmacyservices.lmpsformulary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DisplayOtherResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_other_result);
		getActionBar().hide();

		Intent intent = getIntent();
		String searchInput = intent.getStringExtra(Utilities.EXTRA_INFO);

		TextView topTextView = (TextView) findViewById(R.id.notfound);
		topTextView.setTypeface(null, Typeface.BOLD);

		TextView inputView = (TextView) findViewById(R.id.drugnotfound);
		inputView.setText("Sorry, " + searchInput + " was not found.");
		inputView.setTypeface(null, Typeface.BOLD);

		TextView descriptionView = (TextView) findViewById(R.id.description);
		descriptionView
				.setText("This drug appears to be a non-formulary drug. Please check your spelling and try again."
						+ "\n"
						+ "\n"
						+ "It may be undergoing a formal review to be included on the formulary at a later time. To view the full drug inventory, sorted alphabetically by status, download using the following button:"
						+ "\n");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_other_result, menu);
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

	public void viewTable(View view) {
		Intent browserIntent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AADoJ9wfl8c67vIFjdSuBXOYa/full%20list.pdf?dl=1"));
		startActivity(browserIntent);
	}
}
