package com.lowermainlandpharmacyservices.lmpsformulary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FirstTimeActivity extends Activity {

	SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().hide();

		sharedPref = this.getSharedPreferences("foo",Context.MODE_PRIVATE);
	}

	public void authorizeDevice(View view) {
		EditText input = (EditText) findViewById(R.id.keyphrase_input);
		String passwordAttempt = input.getText().toString();

		if (passwordAttempt.equalsIgnoreCase(Utilities.password)) {
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putBoolean(Utilities.authorizedUser, true);
			editor.commit();
			
			setResult(RESULT_OK);
			finish();
			
		} else {
			Toast.makeText(this,
					"Please try again.",
					Toast.LENGTH_LONG).show();
			input.setText("");
			return;
		}
	}
	
	@Override
	public void onBackPressed(){
		finish();
		System.exit(0);
	}
}
