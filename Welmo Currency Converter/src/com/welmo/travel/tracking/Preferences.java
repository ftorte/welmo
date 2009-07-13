package com.welmo.travel.tracking;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;


public class Preferences extends PreferenceActivity {

    @Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        PreferenceScreen root = this.getPreferenceScreen();
        //	etPreferenceManager().get.createPreferenceScreen(this);

        
     // Launch preferences
        PreferenceCategory launchPrefCat = new PreferenceCategory(this);
        launchPrefCat.setTitle("Target Currencies & Countires");
        root.addPreference(launchPrefCat);
        
        // Intent preference
        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
        Intent i = new Intent(this, ImageCheckList.class);
        intentPref.setIntent(i);
        intentPref.setTitle("Target Currencies & Countires");
        intentPref.setSummary("Select/Deselect target(s)");
        launchPrefCat.addPreference(intentPref);
    }
}