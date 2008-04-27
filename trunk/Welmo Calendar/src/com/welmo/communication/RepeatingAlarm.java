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
import android.widget.Toast;

/**
 * This is an example of implement an {@link IntentReceiver} for an alarm that
 * should occur once.
 */
public class RepeatingAlarm extends IntentReceiver
{
    @Override
    public void onReceiveIntent(Context context, Intent intent)
    {
        Toast.makeText(context, "repeated recived", Toast.LENGTH_SHORT).show();
    }
}
