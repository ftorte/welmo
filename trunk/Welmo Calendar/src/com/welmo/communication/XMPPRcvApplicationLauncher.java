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
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.welmo.R;
import com.welmo.communication.DualServiceClient.CounterServiceConnection;


/**
 * <p>Example of scheduling one-shot and repeating alarms.  See
 * {@link OneShotAlarm} for the code run when the one-shot alarm goes off, and
 * {@link RepeatingAlarm} for the code run when the repeating alarm goes off.
 * This demonstrates a very common background that puts together both alarms
and service: a regularily scheduled alarm that results in the execution of
a relatively long-lived service.  An example of where you would use this is
for background retrieval of mail.  In this situation, you don't want to
retrieve the mail directly in the alarm's intent receiver, because this would
block others while you are working.  Instead, the alarm starts a service that
takes care of retrieving the mail.</p>

<h4>Demo</h4>
App/Service/Alarm Service
 
<h4>Source files</h4>
<table class="LinkTable">
        <tr>
            <td class="LinkColumn">src/com/google/android/samples/app/AlarmService.java</td>
            <td class="DescrColumn">The activity that lets you schedule the alarm</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com/google/android/samples/app/AlarmService_Alarm.java</td>
            <td class="DescrColumn">This is an intent receiver that executes when the
                alarm goes off</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com/google/android/samples/app/AlarmService_Service.java</td>
            <td class="DescrColumn">This is the service that implements our background action,
                which is started by the AlarmService_Alarm</td>
        </tr>
        <tr>
            <td class="LinkColumn">/res/any/layout/alarm_service.xml</td>
            <td class="DescrColumn">Defines contents of the screen</td>
        </tr>
</table>

 */
public class XMPPRcvApplicationLauncher extends Activity
{
    Context mContext;
    private static final String LOG_TAG = "DUALSERVICECLIENT";
    private IXMPPService xmppService=null;
    private XMPPServiceConnection xmppConnection=null;
    
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

    		try {
    			Log.i( "XMPPLauncher","send messge :"  + recipient + ":" + message);
    			boolean ok = xmppService.SendMessage(recipient, message);
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

