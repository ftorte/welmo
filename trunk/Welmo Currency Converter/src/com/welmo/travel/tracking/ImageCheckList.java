package com.welmo.travel.tracking;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.welmo.contents.ExpensesManager;
import com.welmo.contents.ExpensesManager.CurrenciesISO;
import com.welmo.travel.tracking.CountryListAdapter.Country;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This example shows how to use choice mode on a list. This list is 
 * in CHOICE_MODE_MULTIPLE mode, which means the items behave like
 * checkboxes.
 */
public class ImageCheckList extends ListActivity {

	private CountryListAdapter adapter=null;
	private Button confirmButton = null;
	private Button cancelButton  = null;
	private ToggleButton toggleActiveOnly = null;
	private EditText mFilterText = null;
	private String[] mConstraints = new String[2];
	//private  ArrayList<ContentValues> theContentValues = new ArrayList<ContentValues>();	
	
	final private static Uri theUri = ExpensesManager.CurrenciesISO.CONTENT_URI; 
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(adapter == null)
			throw (new IllegalArgumentException("Invalid adapter null pointer")); 
		Country ctr = (Country) adapter.getItem(position);
		if(ctr.selected)
			ctr.selected=false;
		else
			ctr.selected=true;
		ctr.changed = true;
		adapter.changeItem(ctr, position);
	}

	void ActiveFilter(){
		adapter.filterComplex(mConstraints);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listcheck);
		confirmButton = (Button) findViewById(R.id.ButtonConfirm);
		cancelButton = (Button) findViewById(R.id.ButtonCancel);
		toggleActiveOnly = (ToggleButton) findViewById(R.id.ToggleActiveOnly);
		//read current configured source  
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String[] soruce_cfg  = sp.getString(getResources().getString(R.string.prfkey_source_curr), 	"Euro Zone,EU,EUR,EUR").split(",", 4);
		
		Cursor c = getContentResolver().query(Uri.parse( "content://com.welmo.contents.providers.expenses/currcountryISO/all/" + soruce_cfg[2]),
				null, null, null, CurrenciesISO.COUNTRYNAME);        
		// Setup adapter
		adapter = new CountryListAdapter(this);
		adapter.readCurrencyFromCursor(c);
		c.close();
		setListAdapter(adapter);  
		getListView().setTextFilterEnabled(true);

		this.

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				boolean flag=false;
				ContentValues newcv = new ContentValues();	
				Iterator<Country> it = adapter.mCountryList.iterator();
				while(it.hasNext()){
					Country ct = it.next();
					if(ct.changed){
						newcv.put(CurrenciesISO.SELECTED,(ct.selected?1:0));
						getContentResolver().update(theUri,newcv,CurrenciesISO.COUNTRYCODE + " = \"" + ct.countrycode + "\"",null);
						flag = true;
					}
				}
				if(flag){
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getContext());
					Editor ed = sp.edit();
					Calendar cl = Calendar.getInstance();
					ed.putString("test_key", cl.getTime().toGMTString());
					ed.commit();
				}
				finish();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});
		toggleActiveOnly.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton theButton, boolean isChecked) {
				if(isChecked)
					mConstraints[0]="yes";
				else
					mConstraints[0]="no";	
				ActiveFilter();
			}});
	}
}