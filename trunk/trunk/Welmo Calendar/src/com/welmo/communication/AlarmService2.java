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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.welmo.R;


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
public class AlarmService2 extends Activity
{
    Context mContext;
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
    }

    private OnClickListener mStartAlarmListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            // This is the intent receiver who will be run when the alarm
            // goes off.  We just create an intent with an explicit class name
            // to have our own receiver (which has been published in
            // AndroidManifest.xml) instantiated and called.
            Intent intent = new Intent(AlarmService2.this, AlarmService2_Alarm.class);

            // We want the alarm to go off 30 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();

            // Schedule the alarm!
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            firstTime, 30*1000, intent);

            // Tell the user about what we did.
            Toast.makeText(AlarmService2.this, R.string.repeating_scheduled,
                    Toast.LENGTH_LONG).show();
            
            mContext.startService(new Intent(mContext, AlarmService2_Service.class),null);
        }
    };

    private OnClickListener mStopAlarmListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            // Create the same intent that was scheduled.
            Intent intent = new Intent(AlarmService2.this, AlarmService2_Alarm.class);

            // And cancel the alarm.
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.cancel(intent);

            // Tell the user about what we did.
            Toast.makeText(AlarmService2.this, R.string.repeating_unscheduled,
                    Toast.LENGTH_LONG).show();

        }
    };
}

