package com.welmo.meeting;

import java.util.ArrayList;

public interface IMeetingList {
	public Meeting	getMeeting(MeetingUID mUID);
	public void 	AddMeeting(Meeting theMeeting);
	public void 	DelMeeting(MeetingUID mUID);
	public void 	ChangeMeeting(MeetingUID mUID,Meeting theMeeting);
	
	public int 		GetNbOfMeeting();
	public int 		GetNbOfMeeting(short ValidTypes);
	
	public ArrayList<MeetingUID> GetMeetingsUIDs();
	public ArrayList<MeetingUID> GetMeetingsUIDs(short ValidTypes);
	
	public void 	RestoreFromDatabase();
	public void 	UpdateToDatabase();
}
