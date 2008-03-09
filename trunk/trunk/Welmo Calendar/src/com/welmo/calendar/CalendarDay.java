package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;


public class CalendarDay extends TextView{

	public CalendarDay(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
				ShowMessge("Calendar Day Clik Catched: "+id);
			}
		});
		setFocusable(true);
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				int id = v.getId();
				ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b)
					v.setBackgroundColor(0xFF00FF00);
				else
					v.setBackgroundColor(0xFF000000);
			}		
		});
		setAlignment(android.text.Layout.Alignment.ALIGN_CENTER);
	}

	public CalendarDay(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	    
}
