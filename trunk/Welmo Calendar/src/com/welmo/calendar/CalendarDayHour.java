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
		addMeeting(8,30,10,30);
		addMeeting(10,40,11,40);
		setFocusableInTouchMode(true);
		setFocusable(true);
		setEnabled(true);
		DisplayMetrics dsp = mContext.getResources().getDisplayMetrics();
		mWidth = (dsp.widthPixels-11)/7;
		mHeigth = 640;	
	}
	public CalendarDayHour(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	private void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	public void addMeeting(int start_h,int start_m, int end_h, int end_m){
		Vector<MeetingBarView> coficts = new Vector<MeetingBarView>();
		MeetingBarView newMeetingBar = new MeetingBarView(mContext);
		newMeetingBar.setLayoutParams(new AbsoluteLayout.LayoutParams(20,100,5,50));
		newMeetingBar.setPeriod( start_h, start_m,  end_h,  end_m);
		Iterator<MeetingBarView> it = mMeetings.iterator();
		/*while(it.hasNext()){
			MeetingBarView MeetBar = it.next();
			if(MeetBar.HasConflict(start,end)){
				MeetBar.IncrementConflictLevel();
				newMeetingBar.SetMaxConflictLevel(MeetBar.getConflictLevel());
			}
		}*/
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