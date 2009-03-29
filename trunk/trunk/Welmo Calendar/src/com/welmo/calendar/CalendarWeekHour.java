package com.welmo.calendar;

import java.util.Calendar;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;

import com.welmo.R;

public class CalendarWeekHour extends TableLayout{
	public CalendarWeekHour(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public CalendarWeekHour(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public void setWeekDaysFromMonday(int year, int month, int Monday) {
		
		CalendarDayHour day =  null;
		Calendar cl = Calendar.getInstance();
		// set ISO standard calendar
		cl.setFirstDayOfWeek(Calendar.MONDAY);
		cl.setMinimalDaysInFirstWeek(4);
		cl.set(year, month-1, Monday);

		//setup Monday
		day = (CalendarDayHour)findViewById(R.id.WeekHourMon);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
		//setup Thursday
		cl.add(Calendar.DAY_OF_MONTH, 1);
		day = (CalendarDayHour)findViewById(R.id.WeekHourThu);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
		//setup Wednesday
		cl.add(Calendar.DAY_OF_MONTH, 1);
		day = (CalendarDayHour)findViewById(R.id.WeekHourWen);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
		//setup Tuesday
		cl.add(Calendar.DAY_OF_MONTH, 1);
		day = (CalendarDayHour)findViewById(R.id.WeekHourTue);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
		//setup Friday
		cl.add(Calendar.DAY_OF_MONTH, 1);
		day = (CalendarDayHour)findViewById(R.id.WeekHourFri);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
		//setup Saturday
		cl.add(Calendar.DAY_OF_MONTH, 1);
		day = (CalendarDayHour)findViewById(R.id.WeekHourSat);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
		//setup Sunday
		cl.add(Calendar.DAY_OF_MONTH, 1);
		day = (CalendarDayHour)findViewById(R.id.WeekHourSun);
		day.setDay(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DAY_OF_MONTH));
	}
}
