package com.welmo.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TableLayout;

public class CalendarMonthView extends Activity {
    /** Called when the activity is first created. */
	// example of change and update in the repository
	TableLayout theCalendar = null;
	
    @Override
    public void onCreate(Bundle icicle) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);
        setContentView(R.layout.calendarmonthview);
    }
    
    
}