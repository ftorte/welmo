/* 
 * Copyright (C) 2008-2009 Welemo Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welmo.contents.providers;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.welmo.contents.DBHelper;
import com.welmo.contents.Expenses;
import com.welmo.contents.providers.*;

public class ExpensesProvider extends ContentProvider{

	public class ExpensesDBHelper extends DBHelper{

		private String EVENT_TABLE = "Events";
		private String ATTENDS_TABLE = "Attends";

		public static final String EXPENSES_TABLE_CREATE_CONDITIONS = 
	        "(" +  Expenses._ID + "INTEGER PRIMARY KEY," +
	        	Expenses.DSCRIPTION +", TEXT, " +
	        	Expenses.GROUP +", TEXT," +
	        	Expenses.OWNER +", TEXT, " +
	        	Expenses.DATE +", INTEGER, " +
	        	Expenses.VAL +", INTEGER, " +
	        	Expenses.VALCURR +", TEXT, " +
	        	Expenses.COVTAUX +" , Val)";
		
		public static final String ATTENDS_TABLE_CREATE_CONDITIONS = 
	        "(" + Expenses._ID + " INTEGER," 
			+ " ID INTEGER, Name TEXT, Response INTEGER, Message TEXT, isMe INTEGER, PRIMARY KEY(UID,ID))";
		
		public ExpensesDBHelper(Context ctx,String DBName) {
			super(ctx,DBName);
			CreateTableIfNotExists(EVENT_TABLE,EXPENSES_TABLE_CREATE_CONDITIONS);
			CreateTableIfNotExists(ATTENDS_TABLE,ATTENDS_TABLE_CREATE_CONDITIONS);
		}
		public long createAttendsRow(ContentValues content) {
			return super.createRow(ATTENDS_TABLE, content);
		}
		public long createMeetingsRow(ContentValues content) {
			return super.createRow(EVENT_TABLE, content);
		}
		public int deleteAttensRageOfRowsByID(long rowIdMin,long rowIdMax) {
			return super.deleteRageOfRowsByID(ATTENDS_TABLE, rowIdMin, rowIdMax);
		}
		public int deleteMeetingsRageOfRowsByID(long rowIdMin,long rowIdMax) {
			return super.deleteRageOfRowsByID(EVENT_TABLE, rowIdMin, rowIdMax);
		}
		public long deleteAttedsRageOfRowsByWhere(String whereClause, long rowIdMax) {
			return super.deleteRageOfRowsByWhere(ATTENDS_TABLE, whereClause, rowIdMax);
		}
		public long deleteMeetingsRageOfRowsByWhere(String whereClause, long rowIdMax) {
			return super.deleteRageOfRowsByWhere(EVENT_TABLE, whereClause, rowIdMax);
		}
		public int deleteAttendsRowByID(long rowId) {
			return super.deleteRowByID(ATTENDS_TABLE, rowId);
		}
		public int deleteMeetingsRowByID(long rowId) {
			return super.deleteRowByID(EVENT_TABLE, rowId);
		}
		public int deleteAttendsRowByWhere(String whereClause) {
			return super.deleteRowByWhere(ATTENDS_TABLE, whereClause);
		}
		public int deleteMeetingsRowByWhere(String whereClause) {
			return super.deleteRowByWhere(EVENT_TABLE, whereClause);
		}
		public List<Long> fetchAttendsRowIdListByID(long rowIdMin, long rowIdMax) {
			return super.fetchRowIdListByID(ATTENDS_TABLE, rowIdMin, rowIdMax);
		}
		public List<Long> fetchMeetingsRowIdListByID(long rowIdMin, long rowIdMax) {
			return super.fetchRowIdListByID(EVENT_TABLE, rowIdMin, rowIdMax);
		}
		public Cursor fetchAttendsRowsByID(long rowId,String[] columns) {
			return super.fetchRowsByID(ATTENDS_TABLE, rowId, columns);
		}
		public Cursor fetchMeetingsRowsByID(long rowId,String[] columns) {
			return super.fetchRowsByID(EVENT_TABLE, rowId, columns);
		}
		public Cursor fetchAttendsRowsByWhere(String whereClause,String[] columns) {
			return super.fetchRowsByWhere(ATTENDS_TABLE, whereClause, columns);
		}
		public Cursor fetchMeetignsRowsByWhere(String whereClause,String[] columns) {
			return super.fetchRowsByWhere(EVENT_TABLE, whereClause, columns);
		}
		public long updateAttendsRowByID(long rowId,ContentValues content) {
			return super.updateRowByID(ATTENDS_TABLE, rowId, content);
		}
		public long updateMeetingsRowByID(long rowId,ContentValues content) {
			return super.updateRowByID(EVENT_TABLE, rowId, content);
		}
		public long updateAttendsRowByWhere(String whareCaluse,ContentValues content) {
			return super.updateRowByWhere(ATTENDS_TABLE, whareCaluse, content);
		}
		public long updateMeetingsRowByWhere(String whareCaluse,ContentValues content) {
			return super.updateRowByWhere(EVENT_TABLE, whareCaluse, content);
		}
	}

	private static final String TAG = "ExpensesProvider";
	private	ExpensesDBHelper eventDB	= null;	
	
	
	// TODO Handling database version
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int match = sURIMatcher.match(uri);
		switch (match)
		{
		case EVENT: 
			return "vnd.android.cursor.dir/event";
		case EVENT_ID:
			return "vnd.android.cursor.item/event";
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		List<String> sgmt = null;
		sgmt =  uri.getPathSegments(); 
		
		switch (sURIMatcher.match(uri)) {
		case EVENT_ID:
			String sUID = sgmt.get(1);
			long UID = Long.parseLong(sUID);
			values.put(Expenses._ID, UID);
			UID  = eventDB.createMeetingsRow(values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		return uri;
	}

	@Override
	public boolean onCreate() {
		eventDB = new ExpensesDBHelper(this.getContext(),Expenses.DATABASE_NAME);
		if(eventDB != null)
			return true;
		else
			return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		List<String> sgmt = null;

		int match = sURIMatcher.match(uri);
		switch (sURIMatcher.match(uri)) {
		case EVENT:
			c = eventDB.fetchMeetignsRowsByWhere(selection, projection);
			break;
		case EVENT_ID:
			sgmt =  uri.getPathSegments(); 
			c = eventDB.fetchMeetingsRowsByID(Long.parseLong(sgmt.get(1)), projection);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		List<String> sgmt = null;
		sgmt =  uri.getPathSegments(); 
		
		switch (sURIMatcher.match(uri)) {
		case EVENT_ID:
			eventDB.updateMeetingsRowByID(Long.parseLong(sgmt.get(1)), values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		return 0;
	}

	
	private static final int EVENT = 100;
	private static final int EVENT_ID = 101;
	
	private static final UriMatcher sURIMatcher = new UriMatcher(EVENT);

	static
	{
		String strAutority = Expenses.CONTENT_URI.getAuthority();
		sURIMatcher.addURI(strAutority, "event/#", EVENT_ID);
		sURIMatcher.addURI(strAutority, "event", EVENT);
	}

}
