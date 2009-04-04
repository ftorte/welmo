package com.welmo.contents.testapp;



import com.welmo.contents.Calendar;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.Contacts.Phones;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter; 

/**
 * A list view example where the
 * data comes from a cursor, and a
 * SimpleCursorListAdapter is used to map each item to a two-line
 * display.
 */

public class WelmoTestProviders extends ListActivity {
	/** Called when the activity is first created. */
	

	@Override    
	protected void onCreate(Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);        
		// Get a cursor with all phones
		
		
		//fill an example is any
		ContentValues values = new ContentValues(2);
		values.put(Calendar.EventColumns.OBJECT, "Object 1");
		values.put(Calendar.EventColumns.DSCRIPTION,"Description 1");
		Uri res = getContentResolver().insert(
				Calendar.AUTORITY.withAppendedPath(Calendar.AUTORITY, "event/1"), 
				values);
		 res = getContentResolver().insert(
				Calendar.AUTORITY.withAppendedPath(Calendar.AUTORITY, "event/2"), 
				values);
		 res = getContentResolver().insert(
				Calendar.AUTORITY.withAppendedPath(Calendar.AUTORITY, "event/3"), 
				values);
		Cursor c = getContentResolver().query(
				Calendar.AUTORITY.withAppendedPath(Calendar.AUTORITY, "event"),
				null, null, null, null);        
		startManagingCursor(c);        
		// Map Cursor columns to views defined in simple_list_item_2.xml        
		ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2, c,
				new String[] { Calendar.Event.OBJECT, Calendar.Event.DSCRIPTION},                         
				new int[] { android.R.id.text1, android.R.id.text2 });        
		setListAdapter(adapter);    
	}
}