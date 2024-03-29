/**
 * 
 */
package com.welmo.meeting;


import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.welmo.R;
import com.welmo.dbhelper.AgendaDBHelper;

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
		
		public MeetingDayListAdapter(Context ctx,MeetingDay day){
				mContext = ctx;
				theDay = day;
		}
		@Override
		public int getCount() {
			int pos = 0;
			if (theDay != null){
				pos= theDay.GetNbOfMeeting();
			}
			else
				pos =0;
			return pos;
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
			if (theDay != null & position >= 0){
				long key = theDay.GetMeetingsUIDs().get(position).UID;
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
        	LayoutInflater  inf =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	theView = inf.inflate(R.layout.meetingdayrows, null, false); 
        	mTitle = (TextView) theView.findViewById(R.id.MeetingShortDesc); 
        	mDialogue = (TextView) theView.findViewById(R.id.MeetingLongDesc); 
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
		int year =2008;
		int month = 1;
		int day = 1;
		Calendar today=null;
		
		super.onCreate(icicle);  
        
  		dbAgenda = new AgendaDBHelper(this,"Agenda","Meetings","Attends");
  		dbTmp = new AgendaDBHelper(this,"Agenda","MeetingsTmp","AttendsTmp");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.meetingdaylist);
        getListView().setFocusable(true);
        getListView().setFocusableInTouchMode(true);
        theToolBar = (MeetingDayViewToolBar) this.findViewById(R.id.toolbar); 
        theToolBar.setup(this);
        //Setup Staring point
        Bundle extras = getIntent().getExtras();
		if (extras != null){
			//get the meeting UID
			year = extras.getInt("YEAR", 2008);
			month = extras.getInt("MONTH",1);
			day = extras.getInt("DAY",1);
		}
		else{
			//Go today
			today 		= Calendar.getInstance();
			today.setTimeInMillis(System.currentTimeMillis());
		    year 		= today.get(Calendar.YEAR);
		    month 		= today.get(Calendar.MONTH)+1;
		    day 		= today.get(Calendar.DAY_OF_MONTH);
		}
		try{
			setDay(new MeetingDay(year,month,day,dbAgenda));
		}
		catch(IllegalArgumentException Error){
			String msg=Error.getMessage();
		}    
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
			theToolBar.setDayLabel(theDay.getMyDate());
		}
		else
			theToolBar.setDayLabel("---");
		
		if(mdla == null)
			mdla = new MeetingDayListAdapter(this,theDay);
		//mdla.notifyDataSetInvalidated();
		//mdla.notifyDataSetChanged();
		setListAdapter(mdla);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, NEW_ID, 0, R.string.menu_insert_meeting);
		menu.add(1, UPDATE_ID, 0, R.string.menu_update_meeting);
		menu.add(1, DELETE_ID, 0, R.string.menu_delete_meeting);
		return result;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		MeetingUID theUID = null;
		super.onMenuItemSelected(featureId, item);
		int itemPosition = getSelectedItemPosition();
		if(itemPosition<0){
			ShowMessge("No Meeting Selected");
			return false;
		}
		switch(item.getItemId()) {
			case NEW_ID:
				theUID =new MeetingUID(getListAdapter().getItemId(itemPosition));
				MeetingEdit(MEETING_CREATE,theUID);
				break;
			case UPDATE_ID:
				theUID = new MeetingUID(getListAdapter().getItemId(itemPosition));
				MeetingEdit(MEETING_EDIT,theUID);
				break;
			case DELETE_ID:
				theUID = new MeetingUID(getListAdapter().getItemId(itemPosition));
				theDay.DelMeeting(theUID);
				setListAdapter(mdla);
				break;
	    }
	    return true;
	}
	
	
	private void MeetingEdit(int type, MeetingUID MtgUID) {
		//TODO: finalize graphics
    	Intent i = new Intent(this, MeetingView.class); 
    	Meeting currMeeting = theDay.getMeeting(MtgUID);
    	String UID =((Long)currMeeting.getMeetingID().UID).toString();
    	i.putExtra(MEETING_UID_OLD, currMeeting.getMeetingID().UID);
    	switch(type){
    		case MEETING_CREATE:
    			startActivityForResult(i, MEETING_CREATE);
    			break;
		    case MEETING_EDIT:
		    	startActivityForResult(i, MEETING_EDIT);
    	}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		
		Bundle extras = intent.getExtras();

		Meeting newMeeting = new Meeting();
		MeetingUID MUID_OLD = new MeetingUID();
		MeetingUID MUID_NEW = new MeetingUID();
		
		if (resultCode != android.app.ActivityGroup.RESULT_CANCELED){
			MUID_NEW.UID = extras.getLong(MEETING_UID_NEW);	
			MUID_OLD.UID = extras.getLong(MEETING_UID_OLD);
			newMeeting.setMeetingID(MUID_NEW);
			//restore new meeting from tmp database
			newMeeting.RestoreFromDatabase(dbTmp);
			switch(requestCode) {
				case MEETING_CREATE:
				    // TODO handle the case when the user create or edit a meeting for another day
					theDay.AddMeeting(newMeeting);
					setListAdapter(mdla);
					break;
				case MEETING_EDIT:
	        	    // TODO handle the case when the user create or edit a meeting for another day
	    			theDay.ChangeMeeting(MUID_OLD,newMeeting);
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
						newDayUID.getDayNB()).show();				
			break;
		}
		MeetingDay newDay = new MeetingDay(newDayUID.getYear(),
				newDayUID.getMonth(),
				newDayUID.getDayNB(),dbAgenda);
		setDay(newDay);
	}
	
	
		
	private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
            	MeetingDay newDay = new MeetingDay(year,monthOfYear+1,dayOfMonth,dbAgenda);
        		setDay(newDay);
            }
        };
   private static String pad(int c) {
       if (c >= 10)
           return String.valueOf(c);
       else
           return "0" + String.valueOf(c);
   }

   void ShowMessge(String Msg){
       Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}
}