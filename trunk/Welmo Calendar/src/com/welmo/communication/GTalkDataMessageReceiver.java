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

import android.content.IntentReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.widget.Toast;

/**
 * <p>Example of using the GTalkService{@link com.google.android.gtalkservice.IGTalkService} to
 * receive peer to peer data messages.
 * This demonstrates how to use the GTalkService to receive data to/from another Android device.</p>

<h4>Demo</h4>
App/Service/GTalk Data Message Receiver

<h4>Source files</h4>
<table class="LinkTable">
        <tr>
            <td class="LinkColumn">src/com/google/android/samples/app/GTalkDataMessageReceiver.java</td>
            <td class="DescrColumn">The GTalkService data message receiver</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com/google/android/samples/app/GTalkDataMessageSender.java</td>
            <td class="DescrColumn">The GTalkService data message Sender</td>
        </tr>
</table>

 */
public class GTalkDataMessageReceiver extends IntentReceiver {
    private static final String LOG_TAG = "GTalkServiceSample";

    /* package */
    static final String ACTION = "android.intent.action.SAMPLE_GTALK_DATA_MESSAGE";

    public void onReceiveIntent(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            StringBuilder buf = new StringBuilder();
            buf.append("Got data message, action=");
            buf.append(ACTION);

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                if (bundle.getString("poke") != null) {
                    appendData(buf, "poke", bundle.getString("poke"));
                }

                if (bundle.getString("question") != null) {
                    appendData(buf, "question", bundle.getString("question"));
                }
            }

            Log.i(LOG_TAG, "[GTalkDataMessageReceiver] onReceiveIntent: " + buf);


            Toast.makeText(context, buf.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void appendData(StringBuilder buf, String key, String value) {
        buf.append(", ");
        buf.append(key);
        buf.append('=');
        buf.append(value);
    }

}
