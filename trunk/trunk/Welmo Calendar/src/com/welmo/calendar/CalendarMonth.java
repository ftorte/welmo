package com.welmo.calendar;

import java.util.Calendar;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarMonth extends TableLayout{

	protected int 	mYear			= 1970; //default year
	protected int 	mMonth			= Calendar.JANUARY;	
	protected int	mFistDayWeek 	= Calendar.MONDAY;
	
	
	public CalendarMonth(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
    			ShowMessge("Calendar Month Clik Catched: "+id);
			}
    	});
		// TODO Auto-generated constructor stub
	}

	public CalendarMonth(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}

	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight() +10);
		//setMeasuredDimension(35,getMeasuredHeight());
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
		//canvas.drawText(getText().toString(), mPaddingLeft, mPaddingTop - (int) mTextPaint.ascent(), mTextPaint);    
		//draw the occupation bar
	}
	
	public void setMonth(int year, int month){
		mFistDayWeek = Calendar.MONDAY;
		mYear	= year;
		mMonth	= month;
			
		Calendar cl = Calendar.getInstance();
		cl.set(year, month, 1);
		//handle first week
		if(mFistDayWeek < cl.get(Calendar.DAY_OF_WEEK))
			cl.add(Calendar.DAY_OF_YEAR,-(cl.get(Calendar.DAY_OF_WEEK) - mFistDayWeek));
		else
			cl.add(Calendar.DAY_OF_YEAR,-(cl.get(Calendar.DAY_OF_WEEK) - (7 - mFistDayWeek)));
			
		CalendarWeek currWeek = (CalendarWeek) getChildAt(1);
		for (int index = 0; index < 7; index++){
			((CalendarDay)currWeek.getChildAt(index)).setDayNumber(cl.get(Calendar.DAY_OF_MONTH));
			cl.add(Calendar.DAY_OF_YEAR,1);
		}
		for (int week = 2; week < 7; week++){
			currWeek = (CalendarWeek) this.getChildAt(week);
			for (int index = 0; index < 7; index++){
				((CalendarDay)currWeek.getChildAt(index)).setDayNumber(cl.get(Calendar.DAY_OF_MONTH));
				cl.add(Calendar.DAY_OF_YEAR,1);
			}
		}
	}

	public void SetFocusOnDay(int year, int month, int day){
		if ((year != mYear) || (month != mMonth))
			setMonth(year,month);
		Calendar cl = Calendar.getInstance();
		cl.set(year, month, day);
		int WeekOfMonth = cl.get(Calendar.WEEK_OF_MONTH);
		int DayOfWeek = cl.get(Calendar.DAY_OF_WEEK);
		int DayOfWeekIndex = 0;
		
		if(mFistDayWeek < DayOfWeek)
			DayOfWeekIndex = DayOfWeek - mFistDayWeek;
		else
			DayOfWeekIndex = DayOfWeek - (7 - mFistDayWeek);	
			((CalendarDay)((CalendarWeek) getChildAt(WeekOfMonth)).
					getChildAt(DayOfWeekIndex)).requestFocus();
	}

	public int getYear() {
		return mYear;
	}
	public void setYear(int year) {
		mYear = year;
	}
	public int getMonth() {
		return mMonth;
	}
	public void setMonth(int month) {
		mMonth = month;
	}
	public int getFistDayWeek() {
		return mFistDayWeek;
	}
	void setFistDayWeek(int fistDayWeek) {
		mFistDayWeek = fistDayWeek;
	}
}
