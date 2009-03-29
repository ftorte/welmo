package com.welmo.communication;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.welmo.R;

public class XMPPRspMessage extends Activity{

	// handling XMPP configuration
	private IXMPPService xmppService=null;
	private XMPPServiceConnection xmppConnection=null;

	//Handle Log
	private static String LOG_TAG="XMPPRspMessage";

	public final static String CLSID="e473387b-524f-4342-8997-978282aeb5b6";

	private long 	lUID 				= 0L;
	private String 	strRecipient 		= "";
	private String 	strSender 			= "";
	private String  strInvitationObj  	= ""; 
	private String  strInvitationTime 	= ""; 
	private String	whoIsMe				= "";

	public final static int RESULT_ACCEPT = 1;
	public final static int RESULT_REFUSED = 2;
	public final static int RESULT_CANCELLED = 3;
	
	@Override
	public void onCreate(Bundle icicle){ 
		super.onCreate(icicle);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xmpprspmessage);
		
		//bind to xmppService
		xmppConnection = new XMPPServiceConnection();
		if(! bindService( new Intent(XMPPRspMessage.this, XMPPRcvService.class), 
				xmppConnection, Context.BIND_AUTO_CREATE)){
			ShowMessage("Impossible to connect to XMPP service");
		}
		
		//Setup Staring point
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			//get the meeting UID
			lUID = extras.getLong("UID");
			strRecipient = extras.getString("Recipient");
			strSender = extras.getString("Sender");
			((TextView)this.findViewById(R.id.From)).setText(strSender);
			strInvitationObj = extras.getString("Object");
			((TextView)this.findViewById(R.id.Object)).setText(strInvitationObj);
			strInvitationTime = extras.getString("Time");
			((TextView)this.findViewById(R.id.When)).setText(strInvitationTime);
			whoIsMe = extras.getString("Attend");
		}	
		//Buttons 
		ImageButton confirmButton 	= (ImageButton) findViewById(R.id.accept);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				OnResponse(true);
			}
		});
		ImageButton cancelButton 	= (ImageButton) findViewById(R.id.refuse);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				OnResponse(false);
			}
		});	
	}

	void OnResponse(boolean accept){
		int result = 0;
		String response = ((TextView)findViewById(R.id.Response)).getText().toString();
		if(response.compareTo("")==0){
			ShowMessage("Respones cannot be empty");
			return;
		}
		try{
			ProgressDialog.show(XMPPRspMessage.this,
                    "Sendig Response", 
                    "Please wait while the response is sent to:" + strRecipient,  
                    true,
                    true);
			Log.i(LOG_TAG, "OnResponse:" + (accept ? "OK":"NO"));
			//TODO to be transfered in the Service as default connection and as connection list

			if(xmppService != null){
				if(xmppService.isConnected()){
					Log.e(LOG_TAG, "Send response on recipeints" + strRecipient);
					if(accept){
						result = RESULT_ACCEPT;
						response = Long.toString(lUID)+ ",OK,"+ whoIsMe + "," +response;
					}
					else{
						result = RESULT_REFUSED;
						response = Long.toString(lUID)+ ",KO,"+whoIsMe + ","+response;
					}
					xmppService.SendMessage("0",strRecipient, response,CLSID);
				}else{
					ShowMessage("Conenction with GTalk not available");
					result = RESULT_CANCELLED;
				}
			}else{
				ShowMessage("Conenction with GTalk not available");
				result = RESULT_CANCELLED;
			}
		}
		catch( DeadObjectException ex ){
			Log.e(LOG_TAG, "DeadObjectException" );
		}
		catch( RemoteException ex ){
			Log.e(LOG_TAG, "DeadObjectException" );
		}
		Bundle extra = new Bundle();
		extra.putLong("UID",lUID);
		Intent intent = new Intent();
		intent.putExtras(extra);
		setResult(result, intent);
		finish();
	} 



	//	XMPP service Handles
	class XMPPServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className, 
				IBinder boundService ) {
			xmppService = IXMPPService.Stub.asInterface((IBinder)boundService);
		}

		public void onServiceDisconnected(ComponentName className) {
			xmppService = null;
		}
	};
	
	void ShowMessage(String Msg){
        Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}

}
