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
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.welmo.contents.WelmoCalendar.EventColumns;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Definition for content provider related to location.
 *
 */
public final class ExpensesManager implements BaseColumns {
	private static final String TAG = "ExpensesProvider";
	
	public static final Uri AUTORITY = Uri.parse( "content://com.welmo.contents.providers.expenses"); 

	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	public static final String 	DATABASE_NAME 		= "ExpensesManager.db";
	public static final int 	DATABASE_VERSION 	= 1;
	
	
	// Expenses Columns
	public static final String EXPENSES_TABLE = "EXPENSES";
	public interface ExpensesColumns {
		public static final String OBJECT = "Object";
		public static final String DSCRIPTION = "Description";
		public static final String THEGROUP = "TheGroup";
		public static final String OWNER = "Owner";
		public static final String DATE = "Date";
		public static final String VAL = "Val";
		public static final String VALCURR = "ValVCur";
		public static final String COVTAUX = "ConTaux";
	}
	public static final String EXPENSES_TABLE_CREATE_CONDITIONS = 
        "(" +  Expenses._ID + " INTEGER PRIMARY KEY, " +
        	Expenses.DSCRIPTION +" TEXT, " +
        	Expenses.OBJECT + " TEXT, " +
        	Expenses.THEGROUP + " TEXT, "+
			Expenses.OWNER + " TEXT, "+
			Expenses.DATE + " TEXT, "+
			Expenses.VAL +  " REAL, "+
			Expenses.VALCURR + " TEXT, "+
			Expenses.COVTAUX + " REAL DEFAULT 1.0)";
	
	public static final class Expenses implements BaseColumns, ExpensesColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://com.welmo.contents.providers.expenses/expenses");
    }
	// Currency Rates Columns
	public static final String CURRENCIES_RATES_TABLE = "CURRENCIES_RATES";
	public interface CurrenciesColumn {
		public static final String CURRSOURCE = "CurrSource";
		public static final String CURRTARGET = "CurrTarget";
		public static final String RATEDATE = "RateData";
		public static final String RATESRC_VS_TGT = "Rate";
	}
	public static final String CURRENCYES_RATES_TABLE_CREATE_CONDITIONS = 
        "("+ Currency._ID + " ,"+
        Currency.CURRSOURCE +" TEXT, " +
        Currency.CURRTARGET + " TEXT, " +
        Currency.RATEDATE + " TEXT, "+
        Currency.RATESRC_VS_TGT + " REAL DEFAULT 1.0, " +
        "PRIMARY KEY( " + Currency.CURRSOURCE + " , " + Currency.CURRTARGET + " ))";
	
	public static final class Currency implements BaseColumns, CurrenciesColumn {
        public static final Uri CONTENT_URI = Uri.parse("content://com.welmo.contents.providers.expenses/currencies");
        public static final String parseDate(int year, int month, int day){
        	SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        	return bartDateFormat.format(new GregorianCalendar(year,month,day).getTime());
        }
    }
	// Currency ISO Columns
	public static final String CURRENCIES_ISO_TABLE = "CURRENCIESISO";
	public interface CurrenciesISOColumn {
		public static final String COUNTRYCODE = "CountryCode";
		public static final String COUNTRYNAME = "CountryName";
		public static final String CURRCODE = "CurrCode";
		public static final String CURNAME = "CurrName";
		public static final String CURSYMBOL = "CurrSymbol";
		public static final String CURRRANK = "Rank";
		public static final String ISDEFAULT = "IsDefault";
		public static final String SELECTED = "Selected";
	}
	public static final String CURRENCYES_ISO_TABLE_CREATE_CONDITIONS = 
        "("+ Currency._ID + " ," +
        CurrenciesISO.COUNTRYCODE +" TEXT PRIMARY KEY, " +
        CurrenciesISO.COUNTRYNAME + " TEXT, " +
        CurrenciesISO.CURRCODE +" TEXT, " +
        CurrenciesISO.CURNAME + " TEXT, " +
        CurrenciesISO.CURSYMBOL + " TEXT, " +
        CurrenciesISO.CURRRANK + " INTEGER DEFAULT 0, " +
        CurrenciesISO.ISDEFAULT	+ "	INTEGER DEFAULT 0, " +
        CurrenciesISO.SELECTED	+ "	INTEGER DEFAULT 0)";
	
	public static final class CurrenciesISO implements BaseColumns, CurrenciesISOColumn {
		public static final Uri CONTENT_URI = Uri.parse("content://com.welmo.contents.providers.expenses/currcountryISO");
    }
}
