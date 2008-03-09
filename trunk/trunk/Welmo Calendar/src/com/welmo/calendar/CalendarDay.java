package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class CalendarDay extends TextView{

	Drawable defaultBackground;
	int mOrgWidth;
	int mOrgHeight;
	int flag=0;
	
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
				//ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b)
					v.setBackgroundColor(0xffedd400);
				else
					v.setBackground(defaultBackground);
			}		
		});
		setAlignment(android.text.Layout.Alignment.ALIGN_CENTER);
		defaultBackground = this.getBackground();	
	}

	public CalendarDay(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onMeasure(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.onMeasure(arg0, arg1);
		//setMeasuredDimension(arg0, arg1);
	}
	    
}
