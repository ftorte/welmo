package com.welmo.meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewInflate;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.welmo.R;
import com.welmo.contacts.Attends;
import com.welmo.contacts.ContactsList;
import com.welmo.dbhelper.AgendaDBHelper;

public class MeetingView extends ListActivity {
	
	// constants
	public static int SETUP_NULL 			= 0;
	public static int SETUP_START 			= 1;
	public static int SETUP_END 			= 2;
	
	public static int SETUP_TIME 			= 1;
	public static int SETUP_DESC 			= 2;
	public static int SETUP_PEOPLE 			= 3;
	
	// intent request codes
	public static int ADD_PEOPLE			= 1;
	
	// Informations
	protected EditText 		titleText;
	protected EditText 		bodyText;
	protected MeetingUID 	theMUID_OLD 	= new MeetingUID();
	protected Meeting 		newMeeting 		= new Meeting();
	protected boolean 		ShowTimeSetUp 	= false;	
	protected ImageButton 	setUpWindow;
	protected ImageButton 	btnObjDetail;
	protected ImageButton 	btnPeopleList;
	protected ImageButton 	btnAddPeople;
	protected ImageButton 	btnRemovePeople;

	protected ImageButton 	setUpDate;
	protected ImageButton 	setUpStart;
	protected ImageButton 	setUpEnd;
	protected ImageButton 	setUpDuration;

	protected TextView 		DataTitel;
	protected TextView 		StartTimeTitle;
	protected TextView 		Date;
	protected TextView 		Start;
	protected TextView 		End;
	protected TextView 		Duration;
	protected CheckBox 		Fixed;
	
	protected int currentOperation=SETUP_NULL;
	protected PeopleListAdapter pla=null;
	protected AgendaDBHelper dbTmp = null;
	protected AgendaDBHelper dbAgenda = null;
	
	public class PeopleListAdapter extends BaseAdapter {
		
		private Context mContext;
		
		public PeopleListAdapter(Context ctx){
				mContext = ctx;
		}
		@Override
		public int getCount() {
				int n = newMeeting.attendlist.size();
				return n;
		}
		@Override
		public Object getItem(int index) {
				int n = index;
				return newMeeting.attendlist.elementAt(index);
		}
		@Override
		public long getItemId(int position){
				int n = position;
				return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PeopleList sv;
            if (convertView == null) {
                sv = new PeopleList(mContext);
                Attends currItem = (Attends)getItem(position);
                sv.setName(currItem.Name);
                sv.setStatus(currItem.Response);
            } 
            else {
                sv = (PeopleList) convertView;
                Attends currItem = (Attends)getItem(position);
                sv.setName(currItem.Name);
                sv.setStatus(currItem.Response);
            }
            return sv;
        }
	}
	private class PeopleList extends LinearLayout {
        private TextView 	txtName;
        private ImageView 	imgStatus;
        private ImageButton imbCall;
        private ImageButton imbDel;
        
        View theView;
    	
        public PeopleList(Context context){
        	super(context);
        	ViewInflate inf =(ViewInflate)getSystemService(INFLATE_SERVICE);
        	theView = inf.inflate(R.layout.meetingpeoplelistrow, null, false, null); 
        	imgStatus = (ImageView) theView.findViewById(R.id.Status); 
        	txtName = (TextView) theView.findViewById(R.id.Name); 
        	imbCall = (ImageButton) theView.findViewById(R.id.Call); 
        	imbDel = (ImageButton) theView.findViewById(R.id.Del); 
        	
        	addView(theView, new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
        public void setName(String title) {
        	txtName.setText(title);
        }
        public void setStatus(int mStatus) {
            switch(mStatus)
            {
            	case Meeting.RESPONSE_OK:
            		imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.go16x16));
            		break;
            	case Meeting.RESPONSE_REFUSE:
            		imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.stop16x16));
            		break;
            	case Meeting.NO_RESPONSE:
            		imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.questionmark16x16));
            		break;
            	default:
            		imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.questionmark16x16));
            		break;
            }
        }
        public void setPhoneStatus(boolean bHasPhone) {
            if(bHasPhone)
            	imbCall.setEnabled(true);
            else
               	imbCall.setEnabled(false);    	
        }
    }

	private void UpdateLabels(){
		MeetingUID theUID = newMeeting.getMeetingID();
		DataTitel.setText(theUID.getMyDate());
		StartTimeTitle.setText(theUID.toStringStartTime());
		Date.setText(theUID.getMyDate());
		Start.setText(theUID.toStringStartTime());
		End.setText(theUID.toStringEndTime());
		Duration.setText(theUID.toStringDuration());
	}

	//Constructor
	public MeetingView() {
		currentOperation=SETUP_NULL;
	}
	
	@Override
	protected void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);     
		setContentView(R.layout.meetingview);
		
		dbTmp = new AgendaDBHelper(this,"genda","MeetingsTmp","AttendsTmp");
		dbAgenda = new AgendaDBHelper(this,"Agenda","Meetings","Attends");
		
		//Text Labels
		DataTitel 		= (TextView)findViewById(R.id.MeetingViewDateTitle);
		StartTimeTitle 	= (TextView)findViewById(R.id.MeetingViewStartTitle);
		Date 			= (TextView)findViewById(R.id.MeetingViewDate);
		Start 			= (TextView)findViewById(R.id.MeetingViewStart); 
		End 			= (TextView)findViewById(R.id.MeetingViewEnd);    	
		Duration 		= (TextView)findViewById(R.id.MeetingViewDuration);    	
		Fixed 			= (CheckBox)findViewById(R.id.MeetingViewFixed);
		titleText 		= (EditText) findViewById(R.id.MeetingObject);
		bodyText 		= (EditText) findViewById(R.id.MeetingDescription);
		
	    //Close Time Setup Section
	    View viewTimeSetUp= (View) findViewById(R.id.ViewTimeSetUp); 
		viewTimeSetUp.setVisibility(View.GONE);

		//Buttons 
		ImageButton confirmButton 	= (ImageButton) findViewById(R.id.confirm);
		ImageButton cancelButton 	= (ImageButton) findViewById(R.id.cancel);
		
		setUpWindow  			= (ImageButton) findViewById(R.id.TimeSetUp);
		btnObjDetail  			= (ImageButton) findViewById(R.id.btnObjectiveDetail);
		btnPeopleList  			= (ImageButton) findViewById(R.id.btnPeopleList);
		btnAddPeople  			= (ImageButton) findViewById(R.id.btnAddPeople);
		btnRemovePeople  		= (ImageButton) findViewById(R.id.btnRemovePeople);
		
		setUpDate 				= (ImageButton) findViewById(R.id.DateSetUp);
		setUpStart 				= (ImageButton) findViewById(R.id.StartSetUp);
		setUpEnd 				= (ImageButton) findViewById(R.id.EndSetUp);
		setUpDuration 			= (ImageButton) findViewById(R.id.DurationSetUp);
		
		ShowHideWindows(SETUP_DESC);
		
		//rowId = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			//get the meeting UID
			theMUID_OLD.UID = extras.getLong(MeetingDayView.MEETING_UID_OLD);
			//copy meeting in the new meeting 
			newMeeting.setMeetingID(theMUID_OLD);
			//restore meeting info from the database
			newMeeting.RestoreFromDatabase(dbAgenda);
			//set new meeting type by default as working time
			newMeeting.setType(MeetingUID.TYPE_WORKING_PROJECT);
			//init GUI with meeting info
			bodyText.setText(newMeeting.getDescription());
			titleText.setText(newMeeting.getObject());
			// Set by default the MUID equal to the old meeting and the type as working time
			UpdateLabels();
		}
		else{
			//case of new meeting 
			bodyText.setText("...");
			titleText.setText("...");
			theMUID_OLD.UID= 0;
			UpdateLabels();
		}
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//save to the TMP database
				//save in the new database the meeting as it have been modified
				newMeeting.setObject(titleText.getText().toString());
				newMeeting.setDescription(bodyText.getText().toString());
				newMeeting.UpdateToDatabase(dbTmp);
				Bundle bundle = new Bundle();
				//pass meetings UID to the activity (new meeting and old meeting
				bundle.putLong(MeetingDayView.MEETING_UID_OLD, theMUID_OLD.UID);
				bundle.putLong(MeetingDayView.MEETING_UID_NEW, newMeeting.getMeetingID().UID);
				setResult(RESULT_OK, null, bundle);
				finish();
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED, null, null);
				finish();
			}
		});	
		
		setUpWindow.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				ShowHideWindows(SETUP_TIME);
			}
		});
		btnObjDetail.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				ShowHideWindows(SETUP_DESC);
			}
		});
		btnPeopleList.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				ShowHideWindows(SETUP_PEOPLE);
			}
		});
		btnAddPeople.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				AddPeople();
			}
		});
		btnRemovePeople.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {

			}
		});
		
		setUpDate.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				ChangeMeetingDate();
			}
		});
		setUpStart.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				ChangeTime(SETUP_START);
			}
		});
		setUpEnd.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				ChangeTime(SETUP_END);
			}
		});
		setUpDuration.setOnClickListener (new View.OnClickListener() {
			public void onClick(View arg0) {
				//[FT] ChangeTime(SETUP_DURATION);
			}
		});
		
        if(pla == null)
        	pla = new PeopleListAdapter(this);
        setListAdapter(pla);
	
	}
	
	public void ShowHideWindows(int type){
		
		//Hide All Windows
		View windows = (View) findViewById(R.id.ViewTimeSetUp); 
		windows.setVisibility(View.GONE);
		windows = (View) findViewById(R.id.ViewObjectiveDetail);
		windows.setVisibility(View.GONE);
		windows = (View) findViewById(R.id.ViewPeopleList); 
		windows.setVisibility(View.GONE);
		//set Button BackGround to Dark Blue
		setUpWindow.setBackground(R.color.grey_dark);
		btnObjDetail.setBackground(R.color.grey_dark);
		btnPeopleList.setBackground(R.color.grey_dark);
		
		switch(type){
			case 1:
				windows = (View) findViewById(R.id.ViewTimeSetUp); 
				windows.setVisibility(View.VISIBLE);
				setUpWindow.setBackground(R.color.grey_clear);
				break;
			case 2:
				windows = (View) findViewById(R.id.ViewObjectiveDetail); 
				windows.setVisibility(View.VISIBLE);
				btnObjDetail.setBackground(R.color.grey_clear);
				break;
			case 3:
				windows = (View) findViewById(R.id.ViewPeopleList); 
				windows.setVisibility(View.VISIBLE);
				btnPeopleList.setBackground(R.color.grey_clear);
				break;
		}	
	}
	
	public void  ChangeMeetingDate(){ 
		Date date= new Date();	
		try{
			String strCurrentDate = new String();
			strCurrentDate = newMeeting.getMeetingID().getMyDate().toString(); 
			SimpleDateFormat sfd = new SimpleDateFormat("E-dd/MM/yy");
			sfd.setLenient(false);
			date = sfd.parse(strCurrentDate);
		}
		catch(ParseException Err)
		{
			System.out.print(Err.toString());
			Log.e("Meeting View Change Data", Err.getMessage(), Err);
			date.setTime(0);
		}
		Calendar cl = Calendar.getInstance();
		cl.setFirstDayOfWeek(Calendar.MONDAY); 	
		cl.setTime(date);
		new DatePickerDialog(this,mDateSetListener, 
				cl.get(Calendar.YEAR), cl.get(Calendar.MONTH),
				cl.get(Calendar.DAY_OF_MONTH),Calendar.SUNDAY).show();
	}
	
	public void  ChangeTime(int nType){ 
		currentOperation = nType;
		switch(nType){
			case 1:
				new TimePickerDialog(this,
						mTimeSetListener, "Set Start Meeting time",
						newMeeting.getMeetingID().getStartHour(), newMeeting.getMeetingID().getStartMin(), true).show();
				break;
			case 2:
				new TimePickerDialog(this,
						mTimeSetListener, "Set Start Meeting time",
						newMeeting.getMeetingID().getEndHour(), newMeeting.getMeetingID().getEndMin(), true).show();
					
				break;
			default:
				new TimePickerDialog(this,
						mTimeSetListener, "Set Start Meeting time",
						newMeeting.getMeetingID().getStartHour(), newMeeting.getMeetingID().getStartMin(), true).show();
		}		
	}
	
	//---------------------------------------------------------------------
	// add People
	//---------------------------------------------------------------------
	public void  AddPeople(){
		//TODO: finalize passing list of people
    	Intent i = new Intent(this, ContactsList.class);
    	String strTmp = "";
    	String strCointactsIDs="";
    	
    	for (int index = 0; index < newMeeting.attendlist.size(); index++){
    		strTmp = "'" + ((Attends)newMeeting.attendlist.get(index)).ID + "'," +strTmp;
    	}
    	if (strTmp.length() > 1){
			strCointactsIDs = strTmp.substring(0, strTmp.length()-1);
			i.putExtra(ContactsList.CONTACTS_IDS, strCointactsIDs);
		}
    	startSubActivity(i,ADD_PEOPLE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras){
		super.onActivityResult(requestCode, resultCode, data, extras);
		
		if (resultCode != android.app.ActivityGroup.RESULT_CANCELED){
			String IDList  	= extras.getString(ContactsList.CONTACTS_IDS);	
			String NamesList  	= extras.getString(ContactsList.CONTACTS_NAMES);	
			if(IDList.length()>0){
				String IDs[] = IDList.split(",");
				String Names[] = NamesList.split(",");
				for (int index = 0; index < IDs.length; index++){
					Attends newAttend = new Attends();
					newAttend.ID = IDs[index];
					newAttend.Name = Names[index];
					newAttend.Response = Meeting.NO_RESPONSE; 	
					newMeeting.attendlist.add(newAttend);
				}
			}
			//onContentChanged();
			if(pla == null)
		        pla = new PeopleListAdapter(this);
		    setListAdapter(pla);
		}
	}
	//---------------------------------------------------------------------
	// events handlers
	//---------------------------------------------------------------------
	 private DatePicker.OnDateSetListener mDateSetListener =
         new DatePicker.OnDateSetListener() {
             public void dateSet(DatePicker view, int year, int monthOfYear,
                     int dayOfMonth) {
            	 newMeeting.getMeetingID().setUID((short)year, (short)(monthOfYear+1), (short)dayOfMonth);
            	 UpdateLabels();
             }
         };
    
         private TimePicker.OnTimeSetListener mTimeSetListener =
         new TimePicker.OnTimeSetListener() {
             public void timeSet(TimePicker view, int hourOfDay, int minute) {
            	 switch(currentOperation){
            	 	case 1:
            	 		newMeeting.setStartTimeFrame((short)hourOfDay,(short)minute);
            	 		break;
            	 	case 2:
              	 		newMeeting.setEndTimeFrame((short)hourOfDay,(short)minute);
              	 		break;
                }
            	currentOperation = SETUP_NULL;
            	UpdateLabels();
             }
         };
}