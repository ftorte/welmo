/*
 * Copyright 2007 Steven Osborn
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
package com.welmo.util;

import android.widget.CheckBox;
import android.util.Log;

/** @author Steven Osborn - http://steven.bitsetters.com */
public class CheckBoxifiedText implements Comparable<CheckBoxifiedText>{
   
     private String mText = "";
     private boolean mChecked;
     
     public CheckBoxifiedText(String text, boolean checked) {
    	 /* constructor */ 
          mText = text;
          mChecked = checked;
          // creates error!
          //getCheckBox().setChecked(true);
          // TODO: Figure out how to set initial value!
          Log.v("CheckBoxifiedText.java","Constructor Called");
     }
     public void setChecked(boolean value)
     {
    	 this.mChecked = value;
     }
     public boolean getChecked(){
    	 return this.mChecked;
     }
     
     public String getText() {
          return mText;
     }
     
     public void setText(String text) {
          mText = text;
     }
     
     /** Make CheckBoxifiedText comparable by its name */
     //@Override
     public int compareTo(CheckBoxifiedText other) {
          if(this.mText != null)
               return this.mText.compareTo(other.getText());
          else
               throw new IllegalArgumentException();
     }
} 