package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.LayoutInflater; 
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.welmo.R;
import com.welmo.dbhelper.AgendaDBHelper;
import com.welmo.meeting.IMeetingDisplay;
import com.welmo.meeting.IMeetingDisplayFactory;
import com.welmo.meeting.Meeting;
import com.welmo.meeting.MeetingDay;
import com.welmo.meeting.MeetingDayAdapter;

public class CalendarDayMeetingListShort extends ListView implements IMeetingDisplayFactory{

	
	private MeetingDay 			theDay		= null;
	private MeetingDayAdapter 	mDAdp 		= null;
	private Context 			mContext	= null;
	private AgendaDBHelper		dbAgenda 	= null;
	
	//--------------------------------------------------------------------
	// handel view status
	private enum State {NO_DETAIL,DETAILS,NO_EDIT};
	private State				status		= State.NO_DETAIL;
	//--------------------------------------------------------------------
	
	
	public class MeetingDisplayShort extends LinearLayout implements IMeetingDisplay {
		
		private TextView 	mMeetingDescription;
		private TextView 	mMeetingLongDescription;
		private boolean 	mShowLongDesc = false;
		View theView;
		private Context mContext;
		
		public MeetingDisplayShort(Context context)
		{
			super(context);
			mContext = context;
			this.setOrientation(VERTICAL);
			
			LayoutInflater  inf =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	theView = inf.inflate(R.layout.meetingpeoplelistrow, null, false); 
        
			mMeetingDescription = (TextView) theView.findViewById(R.id.MeetingShortDesc); 
			mMeetingLongDescription = (TextView) theView.findViewById(R.id.MeetingLongDesc); 
			
			addView(theView, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			setFocusableInTouchMode(true);
		}
		
		public void setMeetingInfo(Meeting currMeeting){
			String ShortDesc="";
			
			ShortDesc = currMeeting.getStart_h()+":"+currMeeting.getStart_m()+" "+
						currMeeting.getEnd_h()+":"+currMeeting.getEnd_m();
			ShortDesc = ShortDesc + " " + currMeeting.getMeetingID().getMonth() + "-"+
			currMeeting.getMeetingID().getDayNB() +" "+
			currMeeting.getObject();
			mMeetingDescription.setText(ShortDesc);
			mMeetingLongDescription.setText(currMeeting.getDescription());
		}
		public void ShowHideLongDsc(){
			if(mShowLongDesc){
				mShowLongDesc = false;
				mMeetingLongDescription.setVisibility(GONE);
			}
			else{
				mShowLongDesc = true;
				mMeetingLongDescription.setVisibility(VISIBLE);
			}
		}
	}
	//---------------------------------------------
	//Implement IMeetingDisplayFactory
	public IMeetingDisplay getNewView(){
		MeetingDisplayShort newDispaly = new MeetingDisplayShort(mContext);
		return newDispaly;
	}	
	//---------------------------------------------
	public CalendarDayMeetingListShort(Context context, AttributeSet attrs) {
		super(context,attrs);
		setOnItemClickListener(new OnItemClickListener (){ 
			@Override
			public void onItemClick(AdapterView av, View v, int n, long l) {
				boolean getfocus = av.requestFocus();
				((MeetingDisplayShort)v).ShowHideLongDsc();
			}
		});
		/*setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				ShowMessge("Calendar Day On Focus Catched: ");
				//ShowMessge("CalendarDayMeetingList\n Focuse Changed Catched"+ status);
			}		
		});*/
		setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView av, View v, int n, long l){
				boolean getfocus = av.requestFocus();
				//((MeetingDisplayShort)v).mMeetingLongDescription.setVisibility(VISIBLE);
				ShowMessge("CalendarDayMeetingList \n Long Clik Catched"+ status);
				return true;
			}
		});
		setFocusableInTouchMode(true);
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
	//Utility fucntions
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
}
