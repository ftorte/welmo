package com.welmo.travel.tracking;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


public class ExpenseTracker extends ListActivity {

	public ExpenseTracker() {
		super();
		theCfg = new Configuration();
	}
	
	//-----------------------------------------------------------------
	// public constant values & variables
	private static final int REQUEST_CODE_PREFERENCES = 2;
	public static final String CURRENCY = "currency";
	public static final String POSITION = "position";
	public static final String VALUE = "value";
	public static final String KEY_RESULT = "value";
	//-----------------------------------------------------------------
	
	//-----------------------------------------------------------------
	// protected member variables
	private CurrencyListAdapter adapter = null;
	private TextView theSourceValue = null;
	private Configuration theCfg;
	private MyKeyboard theKeyboard = null;
	private Keyboard theKeyboardLayout = null;
	private InputHandler theInputHandler =null;
	private View theSource = null;
	private int colorTextDefault;
	private int colorTextEdit;
	//-----------------------------------------------------------------
	
	
	private class Configuration {
		String[] sourceCurrConfig=null;
		double sourceDefAmount=0;
		String sourceTxtAmmount="";
		double sourceAmmount=0;
		int synchSource=1;
		String updated_info[]=null;
		Configuration(){
			updated_info=new String[2];
		}
		void resetAmount(){sourceAmmount = sourceDefAmount;}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setup default configuration first time application in launched
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	
		//setup layout
		setContentView(R.layout.main);
		
		colorTextDefault = getResources().getColor(R.color.sky_blu_clear);
		colorTextEdit = getResources().getColor(R.color.chamaleon_clear);
		
		// Create the object that will hanlde the input from the keyboard
		theInputHandler = new InputHandler();
		
		//setup the keyboard
		theKeyboard = (MyKeyboard) findViewById(R.id.EditKeyboard01);
		theKeyboardLayout = new Keyboard(this, R.xml.qwerty);
	
		theKeyboard.setKeyboard(theKeyboardLayout);
		
		//attach the keyboard to the list view and the input handler
		theKeyboard.setTheListView(getListView());
		theKeyboard.setInputHandler(theInputHandler);
		
		theSourceValue = ((TextView)findViewById(R.id.srcvalue));
		theSourceValue.setTextColor(colorTextDefault);
	
		//setup handler for the list view item click
		getListView().setOnItemClickListener(new OnItemClickListener() 
		{ 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
					theKeyboard.setInputItem(position);
					theKeyboard.setVisibility(View.VISIBLE);
					theInputHandler.setInputCurrencyList(position);
				}	 
		});
		
		theSource = ((View)findViewById(R.id.source));
		theSource.setOnClickListener(new OnClickListener() 
		{ 
			@Override
			public void onClick(View v) {		
				theKeyboard.setVisibility(View.VISIBLE);
				theInputHandler.setInputCurrencySource();
			}	
		});
		
		//crateAdapter
		adapter = new CurrencyListAdapter(this);
		
		// load data
		readPreferences();
		theCfg.resetAmount();
		UpdateSurceView(false,null);
		loadAdapter();
	}
	private void readPreferences() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		theCfg.sourceCurrConfig  = sp.getString(getResources().getString(R.string.prfkey_source_curr), 	"Euro Zone,EU,EUR,EUR").split(",", 4);
		theCfg.sourceDefAmount = Integer.parseInt(sp.getString(getResources().getString(R.string.prfkey_source_default_ammount), "1000"));
		theCfg.synchSource = Integer.parseInt(sp.getString(getResources().getString(R.string.prfkey_synch_source), "1"));
		theCfg.updated_info[0] = sp.getString(getResources().getString(R.string.cfg_update_date), "");
		theCfg.updated_info[1] = sp.getString(getResources().getString(R.string.cfg_update_source), "");
		((TextView)findViewById(R.id.statusbar)).setText(new CustomStringBuilder().compileString(getResources().getString(R.string.statusbar), theCfg.updated_info));

		theCfg.resetAmount();
		
	}
	void loadAdapter(){
		Cursor c = getContentResolver().query(Uri.parse( "content://com.welmo.contents.providers.expenses/currcountryISO/rates/" 
				+ theCfg.sourceCurrConfig[2]), null, null, null, CurrenciesISO.COUNTRYNAME); 
		if(c!= null){
			adapter.readCurrencyFromCursor(c);
			c.close();
		}
		adapter.ChangeValueInArray(CurrencyListAdapter.CHANGED_VALUE_SOURCE,theCfg.sourceAmmount,null,0);
		setListAdapter(adapter); 
	}
	private void UpdateSurceView(boolean inputText, String InputText){
		
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(4);
		if (f instanceof DecimalFormat) {
			((DecimalFormat) f).setGroupingUsed(true);
		}
		// Update display
		// If the source is used as imput show the value form the text
		if(inputText)
			((TextView)findViewById(R.id.srcvalue)).setText(InputText);
			else
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
		case   REQUEST_CODE_PREFERENCES:
			readPreferences();
			UpdateSurceView(false, null);
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
				Toast.makeText(ctx, "Inpossible to synchornize. Check internet connection and retry", Toast.LENGTH_LONG).show();
			else{
				Toast.makeText(ctx, "Synchronized with rates from BCE", Toast.LENGTH_LONG).show();
				
				Calendar cal = Calendar.getInstance();
			    SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
			    theCfg.updated_info[0]= sdf.format(cal.getTime());
				theCfg.updated_info[1]="BCE";
				                                        
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(getResources().getString(R.string.cfg_update_date),theCfg.updated_info[0]);
				editor.putString(getResources().getString(R.string.cfg_update_source), theCfg.updated_info[1]);
				editor.commit();
				((TextView)findViewById(R.id.statusbar)).setText(new CustomStringBuilder().compileString(getResources().getString(R.string.statusbar), theCfg.updated_info));
			}
		}

		@Override
		protected void onPreExecute() {
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

	public class InputHandler implements CalcInputHandler {

		private boolean isCurrencySource;
		private boolean isCurrencyList;
		private int position; 
		private String strInputValue;
		private NumberFormat theNumberFormat = null;
		private char chrDecimalSeparator;
		private int MAX_NUM_OF_DECIMAL = 4;
		
		//constructor
		public InputHandler(){
			isCurrencySource = false;
			isCurrencyList = false;
			position=0; 
			strInputValue = "";
			theNumberFormat = NumberFormat.getInstance();
			theNumberFormat.setMaximumFractionDigits(MAX_NUM_OF_DECIMAL);
			if (theNumberFormat instanceof DecimalFormat) {
				((DecimalFormat)theNumberFormat).setDecimalSeparatorAlwaysShown(false);
				((DecimalFormat) theNumberFormat).setGroupingUsed(true);
			}
			chrDecimalSeparator = (new DecimalFormatSymbols()).getDecimalSeparator();
		}
		public void setInputCurrencyList(int ItemPosition){
			// Cancel previous input from source if any
			theSourceValue.setTextColor(colorTextDefault);
			// Set new Input from currency list
			double val=((CurrencyConv)adapter.getItem(ItemPosition)).getTrgValue();
			strInputValue  = theNumberFormat.format(((CurrencyConv)adapter.getItem(ItemPosition)).getTrgValue());
			adapter.ChangeValueInArray(CurrencyListAdapter.SET_VALUE_TARGET_FOR_INPUT,0,strInputValue,ItemPosition);
			isCurrencySource = false;
			isCurrencyList = true;
			position = ItemPosition;
		}
		
		public void setInputCurrencySource(){
			// Cancel previous input from currency list if any
			adapter.ChangeValueInArray(CurrencyListAdapter.CANCEL_VALUE_TARGET_FOR_INPUT,0,null,0);
			// Set new Input
			theSourceValue.setTextColor(colorTextEdit);
			isCurrencySource = true;
			isCurrencyList = false;
			strInputValue = theSourceValue.getText().toString();
		}
		
		@Override
		public void inputModifier(Modifier m) {
			switch(m){
			case AC:
				strInputValue = "0";
				break;
			case C:
				strInputValue = "0";
				UpdateInputValue();
				break;
			case BS:
				if(strInputValue.length()>1)
					strInputValue = strInputValue.substring(0, strInputValue.length()-1);
				else
					strInputValue="0";
				break;
			default:
				break;
			}
			UpdateInputValue();
		}
		@Override
		public void inputOperators(Operators o) {
			switch(o){
			case DIVR:
				break;	
			case MUL:
				break;	
			case SUM:
				break;	
			case SUB:
				break;	
			case EQ:
				break;	
			default:
				break;
			}
		}
		@Override
		public void inputSymboil(Symbols s) {
			// Parse current input value
			int decimalPointPositon = strInputValue.lastIndexOf(chrDecimalSeparator);
			if(s == s.DOT && (decimalPointPositon == -1)){
				strInputValue= strInputValue + chrDecimalSeparator;
				UpdateInputValue();
			}
			else{
				//accept extra digits only if number of fraction digit is less than MaximumFractionDigits (warning decimalPointPositon is N -1
				if(decimalPointPositon == -1 || (strInputValue.length()- decimalPointPositon < MAX_NUM_OF_DECIMAL +1)){
					if(strInputValue.compareTo("0")==0)
						strInputValue="";
					switch(s){
						case ZERO:strInputValue = strInputValue + "0";break;
						case ONE:strInputValue = strInputValue + "1";break;	
						case TWO:strInputValue = strInputValue + "2";break;	
						case THREE:strInputValue = strInputValue + "3";;break;	
						case FOUR:strInputValue = strInputValue + "4";break;	
						case FIVE:strInputValue = strInputValue + "5";break;	
						case SIX:strInputValue = strInputValue + "6";;break;	
						case SEVEN:strInputValue = strInputValue + "7";;break;	
						case HEIGHT:strInputValue = strInputValue + "8";;break;	
						case NINE:strInputValue = strInputValue + "9";;break;	
						case THOUSAND:
							if(strInputValue != "")
								strInputValue = strInputValue + "000";;break;
						default:
							break;
					}
					UpdateInputValue();
				}
				else
					Toast.makeText(getBaseContext(), "Warning: Max 4 digit after decimal point", Toast.LENGTH_SHORT).show();
			}
		}
		void UpdateInputValue()
		{
			try {
				if(isCurrencySource){
					theCfg.sourceAmmount = theNumberFormat.parse(strInputValue).doubleValue();
					UpdateSurceView(true,strInputValue);
					adapter.ChangeValueInArray(CurrencyListAdapter.CHANGED_VALUE_SOURCE,theCfg.sourceAmmount,null,0);
				}
				else{
					theCfg.sourceAmmount = adapter.ChangeValueInArray(CurrencyListAdapter.INPUT_VALUE_TARGET,theNumberFormat.parse(strInputValue).doubleValue(),strInputValue,position);
					UpdateSurceView(false,null);
				}
					
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}
		@Override
		public void inputSpecialActions(SpecialActions a) {
			switch(a){
			case HIDEKEYBOARD: 
				theSourceValue.setTextColor(colorTextDefault);
				adapter.ChangeValueInArray(CurrencyListAdapter.CANCEL_VALUE_TARGET_FOR_INPUT,0,null,0);
				isCurrencySource = true;
				isCurrencyList = false;
				position = 0;
				strInputValue = "";
				theKeyboard.setVisibility(View.GONE);
				break;
			case RETURN:
				break;
			default:
				break;
			}
		}
	}
}