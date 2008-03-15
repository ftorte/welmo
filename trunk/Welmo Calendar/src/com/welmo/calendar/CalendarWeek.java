package com.welmo.calendar;

import java.util.Calendar;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.Toast;

public class CalendarWeek extends TableRow{

	protected boolean CurrentWeekFocused = false;
	
	public CalendarWeek(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
    			ShowMessge("Calendar Month Clik Catched: "+id);
			}
    	});
		// TODO Auto-generated constructor stub
	}

	public void HideOtherWeeks(){
		CalendarMonth theParent = (CalendarMonth) this.getParent();
		if(theParent.getChildCount() > 0){
			CurrentWeekFocused = true;
			//TODO better manage the un-hide of first 2 row
			for(int index = 1; index < theParent.getChildCount(); index++ )
				if (!theParent.getChildAt(index).equals(this))
					theParent.getChildAt(index).setVisibility(GONE);
				else
					setBackgroundColor(0x5F204887);
			
		}
	}
	
	public void ShowOtherWeeks(){
		CalendarMonth theParent = (CalendarMonth) this.getParent();
		if(theParent.getChildCount() > 0){
			CurrentWeekFocused=false;	
		for(int index = 1; index < theParent.getChildCount(); index++ )
			if (!theParent.getChildAt(index).equals(this))
				theParent.getChildAt(index).setVisibility(VISIBLE);
			else
				setBackgroundColor(0x00000000);
		}
	}
	public CalendarWeek(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}

	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		//setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
		//setMeasuredDimension(35,getMeasuredHeight());
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
		//canvas.drawText(getText().toString(), mPaddingLeft, mPaddingTop - (int) mTextPaint.ascent(), mTextPaint);    
		//draw the occupation bar
		/*int bar_pad = 2; 	//reserve 1 2 pixel around the bar
		int bar_tick = 6; 	//tickness of the occupation bar
		int n_seg = 6;		// nb of segments
		
		int w_width = getMeasuredWidth();
		int w_height = getMeasuredHeight();
		int tick_w = (w_width - 2*bar_pad - (w_width-2*bar_pad)%n_seg)/n_seg; // width on one bar elements
		
		//setup position of first rectangle
		int seg_lefth 	= bar_pad;
		int seg_top 	= w_height -  bar_tick -  bar_pad;
		int seg_right 	= seg_lefth + tick_w;
		int seg_bottom 	= seg_top + bar_tick;
			
		Paint paint = new Paint(); 
		paint.setStyle(Style.FILL); 
		
		//0%
		paint.setColor(0xFFD3D7CF); 
		RectF rect = new RectF(seg_lefth,seg_top,seg_right,seg_bottom);
		canvas.drawRect(rect, paint); 
		//25%
		paint.setColor(0xFF73D216); 
		rect = new RectF(seg_lefth + tick_w,seg_top,seg_right + tick_w,seg_bottom);
		canvas.drawRect(rect, paint); 

		//50%
		paint.setColor(0xFF729FCF); 
		rect = new RectF(seg_lefth + 2*tick_w,seg_top,seg_right + 2*tick_w,seg_bottom);
		canvas.drawRect(rect, paint); 


		//75%
		paint.setColor(0xFF204887); 
		rect = new RectF(seg_lefth + 3*tick_w,seg_top,seg_right + 3*tick_w,seg_bottom);
		canvas.drawRect(rect, paint); 


		//100%
		paint.setColor(0xFFA40000); 
		rect = new RectF(seg_lefth + 4*tick_w,seg_top,seg_right + 4*tick_w,seg_bottom);
		canvas.drawRect(rect, paint); 


		//0%
		paint.setColor(0xFFD3D7CF); 
		rect = new RectF(seg_lefth + 5*tick_w,seg_top,seg_right + 5*tick_w,seg_bottom);
		canvas.drawRect(rect, paint); */
	}

	public boolean isCurrentWeekFocused() {
		return CurrentWeekFocused;
	}

	public void setCurrentWeekFocused(boolean currentWeekFocused) {
		CurrentWeekFocused = currentWeekFocused;
	}
}
