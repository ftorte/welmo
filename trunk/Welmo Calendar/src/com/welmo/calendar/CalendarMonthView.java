package com.welmo.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.welmo.R;
import com.welmo.tools.DialogListSelector;
import com.welmo.tools.DialogListSelector.OnSelectionListener;

public class CalendarMonthView extends Activity {
 

	/** Called when the activity is first created. */
	// example of change and update in the repository
	CalendarMonth theCalendar = null;
	Calendar today=null;
	int mYear=0;
	int mMonth=0;
	int mFocusedDay=0;
	TextView mLabelMonth=null;
	TextView mLabelYear=null;
	Context mContext;
	
	//----------------------------------------------------------------------------
	//---handle view status
	private enum State {MONTH_VIEW,WEEK_HOUR_VIEW};
	public enum ActionCode {DAY_LONG_CLICK,BACK_BUTTON};
	private State status		= State.MONTH_VIEW;
	//----------------------------------------------------------------------------
	
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
	
	String[] mMonths2={
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
	String[] mYears={
			"1990","1991","1992","1993","1994","1995","1996","1997","1998","1999",
			"2000","2001","2002","2003","2004","2005","2006","2007","2008","2009",
			"2010","2011","2012","2013","2014","2015","2016","2017","2018","2019",
			"2020","2021","2022","2023","2024","2025","2026","2027","2028","2029",
			"2030","2031","2032","2033","2034","2035","2036","2037","2038","2039",
			"2040","2041","2042","2043","2044","2045","2046","2047","2048","2049"
	};
	
    @Override
    public void onCreate(Bundle icicle) {
    	mContext=this;
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
		
		//init the month view 
		theCalendar.setMCMV(this);
		
		//set month Label
		mLabelMonth = (TextView)findViewById(R.id.MontLabel);
		mLabelMonth.setText(mMonths[mMonth]);
		
		//set year Label
		mLabelYear = (TextView)findViewById(R.id.YearLabel);
		mLabelYear.setText(mYears[mYear-1990]);
		
		//init the month view 
		theCalendar.setMonth(mYear,mMonth);
	    theCalendar.SetFocusOnDay(mYear,mMonth,mFocusedDay);
	    
	    //init the short meeting list description
		CalendarDayMeetingListShort theMeetingList = (CalendarDayMeetingListShort)findViewById(R.id.MeetingDayShortList);
		theMeetingList.setDay(mYear,mMonth,mFocusedDay);
		
		//handle toobar buttons
		((ImageView) findViewById(R.id.PrevMonth)).setOnClickListener(mTitelBarListener);
		((ImageView) findViewById(R.id.NextMonth)).setOnClickListener(mTitelBarListener);
		((TextView) findViewById(R.id.MontLabel)).setOnClickListener(mTitelBarListener);
		((TextView) findViewById(R.id.YearLabel)).setOnClickListener(mTitelBarListener);
    }
    private void IncMonth(boolean inc)
    {
    	if(inc){
    		if(mMonth == 12){
    			mYear=mYear+1;
    			mMonth = 1;
    		}
    		else{
    			mMonth ++;
    		}
    	}
    	else{
    		if(mMonth == 1){
    			mYear=mYear+1;
    			mMonth = 12;
    		}
    		else{
    			mMonth --;
    		}
    	}
    	UpdateDate();
    }

    private void UpdateDate()
    {
    	// TODO validate date with calendar
    	//Calendar cal=Calendar.getInstance();
    	//cal.set(mYear,this.mMonth,this.mFocusedDay);
    	
    	mLabelMonth.setText(mMonths[mMonth]);
    	mLabelYear.setText(mYears[mYear-1990]);
    	theCalendar.setMonth(mYear,mMonth);
	    theCalendar.SetFocusOnDay(mYear,mMonth,mFocusedDay);
	    
	    //init the short meeting list description
		CalendarDayMeetingListShort theMeetingList = (CalendarDayMeetingListShort)findViewById(R.id.MeetingDayShortList);
		theMeetingList.setDay(mYear,mMonth,mFocusedDay);
    }
    //Title Bar listeners
    OnClickListener mTitelBarListener = new OnClickListener() {
    	public void onClick(View v) {

    		//handle prev month
    		if(v.getId()==R.id.PrevMonth){
    			IncMonth(false);
    			return;
    		}
    		//handle Next month
    		if(v.getId()==R.id.NextMonth){
    			IncMonth(true);
    		}
    		if(v.getId()==R.id.MontLabel){
    			changeYearsOrMonth(false);
    			return;
    		}
    		if(v.getId()==R.id.YearLabel){
    			changeYearsOrMonth(true);
    			return;
    		}
    	}
    };
   
    //Title Bar listeners
    OnSelectionListener mListMonthListener = new OnSelectionListener() {
    	public void setSelection(int position){
    		mMonth = position+1;
    		UpdateDate();
    		return;
    	}
    };
    OnSelectionListener mListYearListener = new OnSelectionListener() {
    	public void setSelection(int position){
    		mYear = 1990+position;
    		UpdateDate();
    		return;
    	}
    };
   
    public void changeYearsOrMonth(boolean year){
    	DialogListSelector dialog = new DialogListSelector(mContext);
    	if(year){
    		dialog.setContent(R.layout.montselectionrow, R.id.montselection, mYears);
    		dialog.setTitle("Select Yer");
    		dialog.SelectItem(4);
    		dialog.setSelctionListener(mListYearListener);
    	}
    	else{
    		dialog.setContent(R.layout.montselectionrow, R.id.montselection, mMonths2);
    		dialog.setTitle("Select Month");
    		dialog.SelectItem(20);
    		dialog.setSelctionListener(mListMonthListener);
    	}
    	dialog.show();
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
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	
    	switch(keyCode){
    	case KeyEvent.KEYCODE_BACK:    	
    			handleStatus(ActionCode.BACK_BUTTON,null);
    		return true;
    	}
		return super.onKeyDown(keyCode, event);
	}
    public void ShowOtherWeeks(){
    	theCalendar.ShowOtherWeeks();
	}
	
	public void HideOtherWeeks(CalendarWeek theWeek){
		theCalendar.HideOtherWeeks(theWeek);
	}	
	public void LongClickOnADay(ActionCode ac, CalendarWeek arg0){
		handleStatus(ac,arg0);
	}
	public void handleStatus(ActionCode ac, CalendarWeek arg0)
	{
		switch(status){
		case MONTH_VIEW:
			if(ac == ActionCode.DAY_LONG_CLICK){
				theCalendar.HideOtherWeeks(arg0);
				ShowDayMeetingsList(false);
				ShowWeekHour(true);
				status	= State.WEEK_HOUR_VIEW;
			}
			break;
		case WEEK_HOUR_VIEW:
			if(ac == ActionCode.DAY_LONG_CLICK || ac == ActionCode.BACK_BUTTON){
				theCalendar.ShowOtherWeeks();
				ShowDayMeetingsList(true);
				ShowWeekHour(false);
				status	= State.MONTH_VIEW;
			}
		default:
			break;
		}
	}
}