/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.welmo.travel.tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyKeyboard extends KeyboardView {



	// variables
	private ListView theListView = null;
	private int	inputItem = 0;
	private boolean update = false;
	private CalcInputHandler theInputHandler = null;
	//--------------------------------------
	
	public void setInputHandler(CalcInputHandler arg0) {
		if(arg0 != null){
			theInputHandler=arg0;
		}
		else
			throw new NullPointerException("KeyBoard attached to null pointer");
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(theListView != null && update){
			theListView.setSelectionFromTop(inputItem,0);
			update=false;
		}
	}

	public void setInputItem(int inputItem) {
		this.inputItem = inputItem;
		update = true;
	}

	public void setTheListView(ListView theListView) {
		this.theListView = theListView;
	}

	
    public MyKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnKeyboardActionListener(new theKeyboardActionListener());
    }

    public MyKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public class theKeyboardActionListener implements OnKeyboardActionListener{

		@Override
		public void onKey(int primaryCode, int[] keyCodes){
			// TODO Auto-generated method stub
			if(theInputHandler == null)
				throw new NullPointerException("Keybor not attached to a CalcInputHandler");
			switch(primaryCode){
				//Symbols
				case 0: theInputHandler.inputSymboil(CalcInputHandler.Symbols.ZERO); break;
				case 1: theInputHandler.inputSymboil(CalcInputHandler.Symbols.ONE); break;
				case 2: theInputHandler.inputSymboil(CalcInputHandler.Symbols.TWO); break;
				case 3: theInputHandler.inputSymboil(CalcInputHandler.Symbols.THREE); break;
				case 4: theInputHandler.inputSymboil(CalcInputHandler.Symbols.FOUR); break;
				case 5: theInputHandler.inputSymboil(CalcInputHandler.Symbols.FIVE); break;
				case 6: theInputHandler.inputSymboil(CalcInputHandler.Symbols.SIX); break;
				case 7: theInputHandler.inputSymboil(CalcInputHandler.Symbols.SEVEN); break;
				case 8: theInputHandler.inputSymboil(CalcInputHandler.Symbols.HEIGHT); break;
				case 9: theInputHandler.inputSymboil(CalcInputHandler.Symbols.NINE); break;
				case 10: theInputHandler.inputSymboil(CalcInputHandler.Symbols.DOT); break;
				case 11: theInputHandler.inputSymboil(CalcInputHandler.Symbols.THOUSAND); break;
				//Modifiers
				case 101: theInputHandler.inputModifier(CalcInputHandler.Modifier.AC); break;
				case 102: theInputHandler.inputModifier(CalcInputHandler.Modifier.C); break;
				case 103: theInputHandler.inputModifier(CalcInputHandler.Modifier.BS); break;
				//Operators
				case 201: theInputHandler.inputOperators(CalcInputHandler.Operators.MUL); break;
				case 202: theInputHandler.inputOperators(CalcInputHandler.Operators.DIVR); break;
				case 203: theInputHandler.inputOperators(CalcInputHandler.Operators.SUM); break;
				case 204: theInputHandler.inputOperators(CalcInputHandler.Operators.SUB); break;
				case 205: theInputHandler.inputOperators(CalcInputHandler.Operators.EQ); break;
				//SpecialActions
				case 301: theInputHandler.inputSpecialActions(CalcInputHandler.SpecialActions.RETURN); break;
				case 302: theInputHandler.inputSpecialActions(CalcInputHandler.SpecialActions.HIDEKEYBOARD); break;
				
			}
		}

		@Override
		public void onPress(int primaryCode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRelease(int primaryCode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onText(CharSequence text) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void swipeDown() {
			theInputHandler.inputSpecialActions(CalcInputHandler.SpecialActions.HIDEKEYBOARD);	
		}

		@Override
		public void swipeLeft() {
			theInputHandler.inputSpecialActions(CalcInputHandler.SpecialActions.HIDEKEYBOARD);	
		}

		@Override
		public void swipeRight() {
			theInputHandler.inputSpecialActions(CalcInputHandler.SpecialActions.HIDEKEYBOARD);	
		}

		@Override
		public void swipeUp() {
			theInputHandler.inputSpecialActions(CalcInputHandler.SpecialActions.HIDEKEYBOARD);
			// TODO Auto-generated method stub
			
		}};
}
