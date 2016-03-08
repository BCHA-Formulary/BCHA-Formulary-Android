package com.lowermainlandpharmacyservices.lmpsformulary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

	SharedPreferences settings;
	SharedPreferences.Editor editor;

	CSVparser masterList = null;
	GenericDrugList genericList;
	BrandDrugList brandList;
	
	int PAUSE_MILLISECONDS = 500;

	public AssetManager assetManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		getActionBar().hide();

		settings = getApplicationContext().getSharedPreferences("foo", 0);

		//pause to see the pretty splash screen
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				if(!settings.getBoolean(Utilities.authorizedUser, false)){
					Intent validationActivity = new Intent(SplashScreenActivity.this, FirstTimeActivity.class);
					startActivityForResult(validationActivity, 1);
				} 
				else {
					initializeApp();
					finish();
				}
			}
		}, PAUSE_MILLISECONDS);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 1 && resultCode == RESULT_OK)
		{
			initializeApp();
			finish();
		}
	}

	private void initializeApp(){
		assetManager = getAssets();

		boolean updateRequired = (this.getCurrentFileVersion().equals(this.getLatestFileVersion())) ? false : true;

		if (updateRequired) {
			performUpdate();
		}

		System.out.println("Initializing app in splash screen");

		Intent searchActivity = new Intent(this, MainActivity.class);
		startActivity(searchActivity);
		System.out.println("startedmainactivity");

	}

	private String getCurrentFileVersion() {

		FileInputStream fis = null;
		String currVersion;
		BufferedReader reader;
		String line;

		editor = settings.edit();

		try {
			// check if any updates have been done before
			if (settings.contains("filesDownloaded")) {
				fis = openFileInput("fileVersion.txt");
				reader = new BufferedReader(new InputStreamReader(fis));
				line = reader.readLine();
				currVersion = line;
				System.out.println("current version is " + line);
				fis.close();
			} else {
				currVersion = "";
			}
		}
		catch (Exception e)
		{
			currVersion = "-2";

		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return currVersion;
	}

	private String getLatestFileVersion() {

		FileInputStream fis = null;
		String newVersion;
		BufferedReader reader;
		String line;

		DownloadTask fileVersion = new DownloadTask(SplashScreenActivity.this,
				"fileVersion.txt");

		try {

			fileVersion
			.execute(
					"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AAD2BXYQ0oB-i1RLnCYAnA7na/update.txt?dl=1").get();

			fis = openFileInput("fileVersion.txt");
			reader = new BufferedReader(new InputStreamReader(fis));
			line = reader.readLine();
			newVersion = line;

		} catch (Exception e) {
			newVersion = "-3";
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return newVersion;
	}


	private void performUpdate() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();


		System.out.println("We need an update!");
		Toast.makeText(this, "File update in progress",
				Toast.LENGTH_LONG).show();


		// if network is on
		if (isConnected) {

			try{
				final DownloadTask downloadFormulary = new DownloadTask(
						SplashScreenActivity.this, "formularyUpdated.csv");
				downloadFormulary
				.execute(
						"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AABotiW6CP_-JrGAh0mw1nkma/formulary.csv?dl=1").get();
				final DownloadTask downloadExcluded = new DownloadTask(
						SplashScreenActivity.this, "excludedUpdated.csv");
				downloadExcluded
				.execute(
						"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AAAh2jkw2watr9KpopeH_JUsa/excluded.csv?dl=1").get();
				final DownloadTask downloadRestricted = new DownloadTask(
						SplashScreenActivity.this, "restrictedUpdated.csv");
				downloadRestricted
				.execute(
						"https://www.dropbox.com/sh/ctdjnxoemlx9hbr/AACa_xqMx2PZWMoWKe5tJoRda/restricted.csv?dl=1").get();

				// We need an Editor object to make preference changes.
				// All objects are from android.context.Context

				editor.putBoolean("filesDownloaded", true);
				editor.commit();
				Toast.makeText(this, "Update completed", Toast.LENGTH_LONG)
				.show();
			}
			catch(Exception e)
			{

			}
		} else { // if network is off
			Toast.makeText(
					this,
					"A version update is available, please connect to wi-fi "
							+ "and restart to app to update",
							Toast.LENGTH_LONG).show();
		}

	}

}
