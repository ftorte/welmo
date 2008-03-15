package com.welmo.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class CalendarMonthView extends Activity {
    /** Called when the activity is first created. */
	// example of change and update in the repository
	CalendarMonth theCalendar = null;
	Calendar today=null;
	
	String[] mMonths={
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
        today =  Calendar.getInstance();
        
        //set year Label
		TextView labelYear = (TextView)findViewById(R.id.MontLabel);
		labelYear.setText(Integer.toString(today.get(Calendar.YEAR)));
		
		//set month Label
		TextView labelMonth = (TextView)findViewById(R.id.MontLabel);
		labelYear.setText(mMonths[today.get(Calendar.MONTH)]);
		
		//init the month view 
	    theCalendar.setMonth(today.get(Calendar.YEAR),today.get(Calendar.MONTH));
	    theCalendar.SetFocusOnDay(today.get(Calendar.YEAR),
	    		today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH));
	    
	    //init the short meeting list description
		CalendarDayMeetingListShort theMeetingList = (CalendarDayMeetingListShort)findViewById(android.R.id.list);
		theMeetingList.setDay(today.get(Calendar.YEAR), 
				today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }
    
    
}