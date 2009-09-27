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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;


import android.content.ContentProvider;
import android.content.ContentValues;

import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.welmo.contents.DBHelper;
import com.welmo.contents.ExpensesManager;
import com.welmo.contents.ExpensesManager.CurrenciesISO;
import com.welmo.contents.ExpensesManager.Currency;
import com.welmo.travel.tracking.R;

public class ExpensesProvider extends ContentProvider{
	
	private static final int DB_VERSION = 1;

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		// TODO Auto-generated method stub
		long UID=0;
		switch (sURIMatcher.match(uri)) {
		case CURRENCIES:
			for(int index=0; index <values.length; index++)
				UID  = expensesDB.createAndReplaceRow(ExpensesManager.CURRENCIES_RATES_TABLE,values[index]);
			break;
		case ALL_CURRCOUNTRYISO:
			for(int index=0; index <values.length; index++)
				UID  = expensesDB.createAndReplaceRow(ExpensesManager.CURRENCIES_ISO_TABLE,values[index]);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL=" + uri);
		}
		return values.length;		
	}

	private static final String TAG = "ExpensesProvider";
	private	DBHelper expensesDB	= null;	
    Set<String> setCurrValues = new HashSet<String>(Arrays.asList(
    		"AFN","AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG",
    		"AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB",
    		"BOV","BRL","BSD","BWP","BYR","BZD","CAD","CDF","CHF","CHF",
    		"CHW","CHE","CLP","CLF","CNY","COP","COU","CRC","CUP","CUC",
    		"CVE","CZK","DJF","DKK","DKK","DKK","DOP","DZD","EEK","EGP",
    		
    		"ERN","ETB","EUR","FJD","FKP","GBP","GEL","GHS","GIP","GMD",
    		"GNF","GTQ","GWP","XOF","GYD","HKD","HNL","HRK","HTG","USD",
    		"HUF","IDR","ILS","INR","INR","BTN","IQD","IRR","ISK","JMD",
    		"JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD","KYD",
    		"KZT","LAK","LBP","LKR","LRD","LTL","LVL","LYD","MAD","MAD",
    		
    		"MDL","MGA","MKD","MMK","MNT","MOP","MRO","MUR","MVR","MWK",
    		"MXN","MXV","MYR","MZN","NGN","NIO","NOK","NOK","NOK","NPR",
    		"NZD","NZD","NZD","NZD","NZD","OMR","PAB","USD","PEN","PGK",
    		"PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR",
    		"SBD","SCR","SDG","SEK","SGD","SHP","SLL","SOS","SRD","STD",
    		"SVC","USD","SYP","SZL","THB","TJS","TMT","TND","TOP","TRY",
    		
    		"TTD","TWD","TZS","UAH","UGX","USD","USD","USS","USN","UYU",  
    		"UYI","UZS","VEF","VND","VUV","WST","XAF","XCD","XOF","XPF",
    		"YER","ZAR","LSL","ZAR","NAD","ZMK","ZWL"));
    

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
		case EXPENSES: 
			return "vnd.android.cursor.dir/event";
		case EXPENSES_DATA:
			return "vnd.android.cursor.item/event";
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		List<String> sgmt = null;
		sgmt =  uri.getPathSegments();
		long UID=0;
		Log.i(TAG, "insert");
		switch (sURIMatcher.match(uri)) {
		case EXPENSES_DATA:
			values.put(ExpensesManager.Expenses._ID, Long.parseLong(sgmt.get(1)));
			UID  = expensesDB.createAndReplaceRow(ExpensesManager.EXPENSES_TABLE,values);
			break;
		case CURRENCY_XY:
			Log.i(TAG, " case CURRENCY XY \n");
			if(!setCurrValues.contains(sgmt.get(1)) || !setCurrValues.contains(sgmt.get(2)))
				throw new IllegalArgumentException("Invalid Currency[" + sgmt.get(1) + "][" + sgmt.get(2)+ "]");
			values.put(ExpensesManager.Currency.CURRSOURCE, sgmt.get(1));
			values.put(ExpensesManager.Currency.CURRTARGET, sgmt.get(2));
			UID  = expensesDB.createAndReplaceRow(ExpensesManager.CURRENCIES_RATES_TABLE,values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL=" + uri);
		}
		return uri;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onCreate() {
		expensesDB = new DBHelper(this.getContext(),ExpensesManager.DATABASE_NAME);
		if(expensesDB != null){
			
			//--------------------------
			// Crezate and Initialize CURRENCIES_RATES_TABLE
			//--------------------------
			expensesDB.CreateTableIfNotExists(ExpensesManager.EXPENSES_TABLE, ExpensesManager.EXPENSES_TABLE_CREATE_CONDITIONS);
		
			//--------------------------
			// Initialize CURRENCIES_RATES_TABLE
			if (!expensesDB.TableExist(ExpensesManager.CURRENCIES_RATES_TABLE))
			{
				ContentValues values = new ContentValues();
				expensesDB.CreateTableIfNotExists(ExpensesManager.CURRENCIES_RATES_TABLE, ExpensesManager.CURRENCYES_RATES_TABLE_CREATE_CONDITIONS);
				BufferedReader inReader = new BufferedReader(new InputStreamReader(this.getContext().getResources().openRawResource(R.raw.currencies_rates)));
				String strLine;
			    //Read File Line By Line
			    try {
					while ((strLine = inReader.readLine()) != null)   {
					  // Print the content on the console
						String[] tokens = strLine.split(";");
						values.put(Currency.CURRSOURCE, tokens[0]);
						values.put(Currency.CURRTARGET, tokens[1]);
						values.put(Currency.RATESRC_VS_TGT, tokens[2]);
						values.put(Currency.RATEDATE, tokens[3]);
						expensesDB.createAndReplaceRow(ExpensesManager.CURRENCIES_RATES_TABLE, values);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//--------------------------
			// Initialize CURRENCIES_ISO_TABLE
			if (!expensesDB.TableExist(ExpensesManager.CURRENCIES_ISO_TABLE))
			{
				ContentValues values = new ContentValues();
				expensesDB.CreateTableIfNotExists(ExpensesManager.CURRENCIES_ISO_TABLE, ExpensesManager.CURRENCYES_ISO_TABLE_CREATE_CONDITIONS);
				BufferedReader inReader = new BufferedReader(new InputStreamReader(
						this.getContext().getResources().openRawResource(R.raw.currencies_iso3)));
				String strLine;
			    //Read File Line By Line
			    try {
					while ((strLine = inReader.readLine()) != null)   {
					  // Print the content on the console
						String[] tokens = strLine.split(";");
						values.put(CurrenciesISO.COUNTRYNAME, tokens[0]);
						values.put(CurrenciesISO.COUNTRYCODE, tokens[1]);
						values.put(CurrenciesISO.CURRCODE, tokens[2]);
						values.put(CurrenciesISO.CURNAME, tokens[3]);
						values.put(CurrenciesISO.CURSYMBOL, tokens[4]);
						values.put(CurrenciesISO.CURRRANK, tokens[5]);
						values.put(CurrenciesISO.ISDEFAULT, 0);
						values.put(CurrenciesISO.SELECTED, tokens[6]);
						expensesDB.createAndReplaceRow(ExpensesManager.CURRENCIES_ISO_TABLE, values);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		else
			return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		List<String> sgmt = null;
		String selectionArg[]=null;

		int match = sURIMatcher.match(uri);
		switch (sURIMatcher.match(uri)) {
		case EXPENSES:
			c = expensesDB.fetchRowsByWhere(ExpensesManager.EXPENSES_TABLE,projection,selection,selectionArgs,sortOrder);
			break;
		case EXPENSES_DATA:
			sgmt =  uri.getPathSegments(); 
			c = expensesDB.fetchRowsByID(ExpensesManager.EXPENSES_TABLE,Long.parseLong(sgmt.get(1)), projection);
			break;
		case CURRENCY_X:
			sgmt =  uri.getPathSegments(); 
			if(!setCurrValues.contains(sgmt.get(1)))
				throw new IllegalArgumentException("invalid Currency[" + sgmt.get(1) + "]");
			c = expensesDB.fetchRowsByWhere(ExpensesManager.CURRENCIES_RATES_TABLE,projection,Currency.CURRSOURCE + " = " + sgmt.get(1),null,null);
			break;
		case CURRENCY_XY:
			sgmt =  uri.getPathSegments(); 
			if(!setCurrValues.contains(sgmt.get(1)) || !setCurrValues.contains(sgmt.get(2)))
				throw new IllegalArgumentException("invalid Currency[" + sgmt.get(1) + "][" + sgmt.get(2)+ "]");
			c = expensesDB.fetchRowsByWhere(ExpensesManager.CURRENCIES_RATES_TABLE,projection,Currency.CURRSOURCE + " = " + sgmt.get(1) + 
					" " + Currency.CURRTARGET + " = " + sgmt.get(2),null,null);
			break;
		case CURRENCIES:
			c = expensesDB.fetchRowsByWhere(ExpensesManager.CURRENCIES_RATES_TABLE,projection,selection,selectionArgs,sortOrder);
			break;
		case ALL_CURRCOUNTRYISO_BIS:
			sgmt =  uri.getPathSegments(); 
			selectionArg = new String[1];
			selectionArg[0]=sgmt.get(2);
			Log.v("provider", selectionArg[0]);
			c = expensesDB.rawQuery("SELECT * FROM CURRENCIESISO a LEFT OUTER JOIN (SELECT * FROM CURRENCIES_RATES b WHERE b.CurrSource = ?) c ON" + 
					"( a.CURRCODE = c.CurrTarget) ORDER BY (a.CountryName)", 
					selectionArg);
			break;
			/*
			 * 
					
					SELECT * FROM CURRENCIESISO a LEFT OUTER JOIN (SELECT * FROM CURRENCIES_RATES b WHERE b.CurrSource = 'EUR') c ON 
					( a.CURRCODE = c.CurrTarget) WHERE (a.SELECTED = 1 ) ORDER BY (a.CountryName) 
			 */
		case RATESBYCURRENCY:
			sgmt =  uri.getPathSegments(); 
			selectionArg = new String[1];
			selectionArg[0]=sgmt.get(2);
			Log.v("provider", selectionArg[0]);
			c = expensesDB.rawQuery("SELECT * FROM CURRENCIESISO a LEFT OUTER JOIN (SELECT * FROM CURRENCIES_RATES b WHERE b.CurrSource = ?) c ON" + 
					"( a.CURRCODE = c.CurrTarget) WHERE (a.SELECTED = 1 ) ORDER BY (a.CountryName)", 
					selectionArg);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL=" + uri);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		List<String> sgmt = null;
		sgmt =  uri.getPathSegments(); 

		switch (sURIMatcher.match(uri)) {
		case EXPENSES_DATA:
			expensesDB.updateRowByID(ExpensesManager.EXPENSES_TABLE,Long.parseLong(sgmt.get(1)), values);
			break;
		case	ALL_CURRCOUNTRYISO:
			expensesDB.updateRowByWhere(ExpensesManager.CURRENCIES_ISO_TABLE,selection ,values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL=" + uri);

		}
		return 0;
	}

	// Uri tags
	private static final int EXPENSES = 100;
	private static final int EXPENSES_DATA = 101;
	
	private static final int CURRENCIES = 200;
	private static final int CURRENCY_X = 201;
	private static final int CURRENCY_XY = 202;
	
	
	private static final int ALL_CURRCOUNTRYISO = 205;
	private static final int ALL_CURRCOUNTRYISO_BIS = 205;
	private static final int RATESBYCURRENCY = 207;
	
	private static final UriMatcher sURIMatcher = new UriMatcher(EXPENSES);

	static
	{
		String strAutority = ExpensesManager.AUTORITY.getAuthority();
		
		sURIMatcher.addURI(strAutority, "expenses/", EXPENSES);
		sURIMatcher.addURI(strAutority, "expenses/#", EXPENSES_DATA);
		
		// get all latest currencies rates 
		sURIMatcher.addURI(strAutority, "currencies/", CURRENCIES);
		// get latest currency conversion
		sURIMatcher.addURI(strAutority, "currencies/*/", CURRENCY_X);
		// get latest currency conversion
		sURIMatcher.addURI(strAutority, "currencies/*/*/", CURRENCY_XY);
		// get all currency/country  
		sURIMatcher.addURI(strAutority, "currcountryISO/", ALL_CURRCOUNTRYISO);
		sURIMatcher.addURI(strAutority, "currcountryISO/all/*/", ALL_CURRCOUNTRYISO_BIS);
		sURIMatcher.addURI(strAutority, "currcountryISO/rates/*/", RATESBYCURRENCY);
	}
}
