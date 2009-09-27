package com.welmo.contents;




import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.welmo.contents.ExpensesManager.CurrenciesISO;
import com.welmo.contents.ExpensesManager.Currency;
import com.welmo.contents.ExpensesManager.Expenses;
import com.welmo.contents.synchronizers.SynchCurrenciesRates;


import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * A list view example where the
 * data comes from a cursor, and a
 * SimpleCursorListAdapter is used to map each item to a two-line
 * display.
 */

public class WelmoTestProviders extends ListActivity {
	/** Called when the activity is first created. */
	

	@SuppressWarnings("static-access")
	@Override    
	protected void onCreate(Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);  
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		// Get a cursor with all phones
		
		
		//fill an example is any
		double date = cal.getTimeInMillis();
		ContentValues values = new ContentValues(2);
		values.put(Expenses.DSCRIPTION, "AchatTele");
		values.put(Expenses.THEGROUP, "ElectroManager");
		values.put(Expenses.OBJECT, "ammeublement");
		values.put(Expenses.OWNER, "Me");
		values.put(Expenses.VAL, 10.03);
		values.put(Expenses.VALCURR, "EUR");
		values.put(Expenses.DATE, bartDateFormat.format(cal.getTime()));

		Uri res = getContentResolver().insert(
				ExpensesManager.AUTORITY.withAppendedPath(ExpensesManager.AUTORITY, "expenses/22"), 
				values);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		values.put(Expenses.DATE, bartDateFormat.format(cal.getTime()));
		res = getContentResolver().insert(
				ExpensesManager.AUTORITY.withAppendedPath(ExpensesManager.AUTORITY, "expenses/32"), 
				values);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		values.put(Expenses.DATE, bartDateFormat.format(cal.getTime()));
		res = getContentResolver().insert(
				ExpensesManager.AUTORITY.withAppendedPath(ExpensesManager.AUTORITY, "expenses/42"), 
				values);
		
		//fill an currency example
		values = new ContentValues(2);
		values.put(Currency.RATESRC_VS_TGT, 0.115);
		values.put(Currency.RATEDATE, Currency.parseDate(2009,10,10));
		
		String[] projection = new String[1];
		String selection;
		String[] selectionArgs = new String[6];
		String sortOrder;
		
		projection=null;
		selection=null;
		selectionArgs=null;
		sortOrder = CurrenciesISO.CURNAME;
		
		Cursor c = getContentResolver().query(Uri.parse( "content://com.welmo.contents.providers.expenses/currcountryISO/all/EUR/"),
				projection, selection, selectionArgs, sortOrder);        
		startManagingCursor(c);        	
		
		// Map Cursor columns to views defined in simple_list_item_2.xml        
		ListAdapter adapter = new SimpleCursorAdapter(this,R.layout.listlayout, c,
				new String[] {CurrenciesISO.COUNTRYNAME,CurrenciesISO.COUNTRYCODE,CurrenciesISO.CURNAME,
				CurrenciesISO.CURRCODE,CurrenciesISO.CURSYMBOL,CurrenciesISO.CURRRANK},                        
				new int[] { R.id.text1, R.id.text2, R.id.text3,R.id.text4,R.id.text5,R.id.text6});
		setListAdapter(adapter);   
		
		SynchCurrenciesRates sync = new SynchCurrenciesRates();
		try{
			sync.synch(getContentResolver(),SynchCurrenciesRates.SERVICE_BCE);
		}
		catch(Exception e)
		{
			Log.e("synch", "Fatal synch error: " + e.getMessage());
		}
		
	}
}