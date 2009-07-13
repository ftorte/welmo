package com.welmo.travel.tracking;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;

public class ChangeValue extends Activity{
	TextView mCurrCode = null;
	EditText mValue = null;
	Button confirmButton = null;
	Button cancelButton  = null;
	int position = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.valueinput);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
				android.R.drawable.ic_dialog_alert);
		
		mCurrCode = (TextView) findViewById(R.id.currencycode);;
		mValue = (EditText) findViewById(R.id.EditText01);
		confirmButton = (Button) findViewById(R.id.ButtonConfirm);
		cancelButton = (Button) findViewById(R.id.ButtonCancel);
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	    	mCurrCode.setText(extras.getString(ExpenseTracker.CURRENCY));
	        mValue.setText(Double.toString(extras.getDouble(ExpenseTracker.VALUE)));
	        position = extras.getInt(ExpenseTracker.POSITION);
	        
	      /*[FT] Toast.makeText(this, "get vaalues" + extras.getString(ExpenseTracker.CURRENCY)
	    			+ " " + (Double.toString(extras.getDouble(ExpenseTracker.VALUE)))
	    			+ " " + position, Toast.LENGTH_SHORT).show();*/
	    }
	   
	    confirmButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Bundle bundle = new Bundle();
	            double value =  Double.parseDouble(mValue.getText().toString());
	            bundle.putDouble(ExpenseTracker.VALUE,value);
	            bundle.putInt(ExpenseTracker.POSITION,position);
	            Intent mIntent = new Intent();
	            mIntent.putExtras(bundle);
	            setResult(RESULT_OK, mIntent);
	            finish();
	        }
	    });
	    cancelButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	setResult(RESULT_CANCELED, null);
	            finish();
	        }
	    });
	}
}
