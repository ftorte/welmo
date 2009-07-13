package com.welmo.travel.tracking;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


public class CustomListPreference extends ListPreference{


	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		this.setSummary(getEntry());
		return super.onCreateView(parent);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		this.setSummary(getEntry());
	}
	private String mSummaryString;

	// This is the constructor called by the inflater
	public CustomListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setSummary(getPersistedString(""));
	}
}
