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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gtalkservice.IGTalkService;
import com.google.android.gtalkservice.IGTalkSession;
import com.google.android.gtalkservice.Presence;
import com.welmo.R;

/**
 * <p>Example of using the {@link com.google.android.gtalkservice.IGTalkService} to
 * receive peer to peer data messages.
 * This demonstrates how to use the GTalkService to receive data to/from another Android device.</p>

<h4>Demo</h4>
App/Service/GTalk Data Message Receiver

<h4>Source files</h4>
<table class="LinkTable">
        <tr>
         <td class="LinkColumn">src/com/google/android/samples/app/GTalkDataMessageSender.java</td>
         <td class="DescrColumn">The GTalkService data message Sender</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com/google/android/samples/app/GTalkDataMessageReceiver.java</td>
            <td class="DescrColumn">The GTalkService data message receiver</td>
        </tr>
        <tr>
            <td class="LinkColumn">res/layout/xmpp_data_message_sender.xml</td>
            <td class="DescrColumn">Defines contents of the screen for the Xmpp message sender</td>
        </tr>
</table>

 */
public class GTalkDataMessageSender extends Activity {
    private static final String LOG_TAG = "GTalkServiceSample";

    IGTalkSession mGTalkSession = null;
    EditText mUsernameField;
    Button mSendButton;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.gtalk_data_message_sender);

        mUsernameField = (EditText)findViewById(R.id.username);
        mUsernameField.setOnClickListener(mOnClickListener);
        mUsernameField.requestFocus();

        mSendButton = (Button)findViewById(R.id.send);
        mSendButton.setOnClickListener(mOnClickListener);
        mSendButton.setEnabled(false);

        bindGTalkService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private void bindGTalkService() {
        bindService((new Intent()).setComponent(
                com.google.android.gtalkservice.GTalkServiceConstants.GTALK_SERVICE_COMPONENT),
                mConnection, 0);
    }

    private boolean isValidUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        }

        if (username.indexOf('@') == -1) {
            return false;
        }

        return true;
    }

    private Intent getIntentToSend() {
        Intent intent = new Intent(GTalkDataMessageReceiver.ACTION);
        intent.putExtra("poke", "Hi, I am Sam.");
        intent.putExtra("question", "would you like to eat green eggs and ham?");

        return intent;
    }

    private void showMessage(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the GTalkService has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            IGTalkService GTalkService = IGTalkService.Stub.asInterface(service);

            try {
                mGTalkSession = GTalkService.getDefaultSession();
                if (mGTalkSession == null) {
                    // this should not happen.
                    showMessage("session not found");
                    return;
                }
            } catch (DeadObjectException ex) {
                Log.e(LOG_TAG, "caught " + ex);
                showMessage("found stale service");
            }

            mSendButton.setEnabled(true);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mGTalkSession = null;
            mSendButton.setEnabled(false);
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    	String Jid=new String();
    	int state=0;
    	int uptime=0;
    	Presence presence=null;
    	String username=new String();
    	public void onClick(View v) {
            if (v == mUsernameField) {
                mSendButton.requestFocus();
            } else {
                // use GTalkService to send data message to someone
                String username = mUsernameField.getText().toString();
                if (!isValidUsername(username)) {
                    showMessage("invaid Usernam");
                    return;
                }

                if (mGTalkSession == null) {
                    showMessage("service not connected");
                    return;
                }

                try {
                	Jid = mGTalkSession.getJid(); 
                	state = mGTalkSession.getConnectionState();
                    mGTalkSession.sendDataMessage("25apr1945@gmail.com/androidwCxwXphYj3JM", getIntentToSend());
                } catch (DeadObjectException ex) {
                    Log.e(LOG_TAG, "caught " + ex);
                    showMessage("found stale service");
                    mGTalkSession = null;
                    bindGTalkService();
                }
            }
        }
    };

}









































