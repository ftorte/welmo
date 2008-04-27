package com.welmo.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.welmo.tools.WelmoConfig;

public class WelmoConfigDBHelper extends DBHelper{

	String strTable = "";
		
	public WelmoConfigDBHelper(Context ctx,String DBName, String ConfigTable) {
		super(ctx,DBName);
		strTable = ConfigTable;
		CreateTableIfNotExists(strTable,WelmoConfig.WELMO_CONFIG_TABLE);
	}
	
	public long createConfigEntry(ContentValues content) {
		return super.createRow(strTable, content);
	}
	
	public long deleteConfigAllRows() {
		return super.deleteRowByWhere(strTable, null);
	}

	public Cursor fetchConfigAllRows(String[] columns) {
		return super.fetchRowsByWhere(strTable, null, columns);
	}	
}
