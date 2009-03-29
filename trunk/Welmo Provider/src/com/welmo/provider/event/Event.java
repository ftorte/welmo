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

package com.welmo.provider.event;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Definition for content provider related to location.
 *
 */
public final class Event implements BaseColumns {
	private static final String TAG = "EventProvider";
	
	public static final Uri CONTENT_URI = Uri.parse( "content://com.welmo.provider.event"); 

	public static final String DEFAULT_SORT_ORDER = "modified DESC";

	// columns label
	public static final String OBJECT = "Object";
	public static final String DSCRIPTION = "Description";
	
	public static final String 	DATABASE_NAME 		= "Events.db";
	public static final int 	DATABASE_VERSION 	= 1;

}
