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
import com.welmo.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.widget.Toast;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an
 * intent receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class AlarmService2_Service extends Service
{
    NotificationManager mNM;

    @Override
    protected void onCreate()
    {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // show the icon in the status bar
        showNotification();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread thr = new Thread(null, mTask, "AlarmService2_Service");
        thr.start();
    }

    @Override
    protected void onDestroy()
    {
        // Cancel the notification -- we use the same ID that we had used to start it
        mNM.cancel(R.string.alarm_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.alarm_service_finished, Toast.LENGTH_SHORT).show();
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {
        public void run()
        {
            // Normally we would do some work here...  for our sample, we will
            // just sleep for 30 seconds.
            long endTime = System.currentTimeMillis() + 15*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (mBinder) {
                    try {
                        mBinder.wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }

            // Done with our work...  stop the service!
            AlarmService2_Service.this.stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // This is who should be launched if the user selects our notification.
        Intent contentIntent = new Intent(this, AlarmService2.class);

        // This is who should be launched if the user selects the app icon in the notification,
        // (in this case, we launch the same activity for both)
        Intent appIntent = new Intent(this, AlarmService2.class);

        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.alarm_service_started);

        mNM.notify(R.string.alarm_service_started,  // we use a string id because it is a unique
                                                    // number.  we use it later to cancel the
                                                    // notification
                   new Notification(
                       this,                        // our context
                       R.drawable.stat_sample,      // the icon for the status bar
                       text,                        // the text to display in the ticker
                       System.currentTimeMillis(),  // the timestamp for the notification
                       getText(R.string.alarm_service_label), // the title for the notification
                       text,                        // the details to display in the notification
                       contentIntent,               // the contentIntent (see above)
                       R.drawable.allert16x16,  // the app icon
                       getText(R.string.activity_sample_code), // the name of the app
                       appIntent));                 // the appIntent (see above)
    }

    /**
     * This is the object that receives interactions from clients.  See RemoteService
     * for a more complete example.
     */
    private final IBinder mBinder = new Binder()
    {
        @Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
            return super.onTransact(code, data, reply, flags);
        }
    };
}

