package com.welmo.agenda;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.os.Bundle;
import com.tft.myoffice.R;

import com.tft.myoffice.dbhelper.DBHelper;
import com.tft.myoffice.meeting.Meeting;
import com.tft.myoffice.meeting.MeetingUID;

public class Agenda extends Activity {
    /** Called when the activity is first created. */
    //private SQLiteDatabase dbAgenda = null;
    //private CursorFactory dBCursorFactory = null;
    DBHelper db = null;
   
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.setupcontent);
        //testMeetingClass();
        //testDayClass();
        //db = new DBHelper(this);
        //testDataBase();
        testDayClass();
     }    
    //-------------------------------------
    // Test functions
    //------------------------------------
    void testMeetingClass()
    {
    	try{
    		MeetingUID theMUID = new MeetingUID();
    		Meeting theMeeting = new Meeting();
    		
    		theMUID.setUID((short)2007,(short)12,(short)25);
    		
    		int data=theMUID.getYear();
       		data=theMUID.getMonth();
       		data=theMUID.getWeek();
       		data=theMUID.getDayOW();
       		data=theMUID.getDayNB();
       		data=theMUID.getStartHour();
       		data=theMUID.getStartMin();
       		data=theMUID.getCount();
       		
    		theMeeting.setMeetingIDDay(theMUID);
    		theMeeting.setDescription("meering description");
    		theMeeting.setTimeFrame((short)2, (short)45,(short)3, (short)10);
    		data = theMeeting.getEnd_h();
    		data = theMeeting.getEnd_m();
    		data = theMeeting.getStart_h();
    		data = theMeeting.getStart_m();
       		String content = theMeeting.getDescription();
       		content = theMeeting.getObject();
       		content = theMeeting.toString();
       		MeetingUID mUID = theMeeting.getMeetingID();
       		data=mUID.getYear();
       		data=mUID.getMonth();
       		data=mUID.getWeek();
       		data=mUID.getDayOW();
       		data=mUID.getDayNB();
       		data=mUID.getStartHour();
       		data=mUID.getStartMin();
       		data=mUID.getCount();
       		theMeeting.setTimeFrame((short)2, (short)45,(short)1, (short)10);
    	}
    	catch(IllegalArgumentException Error)
    	{
    		String msg=Error.getMessage();
    	}
    }

    //-------------------------------------
    // Test functions
    //------------------------------------
    void testDayClass()
    {
    	/*try{
    		MeetingDay theDay = new MeetingDay(2007,12,25);
    		MeetingUID theMUID = new MeetingUID();
    		Meeting theMeeting = new Meeting();
    		theMUID.setUID((short)2007,(short)12,(short)25);
    		theMeeting.setDescription("meeting description");
    		theMeeting.setDescription("meeting object");	
    		theMeeting.setTimeFrame((short)2, (short)45,(short)18, (short)10);
    		theDay.AddMeeting(theMeeting);
    		theMeeting = new Meeting();
    		theMeeting.setTimeFrame((short)16, (short)45,(short)20, (short)10);
    		theDay.AddMeeting(theMeeting);
    	}
    	catch(IllegalArgumentException Error)
    	{
    		String msg=Error.getMessage();
    	}*/
    }
    
    void testDataBase()
    {
    	try{
    		List<Long> selection = null;
    		Map MeetingMap = new HashMap();
    		MeetingUID theMUID = new MeetingUID();
    		Meeting theMeeting = new Meeting();
    		theMUID.setUID((short)2007,(short)12,(short)25);
    		theMeeting.setMeetingIDDay(theMUID);
    		//meeting 1
    		theMeeting.setDescription("meering description");
    		theMeeting.setObject("The Object");
    		theMeeting.setTimeFrame((short)1, (short)45,(short)11, (short)10);
    		/*theMeeting.UpdateToDatabase(db,	Meeting.MEETINGTMP_TABLE_NAME,
    				Meeting.ATTENDSTMP_TABLE_NAME);
    				
    		//meeting 2
    		theMeeting.setDescription("meeting 2 description");
    		theMeeting.setObject("The Object 2");
    		theMeeting.setTimeFrame((short)2, (short)45,(short)12, (short)10);
    		theMeeting.UpdateToDatabase(db,Meeting.MEETINGTMP_TABLE_NAME,
    				Meeting.ATTENDSTMP_TABLE_NAME);
    		//meeting 3
    		theMeeting.setDescription("meering 3 description");
    		theMeeting.setObject("The Object 3");
    		theMeeting.setTimeFrame((short)3, (short)45,(short)13, (short)10);
    		theMeeting.UpdateToDatabase(db,Meeting.MEETINGTMP_TABLE_NAME,
    				Meeting.ATTENDSTMP_TABLE_NAME);
    		selection = db.fetchRowIdListByID(Meeting.MEETING_TABLE_NAME, 
    				theMUID.UID, 
    				theMUID.UID | 0xFFFFFFFFL);
    		if(selection.size() != 0){
    			for( int i = 1; i<=selection.size(); i++)
    			{
    				theMeeting = new Meeting();
    				theMUID.UID = ((Long)selection.get(i-1)).longValue();
    				theMeeting.setMeetingID(theMUID);
    				theMeeting.RestoreFromDatabase(db,Meeting.MEETINGTMP_TABLE_NAME,
    	    				Meeting.ATTENDSTMP_TABLE_NAME);
    				MeetingMap.put(theMeeting.getMeetingID().UID, theMeeting);
    			}
    		}*/
    	}
    	catch(IllegalArgumentException Error)
    	{
    		String msg=Error.getMessage();
    	}
    }
    
}
