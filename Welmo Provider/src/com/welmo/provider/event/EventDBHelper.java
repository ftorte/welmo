package com.welmo.provider.event;


import java.util.List;


//import com.welmo.meeting.Meeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class EventDBHelper extends DBHelper{

	private static String EVENT_TABLE = "Events";
	private static String ATTENDS_TABLE = "Attends";

	public static final String MEETING_TABLE_CREATE_CONDITIONS = 
        "(" + Event._ID + " INTEGER PRIMARY KEY," 
		+ " Object TEXT, Description TEXT, Owner TEXT, Duration INTEGER, Timestamp INTEGER)";
	
	public static final String ATTENDS_TABLE_CREATE_CONDITIONS = 
        "(" + Event._ID + " INTEGER," 
		+ " ID INTEGER, Name TEXT, Response INTEGER, Message TEXT, isMe INTEGER, PRIMARY KEY(UID,ID))";
	
	public EventDBHelper(Context ctx,String DBName) {
		super(ctx,DBName);
		CreateTableIfNotExists(EVENT_TABLE,MEETING_TABLE_CREATE_CONDITIONS);
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
