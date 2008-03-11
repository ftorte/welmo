package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class CalendarDay extends TextView{

	Drawable defaultBackground;
	int mOrgWidth;
	int mOrgHeight;
	int flag=0;
	private Paint mTextPaint;
	
	
	//manage dimension
	protected boolean	fixedDimension	= false;
	protected int 		mWidth			=0;
	protected int		mHeigth			=0;
	//manage colors
	protected int 		mFocusedBackground		=0xFFFFFFFF;
	protected int 		mLongSelectedBackground	=0xFFFFFFFF;
	//manage day constants
	protected int 		mTheDay			=0;
	protected int 		mTheMonth		=0;
	protected int 		mTheYear		=0;
	//manage occupation map
	protected int 		mBarPpad 		=2; 	//reserve 1 2 pixel around the bar
	protected int	 	mBarTick 		=6; 	//tickness of the occupation bar
	protected int 		mNbBarPeriods 	=6;		// nb of segments
	
	protected int 		mSeg_lefth		=0;
	protected int 		mSeg_top 		=0;
	protected int 		mSeg_right 		=0;
	protected int 		mSeg_bottom 	=0;
	protected Paint 	mPaint 			= new Paint(); 
	
	
	public CalendarDay(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
				ShowMessge("Calendar Day Clik Catched: "+id + " :" + getMeasuredWidth() + getMeasuredHeight());
				boolean getfocus = arg0.requestFocus();
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				//ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b)
					v.setBackgroundColor(0xffedd400);
				else
					v.setBackground(defaultBackground);
			}		
		});
		setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0){
				TableRow theParent = (TableRow) arg0.getParent();
				theParent.setBackgroundColor(0xAF3465A4);
				/*if(theParent.getChildCount() > 0)
					for(int index = 0; index < theParent.getChildCount(); index++ )
						theParent.getChildAt(index).setBackgroundColor(0x3F4E9A06);
				*/
				ShowMessge("Calendar Day Long Clik Catched: ");
				long i= 100000;
	            while(i > 0)
	        			i= i-1;
				//theParent.setBackgroundColor(0x00000000);
				return true;
			}
		});
	

		defaultBackground = this.getBackground();
		mTextPaint = new Paint();
		setFocusableInTouchMode(true);
		setFocusable(true);
		setEnabled(true);
		//this.setTextAppearance(mContext, R.style.clendarfreeday);
	}

	public CalendarDay(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight() +10);
		mWidth = getMeasuredWidth();
		mHeigth = getMeasuredHeight();		
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
		//canvas.drawText(getText().toString(), mPaddingLeft, mPaddingTop - (int) mTextPaint.ascent(), mTextPaint);    
		//draw the occupation bar
		
		int tick_w = (w_width - 2*bar_pad - (w_width-2*bar_pad)%n_seg)/n_seg; // width on one bar elements
		
		//setup position of first rectangle
		mSeg_lefth 	= bar_pad;
		mSeg_top 	= w_height -  bar_tick -  bar_pad;
		mSeg_right 	= mSeg_lefth + tick_w;
		mSeg_bottom = mSeg_top + bar_tick;	
		mPaint.setStyle(Style.FILL); 
		
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
		canvas.drawRect(rect, paint); 
	}
	
	private void paintOccupationTag(Canvas canvas,int N, int color)
	{
		mPaint.setColor(color); 
		RectF rect = new RectF(	mSeg_lefth + N*tick_w,
								mSeg_top,
								mSeg_right + 5*tick_w,
								mSeg_bottom);
		canvas.drawRect(rect, mPaint); 
	}

	public boolean isFixedDimension() {
		return fixedDimension;
	}
	public void setFixedDimension(boolean fixedDimension) {
		this.fixedDimension = fixedDimension;
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
	public int getMFocusedBackground() {
		return mFocusedBackground;
	}
	public void setMFocusedBackground(int focusedBackground) {
		mFocusedBackground = focusedBackground;
	}
	public int getMLongSelectedBackground() {
		return mLongSelectedBackground;
	}
	public void setMLongSelectedBackground(int longSelectedBackground) {
		mLongSelectedBackground = longSelectedBackground;
	}
	public int getMTheDay() {
		return mTheDay;
	}
	public void setMTheDay(int theDay) {
		mTheDay = theDay;
	}
	public int getMTheMonth() {
		return mTheMonth;
	}
	public void setMTheMonth(int theMonth) {
		mTheMonth = theMonth;
	}
	public int getMTheYear() {
		return mTheYear;
	}
	public void setMTheYear(int theYear) {
		mTheYear = theYear;
	}
	public int getMNbPeriods() {
		return mNbPeriods;
	}
	public void setMNbPeriods(int nbPeriods) {
		mNbPeriods = nbPeriods;
	}
}