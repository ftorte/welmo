package com.welmo.calendar;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.welmo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewInflate;
import android.widget.AbsoluteLayout;
import android.widget.Toast;
import android.util.DisplayMetrics;  

public class CalendarDayHour extends AbsoluteLayout{

	Vector<MeetingBarView> 	mMeetings 	= new Vector<MeetingBarView>();	
	protected int 			mWidth		=0;
	protected int			mHeigth		=0;
	
	public CalendarDayHour(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		mContext = context;
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShowMessge("Calendar \n Clik Catched: ");
				
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				ShowMessge("Calendar \n Focus Catched: ");
			}		
		});
		setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0){
				ShowMessge("Calendar \n long Clik Catched: ");
				return true;
			}
		});
		setFocusableInTouchMode(true);
		setFocusable(true);
		setEnabled(true);
		DisplayMetrics dsp = mContext.getResources().getDisplayMetrics();
		mWidth = (dsp.widthPixels-11)/7;
		mHeigth = 640;
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		addMeeting(7,00,8,00);
		addMeeting(8,00,10,00);
		addMeeting(11,00,12,00);
		addMeeting(11,30,12,30);
		addMeeting(8,30,12,00);
		addMeeting(10,30,11,00);
	}

	public CalendarDayHour(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	private void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	public void addMeeting(int start_h,int start_m, int end_h, int end_m){
		int position = 0;
		int occupationMap = 0;
		int MaxOccupationPos = 0;
		Vector<MeetingBarView> coflicts = new Vector<MeetingBarView>();
		MeetingBarView newMeetingBar = new MeetingBarView(mContext,mWidth,mHeigth);
		newMeetingBar.setPeriod( start_h, start_m,  end_h,  end_m);
		//---------------------------------------------------
		//exams all meeting and detect conflicts
		Iterator<MeetingBarView> it = mMeetings.iterator();
		while(it.hasNext()){
			MeetingBarView MeetBar = it.next();
			if(MeetBar.HasConflict(start_h,start_m,end_h,end_m)){
				coflicts.add(MeetBar);
				occupationMap =  occupationMap | (1 << MeetBar.getColumnPosition());
			}
		}
		//---------------------------------------------------
		//Setup conflict level
		position = 0;
		while(((occupationMap & 0x1 ) > 0) && (position < 16)){
			position ++;
			occupationMap = occupationMap >>> 1;
		}
		occupationMap = occupationMap >>> 1;
		if(position >= 16)
			throw new IllegalArgumentException ("Unusual expetation more than 16 eeting conflicts");
		MaxOccupationPos = position;
		while ((occupationMap > 0) && (MaxOccupationPos < 16)){
			MaxOccupationPos ++;
			occupationMap = occupationMap >>> 1;
		}
		if(MaxOccupationPos >= 16)
			throw new IllegalArgumentException ("Unusual expetation more than 16 eeting conflicts");
		//---------------------------------------------------
		//for each meeting in conflict change the conflict level if necessary
		if(position >= MaxOccupationPos){
			it = coflicts.iterator();
			while(it.hasNext()){
				MeetingBarView MeetBar = it.next();
				MeetBar.setConflictLevel(MaxOccupationPos);
			}
		}
		coflicts.removeAllElements();
		newMeetingBar.setConflictLevel(MaxOccupationPos);
		newMeetingBar.setColumnPosition(position);
		mMeetings.add(newMeetingBar);
		addView(newMeetingBar);
	}
	
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(mWidth,mHeigth);	
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
	}
	public int getMWidth() {
		return mWidth;
	}
	public void setMWidth(int width) {
		mWidth = width;
	}
	public int getMHeigth() {
		return mHeigth;
	}
	public void setMHeigth(int heigth) {
		mHeigth = heigth;
	}
}