package com.welmo.calendar;

import java.util.Calendar;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.Toast;

public class CalendarWeek extends TableRow{

	protected boolean CurrentWeekFocused = false;
	int mYear;
	int mMonth;
	int mFirstDay;
	
	public CalendarWeek(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
			}
    	});
		// TODO Auto-generated constructor stub
	}
	public CalendarWeek(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}

	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
	}

	public boolean isCurrentWeekFocused() {
		return CurrentWeekFocused;
	}

	public void setCurrentWeekFocused(boolean currentWeekFocused) {
		CurrentWeekFocused = currentWeekFocused;
	}
	public void setFisrtDayWeekData(int year, int month,int day){
		mYear=year;
		mMonth=month;
		mFirstDay=day;
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
	public int getFirstDayOfTheWeek() {
		return mFirstDay;
	}
	public void setFirstDayOfTheWeek(int day) {
		mFirstDay = day;
	}

}
