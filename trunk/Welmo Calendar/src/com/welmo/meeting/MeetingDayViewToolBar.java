package com.welmo.meeting;

import java.util.Map;

import com.welmo.R;
import com.welmo.tools.ToolBar;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MeetingDayViewToolBar extends ToolBar {

	// ToolBar button  list IDS  
	public static final int GO_MIN7 	= 100;
    public static final int GO_MIN1 	= 101;
    public static final int GO_PLUS1 	= 102;
    public static final int GO_PLUS7 	= 103;
    public static final int DAYLABEL 	= 104;

    protected 	static MeetingDayView mCurrentActivity;
	 
	private static Button dateLabel=null;
	
	public MeetingDayViewToolBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//create 
        CreateImageButton(GO_MIN7,R.drawable.go_previousdoub, 0, 3, 0, 0, R.drawable.background);
        CreateImageButton(GO_MIN1,R.drawable.go_previous, 0, 3, 0, 0, R.drawable.background);
        dateLabel= CreateButton(DAYLABEL,"Test", 0, 3, 0, 0, R.drawable.background);
        CreateImageButton(GO_PLUS1,R.drawable.go_next, 0, 3, 0, 0, R.drawable.background);
        CreateImageButton(GO_PLUS7,R.drawable.go_previousdoub, 0, 3, 0, 0, R.drawable.background);
        
        //set focus change listeaners
        setOnFocusChangeListener(GO_MIN7,toolbarFocusListener);
        setOnFocusChangeListener(GO_MIN1,toolbarFocusListener);
        setOnFocusChangeListener(DAYLABEL,toolbarFocusListener);
        setOnFocusChangeListener(GO_PLUS1,toolbarFocusListener);
        setOnFocusChangeListener(GO_PLUS7,toolbarFocusListener);
        
        setOnClickListener(GO_MIN7,ClickListener);
        setOnClickListener(GO_MIN1,ClickListener);
        setOnClickListener(DAYLABEL,ClickListener);
        setOnClickListener(GO_PLUS1,ClickListener);
        setOnClickListener(GO_PLUS7,ClickListener);
 	} 
    public static void setDayLabel(CharSequence sLabel){
    	dateLabel.setText(sLabel);
    	dateLabel.setTextColor(0xffff0000);
   	 	Typeface tp;
   	 	tp = Typeface.create(Typeface.DEFAULT,Typeface.BOLD);
   	 	dateLabel.setTypeface(tp);
    }
	
    private static Button.OnClickListener ClickListener = 
        new ImageButton.OnClickListener() {
        public void onClick(View arg0) {
        	int id=arg0.getId();
        	mCurrentActivity.ChangeDate(id);
        }
    };
    
    private static ImageButton.OnFocusChangeListener toolbarFocusListener =
        new ImageButton.OnFocusChangeListener() {
        public void onFocusChange(View arg0, boolean arg1) {
            if(arg1) {
                arg0.setBackgroundResource(R.drawable.background2);
            } else {
                arg0.setBackgroundResource(R.drawable.background);
            }
        }
    };

    public void setup(MeetingDayView a){
    	mCurrentActivity = a;
    }
}
