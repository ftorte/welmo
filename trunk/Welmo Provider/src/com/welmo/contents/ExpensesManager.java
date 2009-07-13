/* 
 * Copyright (C) 2007-2008 OpenIntents.org
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

package com.welmo.contents;

import java.sql.Date;

import com.welmo.contents.Calendar.EventColumns;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Definition for content provider related to location.
 *
 */
public final class ExpensesManager implements BaseColumns {
	private static final String TAG = "ExpensesProvider";
	
	public static final Uri CONTENT_URI = Uri.parse( "content://com.welmo.contents.providers.expenses"); 

	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	public static final String 	DATABASE_NAME 		= "ExpensesManager.db";
	public static final int 	DATABASE_VERSION 	= 1;

	
	// columns label
	public interface ExpensesColumns {
		public static final String OBJECT = "Object";
		public static final String DSCRIPTION = "DSCRIPTION";
		public static final String GROUP = "Group";
		public static final String OWNER = "Owner";
		public static final String DATE = "Date";
		public static final String VAL = "Val";
		public static final String VALCURR = "ValVCur";
		public static final String COVTAUX = "ConTaux";
	}
	
	public static final class Expenses implements BaseColumns, EventColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.welmo.contents.providers.calendar/events");
    }
}