package com.welmo.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.welmo.R;

public class CalendarMonthView extends Activity {
    /** Called when the activity is first created. */
	// example of change and update in the repository
	CalendarMonth theCalendar = null;
	Calendar today=null;
	int mYear=0;
	int mMonth=0;
	int mFocusedDay=0;
	
	String[] mMonths={
			"..",
			"Jannuary",
			"Febbruary",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December"
	};
	
    @Override
    public void onCreate(Bundle icicle) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);
        setContentView(R.layout.calendarmonthview);
        theCalendar = (CalendarMonth) findViewById(R.id.Month);
        
        today 		= Calendar.getInstance();
        mYear 		= today.get(Calendar.YEAR);
        mMonth 		= today.get(Calendar.MONTH)+1;
        mFocusedDay = today.get(Calendar.DAY_OF_MONTH);
        
        ShowWeekHour(false);
        ShowDayMeetingsList(true);
        //set year Label
		TextView labelYear = (TextView)findViewById(R.id.MontLabel);
		labelYear.setText(Integer.toString(today.get(Calendar.YEAR)));
		
		//set month Label
		TextView labelMonth = (TextView)findViewById(R.id.MontLabel);
		labelYear.setText(mMonths[mMonth]);
		
		//init the month view 
		theCalendar.setMCMV(this);
	    theCalendar.setMonth(mYear,mMonth);
	    theCalendar.SetFocusOnDay(mYear,mMonth,mFocusedDay);
	    
	    //init the short meeting list description
		CalendarDayMeetingListShort theMeetingList = (CalendarDayMeetingListShort)findViewById(R.id.MeetingDayShortList);
		theMeetingList.setDay(mYear,mMonth,mFocusedDay);
		
    }
    
    public void ChangeFocusedDay(int day){
    	mFocusedDay = day;
    	CalendarDayMeetingListShort theMeetingList = (CalendarDayMeetingListShort)findViewById(R.id.MeetingDayShortList);
		theMeetingList.setDay(mYear,mMonth,mFocusedDay);
    }
    public void ShowDayMeetingsList(boolean show){
    	View theView = (View)findViewById(R.id.MeetingDayShort);
    	if(show){
    		theView.setVisibility(View.VISIBLE);
    	}
    	else{
    		theView.setVisibility(View.GONE);
    	}
    }
    public void ShowWeekHour(boolean show){
    	View theView1 = (View)findViewById(R.id.CalendarWeekHour);
    	View theView2 = (View)findViewById(R.id.NullLabel);
    	if(show){
    		theView1.setVisibility(View.VISIBLE);
    		theView2.setVisibility(View.VISIBLE);
    	}
    	else{
    		theView1.setVisibility(View.GONE);
    		theView2.setVisibility(View.GONE);
    	}
    }
    
    void ShowMessge(String Msg){
        Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}
}