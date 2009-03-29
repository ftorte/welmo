package com.welmo.dbhelper;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DBHelper {
    private String strDatabaseName = "";
    private SQLiteDatabase db = null;
    Vector<String> TableList = new Vector<String>();
  

	public DBHelper(Context ctx, String DBName) {
		super();
		try {
			strDatabaseName = DBName;
            db = ctx.openOrCreateDatabase(strDatabaseName,android.content.Context.MODE_WORLD_WRITEABLE,null);
        }
		catch (SQLiteException e) {
                 db = null;
        }
	}
	
	public void CreateTableIfNotExists(String strTableName, String strTableDescription){
        try{
        	if(db != null){
        		String SQLQueryString = "CREATE TABLE IF NOT EXISTS " 
        			+ strTableName  + " " + strTableDescription;
        		db.execSQL(SQLQueryString);
        		TableList.add(strTableName);
        	}
        	else{
        		throw new IllegalArgumentException ("Inpossible to open or crate the table " + strTableName);
        	}
        }
        catch (SQLException se)
        {
        	String msg = se.toString();			
			System.out.println(msg);
        }
	}
    
	
	public void close() {
        db.close();
        TableList.clear();        
        db = null;
    }
	
	protected long createRow(String databaseTable, ContentValues content)
	{
		long lRowId;
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		lRowId = db.insert(databaseTable, null, content);
		return lRowId;
	}
	protected long updateRowByID(String databaseTable, long rowId, ContentValues content) 
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
       return db.update(databaseTable, content, "rowid=" + rowId, null);
    }
	protected long updateRowByWhere(String databaseTable, String whareCaluse, ContentValues content) 
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
       return db.update(databaseTable, content, whareCaluse, null);
    }
	protected int deleteRowByID(String databaseTable, long rowId)
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		return db.delete(databaseTable, "rowid=" + rowId, null);
    }
	protected int deleteRowByWhere(String databaseTable, String whereClause)
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		return db.delete(databaseTable, whereClause, null);
    }
	protected int deleteRageOfRowsByID(String databaseTable, long rowIdMin, long rowIdMax)
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		return db.delete(databaseTable, "rowid>=" + rowIdMin + 
				" and rowid<= " + rowIdMax, null);
    }
	protected long deleteRageOfRowsByWhere(String databaseTable, String whereClause, long rowIdMax)
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		return db.delete(databaseTable, whereClause, null);
    }
	protected Cursor fetchRowsByID(String databaseTable, long rowId, String[] columns) 
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		Cursor cur = db.query(true, databaseTable, columns, "rowid=" + rowId, null, null, null, null, null);
		if (cur.getCount() > 0)
				return cur;
		else{
			if(cur != null)
				cur.close();
			return null;
		}
	}
	protected Cursor fetchRowsByWhere(String databaseTable, String whereClause, String[] columns) 
	{
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		Cursor cur = db.query(true, databaseTable, columns, whereClause, null,null, null, null, null);
		if (cur.getCount() > 0)
				return cur;
		else{
			if(cur != null)
				cur.close();
			return null;
		}
	}
	protected List<Long> fetchRowIdListByID(String databaseTable, long rowIdMin, long rowIdMax) 
	{
		ArrayList<Long> ret = new ArrayList<Long>();
		if (db == null || !TableList.contains(databaseTable))
       		throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		try{		
			Cursor cur = db.query(true, databaseTable,new String[]{"rowid"}, 
					"rowid BETWEEN " + rowIdMin + " AND " + rowIdMax, 
					null, null,null,null, "rowid");
			
			if (cur.getCount() > 0)
			{
				cur.moveToFirst();
				while (!cur.isLast())
				{
					ret.add(cur.getLong(0));
					cur.moveToNext();
				}
				ret.add(cur.getLong(0));
			}
			if(cur != null) cur.close();
		}
		catch(SQLException se){
			String msg = se.toString();			
			System.out.println(msg);
		}
		return ret;
	}
}
