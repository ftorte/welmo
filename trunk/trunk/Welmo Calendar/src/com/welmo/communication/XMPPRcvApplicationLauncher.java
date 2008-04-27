/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welmo.communication;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.welmo.R;
import com.welmo.meeting.Meeting;
import com.welmo.meeting.MeetingUID;



public class XMPPRcvApplicationLauncher extends Activity
{
    Context mContext;
    private static final String LOG_TAG = "DUALSERVICECLIENT";
    private IXMPPService xmppService=null;
    private XMPPServiceConnection xmppConnection=null;
    private Meeting sendMeeting = new Meeting();
    private int meetingcount = 0;
    
    @Override
	protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.alarm_service);
        mContext = this;
        
        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.start_alarm);
        button.setOnClickListener(mStartAlarmListener);
        button = (Button)findViewById(R.id.stop_alarm);
        button.setOnClickListener(mStopAlarmListener);
        button = (Button)findViewById(R.id.Login);
        button.setOnClickListener(mLogin);
        button = (Button)findViewById(R.id.test);
        button.setOnClickListener(mSendMessage);
    }

    private OnClickListener mStartAlarmListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{

    		Log.i( "XMPPLauncher","create service" );
    		//Create the service
    		//Intent intent = new Intent(XMPPRcvApplicationLauncher.this, XMPPRcvSrvIntentLaucher.class);
    		//long firstTime = SystemClock.elapsedRealtime();
    		//AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
    		//am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime, intent);
    		Bundle b = new Bundle();
    	    startService( new Intent(XMPPRcvApplicationLauncher.this,XMPPRcvService.class),b );
    		Toast.makeText(XMPPRcvApplicationLauncher.this, "XMMP Receiver Sevice Launched", Toast.LENGTH_SHORT).show();            
    		Log.i( "XMPPLauncher","service launced" );
    		
    		Log.i( "XMPPLauncher","bind to service" );
    		//Bind to the service
    		    		    
    		xmppConnection = new XMPPServiceConnection();
    		boolean res = bindService( new Intent(XMPPRcvApplicationLauncher.this, XMPPRcvService.class), 
    				xmppConnection, Context.BIND_AUTO_CREATE);
    		
    		
    		Log.i( "XMPPLauncher","connect to XMPP" );

    	}	
    };

    private OnClickListener mLogin = new OnClickListener(){
    	public void onClick(View v)
    	{
    		//Open the conenctions
    		String user = ((TextView)findViewById( R.id.username)).getText().toString();
    		String password = ((TextView)findViewById( R.id.password)).getText().toString();

    		try {
    			Log.i( "XMPPLauncher","send messge :"  + user + ":" + password);
    			xmppService.setLoginInfo(user, password);
    			boolean ok = xmppService.openConnection();
    			if(ok){
    				Log.i( "XMPPLauncher","Conencted and logged to server" );
    				Toast.makeText(XMPPRcvApplicationLauncher.this, 
    						"XMMP Connection Estabilished", Toast.LENGTH_SHORT).show();            
    			}
    			else{
    				Log.i( "XMPPLauncher","Impossible to connect" );
    				Toast.makeText(XMPPRcvApplicationLauncher.this, 
    						"XMMP Impossible to connect", Toast.LENGTH_SHORT).show();            
    			}
    		} catch( DeadObjectException ex ) {
    			Log.e("XMPPLauncher", "DeadObjectException" );
    		}
    	}	
    };
    
    private OnClickListener mSendMessage = new OnClickListener(){
    	public void onClick(View v)
    	{
    		//Open the conenctions
    		String recipient = ((TextView)findViewById( R.id.recipient)).getText().toString();
    		String message = ((TextView)findViewById( R.id.message)).getText().toString();

    		MeetingUID theUID = new MeetingUID();
    		theUID.setUID((short)2008, (short)4, (short)21);
    		
    		sendMeeting.setMeetingID(theUID);
    		sendMeeting.setTimeFrame((short)(8+meetingcount), (short)30, (short)(9+meetingcount), (short)30);
    		meetingcount++;
    		if(meetingcount>10)
    			meetingcount=0;
    		sendMeeting.setDescription("Pippo Description");
    		sendMeeting.setObject("Pippo Object");
    		sendMeeting.setType(MeetingUID.TYPE_PERSONAL_GENARIC);
    		sendMeeting.setOwner(((TextView)findViewById( R.id.username)).getText().toString());
    		try {
    			Log.i( "XMPPLauncher","send messge :"  + recipient + ":" + sendMeeting.toString());
    			boolean ok = xmppService.SendMessage("0",recipient, sendMeeting.toString(),Meeting.CLSID);
    			if(ok){
    				Log.i( "XMPPLauncher","messge to client sent" );
    				Toast.makeText(XMPPRcvApplicationLauncher.this, 
    						"messge to client sent", Toast.LENGTH_SHORT).show();            
    			}
    			else{
    				Log.i( "XMPPLauncher","messge to client faild" );
    				Toast.makeText(XMPPRcvApplicationLauncher.this, 
    						"messge to client faild", Toast.LENGTH_SHORT).show();            
    			}
    		} catch( DeadObjectException ex ) {
    			Log.e("XMPPLauncher", "DeadObjectException" );
    		}
    	}	
    };

    private OnClickListener mStopAlarmListener = new OnClickListener()
    {
        public void onClick(View v)
        {
           // Tell the user about what we did.
        	boolean res = XMPPRcvApplicationLauncher.this.stopService(new Intent(XMPPRcvApplicationLauncher.this,XMPPRcvService.class));
        	String msg;
         		msg = "XMMP Service Stopped: yes"; 
         	Toast.makeText(XMPPRcvApplicationLauncher.this, msg, Toast.LENGTH_SHORT).show();
        }
    };
    
    
    class XMPPServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, 
			IBinder boundService ) {
        	xmppService = IXMPPService.Stub.asInterface((IBinder)boundService);
		  Log.d( LOG_TAG,"onServiceConnected" );
        }

        public void onServiceDisconnected(ComponentName className) {
        	xmppService = null;
		  Log.d( LOG_TAG,"onServiceDisconnected" );
        }
    };
    
}

