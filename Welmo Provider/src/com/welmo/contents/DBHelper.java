package com.welmo.contents;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.welmo.contents.ExpensesManager.CurrenciesISO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

public class DBHelper{
    private String strDatabaseName = "";
    private SQLiteDatabase db = null;
    //Vector<String> TableList = new Vector<String>();
  

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

	public boolean TableExist(String strTableName){
		try{
			if(db != null){
				String SQLQueryString = "PRAGMA table_info(" 
					+ strTableName  + ")";
				Cursor cur = db.rawQuery(SQLQueryString,null);
				if(cur.getCount() <= 0){
					return false;
				}
				else{
					return true;
				}
			}
			else{
				throw new IllegalArgumentException ("Inpossible to open the table " + strTableName);
			}
		}
		catch (SQLException se)
		{
			String msg = se.toString();			
			System.out.println(msg);
			return false;
		}
	}
	public void CreateTableIfNotExists(String strTableName, String strTableDescription){
        try{
        	if(db != null){
        		String SQLQueryString = "CREATE TABLE IF NOT EXISTS " 
        			+ strTableName  + " " + strTableDescription;
        		db.execSQL(SQLQueryString);
        		//TableList.add(strTableName);
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
        //TableList.clear();        
        db = null;
    }
	
	public long createAndReplaceRow(String databaseTable, ContentValues content)
	{
		long lRowId=-1;
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		
		try{
			lRowId = db.insertOrThrow(databaseTable, null, content);
		}
		catch(SQLException err){
			lRowId = db.replace(databaseTable, null, content);
		}
		return lRowId;
	}
	public long updateRowByID(String databaseTable, long rowId, ContentValues content) 
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
       return db.update(databaseTable, content, "rowid=" + rowId, null);
    }
	public long updateRowByWhere(String databaseTable, String whareCaluse, ContentValues content) 
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
       return db.update(databaseTable, content, whareCaluse, null);
    }
	public int deleteRowByID(String databaseTable, long rowId)
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		return db.delete(databaseTable, "rowid=" + rowId, null);
    }
	public int deleteRowByWhere(String databaseTable, String whereClause)
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		return db.delete(databaseTable, whereClause, null);
    }
	public int deleteRageOfRowsByID(String databaseTable, long rowIdMin, long rowIdMax)
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		return db.delete(databaseTable, "rowid>=" + rowIdMin + 
				" and rowid<= " + rowIdMax, null);
    }
	public long deleteRageOfRowsByWhere(String databaseTable, String whereClause, long rowIdMax)
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		return db.delete(databaseTable, whereClause, null);
    }
	public Cursor fetchRowsByID(String databaseTable, long rowId, String[] columns) 
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		Cursor cur = db.query(true, databaseTable, columns, "rowid=" + rowId, null, null, null, null, null);
		if (cur.getCount() > 0)
				return cur;
		else{
			if(cur != null)
				cur.close();
			return null;
		}
	}
	public Cursor fetchRowsByWhere(String databaseTable, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) 
	{
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
		Cursor cur = db.query(true, databaseTable, projection, selection, selectionArgs,null, null, sortOrder, null);
		if (cur.getCount() > 0)
				return cur;
		else{
			if(cur != null)
				cur.close();
			return null;
		}
	}
	public Cursor rawQuery(String strSQLQueryString, String[] selectionArgs) 
	{
		Cursor cur = db.rawQuery(strSQLQueryString, selectionArgs);
		if (cur.getCount() > 0)
				return cur;
		else{
			if(cur != null)
				cur.close();
			return null;
		}
	}
	public List<Long> fetchRowIdListByID(String databaseTable, long rowIdMin, long rowIdMax) 
	{
		ArrayList<Long> ret = new ArrayList<Long>();
		//if (db == null || !TableList.contains(databaseTable))
       	//	throw new IllegalArgumentException ("Inpossible to open or crate the table " + databaseTable);
		if (db == null)
       		throw new IllegalArgumentException ("Internal Error: Database not initialized");
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
