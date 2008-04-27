package com.welmo.communication;


import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.welmo.R;
import com.welmo.dbhelper.AgendaDBHelper;
import com.welmo.meeting.IMeetingDisplay;
import com.welmo.meeting.IMeetingDisplayFactory;
import com.welmo.meeting.Meeting;
import com.welmo.meeting.MeetingList;
import com.welmo.meeting.MeetingListAdapter;
import com.welmo.meeting.MeetingUID;

public class XMPPRcvMessageHandler extends ListActivity implements IMeetingDisplayFactory{

	private MeetingList 		theList		= null;
	private MeetingListAdapter 	mDAdp 		= null;
	private Context 			mContext	= null;
	private AgendaDBHelper		dbMessages 	= null;

	//---------------------------------------------
	//Implement IMeetingDisplayFactory
	public IMeetingDisplay getNewView(){
		XMPPMeetingDisplay newDispaly = new XMPPMeetingDisplay(this);
		return newDispaly;
	}	
	//---------------------------------------------
	
		
	
	@Override
    public void onCreate(Bundle icicle){ 
		super.onCreate(icicle);
		  		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notificationslist);
        getListView().setFocusable(true);
        getListView().setFocusableInTouchMode(true);
    
        dbMessages = new AgendaDBHelper(this,"Agenda","MeetingsIncoming","AttendsIncoming");
   	 
        //Setup Staring point
        Bundle extras = getIntent().getExtras();
		if (extras != null){
			//get the meeting UID
			long Caller = extras.getLong("Caller");
			if(Caller == XMPPRcvService.CLSID){
				 int NotifID 		= extras.getInt("NotifID");
				 String CLSID 	= extras.getString("CLSID");
				 NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				 nm.cancel(NotifID);
			}	
		}	
		this.getListView().setOnItemLongClickListener(new ListView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView av, View v, int n, long l){
				boolean getfocus = av.requestFocus();
    			Log.i("XMPPRcvMessageHandler", "[onItemLongClick] Send Response");   
				SendResponse(((XMPPMeetingDisplay)v).getUID(),n);
				return true;
			}
		});
		InitDataConnection();	
 	}
	public void InitDataConnection()
	{
		if (theList != null)
			theList.UpdateToDatabase();
		if (dbMessages != null){
			theList = new MeetingList(this.dbMessages);
			theList.RestoreFromDatabase();
		}
		if(mDAdp == null)
			mDAdp = new MeetingListAdapter(this,mContext,theList);
		setListAdapter(mDAdp);
	}
	public void SendResponse(long UID,int n){
		Intent i = new Intent(this, XMPPRspMessage.class);    	 
		Meeting mtg = (Meeting)mDAdp.getItem(n);
		i.putExtra("UID", mtg.getMeetingID().UID);
		i.putExtra("Recipient", mtg.getOwner());
		Log.i("XMPPRcvMessageHandler", "[SendResponse] Recipient:" +  mtg.getOwner()); 
		i.putExtra("Sender", mtg.getOwner());
		Log.i("XMPPRcvMessageHandler", "[SendResponse] Sender:" +  mtg.getOwner()); 
		i.putExtra("Object", mtg.getObject());
		Log.i("XMPPRcvMessageHandler", "[SendResponse] Object:" +  mtg.getObject()); 
		i.putExtra("Time", mtg.getMeetingID().getMyDate() + " " + mtg.getMeetingID().toStringStartTime());
		Log.i("XMPPRcvMessageHandler", "[SendResponse] Time:" +  mtg.getMeetingID().getMyDate() + " " + mtg.getMeetingID().toStringStartTime()); 		
		i.putExtra("Attend", mtg.whoIsMe());
		startSubActivity(i, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras){
		super.onActivityResult(requestCode, resultCode, data, extras);
		if (resultCode != XMPPRspMessage.RESULT_CANCELLED){
			long UID = extras.getLong("UID");	
			MeetingUID theUID = new MeetingUID(UID);
			theList.DelMeeting(theUID);
			mDAdp.notifyDataSetInvalidated();
		}
	}
}
