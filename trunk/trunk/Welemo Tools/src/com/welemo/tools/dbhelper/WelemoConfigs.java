package com.welemo.tools.dbhelper;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.database.Cursor;

public class WelemoConfigs extends ContentProvider{

	public static final Uri CONTENT_URI = Uri.parse( "content://com.welemo.configuration"); 
	private Context mcontext;
	private WelemoConfigDB theDB = null;
	
	WelemoConfigs(Context ctx){
		mcontext = ctx;
	}
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		String theautority = uri.getAuthority();
		String path = uri.getPath();
		String lastSegment = uri.getLastPathSegment();
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		String thePath = uri.getPath();
		String lastSegment = uri.getLastPathSegment();
		
		List<String> PathSegment = uri.getPathSegments();
		if(PathSegment.size()==0)
			return null;
		
		//create update sub-path folders if not exists
		/*String path = new String();
		for(int index = 0; index < PathSegment.size()-2; index++){
			path = path+ PathSegment.get(index);
			String wereclause = "path=" + path + "and content = " + PathSegment.get(index + 1);
			if ((Cursor c = theDB.fetchRowsByWhere("path=" + path ,null)) == null)
				theDB.
				
				
				CONTENT_FOLDER
		}*/
			
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		
		theDB = new WelemoConfigDB(mcontext);
		return false;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
