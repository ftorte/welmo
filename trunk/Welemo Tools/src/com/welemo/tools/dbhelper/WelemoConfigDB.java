package com.welemo.tools.dbhelper;

import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class WelemoConfigDB extends DBHelper{
	
	public static int CONTENT_FOLDER = 1;
	public static int CONTENT_VARIABLE = 2;
	
	//Constant for SQL handling of Meetings
	public static final String WELEMO_CONFIG_TABLE= 
        "(_id INTEGER PRIMARY KEY AUTOINCREMENT," 
		+ " Autority TEXT, Path TEXT, Content TEXT, ContentType TEXT, Value TEXT)";
	
	public static final String strTableName = "WelemoConfig";
	
	public WelemoConfigDB(Context ctx) {
		super(ctx,strTableName);
		CreateTableIfNotExists(strTableName,WELEMO_CONFIG_TABLE);
	}
	public long createRow(ContentValues content) {
		return super.createRow(strTableName, content);
	}
	public int deleteRageOfRowsByID(long rowIdMin,long rowIdMax) {
		return super.deleteRageOfRowsByID(strTableName, rowIdMin, rowIdMax);
	}
	public long deleteRageOfRowsByWhere(String whereClause, long rowIdMax) {
		return super.deleteRageOfRowsByWhere(strTableName, whereClause, rowIdMax);
	}
	public int deleteRowByID(long rowId) {
		return super.deleteRowByID(strTableName, rowId);
	}
	public int deleteRowByWhere(String whereClause) {
		return super.deleteRowByWhere(strTableName, whereClause);
	}
	public List<Long> fetchRowIdListByID(long rowIdMin, long rowIdMax) {
		return super.fetchRowIdListByID(strTableName, rowIdMin, rowIdMax);
	}
	public Cursor fetchRowsByID(long rowId,String[] columns) {
		return super.fetchRowsByID(strTableName, rowId, columns);
	}
	public Cursor fetchRowsByWhere(String whereClause,String[] columns) {
		return super.fetchRowsByWhere(strTableName, whereClause, columns);
	}
	public long updateRowByID(long rowId,ContentValues content) {
		return super.updateRowByID(strTableName, rowId, content);
	}
	public long updateRowByWhere(String whareCaluse,ContentValues content) {
		return super.updateRowByWhere(strTableName, whareCaluse, content);
	}
	public String toString(){
		String strContent;
		String[] columns;
		Cursor cur;
		
		strContent= new String();
		columns = new String[]{"Autority","Path","Content","Value"};
		cur = fetchRowsByWhere("",columns);
		
		if(cur != null)
		{
			cur.first();
			while(!cur.isAfterLast()){
				strContent = strContent + "[" + cur.getString(0) + "]";
				strContent = strContent + "[" + cur.getString(1) + "]";	
				strContent = strContent + "[" + cur.getString(2) + "]";
				strContent = strContent + "\n";			    
				cur.next();   
			}
		}
		return strContent;
	}
}
