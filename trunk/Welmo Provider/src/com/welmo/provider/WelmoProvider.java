package com.welmo.provider;

import com.welmo.provider.event.Event;

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

public class WelmoProvider extends ListActivity {
	/** Called when the activity is first created. */
	

	@Override    
	protected void onCreate(Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);        
		// Get a cursor with all phones
		
		
		//fill an example is any
		ContentValues values = new ContentValues(2);
		values.put(Event.OBJECT, "Object 1");
		values.put(Event.DSCRIPTION,"Description 1");
		Uri res = getContentResolver().insert(
				Event.CONTENT_URI.withAppendedPath(Event.CONTENT_URI, "event/1"), 
				values);
		 res = getContentResolver().insert(
				Event.CONTENT_URI.withAppendedPath(Event.CONTENT_URI, "event/2"), 
				values);
		 res = getContentResolver().insert(
				Event.CONTENT_URI.withAppendedPath(Event.CONTENT_URI, "event/3"), 
				values);

		Cursor c = getContentResolver().query(
				Event.CONTENT_URI.withAppendedPath(Event.CONTENT_URI, "event"),
				null, null, null, null);        
		startManagingCursor(c);        
		// Map Cursor columns to views defined in simple_list_item_2.xml        
		ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2, c,
				new String[] { Event.OBJECT, Event.DSCRIPTION},                         
				new int[] { android.R.id.text1, android.R.id.text2 });        
		setListAdapter(adapter);    
	}
}