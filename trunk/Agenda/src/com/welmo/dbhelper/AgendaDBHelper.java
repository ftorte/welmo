package com.welmo.dbhelper;


import java.util.List;

import com.tft.myoffice.meeting.Meeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AgendaDBHelper extends DBHelper{

	String strMeetingTable = "";
	String strAttendsTable = "";
	
	public AgendaDBHelper(Context ctx,String DBName, String MeetingTable, String AttendsTable) {
		super(ctx,DBName);
		strMeetingTable = MeetingTable;
		strAttendsTable = AttendsTable;
		CreateTableIfNotExists(strMeetingTable,Meeting.MEETING_TABLE_CREATE_CONDITIONS);
		CreateTableIfNotExists(strAttendsTable,Meeting.ATTENDS_TABLE_CREATE_CONDITIONS);
	}
	public long createAttendsRow(ContentValues content) {
		return super.createRow(strAttendsTable, content);
	}
	public long createMeetingsRow(ContentValues content) {
		return super.createRow(strMeetingTable, content);
	}
	public int deleteAttensRageOfRowsByID(long rowIdMin,long rowIdMax) {
		return super.deleteRageOfRowsByID(strAttendsTable, rowIdMin, rowIdMax);
	}
	public int deleteMeetingsRageOfRowsByID(long rowIdMin,long rowIdMax) {
		return super.deleteRageOfRowsByID(strMeetingTable, rowIdMin, rowIdMax);
	}
	public long deleteAttedsRageOfRowsByWhere(String whereClause, long rowIdMax) {
		return super.deleteRageOfRowsByWhere(strAttendsTable, whereClause, rowIdMax);
	}
	public long deleteMeetingsRageOfRowsByWhere(String whereClause, long rowIdMax) {
		return super.deleteRageOfRowsByWhere(strMeetingTable, whereClause, rowIdMax);
	}
	public int deleteAttendsRowByID(long rowId) {
		return super.deleteRowByID(strAttendsTable, rowId);
	}
	public int deleteMeetingsRowByID(long rowId) {
		return super.deleteRowByID(strMeetingTable, rowId);
	}
	public int deleteAttendsRowByWhere(String whereClause) {
		return super.deleteRowByWhere(strAttendsTable, whereClause);
	}
	public int deleteMeetingsRowByWhere(String whereClause) {
		return super.deleteRowByWhere(strMeetingTable, whereClause);
	}
	public List<Long> fetchAttendsRowIdListByID(long rowIdMin, long rowIdMax) {
		return super.fetchRowIdListByID(strAttendsTable, rowIdMin, rowIdMax);
	}
	public List<Long> fetchMeetingsRowIdListByID(long rowIdMin, long rowIdMax) {
		return super.fetchRowIdListByID(strMeetingTable, rowIdMin, rowIdMax);
	}
	public Cursor fetchAttendsRowsByID(long rowId,String[] columns) {
		return super.fetchRowsByID(strAttendsTable, rowId, columns);
	}
	public Cursor fetchMeetingsRowsByID(long rowId,String[] columns) {
		return super.fetchRowsByID(strMeetingTable, rowId, columns);
	}
	public Cursor fetchAttendsRowsByWhere(String whereClause,String[] columns) {
		return super.fetchRowsByWhere(strAttendsTable, whereClause, columns);
	}
	public Cursor fetchMeetignsRowsByWhere(String whereClause,String[] columns) {
		return super.fetchRowsByWhere(strMeetingTable, whereClause, columns);
	}
	public long updateAttendsRowByID(long rowId,ContentValues content) {
		return super.updateRowByID(strAttendsTable, rowId, content);
	}
	public long updateMeetingsRowByID(long rowId,ContentValues content) {
		return super.updateRowByID(strMeetingTable, rowId, content);
	}
	public long updateAttendsRowByWhere(String whareCaluse,ContentValues content) {
		return super.updateRowByWhere(strAttendsTable, whareCaluse, content);
	}
	public long updateMeetingsRowByWhere(String whareCaluse,ContentValues content) {
		return super.updateRowByWhere(strMeetingTable, whareCaluse, content);
	}
}
