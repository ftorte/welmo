package com.welmo.meeting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MeetingDayAdapter extends BaseAdapter{

	//private Context mContext;
	private MeetingDay 					theDay 				= null;
	private IMeetingDisplayFactory		theDisplayFactory	= null; 

	public MeetingDayAdapter(IMeetingDisplayFactory DisplayFacotry, Context ctx,MeetingDay day){
		theDay = day;
		theDisplayFactory = DisplayFacotry;
	}
	@Override
	public int getCount() {
		if (theDay != null){
			int pos= theDay.GetNbOfMeeting();
			return pos;
		}
		else
			return 0;
	}
	@Override
	public Object getItem(int index) {
		if (theDay != null){
			Meeting theMeeting  = theDay.getMeeting(theDay.GetMeetingsUIDs().get(index));
			return theMeeting;	
		}
		else
			return null;
	}
	@Override
	public long getItemId(int position){
		if (theDay != null){
			long key = theDay.GetMeetingsUIDs().get(position).UID;
			return key;
		}
		else
			return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IMeetingDisplay sv;
		if (convertView == null) {
			sv = theDisplayFactory.getNewView();
			Meeting currMeeting = (Meeting)getItem(position);
			sv.setMeetingInfo(currMeeting);
		} else {
			sv = (IMeetingDisplay)convertView;
			Meeting currMeeting = (Meeting)getItem(position);
			sv.setMeetingInfo(currMeeting);
		}
		return (View)sv;
	}
	public void setView(){
		
	}
}
	