package com.welmo.agenda;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.tft.myoffice.R;

public class ToolBar extends TableLayout{
	
	 protected	SortedMap 		mButtons= new TreeMap();
	 protected 	Context 		mContext;
	 protected 	TableRow 		row;
	 protected	int				nButtons=0;

	  
     public ToolBar(Context context,AttributeSet attrs, Map inflateParams){
         super(context,attrs, inflateParams);
         mContext = context;
         row = new TableRow(mContext);
         // Set row background color
         row.setBackground(getResources().getDrawable(R.drawable.background));
         // Set row background color
         row.setBackground(getResources().getDrawable(R.drawable.background));
         // Add row to table
         addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, 
        		 LayoutParams.WRAP_CONTENT));

    }
    public ImageButton CreateImageButton(int id, int buttonImage, int padl,int padt,int padr,int padb, int ButtonBG)
    {
    	ImageButton newButton = new ImageButton(mContext);
    	// Give buttons UID's (sorta)
    	newButton.setId(id);
    	// Set button images
    	newButton.setImageDrawable(getResources().getDrawable(buttonImage));
        // Set button Padding l,t,r,b
    	newButton.setPadding(padl, padt,padr, padb);
     	// Set button background
    	newButton.setBackground(getResources().getDrawable(ButtonBG));
    	// Add buttons to row
    	row.addView(newButton, new TableRow.LayoutParams(
    			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	setColumnStretchable(nButtons, true);
    	nButtons++;
     	mButtons.put(id, newButton);
        return newButton;
    }
    public Button CreateButton(int id, String Label, int padl,int padt,int padr,int padb, int ButtonBG){
   
    	Button newButton = new Button(mContext);
    	// Give buttons UID's (sorta)
    	newButton.setId(id);
    	// Set button label
    	newButton.setText(Label);
    	newButton.setTextColor(0xffff0000);
    	Typeface tp;
      	tp = Typeface.create(Typeface.DEFAULT,Typeface.BOLD);
      	newButton.setTypeface(tp);
      	// Set button Padding l,t,r,b
    	newButton.setPadding(padl, padt,padr, padb);
     	// Set button background
    	newButton.setBackground(getResources().getDrawable(ButtonBG));
    	// Add buttons to row
    	row.addView(newButton, new TableRow.LayoutParams(
    			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	setColumnStretchable(nButtons, true);
    	nButtons++;
     	mButtons.put(id, newButton);
        return newButton;
    }
    public void setOnFocusChangeListener(int id, OnFocusChangeListener FocusListener){
    	View theView = (View)mButtons.get(id);
    	theView.setOnFocusChangeListener(FocusListener);
    }
    
    public void setOnClickListener(int id, OnClickListener FocusListener){
    	View theView = (View)mButtons.get(id);
    	theView.setOnClickListener(FocusListener);
    }
 }

