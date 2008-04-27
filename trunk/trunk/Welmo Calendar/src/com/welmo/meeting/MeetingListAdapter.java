package com.welmo.meeting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MeetingListAdapter extends BaseAdapter{

	//private Context mContext;
	private IMeetingList 					theMeetingList 				= null;
	private IMeetingDisplayFactory			theDisplayFactory			= null; 
	private short mMeetingTypeFilter = 0xF;

	public MeetingListAdapter(IMeetingDisplayFactory DisplayFacotry, Context ctx,IMeetingList mtgList){
		theMeetingList = mtgList;
		theDisplayFactory = DisplayFacotry;
	}
	@Override
	public int getCount() {
		if (theMeetingList != null){
			int pos= theMeetingList.GetNbOfMeeting(mMeetingTypeFilter);
			return pos;
		}
		else
			return 0;
	}
	@Override
	public Object getItem(int index) {
		if (theMeetingList != null && index >=0){
			Meeting theMeeting  = theMeetingList.getMeeting(theMeetingList.GetMeetingsUIDs(mMeetingTypeFilter).get(index));
			return theMeeting;	
		}
		else
			return null;
	}
	@Override
	public long getItemId(int position){
		if (theMeetingList != null){
			long key = theMeetingList.GetMeetingsUIDs(mMeetingTypeFilter).get(position).UID;
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
	public short getMeetingTypeFilter() {
		return mMeetingTypeFilter;
	}
	public void setMeetingTypeFilter(short meetingTypeFilter) {
		mMeetingTypeFilter = meetingTypeFilter;
	}
}
	