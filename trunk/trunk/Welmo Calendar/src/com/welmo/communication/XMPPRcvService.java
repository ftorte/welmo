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
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.welmo.R;
import com.welmo.contacts.Attends;
import com.welmo.dbhelper.AgendaDBHelper;
import com.welmo.meeting.Meeting;
import com.welmo.meeting.MeetingUID;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class XMPPRcvService extends Service implements Runnable
{
    private NotificationManager mNM;    
    private volatile Looper mServiceLooper;
    private String mHost = "talk.google.com";
	private String mPort = "5222";
	private String mService = "gmail.com";
	private String mUsername = "25apr1946";
	private String mPassword = "TFTsw0801"; 

	
	public static long CLSID = 0x929af70f84542bffL; 
	public static int MeetingNotificaton=0x7658e2f0;
	//XMPP handling
	XMPPConnection mConnection = null;
	private static String LOD_TAG = "XMPPRcvService";
	//Meeting handling

	//handling meeting invitation requests
	private Meeting 			MeetingInvitation = new Meeting();  
	private Meeting 			MeetingResponse = new Meeting();  
	
	private AgendaDBHelper 		dbMessages 	= null;
	private AgendaDBHelper 		dbAgenda 	= null;
	private int					mMassages	= 0; 
	//handling meeting requests 
	

	@Override
	protected void onCreate()
	{
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		//start the service
		//Connect();
		MeetingUID mUID = new MeetingUID();
		//TEST meeting serialization START
		mUID.setUID((short)2007, (short)3, (short)15);
		mUID.setStartHour((short) 17);
		mUID.setStartMin((short) 30);
		mUID.setEndHour((short) 19);
		mUID.setEndMin((short) 30);
		//TEST meeting serialization START
    	
    	Thread thr = new Thread(null, this, "XMPPRcvService");        
    	thr.start();
    }
    public boolean Connect(){
    	// Create a connection   
    	ConnectionConfiguration connConfig = new ConnectionConfiguration(
    			mHost, Integer.parseInt(mPort), mService);   
    	if(mConnection==null)
    		mConnection = new XMPPConnection(connConfig);   

    	if(dbMessages==null)
    		dbMessages = new AgendaDBHelper(this,"Agenda","MeetingsIncoming","AttendsIncoming");
    	
    	if(dbAgenda==null)
    		dbAgenda = new AgendaDBHelper(this,"Agenda","Meetings","Attends");

    	if(mConnection!=null){
    		try {   
    			mConnection.connect();   
    			Log.i(LOD_TAG, "[Connect] Connected to " + mConnection.getHost());   
    		} catch (XMPPException ex) {   
    			Log.e("XMPPRcvService", "[Connect] Failed to connect to " + mConnection.getHost());   
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
    			mConnection = null;
    		}catch(java.lang.IllegalStateException err){
    			
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
    		mConnection=null;
    	}
    	else{
    		Log.i("XMPPRcvService", "[CloseConnection] not connected");   
    	}
    	mConnection=null;
    }
    @Override    
    protected void onStart(int startId, Bundle arguments){        
    	//serviceHandler = new Handler();
  	  	//serviceHandler.postDelayed( new RunTask(),1000L );
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
    }

    /**
     * The function that runs in our worker thread
     */
    public boolean XMPPSendMessage(String id, String recipient, String message,String CLSID){
    	if(mConnection!= null){
			Log.i("XMPPRcvService", "[run] Sending message");   
			String to = recipient;
			Message msg = new Message(to, Message.Type.chat); 
			msg.setProperty("welmo_CLSID", CLSID);
			msg.setProperty("welmo_attend", id);
			msg.setBody(message);
			mConnection.sendPacket(msg);    			
			Log.i("XMPPRcvService", "[run] Message Sent: text [" + message + "] to [" + to + "]");   
			return true;
    	}
    	else{
    		Log.w("XMPPRcvService", "[run] mesand not sent cause no connection");   
    		return false;
    	}
    }
    
    public void run()
    {
    	
    	Log.i("XMPPRcvService", "[run] process looper launched"); 
    	Looper.prepare();        
    	mServiceLooper = Looper.myLooper();        
    	Looper.loop();   
    }
    
    public void InitPackLeatener(){  
    	Log.i("XMPPRcvService", "[InitPackLeatener] initialized packet listener"); 
    	PacketFilter filter = new MessageTypeFilter(Message.Type.chat);   
    	mConnection.addPacketListener(new PacketListener(){
    		public void processPacket(Packet packet) {   
    			Message message = (Message) packet;   
    			String CLSID = (String) message.getProperty("welmo_CLSID");
    			Log.i("XMPPRcvService", "[InitPackLeatener] Packet CLSID=" + CLSID);
    			if (CLSID.contains(Meeting.CLSID)){  
    				String msg = message.getBody();
    				String AttendID = (String) message.getProperty("welmo_attend");
    				Log.i("XMPPRcvService", "[InitPackLeatener] Attnd Id:" + AttendID);
    				Log.i("XMPPRcvService", "[InitPackLeatener] Body=" + msg);
    				MeetingInvitation.fromString(msg);
    				Log.i("XMPPRcvService", "[InitPackLeatener] Meeting:" + MeetingInvitation.toString());
    				Log.i("XMPPRcvService", "[InitPackLeatener] Message From:" + message.getFrom());
    				String fromName = StringUtils.parseBareAddress(message.getFrom());  
    				Log.i("XMPPRcvService", "[InitPackLeatener] Message From:" + fromName);
    				MeetingInvitation.setOwner(fromName);
    				MeetingInvitation.setIsMe(AttendID);
    				MeetingInvitation.setTimestamp(java.lang.System.currentTimeMillis());
    				Log.i("XMPPRcvService", "[InitPackLeatener] message Update to Tadabase");   
    				MeetingInvitation.UpdateToDatabase(dbMessages);
    				mMassages++;
        			Log.i("XMPPRcvService", "[InitPackLeatener] Got text [" + message.getBody() + "] from [" + fromName + "]");   
        			showNotification("",MeetingNotificaton,Meeting.CLSID);
        			Log.i("XMPPRcvService", "[InitPackLeatener] Sent Notification [" + MeetingNotificaton + 
        					"] CLSID [" + Meeting.CLSID + "]"); 
    			}
    			if (CLSID.contains(XMPPRspMessage.CLSID)){ 
    				try{
    					boolean accept = false;
    					String msg = message.getBody();
    					Log.i("XMPPRcvService", "[InitPackLeatener] Get Message:" + msg);
    					String[] tokens = msg.split(",");
    					Log.i("XMPPRcvService", "[InitPackLeatener] Get Respose:" 
    							+ "[" + tokens[0]+ "]" //Meeting UID
    							+ "[" + tokens[1]+ "]" //Response
    							+ "[" + tokens[2]+ "]" //Attend
    							+ "[" + tokens[3]+ "]"); //Message
    					MeetingResponse.getMeetingID().UID = Long.decode(tokens[0]);
    					if(MeetingResponse.RestoreFromDatabase(dbAgenda)){
    						Attends at = MeetingResponse.findAttend(tokens[2]);
    						if(at !=null){
    							if((accept = (tokens[1].compareTo("OK")==0? true:false)))
    								at.Response = Meeting.RESPONSE_OK;
    							else
    								at.Response = Meeting.RESPONSE_REFUSE;
    							at.Message = tokens[3];
    							MeetingResponse.UpdateToDatabase(dbAgenda);
    						}
    						else{
    							Log.e("XMPPRcvService", "recived response from unnow attend");   
    						}
    					}
    					else
    						Log.e("XMPPRcvService", "recived unknown UID meeting");       						
    				}
    				catch(IndexOutOfBoundsException exc){
    					Log.e("XMPPRcvService", "recived not well formed message");   
    				}
    				catch(NumberFormatException exc){
    					Log.e("XMPPRcvService", "recived not well formed message bad UID");   
    				}
    			} 
    		}   
    	}, filter);   
    }   

    @Override
    public IBinder onBind(Intent intent) {
    		if(mServiceBinder != null){
    			Log.i("XMPPRcvService", "[onBind] returned IXMPPService bind");  
    			return mServiceBinder;
    		}
    		Log.w("XMPPRcvService", "[onBind] returned mServiceBinder = Null bind");  
    		return null;
    } 
    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String Message, int notificationID, String CLSID) {
    	// This is who should be launched if the user selects our notification.
    	Intent contentIntent = new Intent(this, com.welmo.communication.XMPPRcvMessageHandler.class);
    	contentIntent.putExtra("NotifID", notificationID);
    	contentIntent.putExtra("CLSID", CLSID);
    	contentIntent.putExtra("Caller", this.CLSID);
    	// This is who should be launched if the user selects the app icon in the notification,
    	Intent appIntent = new Intent(this, com.welmo.communication.XMPPRcvMessageHandler.class);
    	contentIntent.putExtra("NotifID", notificationID);
    	contentIntent.putExtra("CLSID", CLSID);
    	contentIntent.putExtra("Caller", this.CLSID);

    	//cancel the notification with the same type
    	mNM.cancel(notificationID);
    	//create a notification updated
    	mNM.notify(notificationID, new Notification(this,R.drawable.stat_sample,      
    			getText(R.string.recived_messge), System.currentTimeMillis(),getText(R.string.recived_messge), 
    			Message,contentIntent,R.drawable.appointmentnew32x32,getText(R.string.activity_sample_code), // the name of the app
    			appIntent));                 
    }
   
    // No need to import IRemoteService if it's in the same project.
    private final IXMPPService.Stub mServiceBinder = new IXMPPService.Stub(){
			public boolean SendMessage(String id, String recipient, String message, String CLSID)
					throws DeadObjectException {
				Log.i("XMPPRcvService", "[IXMPPService] SendMessage");
				return XMPPSendMessage(id, recipient,  message, CLSID);
			}
			public void setConnectionServer(String host, String service,String port) throws DeadObjectException {
				Log.i("XMPPRcvService", "[IXMPPService] setConnectionServer");
				mHost = host;
				mService = service;
				mPort=port;
			}
			public void setLoginInfo(String user, String password) throws DeadObjectException {
				Log.i("XMPPRcvService", "[IXMPPService] setLoginInfo");
				
				mUsername = user;
				mPassword=password;
			}
			public void closeConnection() throws DeadObjectException {
				Log.i("XMPPRcvService", "[IXMPPService] closeConnection");
				CloseConnection();
			}
			public boolean isConnected() throws DeadObjectException {
				// TODO Auto-generated method stub
				Log.i("XMPPRcvService", "[IXMPPService] isConnected");
				if (mConnection==null)
    				return false;
    			else
    				return true;
			}
			public boolean openConnection() throws DeadObjectException {
				// TODO Auto-generated method stub
				Log.i("XMPPRcvService", "[IXMPPService] openConnection");
				return Connect();
			}
			public int PendingMessage() throws DeadObjectException {
				// TODO Auto-generated method stub
				Log.i("XMPPRcvService", "[IXMPPService] openConnection");
				return mMassages;
			}
			public void MessageHandled(int NumMessageHandled) throws DeadObjectException {
				// TODO Auto-generated method stub
				Log.i("XMPPRcvService", "[IXMPPService] openConnection");
				mMassages = mMassages - NumMessageHandled;
			}
			public String getServerInfo() throws DeadObjectException {
				// TODO Auto-generated method stub
				Log.i("XMPPRcvService", "[getServerInfo]");
				String response = "[h:" + mHost + "][s:" + mService + "][s:" + mPort;
				return response;
			}
			public String getLoginInfo() throws DeadObjectException {
				// TODO Auto-generated method stub
				Log.i("XMPPRcvService", "[getLoginInfo]");
				return mUsername;
			}
    };
}

