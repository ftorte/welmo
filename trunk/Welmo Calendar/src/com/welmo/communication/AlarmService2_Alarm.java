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

import android.content.Context;
import android.content.Intent;
import android.content.IntentReceiver;

/**
 * This is an example of implementing an {@link IntentReceiver} for an alarm
 * that will start a service when it goes off.  This is useful when your code
 * handling an alarm will not be quite (more than 5 seconds or so) -- instead
 * of doing all your work in the receiver (and making all other receivers wait
 * until you are done), you can start a service that will then take care of
 * things.
 */
public class AlarmService2_Alarm extends IntentReceiver
{
    @Override
    public void onReceiveIntent(Context context, Intent intent)
    {
        // Start up the service.  Note that if the service is taking too long
        // to complete -- longer than our alarm's repeat rate -- then this will
        // just leave the current service running, skipping this alarm.  For
        // most situations this is probably a reasonable thing to do.
        context.startService(new Intent(context, AlarmService2_Service.class),
                null);
    }
}

