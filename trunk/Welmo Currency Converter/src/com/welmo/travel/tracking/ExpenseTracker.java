package com.welmo.travel.tracking;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


import com.welmo.contents.ExpensesManager.CurrenciesISO;
import com.welmo.contents.synchronizers.SynchCurrenciesRates;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class ExpenseTracker extends ListActivity {

	public ExpenseTracker() {
		super();
		theCfg = new Configuration();
	}
	
	//-----------------------------------------------------------------
	// public constant values & variables
	private static final int DIALOG_INPUTVALUE_TARGET = 0;
	private static final int DIALOG_INPUTVALUE_SOURCE = 1;
	private static final int REQUEST_CODE_PREFERENCES = 2;
	public static final String CURRENCY = "currency";
	public static final String POSITION = "position";
	public static final String VALUE = "value";
	public static final String KEY_RESULT = "value";
	//-----------------------------------------------------------------
	
	//-----------------------------------------------------------------
	// protected member variables
	private CurrencyListAdapter adapter = null;
	private Configuration theCfg;
	private Cursor c = null;
	private KeyboardView theKeyboard = null;
	private Keyboard theKeyboardLayout = null;
	//-----------------------------------------------------------------
	
	
	private class Configuration {
		String[] sourceCurrConfig=null;
		double sourceDefAmount=0;
		double sourceAmmount=0;
		int synchSource=1;
		String updated_date="";
		void resetAmount(){sourceAmmount = sourceDefAmount;}
	}
	
	public class theKeyboardActionListener implements OnKeyboardActionListener{

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPress(int primaryCode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRelease(int primaryCode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onText(CharSequence text) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void swipeDown() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void swipeLeft() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void swipeRight() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void swipeUp() {
			// TODO Auto-generated method stub
			
		}};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setup default configuration first time application in launched
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		//setup layout
		setContentView(R.layout.main);
		
		//setup input keyboard
		theKeyboard = (KeyboardView) findViewById(R.id.EditKeyboard01);
		theKeyboardLayout = new Keyboard(this, R.xml.qwerty);
		theKeyboard.setKeyboard(theKeyboardLayout);
		theKeyboard.setOnKeyboardActionListener(new theKeyboardActionListener());
			
		
		//setup event handlers for ckick event
		View theView = ((View)findViewById(R.id.source));
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() 
		{ 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				ChangeValueView(DIALOG_INPUTVALUE_TARGET, position);
				}	 
		});
		theView.setOnClickListener(new OnClickListener() 
		{ 
			@Override
			public void onClick(View v) {
				ChangeValueView(DIALOG_INPUTVALUE_SOURCE, -1);
			}	
		});
		theView.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		//crateAdapter
		adapter = new CurrencyListAdapter(this);
		
		// load data
		readPreferences();
		theCfg.resetAmount();
		UpdateSurceView();
		loadAdapter();
	}
	void ChangeValueView(int type,int position){
		Intent i = new Intent(this, ChangeValue.class);
		switch(type){
		case DIALOG_INPUTVALUE_TARGET:
			i.putExtra(POSITION, position);
			i.putExtra(CURRENCY, ((CurrencyConv)adapter.getItem(position)).getTrgCurrISO3());
			i.putExtra(VALUE, ((CurrencyConv)adapter.getItem(position)).getTrgValue());
			startActivityForResult(i, DIALOG_INPUTVALUE_TARGET);
			break;
		case DIALOG_INPUTVALUE_SOURCE:
			i.putExtra(POSITION, -1);
			i.putExtra(CURRENCY, theCfg.sourceCurrConfig[2]);
			i.putExtra(VALUE, theCfg.sourceAmmount);
			startActivityForResult(i, DIALOG_INPUTVALUE_SOURCE);
			break;
		}
	}
	private void readPreferences() {
        
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		theCfg.sourceCurrConfig  = sp.getString(getResources().getString(R.string.prfkey_source_curr), 	"Euro Zone,EU,EUR,EUR").split(",", 4);
		theCfg.sourceDefAmount = Integer.parseInt(sp.getString(getResources().getString(R.string.prfkey_source_default_ammount), "1000"));
		theCfg.synchSource = Integer.parseInt(sp.getString(getResources().getString(R.string.prfkey_synch_source), "1"));
		theCfg.updated_date = sp.getString(getResources().getString(R.string.prfkey_target_list), "");
		theCfg.resetAmount();
	}
	void loadAdapter(){
		Cursor c = getContentResolver().query(Uri.parse( "content://com.welmo.contents.providers.expenses/currcountryISO/rates/" 
				+ theCfg.sourceCurrConfig[2]), null, null, null, CurrenciesISO.COUNTRYNAME); 
		if(c!= null){
			adapter.readCurrencyFromCursor(c);
			c.close();
		}
		adapter.ChangeValueInArray(CurrencyListAdapter.CHANGED_VALUE_SOURCE,theCfg.sourceAmmount,0);
		setListAdapter(adapter); 
	}
	private  void UpdateSurceView(){
		
		NumberFormat f = NumberFormat.getInstance(Locale.US);
		if (f instanceof DecimalFormat) {
			((DecimalFormat) f).setGroupingUsed(true);
		}
		// Update display
		((TextView)findViewById(R.id.srcvalue)).setText(f.format(theCfg.sourceAmmount));
		((ImageView)findViewById(R.id.srcflag)).setImageResource(getResources().getIdentifier(theCfg.sourceCurrConfig[1].toLowerCase(), "drawable","com.welmo.travel.tracking"));
		((TextView)findViewById(R.id.srccountryname)).setText(theCfg.sourceCurrConfig[0]);
		((TextView)findViewById(R.id.srccurrcode)).setText(theCfg.sourceCurrConfig[3]);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		theKeyboard.setVisibility(View.GONE);
		switch(requestCode) {
		case DIALOG_INPUTVALUE_TARGET:
		    if(resultCode == RESULT_OK){
		    	Bundle extras = data.getExtras();
		    	double newValue = extras.getDouble(VALUE);
		    	int position =  extras.getInt(POSITION);
		    	theCfg.sourceAmmount = adapter.ChangeValueInArray(CurrencyListAdapter.CHANGED_VALUE_TARGET,newValue,position);
		    	UpdateSurceView();
		    }
		    break;
		case DIALOG_INPUTVALUE_SOURCE:
		    if(resultCode == RESULT_OK){
		    	Bundle extras = data.getExtras();
		    	double newValue = extras.getDouble(VALUE);
		    	theCfg.sourceAmmount = newValue;
		    	UpdateSurceView();
		    	adapter.ChangeValueInArray(CurrencyListAdapter.CHANGED_VALUE_SOURCE,newValue,0);
		    }
		    break;
		case   REQUEST_CODE_PREFERENCES:
			readPreferences();
			UpdateSurceView();
			loadAdapter();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.currratemenu, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.synch:
                new ExecuteSynch(this).execute(SynchCurrenciesRates.SERVICE_BCE);
        		loadAdapter();
                return true;

            case R.id.help:
            	Intent launchHelpView = new Intent().setClass(this, HelpView.class);
            	this.startActivity(launchHelpView);
            	return true;
            case R.id.config:
                Intent launchPreferencesIntent = new Intent().setClass(this, Preferences.class);
                startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
            	return true;
            default:
                break;
        }
        return false;
    }
	private class ExecuteSynch extends AsyncTask<Integer,Integer,Integer>{

		ProgressDialog prgDlg = null;
		Context ctx;
		String errMessage;
		int result;
		
		static final int NO_ERROR = 0;
		static final int ERROR = 1;
		
		
		ExecuteSynch(Context context){
			ctx = context;
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			showDialog(1);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			loadAdapter();
			if(prgDlg != null){
				prgDlg.cancel();
				prgDlg.dismiss();
			}
			if (result == ERROR)
			{
				Toast.makeText(ctx, "Inpossible to synchornize. Check internet connection and retry", Toast.LENGTH_LONG).show();
				String[] params = new String[2];
				params[0] = "today";
				params[1] = "BCE";
				CustomStringBuilder cst = new CustomStringBuilder();
				((TextView)findViewById(R.id.statusbar)).setText(cst.compileString(getResources().getString(R.string.statusbar), params));
			}
			else
				Toast.makeText(ctx, "Synchronized with rates from BCE", Toast.LENGTH_LONG).show();
				/*String[] params = new String[2];
				params[0] = "today";
				params[1] = "BCE";
				((TextView)findViewById(R.id.statusbar)).setTag(new CustomStringBuilder().compileString(getResources().getString(R.string.statusbar), params));*/
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(ctx != null){
				prgDlg  = new ProgressDialog(ctx);
				prgDlg.setTitle("Synchronization");
				prgDlg.setMessage("Please wait while loading rates...");
				prgDlg.setIndeterminate(true);
				prgDlg.setCancelable(true);   
				prgDlg.show();
			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			SynchCurrenciesRates sync = new SynchCurrenciesRates();
			try {
				sync.synch(getContentResolver(), params[0]);
			} catch (IOException e) {
				e.printStackTrace();
				errMessage = e.getMessage();
				return ERROR;
			}
			catch (Exception e){
				e.printStackTrace();
				errMessage = e.getMessage();
				return ERROR;
			}
			return NO_ERROR;

		}

	}
}