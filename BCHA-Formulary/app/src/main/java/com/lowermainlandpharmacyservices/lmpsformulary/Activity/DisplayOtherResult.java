package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lowermainlandpharmacyservices.lmpsformulary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayOtherResult extends Activity {

	public static final String DRUG_NOT_FOUND = "drugNotFoundNameExtra";
	@BindView(R.id.drugNotFound) TextView drugNotFoundName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_other_result);
		ButterKnife.bind(this);

		Intent intent = getIntent();
		String searchInput = intent.getStringExtra(DRUG_NOT_FOUND);

		String message = String.format("Sorry, %s was not found.", searchInput);
		drugNotFoundName.setText(message);
	}

	@Override
	public void onStart() {
		super.onStart();
		getActionBar().hide();
	}

	@Override
	public void onStop() {
		super.onStop();
		getActionBar().show();
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
}
