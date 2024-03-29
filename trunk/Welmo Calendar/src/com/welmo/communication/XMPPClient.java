package com.welmo.communication;

import android.app.Activity;   
import android.os.Bundle;   
import android.os.Handler;   
import android.util.Log;   
import android.view.View;   
import android.widget.ArrayAdapter;   
import android.widget.Button;   
import android.widget.EditText;   
import android.widget.ListView;   
import org.jivesoftware.smack.PacketListener;   
import org.jivesoftware.smack.XMPPConnection;   
import org.jivesoftware.smack.filter.MessageTypeFilter;   
import org.jivesoftware.smack.filter.PacketFilter;   
import org.jivesoftware.smack.packet.Message;   
import org.jivesoftware.smack.packet.Packet;   
import org.jivesoftware.smack.util.StringUtils; 
import com.welmo.R;

import java.util.ArrayList;   

public class XMPPClient extends Activity {   

	private ArrayList<String> messages = new ArrayList();   
	private Handler mHandler = new Handler();   
	private XMPPSettingHandler mDialog;   
	private EditText mRecipient;   
	private EditText mSendText;   
	private ListView mList;   
	private XMPPConnection connection;   

	/**  
	 * Called with the activity is first created.  
	 */  
	@Override  
	public void onCreate(Bundle icicle) {   
		super.onCreate(icicle);   
		setContentView(R.layout.xmppclient);   

		mRecipient = (EditText) this.findViewById(R.id.recipient);   
		mSendText = (EditText) this.findViewById(R.id.sendText);   
		mList = (ListView) this.findViewById(R.id.listMessages);   
		setListAdapter();   


		// Dialog for getting the xmpp settings   
		mDialog = new XMPPSettingHandler(this);   

		// Set a listener to show the settings dialog   
		Button setup = (Button) this.findViewById(R.id.setup);   
		setup.setOnClickListener(new View.OnClickListener() {   
			public void onClick(View view) {   
				mHandler.post(new Runnable() {   
					public void run() {   
						mDialog.show();   
					}   
				});   
			}   
		});   

		// Set a listener to send a chat text message   
		Button send = (Button) this.findViewById(R.id.send);   
		send.setOnClickListener(new View.OnClickListener() {   
			public void onClick(View view) {   
				String to = mRecipient.getText().toString();   
				String text = mSendText.getText().toString();   

				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");   
				Message msg = new Message(to, Message.Type.chat);   
				msg.setBody(text);   
				connection.sendPacket(msg);   
				messages.add(connection.getUser() + ":");   
				messages.add(text);   
				setListAdapter();   
			}   
		});   
	}   

	/**  
	 * Called by Settings dialog when a connection is establised with the XMPP server  
	 *  
	 * @param connection  
	 */  
	public void setConnection   
	(XMPPConnection   
			connection) {   
		this.connection = connection;   
		if (connection != null) {   
			// Add a packet listener to get messages sent to us   
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);   
			connection.addPacketListener(new PacketListener() {   
				public void processPacket(Packet packet) {   
					Message message = (Message) packet;   
					if (message.getBody() != null) {   
						String fromName = StringUtils.parseBareAddress(message.getFrom());   
						Log.i("XMPPClient", "Got text [" + message.getBody() + "] from [" + fromName + "]");   
						messages.add(fromName + ":");   
						messages.add(message.getBody());   
						// Add the incoming message to the list view   
						mHandler.post(new Runnable() {   
							public void run() {   
								setListAdapter();   
							}   
						});   
					}   
				}   
			}, filter);   
		}   
	}   

	private void setListAdapter   
	() {   
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   
				R.layout.xmppmulti_line_list_item,   
				messages);   
		mList.setAdapter(adapter);   
	}   
}  
