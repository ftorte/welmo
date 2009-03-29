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

package com.welmo.provider.event;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.welmo.provider.event.*;

public class EventProvider extends ContentProvider{

	private static final String TAG = "EventProvider";
	private	EventDBHelper eventDB	= null;	
	
	
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
			values.put(Event._ID, UID);
			UID  = eventDB.createMeetingsRow(values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		return uri;
	}

	@Override
	public boolean onCreate() {
		eventDB = new EventDBHelper(this.getContext(),Event.DATABASE_NAME);
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
		String strAutority = Event.CONTENT_URI.getAuthority();
		sURIMatcher.addURI(strAutority, "event/#", EVENT_ID);
		sURIMatcher.addURI(strAutority, "event", EVENT);
	}

}
