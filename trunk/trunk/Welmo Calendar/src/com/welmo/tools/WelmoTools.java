package com.welmo.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.welmo.R;
import com.welmo.communication.IXMPPService;
import com.welmo.communication.XMPPConfigHandler;
import com.welmo.communication.XMPPRcvService;
import com.welmo.dbhelper.WelmoConfigDBHelper;

public class WelmoTools extends Activity{
	
	private Thread syncThread;
	private Timer timer;
	private ImageView arrowsImage;
	private Handler mHandler = new Handler();
	private XMPPConfigHandler mDialog = null;
	private XMPPServiceConnection xmppConnection=null;
	private IXMPPService xmppService=null;
	WelmoConfig theConfig =null;
	boolean XMPPConnected = false;


	//handle debug/log
	private static final String LOG_TAG = "[WelmoTools]";
	
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.welmotools);

		((ImageView) findViewById(R.id.contacts_shape)).setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.contacts_shape)).setOnClickListener(mListener);

		((ImageView) findViewById(R.id.calendar_shape)).setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.calendar_shape)).setOnClickListener(mListener);

		((ImageView) findViewById(R.id.tasks_shape)).setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.tasks_shape)).setOnClickListener(mListener);

		((ImageView) findViewById(R.id.notes_shape)).setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.notes_shape)).setOnClickListener(mListener);

		((ImageView) findViewById(R.id.config_shape)).setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.config_shape)).setOnClickListener(mListener);

		((ImageView) findViewById(R.id.launchstop_xmpp)).setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.launchstop_xmpp)).setOnClickListener(mListener);
		
		//Launch XMPP Services
		startService( new Intent(WelmoTools.this,XMPPRcvService.class));
		Log.i( "WelmoTools","service launced" );
		
		//bind to XMPP Service
		xmppConnection = new XMPPServiceConnection();
		bindService( new Intent(WelmoTools.this, XMPPRcvService.class), xmppConnection, Context.BIND_AUTO_CREATE);
		Log.i(LOG_TAG,"service binded" );

		//Read Welmo Configurations
		WelmoConfigDBHelper dbConfig = new WelmoConfigDBHelper(this,"Welmo","Config");
		theConfig = new WelmoConfig();
		theConfig.RestoreFromDatabase(dbConfig);
	}

	void ShowMessge(String Msg){
		Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}

	OnClickListener mListener = new OnClickListener() {
		public void onClick(View v) {
			// This class extends Thread
			class SyncThread extends Thread {

				private String[] strProcessNames= new String[]{ 
						"Welemo.Content",
						"Welemon Init Agende", 
						"",
						"",
						"Welemo.Coinfig",
						"XMPP StartStop"};
				
				private int nProcessType;
				
				public void setProcessType(int type){
					nProcessType = type;
					setName(strProcessNames[type]);
				}
				public void run() {
					try {
						switch (nProcessType) {
						case 0:
							SetupContactContent();
							break;
						case 1:
							SetupAgendaContent();
							break;
						case 4:
							ConfigWelemo();
							break;
						case 5:
							ConnectXMPP();
							break;
						}
					} catch (Exception e) {
						Log.e("Error" , e.toString());
					}
				}
			}

			class ArrowsTask extends TimerTask {
				int count = 0;
				public void run() {
					mHandler.post(new Runnable() {
						public void run() {
							switch (count) {
							case 0:
								arrowsImage.setImageResource(R.drawable.icon_sync3_dark);
								break;
							case 1:
								arrowsImage.setImageResource(R.drawable.icon_sync4_dark);
								break;
							case 2:
								arrowsImage.setImageResource(R.drawable.icon_sync1_dark);
								break;
							case 3:
								arrowsImage.setImageResource(R.drawable.icon_sync2_dark);
								count = -1;
								break;
							}
							count++;
						}
					});
				}
			}

			// Create and start the sync thread
			syncThread = new SyncThread();
			ArrowsTask arrowTask = new ArrowsTask();
			timer = new Timer("fnblArrows");

			//
			// Set the correct arrow to animate
			//

			if (v.getId() == R.id.contacts_shape) {
				arrowsImage = (ImageView) findViewById(R.id.contacts_arrow);
				((SyncThread)syncThread).setProcessType(0);
			} 
			else{ 
				if (v.getId() == R.id.config_shape) {
					arrowsImage = (ImageView) findViewById(R.id.config_arrow);
					((SyncThread)syncThread).setProcessType(4);
				} 
				else{
					if (v.getId() == R.id.calendar_shape) {
						arrowsImage = (ImageView) findViewById(R.id.calendar_arrow);
						((SyncThread)syncThread).setProcessType(1);
					} 
					else{
						if (v.getId() == R.id.launchstop_xmpp) {
							arrowsImage = (ImageView) findViewById(R.id.xmpp_arrow);
							((SyncThread)syncThread).setProcessType(5);
						} 
						else{
							ShowMessge("Not supported (yet), try later...");
							return;
						}
					}
				}
			}

			//Start the sync thread
			syncThread.start();
			timer.schedule(arrowTask, 1, 150);
		}
	};

	OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean b) {


			if (b) {
				((ImageView) v).setImageResource(R.drawable.sync_shape_over);

				if (v.getId() == R.id.contacts_shape) {
					((ImageView) findViewById(R.id.contacts_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				} else if (v.getId() == R.id.calendar_shape) {
					((ImageView) findViewById(R.id.calendar_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				} else if (v.getId() == R.id.tasks_shape) {
					((ImageView) findViewById(R.id.tasks_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				} else if (v.getId() == R.id.notes_shape) {
					((ImageView) findViewById(R.id.notes_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				}
			} else {
				((ImageView) v).setImageResource(R.drawable.sync_shape);

				if (v.getId() == R.id.contacts_shape) {
					((ImageView) findViewById(R.id.contacts_arrow))
					.setImageResource(0);
				} else if (v.getId() == R.id.calendar_shape) {
					((ImageView) findViewById(R.id.calendar_arrow))
					.setImageResource(0);
				} else if (v.getId() == R.id.tasks_shape) {
					((ImageView) findViewById(R.id.tasks_arrow))
					.setImageResource(0);
				} else if (v.getId() == R.id.notes_shape) {
					((ImageView) findViewById(R.id.notes_arrow))
					.setImageResource(0);
				}
			}
		}
	};

	void SetupContactContent()
	{
		//Precondition
		mHandler.post(new Runnable() {
			public void run() {
				((TextView) findViewById(R.id.sync_message_contacts))
				.setText("Initilizing Contacts...");
			}
		});

		//--------------------------------------------------------------
		//clear contact content
		getContentResolver().delete(People.CONTENT_URI, null, null);
		getContentResolver().delete(Phones.CONTENT_URI, null, null);
		//--------------------------------------------------------------

		InputStream in = this.getResources().openRawResource(R.raw.welmocontactinit); 

		//get SAX a factory
		XMLContentContactHandler cntContactHandler = new XMLContentContactHandler(this);

		try {

			//get a new instance of XML Reader
			XMLReader saxReader = XMLReaderFactory.createXMLReader("org.xmlpull.v1.sax2.Driver");
			saxReader.setContentHandler(cntContactHandler);
			saxReader.parse(new InputSource(in));
		}catch(SAXException se) {
			se.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		//Terminate
		timer.cancel();
		mHandler.post(new Runnable() {
			public void run() {
				arrowsImage.setImageResource(R.drawable.icon_complete);
				((TextView) findViewById(R.id.sync_message_contacts))
				.setText("Contacts Initilized...");
			}
		});	
	}

	void SetupAgendaContent()
	{

		//Precondition
		mHandler.post(new Runnable() {
			public void run() {
				((TextView) findViewById(R.id.sync_message_calendar))
				.setText("Initializing Agenda...");
			}
		});

		InputStream in = this.getResources().openRawResource(R.raw.welmoagendainit); 

		//get SAX a factory
		XMLContentAgendaHandler cntContactHandler = new XMLContentAgendaHandler(this);

		try {

			//get a new instance of XML Reader
			XMLReader saxReader = XMLReaderFactory.createXMLReader("org.xmlpull.v1.sax2.Driver");
			saxReader.setContentHandler(cntContactHandler);
			saxReader.parse(new InputSource(in));
		}catch(SAXException se) {
			se.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}

		//Terminate
		timer.cancel();
		mHandler.post(new Runnable() {
			public void run() {
				arrowsImage.setImageResource(R.drawable.icon_complete);
				((TextView) findViewById(R.id.sync_message_calendar))
				.setText("Calendar Initilized...");
			}
		});	
	}


	void ConfigWelemo()
	{

		//Precondition
		mHandler.post(new Runnable() {
			public void run() {
				((TextView) findViewById(R.id.sync_welmo_config))
				.setText("Configuring Welmo...");
			}
		});

		InputStream in = this.getResources().openRawResource(R.raw.welmoapplicationconfig); 


		//get SAX a factory
		XMLConfigurationHandler cfgHandler = new XMLConfigurationHandler(this);		
		try {
			//get a new instance of XML Reader
			XMLReader saxReader = XMLReaderFactory.createXMLReader("org.xmlpull.v1.sax2.Driver");
			saxReader.setContentHandler(cfgHandler);
			saxReader.parse(new InputSource(in));
		}catch(SAXException se) {
			se.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}

		//Terminate
		timer.cancel();
		mHandler.post(new Runnable() {
			public void run() {
				arrowsImage.setImageResource(R.drawable.icon_complete);
				((TextView) findViewById(R.id.sync_welmo_config))
				.setText("Welmo Configured...");
			}
		});	
	}


	void ConnectXMPP()
	{

		//Precondition
		mHandler.post(new Runnable() {
			public void run() {
				((TextView) findViewById(R.id.launchstop_xmpp_text))
				.setText("Loginning XMPP...");
			}
		});


		//Login to service
		try{
			if (!xmppService.isConnected()){
				String login = theConfig.GetEntryValue("/XMPP/Login/Default","value");
				xmppService.setConnectionServer(theConfig.GetEntryValue("/XMPP/Login/"+login,"host"),theConfig.GetEntryValue("/XMPP/Login/"+login,"service"),theConfig.GetEntryValue("/XMPP/Login/"+login,"port"));
				xmppService.setLoginInfo(theConfig.GetEntryValue("/XMPP/Login/"+login,"username"),
						theConfig.GetEntryValue("/XMPP/Login/"+login,"password"));
				XMPPConnected = xmppService.openConnection();
			}
			else{
				xmppService.closeConnection();
				XMPPConnected = false;
			}
		}
		//Chatch Dead object
		catch(DeadObjectException excp){
		}
		//Catch Illegal arg exception generated by GetEntryValue
		catch(IllegalArgumentException excp){
			XMPPConnected=false;
		}
		catch(RemoteException excp){
			XMPPConnected=false;
		}
		timer.cancel();
		mHandler.post(new Runnable() {
			public void run() {
				try{
					arrowsImage.setImageResource(R.drawable.icon_complete);
					if(XMPPConnected){
						((TextView) findViewById(R.id.launchstop_xmpp_text)).setText("XMPP Logged In:" + xmppService.getLoginInfo());
						((ImageView) findViewById(R.id.image_xmpp)).setImageResource(R.drawable.go32x32);
					}
					else{
						((TextView) findViewById(R.id.launchstop_xmpp_text)) .setText("XMPP Logged Out");
						((ImageView) findViewById(R.id.image_xmpp)).setImageResource(R.drawable.stop32x32);
					}
				}
				catch(DeadObjectException excp){
				}
				catch(RemoteException excp){
				}
			}
		});	
	}


	//XMPP service Handles
	class XMPPServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className, 
				IBinder boundService ) {
			xmppService = IXMPPService.Stub.asInterface((IBinder)boundService);
			try{
				((TextView) findViewById(R.id.launchstop_xmpp_text)) .setText("XMPP Conected" +xmppService.getServerInfo());
				if (xmppService.isConnected()){
					((TextView) findViewById(R.id.launchstop_xmpp_text)) .setText("XMPP Logged In :" +xmppService.getLoginInfo());
					((ImageView) findViewById(R.id.image_xmpp)).setImageResource(R.drawable.go32x32);
				}	
			}
			catch(DeadObjectException excp){
			}
			catch(RemoteException excp){
			}
			//Log.d( LOG_TAG,"onServiceConnected" );
		}

		public void onServiceDisconnected(ComponentName className) {
			xmppService = null;
			((TextView) findViewById(R.id.launchstop_xmpp_text)) .setText("XMPP Disconencted");
			((ImageView) findViewById(R.id.image_xmpp)).setImageResource(R.drawable.stop32x32);
		}
	};

	//handle menu configuration
	void ShowMessage(String Msg){
		Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "Configure");
		return result;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem  item){
		switch(item.getItemId()) {
		case 1:
			// Dialog for getting the xmpp settings   
			mDialog = new XMPPConfigHandler(this);   
			mHandler.post(new Runnable() {   
				public void run() {   
					mDialog.show();   
				}   
			});   
			break;
		}
		return true;
	}
}