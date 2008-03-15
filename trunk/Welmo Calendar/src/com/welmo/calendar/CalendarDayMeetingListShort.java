package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewInflate;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.welmo.meeting.*;
import com.welmo.dbhelper.*;

public class CalendarDayMeetingListShort extends ListView 
	implements IMeetingDisplayFactory{

	
	private MeetingDay 			theDay		= null;
	private MeetingDayAdapter 	mDAdp 		= null;
	private Context 			mContext	= null;
	private AgendaDBHelper		dbAgenda 	= null;
	
	public class MeetingDisplayShort extends LinearLayout implements IMeetingDisplay{
		
		private TextView mObject;
		private TextView mDescription;
		View theView;
		private Context mContext;
		
		public MeetingDisplayShort(Context context)
		{
			super(context);
			mContext = context;
			this.setOrientation(VERTICAL);
			ViewInflate inf =(ViewInflate)mContext.getSystemService(android.content.Context.INFLATE_SERVICE);
			theView = inf.inflate(R.layout.meetingdayrows, null, false, null); 
			mObject = (TextView) theView.findViewById(R.id.Text1); 
			mDescription = (TextView) theView.findViewById(R.id.Text2); 
			addView(theView, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		public void setMeetingInfo(Meeting currMeeting){
			mObject.setText(currMeeting.getObject());
			mDescription.setText(currMeeting.getDescription());
		}
	}
	//---------------------------------------------
	//Implement IMeetingDisplayConstructor
	public IMeetingDisplay getNewView(){
		MeetingDisplayShort newDispaly = new MeetingDisplayShort(mContext);
		return newDispaly;
	}	
	//---------------------------------------------
	
	
	
	public CalendarDayMeetingListShort(Context context, AttributeSet attrs, Map inflateParams) {
		super(context,attrs,inflateParams);
		mContext 	= context;
		dbAgenda 	= new AgendaDBHelper(context,"Agenda","Meetings","Attends");
  		mDAdp 		= new MeetingDayAdapter(this,mContext,null);
  		setAdapter(mDAdp);
	}
	
	
	public void setDay(int year, int month, int day)
	{
		theDay = new MeetingDay(year, month, day,dbAgenda);
		
		if (theDay != null){
			theDay.RestoreFromDatabase();
		}
		if(mDAdp != null){
			mDAdp = new MeetingDayAdapter(this,mContext,theDay);
			setAdapter(mDAdp);
		}
		mDAdp = new MeetingDayAdapter(this,mContext, theDay);
	}

}
