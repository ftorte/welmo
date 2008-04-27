package com.welmo.communication;

import java.util.Calendar;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewInflate;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.welmo.R;
import com.welmo.contacts.Attends;
import com.welmo.meeting.IMeetingDisplay;
import com.welmo.meeting.Meeting;

public class XMPPMeetingDisplay extends LinearLayout implements IMeetingDisplay {
	
	View theView;
	private Context mContext;
	private long TimeStamp=0L;
	private long UID=0L;
	private View header=null;
	private View body=null;
	private boolean bopdyislive=false;
	
	
	public XMPPMeetingDisplay(Context context)
	{
		super(context);
		mContext = context;
		this.setOrientation(VERTICAL);
		
		ViewInflate inf =(ViewInflate)mContext.getSystemService(android.content.Context.INFLATE_SERVICE);
		theView = inf.inflate(R.layout.xmppinvitationshandler, null, false, null); 
		addView(theView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		header = (View)theView.findViewById(R.id.header);
		body = (View)theView.findViewById(R.id.body);
		body.setVisibility(GONE);
		theView.setFocusableInTouchMode(true);
		theView.setFocusable(true);
		setOnClickListener(new LinearLayout.OnClickListener(){
			public void onClick(View arg0){
				ShowHideBody();
				Log.i("XMPPMeetingDisplay", "[onClick] ShowHideBody");   
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				/*if(b){
					v.setBackground(R.drawable.listselector);
				}
				else{
					v.setBackground(R.drawable.listelement);
				}*/
			}		
		});
		this.setClickable(true);
		this.setFocusableInTouchMode(true);
		this.setFocusable(true);
	}
	
	void ShowHideBody(){
		if(bopdyislive){
			bopdyislive=false;
			body.setVisibility(GONE);
		}
		else{
			bopdyislive=true;
			body.setVisibility(VISIBLE);
		}
		this.requestFocus();
	}
	
	public void setMeetingInfo(Meeting currMeeting){
		String strAttendList="";
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(currMeeting.getTimestamp());
		CharSequence MessgeDate = android.util.DateFormat.format("E-dd/MM/yy",c);
		((TextView)header.findViewById(R.id.MessageDate)).setText(MessgeDate);
		((TextView)header.findViewById(R.id.MessageSender)).setText(currMeeting.getOwner());
		((TextView)header.findViewById(R.id.MeetingTime)).setText(currMeeting.getStart_h()+":"+currMeeting.getStart_m());
		((TextView)header.findViewById(R.id.MeetingShortDesc)).setText(currMeeting.getObject());
		((TextView)body.findViewById(R.id.MeetingLongDesc)).setText("Subject:\n" + currMeeting.getDescription());
		
		Iterator<Attends> it = currMeeting.attendlist.iterator();
		strAttendList="Partecipants:\n";
		while(it.hasNext()){
				strAttendList=strAttendList + "," + it.next().Name;
		}
		((TextView)body.findViewById(R.id.MeetingAttends)).setText(strAttendList);
	}

	public long getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		TimeStamp = timeStamp;
	}

	public long getUID() {
		return UID;
	}

	public void setUID(long uid) {
		UID = uid;
	}
}