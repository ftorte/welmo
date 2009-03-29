package com.welmo.tools;

import java.util.Iterator;
import java.util.Vector;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.welmo.dbhelper.WelmoConfigDBHelper;

public class WelmoConfig{

	//Constant for SQL handling of Meetings
	private static String LOG_TAG="WelmoConfig"; 
	public static final String WELMO_CONFIG_TABLE = 
		"(URI TEXT, Parameter TEXT, ParValue TEXT, PRIMARY KEY(URI,Parameter))";

	public static final long serialVersionUID = 1;

	public Vector<ConfigEntry> ConfigEntries = new Vector<ConfigEntry>(0);

	public class ConfigEntry{
		public String 	URI			="";
		public String 	Parameter 	=""; 
		public String 	ParValue 	="";
		public ContentValues getContent()
		{
			ContentValues theContent = new ContentValues();
			theContent.put("URI",this.URI);
			theContent.put("Parameter",this.Parameter);
			theContent.put("ParValue",this.ParValue);
			return theContent;
		}
		public void copyFormCursor(Cursor cur){
			URI = cur.getString(0);
			Parameter = cur.getString(1);
			ParValue = cur.getString(2);
		}
	} 
	//-------------------------------------------------------
	// Constructors
	//-------------------------------------------------------
	public WelmoConfig() {
		super();
	}

	public void UpdateToDatabase(WelmoConfigDBHelper db)
	{
		ContentValues content;
		db.deleteConfigAllRows();

		if (ConfigEntries.size()!=0){
			Iterator<ConfigEntry> it = ConfigEntries.listIterator();
			while(it.hasNext()){
				ConfigEntry theEntry = it.next();  
				content = theEntry.getContent();
				db.createConfigEntry(content);
			}		
		}
	}
	public void RestoreFromDatabase (WelmoConfigDBHelper db)
	{
		String[] columns;
		Cursor cur;

		ConfigEntries.clear();
		columns = new String[]{"URI","Parameter", "ParValue"};

		cur = db.fetchConfigAllRows(columns);
		if(cur != null)
		{
			cur.moveToFirst();
			while(!cur.isAfterLast()){
				ConfigEntry theEntry = new ConfigEntry();
				theEntry.copyFormCursor(cur);					
				ConfigEntries.add(theEntry);
				cur.moveToNext();
			}
			cur.close();
		}
	}
	public Vector<ConfigEntry> GetEntries(String URI, String parameter)
	{
		Vector<ConfigEntry> retval = new Vector<ConfigEntry>(ConfigEntries); 

		if(parameter==""){
			Iterator<ConfigEntry> it = retval.listIterator();
			while(it.hasNext()){
				ConfigEntry entry = it.next();
				if(entry.URI.compareTo(URI)!=0) 
					it.remove();
			}		
		}
		else{
			Iterator<ConfigEntry> it = retval.listIterator();
			while(it.hasNext()){
				ConfigEntry entry = it.next();
				if((entry.URI.compareTo(URI)!=0) ||(entry.Parameter.compareTo(parameter) != 0)) 
					it.remove();
			}	
		}
		return retval;
	}
	public String GetEntryValue(String URI, String parameter)
	{
		Log.i(LOG_TAG, "[GetEntryValue]" + URI + ":" + parameter);
		Iterator<ConfigEntry> it = ConfigEntries.listIterator();
		while(it.hasNext()){
			ConfigEntry entry = it.next();
			if((entry.URI.compareTo(URI)==0) &&(entry.Parameter.compareTo(parameter) == 0)) 
				return entry.ParValue;
		}
		throw new java.lang.IllegalArgumentException("The requested URI and parameter don't exists");
	}
	public void AddEntries(String URI, String parameter,String value)
	{
		ConfigEntry theEntry = new ConfigEntry();
		theEntry.URI=URI;
		theEntry.Parameter = parameter;
		theEntry.ParValue = value;
		ConfigEntries.add(theEntry);
	}
	public void DelEntries(String URI, String parameter)
	{
		
		Iterator<ConfigEntry> it = ConfigEntries.listIterator();
		while(it.hasNext()){
			ConfigEntry entry = it.next();
			if((entry.URI.compareTo(URI)==0) &&(entry.Parameter.compareTo(parameter) == 0)) 
				it.remove();
		}
	}
}

