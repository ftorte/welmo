/**
 * 
 */
package com.welmo.meeting;


import java.util.Calendar;

import com.tft.myoffice.R;
import com.tft.myoffice.dbhelper.AgendaDBHelper;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewInflate;
import android.view.Window;
import android.view.Menu.Item;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MeetingDayView extends ListActivity {
	
	public static final int MEETING_CREATE=0;
	public static final int MEETING_EDIT=1;
	
	public static final int NEW_ID = Menu.FIRST;
	public static final int UPDATE_ID = Menu.FIRST + 1;
	public static final int DELETE_ID = Menu.FIRST + 2;
	
	// KEY INFORAMTION FOR MEETING HANDLING
    //public static final String MEETING = "Meeting";
    public static final String MEETING_UID_OLD = "MUidOld";
    public static final String MEETING_UID_NEW = "MUidNew";
    
	private MeetingDay theDay=null;
	private MeetingDayListAdapter mdla=null;
	private MeetingDayViewToolBar theToolBar;
	private AgendaDBHelper dbAgenda=null;
	private AgendaDBHelper dbTmp=null;	
	
	public class MeetingDayListAdapter extends BaseAdapter {
		
		private Context mContext;
		private MeetingDay theDay;
		
		
		public MeetingDayListAdapter(Context ctx,MeetingDay day){
				mContext = ctx;
				theDay = day;
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
				Object theMeeting = theDay.getMeeting(((Long)(theDay.GetMeetingsUIDs()[index])).longValue());
				return theMeeting;	
			}
			else
				return null;
		}
		@Override
		public long getItemId(int position){
			if (theDay != null){
				long key = ((Long)theDay.GetMeetingsUIDs()[position]).longValue();
				return key;
			}
			else
				return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            SpeechView sv;
            if (convertView == null) {
                sv = new SpeechView(mContext);
                Meeting currMeeting = (Meeting)getItem(position);
                sv.setTitle(pad(currMeeting.getStart_h()) + ":" +
                		pad(currMeeting.getStart_m()) + "-"+
                		pad(currMeeting.getEnd_h()) + ":" +
                		pad(currMeeting.getEnd_m()) + " "+
                		currMeeting.getObject());
                sv.setDialogue(currMeeting.getDescription());
            } else {
                sv = (SpeechView) convertView;
                Meeting currMeeting = (Meeting)getItem(position);
                sv.setTitle(pad(currMeeting.getStart_h()) + ":" +
                		pad(currMeeting.getStart_m()) + "-"+
                		pad(currMeeting.getEnd_h()) + ":" +
                		pad(currMeeting.getEnd_m()) + " "+
                		currMeeting.getObject());
                sv.setDialogue(currMeeting.getDescription());
            }
            return sv;
        }
	}
	private class SpeechView extends LinearLayout {
        private TextView mTitle;
        private TextView mDialogue;
        View theView;
    	
        public SpeechView(Context context)
        {
        	super(context);
        	this.setOrientation(VERTICAL);
        	ViewInflate inf =(ViewInflate)getSystemService(INFLATE_SERVICE);
        	theView = inf.inflate(R.layout.meetingdayrows, null, false, null); 
        	mTitle = (TextView) theView.findViewById(R.id.Text1); 
        	mDialogue = (TextView) theView.findViewById(R.id.Text2); 
        	addView(theView, new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
           
        public void setTitle(String title) {
            mTitle.setText(title);
        }
        public void setDialogue(String words) {
            mDialogue.setText(words);
        }

    }
		
	@Override
    public void onCreate(Bundle icicle){ 
        super.onCreate(icicle);  
        
  		dbAgenda = new AgendaDBHelper(this,"Agenda","Meetings","Attends");
  		dbTmp = new AgendaDBHelper(this,"Agenda","MeetingsTmp","AttendsTmp");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.meetingdaylist);
        theToolBar = (MeetingDayViewToolBar) this.findViewById(R.id.toolbar); 
        theToolBar.setup(this);
        if(mdla == null)
			mdla = new MeetingDayListAdapter(this,theDay);
        setListAdapter(mdla);
        testDayClassShort();
    }
	public void setDay(MeetingDay day)
	{
		if (theDay != null)
			theDay.UpdateToDatabase();
		theDay = day;
		if (theDay != null){
			dbAgenda = new AgendaDBHelper(this,"Agenda","Meetings","Attends");
	  		dbTmp = new AgendaDBHelper(this,"Agenda","MeetingsTmp","AttendsTmp");
			theDay.RestoreFromDatabase();
		}
		if(mdla != null){
			mdla = new MeetingDayListAdapter(this,theDay);
			theToolBar.setDayLabel(theDay.getMyDate());
			setListAdapter(mdla);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_ID, R.string.menu_insert_meeting);
		menu.add(1, UPDATE_ID, R.string.menu_update_meeting);
		menu.add(1, DELETE_ID, R.string.menu_delete_meeting);
		return result;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, Item item){
		long lMeetingID;
		super.onMenuItemSelected(featureId, item);
		switch(item.getId()) {
			case NEW_ID:
				lMeetingID = getListAdapter().getItemId(getSelectedItemPosition());
				MeetingEdit(MEETING_CREATE,lMeetingID);
				break;
			case UPDATE_ID:
				lMeetingID = getListAdapter().getItemId(getSelectedItemPosition());
				MeetingEdit(MEETING_EDIT,lMeetingID);
				break;
			case DELETE_ID:
				lMeetingID = getListAdapter().getItemId(getSelectedItemPosition());
				theDay.DelMeeting(lMeetingID);
				setListAdapter(mdla);
				break;
	    }
	    return true;
	}
	
	
	@Override
	//[SDK changes]
	public boolean onPrepareOptionsMenu(Menu menu){
		long id = getListAdapter().getItemId(getSelectedItemPosition());
		if((id & MeetingUID.MASK_TYPE) == MeetingUID.TYPE_FREE_TYME){
			menu.setGroupShown(1,false);
		}
		else{
			menu.setGroupShown(1,true);	
		}
		return true;
	}
	
	private void MeetingEdit(int type, long lMeetingID) {
		//TODO: finalize graphics
    	Intent i = new Intent(this, MeetingView.class);
    	Meeting currMeeting = theDay.getMeeting(lMeetingID);
    	String UID =((Long)currMeeting.getMeetingID().UID).toString();
    	i.putExtra(MEETING_UID_OLD, currMeeting.getMeetingID().UID);
    	switch(type){
    		case MEETING_CREATE:
    		    startSubActivity(i, MEETING_CREATE);
    			break;
		    case MEETING_EDIT:
		    	startSubActivity(i, MEETING_EDIT);
    	}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras){
		super.onActivityResult(requestCode, resultCode, data, extras);
		
		Meeting newMeeting = new Meeting();
		MeetingUID MUID_OLD = new MeetingUID();
		MeetingUID MUID_NEW = new MeetingUID();
		
		if (resultCode != android.app.ActivityGroup.RESULT_CANCELED){
			MUID_NEW.UID = extras.getLong(MEETING_UID_NEW);	
			MUID_OLD.UID = extras.getLong(MEETING_UID_OLD);
			newMeeting.setMeetingID(MUID_NEW);
			//restore new meeting from tmp database
			newMeeting.RestoreFromDatabase(dbTmp);
			//newMeeting.setDescription(extras.getString(MEETING_DESCRIPTION));
			//newMeeting.setObject(extras.getString(MEETING_OBJECT));
			switch(requestCode) {
				case MEETING_CREATE:
				    // TODO handle the case when the user create or edit a meeting for another day
					theDay.AddMeeting(newMeeting);
					setListAdapter(mdla);
					break;
				case MEETING_EDIT:
	        	    // TODO handle the case when the user create or edit a meeting for another day
	    			theDay.ChangeMeeting(MUID_OLD.UID,newMeeting);
	    			setListAdapter(mdla);
					break;
	        }
	    }
	}
	
	long getCurrDayUID(){
		return theDay.getDayUID();
	}
	
	public void ChangeDate(int TypeOfChange){
		// Set newDay = to current day
		MeetingUID newDayUID = new MeetingUID(getCurrDayUID());
		switch(TypeOfChange){
			case MeetingDayViewToolBar.GO_MIN7:
				newDayUID.addNbOfDay(-7);
				break;
			case MeetingDayViewToolBar.GO_MIN1:
				newDayUID.addNbOfDay(-1);
				break;
			case MeetingDayViewToolBar.GO_PLUS1:
				newDayUID.addNbOfDay(1);
				break;
			case MeetingDayViewToolBar.GO_PLUS7:
				newDayUID.addNbOfDay(7);
				break;
			case 	MeetingDayViewToolBar.DAYLABEL:
				new DatePickerDialog(this,
						mDateSetListener,
						newDayUID.getYear(), 
						newDayUID.getMonth()-1, 
						newDayUID.getDayNB(), 
						Calendar.SUNDAY).show();
			break;
		}
		MeetingDay newDay = new MeetingDay(newDayUID.getYear(),
				newDayUID.getMonth(),
				newDayUID.getDayNB(),dbAgenda);
		setDay(newDay);
	}
	
	
		
	private DatePicker.OnDateSetListener mDateSetListener =
        new DatePicker.OnDateSetListener() {

            public void dateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
            	MeetingDay newDay = new MeetingDay(year,monthOfYear+1,dayOfMonth,dbAgenda);
        		setDay(newDay);
            }
        };

	
   void testDayClassShort(){
	   try{
		   MeetingDay theDay2 = new MeetingDay(2007,12,25,dbAgenda);
		   this.setDay(theDay2);
	   }
	   catch(IllegalArgumentException Error){
		   String msg=Error.getMessage();
	   }
   }
   private static String pad(int c) {
       if (c >= 10)
           return String.valueOf(c);
       else
           return "0" + String.valueOf(c);
   }


}