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
import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.packet.Message;   

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;

import com.welmo.R;
import com.welmo.communication.DualService.RunTask;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class XMPPRcvService extends Service
{
    private NotificationManager mNM;    
    private Intent mInvokeIntent;    
    private ArrayList<String> mMessages = new ArrayList();
    private volatile Looper mServiceLooper;
    private String mHost = "talk.google.com";
	private String mPort = "5222";
	private String mService = "gmail.com";
	private String mUsername = "25apr1946";
	private String mPassword = "TFTsw0801"; 
	private Handler serviceHandler = null;
	
    //XMPP handling
    XMPPConnection mConnection = null;
      
    @Override
    protected void onCreate()
    {
    	mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	//start the service
    	//Connect();
    	//Thread thr = new Thread(null, this, "XMPPRcvService");        
    	//thr.start();
    }
    public boolean Connect(){
    	// Create a connection   
    	ConnectionConfiguration connConfig = new ConnectionConfiguration(
    			mHost, Integer.parseInt(mPort), mService);   
    	mConnection = new XMPPConnection(connConfig);   
    	if(mConnection!=null){
    		try {   
    			mConnection.connect();   
    			Log.i("XMPPRcvService", "[Connect] Connected to " + mConnection.getHost());   
    		} catch (XMPPException ex) {   
    			Log.e("XMPPRcvService", "[Connect] Failed to connect to " + mConnection.getHost());   
    			Toast.makeText(this, "XMPP Connection Failed to connect to " + mConnection.getHost(), Toast.LENGTH_LONG).show();
    			mConnection = null;  
    		}   
    	}
    	if(mConnection!=null){
    		try {   
    			mConnection.login(mUsername, mPassword);   
    			Log.i("XMPPRcvService", "[Connect] Logged in as " + mConnection.getUser());   
    			// Set the status to available   
    			Presence presence = new Presence(Presence.Type.available);   
    			mConnection.sendPacket(presence);   
    			Log.i("XMPPRcvService", "[Connect] Signaled Presence" + mConnection.getUser());   
    		} catch (XMPPException ex) {   
    			Log.e("XMPPRcvService", "[Connect] Failed to log in as " + mUsername);   
    			Toast.makeText(this, "XMPP Connection Failed to connect to " + mConnection.getUser(), Toast.LENGTH_LONG).show();
    			mConnection = null;
    		}
    	}
    	if(mConnection != null){
    		InitPackLeatener();
    		return true;
    	}
    	return false;
    }
    
    public void CloseConnection(){
    	Log.i("XMPPRcvService", "[CloseConnection] close XMPP connection");   
    	if(mConnection != null){
    		Presence presence = new Presence(Presence.Type.unavailable);   
    		Log.i("XMPPRcvService", "[CloseConnection] disconnect presence");   
    		mConnection.disconnect(presence);
    		Log.i("XMPPRcvService", "[CloseConnection] connection closed");   
    	}
    	else{
    		Log.i("XMPPRcvService", "[CloseConnection] not connected");   
    		Toast.makeText(this, "XMPP No Connection\n", Toast.LENGTH_LONG).show();		
    	}
    	mConnection=null;
    }
    @Override    
    protected void onStart(int startId, Bundle arguments){        
    	serviceHandler = new Handler();
  	  	serviceHandler.postDelayed( new RunTask(),1000L );
    }
    @Override
    protected void onDestroy()
    {
		Log.i("XMPPRcvService", "[onDestroy] recived onDestroy");          
		CloseConnection();
    	Log.i("XMPPRcvService", "[onDestroy] close looper");   
    	while (mServiceLooper == null){            
    		Log.i("XMPPRcvService", "[onDestroy] wite for looper close"); 
    		synchronized (this) {                
    			try {                    
    				wait(100);                
    			} 
    			catch (InterruptedException e) {                

    			}            
    		}        
    	}  
    	Log.i("XMPPRcvService", "[onDestroy] close looper"); 
    	mServiceLooper.quit(); 
    	
    	mNM.cancel(R.string.alarm_service_started);
    	// Tell the user we stopped.
    	Toast.makeText(this, "XMPP service Closed", Toast.LENGTH_SHORT).show();	
    }

    /**
     * The function that runs in our worker thread
     */
    public boolean SendMessage(String recipient, String message){
    	if(mConnection!= null){
			//send message to talk
			Log.i("XMPPRcvService", "[run] Sending message");   
			String to = recipient;
			Message msg = new Message(to, Message.Type.chat);   
			msg.setBody(message);   
			mConnection.sendPacket(msg);    			
			Log.i("XMPPClient", "[run] Message Sent: text [" + message + "] to [" + to + "]");   
			return true;
    	}
    	else{
    		Log.w("XMPPRcvService", "[run] mesand not sent cause no connection");   
    		return false;
    	}
    }
    
    class RunTask implements Runnable {
  	  public void run() {
  		serviceHandler.postDelayed( this, 1000L );
  	  }
  	}
    
    /*public void run()
    {
    	++counter;
		serviceHandler.postDelayed( this, 1000L );
		
    	//Log.i("XMPPRcvService", "[run] process looper launched"); 
    	//Looper.prepare();        
    	//mServiceLooper = Looper.myLooper();        
    	//Looper.loop();   
    }*/
    
    public void InitPackLeatener(){  
    	Log.i("XMPPRcvService", "[InitPackLeatener] initialized packet listener"); 
    	PacketFilter filter = new MessageTypeFilter(Message.Type.chat);   
    	mConnection.addPacketListener(new PacketListener(){
    		public void processPacket(Packet packet) {   
    			Message message = (Message) packet;   
    			if (message.getBody() != null) {   
    				String fromName = StringUtils.parseBareAddress(message.getFrom());  
    				Log.i("XMPPRcvService", "Got text [" + message.getBody() + "] from [" + fromName + "]");   
    				mMessages.add(fromName + ":");   
    				mMessages.add(message.getBody()); 
    			}   
			}   
		}, filter);   
	}   
    
    @Override
    public IBinder onBind(Intent intent) {
    	//if (IXMPPService.class.getName().equals(intent.getAction())) {
    		if(mServiceBinder != null){
    			Log.i("XMPPRcvService", "[onBind] returned IXMPPService bind");  
    			return mServiceBinder;
    		}
    		Log.w("XMPPRcvService", "[onBind] returned mServiceBinder = Null bind");  
    		return null;
    	//}
    	//Log.w("XMPPRcvService", "[onBind] returned Unknown Bind = Null bind");  
    	//return null;
    }
    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String Message) {
        // This is who should be launched if the user selects our notification.
        Intent contentIntent = new Intent(this, XMPPRcvApplicationLauncher.class);

        // This is who should be launched if the user selects the app icon in the notification,
        // (in this case, we launch the same activity for both)
        Intent appIntent = new Intent(this, XMPPRcvApplicationLauncher.class);

        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.alarm_service_started);

        mNM.notify(R.string.alarm_service_started,  // we use a string id because it is a unique
                                                    // number.  we use it later to cancel the
                                                    // notification
                   new Notification(
                       this,                        // our context
                       R.drawable.stat_sample,      // the icon for the status bar
                       Message,                        // the text to display in the ticker
                       System.currentTimeMillis(),  // the timestamp for the notification
                       getText(R.string.alarm_service_label), // the title for the notification
                       Message,                        // the details to display in the notification
                       contentIntent,               // the contentIntent (see above)
                       R.drawable.allert16x16,  // the app icon
                       getText(R.string.activity_sample_code), // the name of the app
                       appIntent));                 // the appIntent (see above)
    }
   
    // No need to import IRemoteService if it's in the same project.
    private final IXMPPService.Stub mServiceBinder = new IXMPPService.Stub(){
			public boolean SendMessage(String recipient, String message)
					throws DeadObjectException {
				return SendMessage( recipient,  message);
			}
			public void setConnectionServer(String host, String service,String port) throws DeadObjectException {
				mHost = host;
				mService = service;
				mPort=port;
			}
			public void setLoginInfo(String user, String password) throws DeadObjectException {
				mUsername = user;
				mPassword=password;
			}
			public void closeConnection() throws DeadObjectException {
				CloseConnection();
			}
			public boolean isConnected() throws DeadObjectException {
				// TODO Auto-generated method stub
				if (mConnection==null)
    				return false;
    			else
    				return true;
			}
			public boolean openConnection() throws DeadObjectException {
				// TODO Auto-generated method stub
				return Connect();
			}
    };
}

