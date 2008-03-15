package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class CalendarDay extends TextView{

	Drawable defaultBackground;
	int mOrgWidth;
	int mOrgHeight;
	int flag=0;
	int waitFocus=500; // wait for focus fixed for a minimu time of xMS to shot details
	private Paint mTextPaint;
	private int mDayOfMonth;
	
	
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
	protected int 		mBarPad 		=2; 	//reserve 1 2 pixel around the bar
	protected int	 	mBarTick 		=12; 	//tickness of the occupation bar
	protected int 		mNbBarPeriods 	=6;		//nb of segments
	
	protected int 		mSeg_lefth		=0;
	protected int 		mSeg_top 		=0;
	protected int 		mSeg_right 		=0;
	protected int 		mSeg_bottom 	=0;
	protected int 		mSeg_width 		=0;
	protected Paint 	mPaint 			= new Paint(); 
	
	FocusHandler fh = new FocusHandler();
	
	protected class FocusHandler implements Runnable{
		private boolean activited=false;
		public int objectID=0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(activited)
				ShowMessge("Handle Focus on object:" + objectID);
			activited=false;
		}
		public void Activate(){
			activited=true;
			//android.os.SystemClock.uptimeMillis();
		}
		public void Cancelled(){
			activited=false;	
		}
	}
	
	public CalendarDay(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//int id = arg0.getId();
				//ShowMessge("Calendar Day Clik Catched: "+id + " :" + getMeasuredWidth() + getMeasuredHeight());
				boolean getfocus = arg0.requestFocus();
				ShowMessge("Calendar Day Clik Catched: "+ getfocus);
				
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				//ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b){
					fh.objectID = v.getId();
					fh.Activate();
					v.postDelayed(fh, waitFocus);
					v.setBackgroundColor(0xffedd400);
				}
				else{
					fh.Cancelled();
					v.setBackground(defaultBackground);
				}
			}		
		});
		setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0){
				CalendarWeek theParent = (CalendarWeek) arg0.getParent();
				if(!theParent.isCurrentWeekFocused()){
					theParent.HideOtherWeeks();
					boolean getfocus = arg0.requestFocus();
				}
				else
					theParent.ShowOtherWeeks();
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
		
		mSeg_width = (mWidth - 2*mBarPad - (mWidth-2*mBarPad)%mNbBarPeriods)/mNbBarPeriods; // width on one bar elements
		
		//setup position of first rectangle
		mSeg_lefth 	= mBarPad;
		mSeg_top 	= mHeigth -  mBarTick -  mBarPad;
		mSeg_right 	= mSeg_lefth + mSeg_width;
		mSeg_bottom = mSeg_top + mBarTick;	
		mPaint.setStyle(Style.FILL); 
		
		
		//0%
		paintOccupationTag(canvas,0,0); 
		//25%
		paintOccupationTag(canvas,1,50); 
		//50%
		paintOccupationTag(canvas,2,25); 
		//75%
		paintOccupationTag(canvas,3,75); 
		//100%
		paintOccupationTag(canvas,4,100); 
		//0%
		paintOccupationTag(canvas,5,25); 
	}
	
	private void paintOccupationTag(Canvas canvas,int N, int pct){
		switch(pct){
		case 0:
			mPaint.setColor(0x5FD3D7CF); 
			break;
		case 25:
			mPaint.setColor(0x5F73D216); 
			break;
		case 50:
			mPaint.setColor(0x5F729FCF); 
			break;
		case 75:
			mPaint.setColor(0x5F204887); 
			break;
		case 100:
			mPaint.setColor(0x5FA40000); 
			break;
		default:
			mPaint.setColor(0x5FD3D7CF); 
			break;			
		}
		RectF rect = new RectF(	mSeg_lefth + N*mSeg_width,
								mSeg_bottom - pct/25*2,
								mSeg_right + N*mSeg_width,
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
	public int getMbBarPeriods() {
		return mNbBarPeriods;
	}
	public void setMbBarPeriods(int nbPeriods) {
		mNbBarPeriods = nbPeriods;
	}
	public void setDayNumber(int DayOfMonth){
		mDayOfMonth = DayOfMonth;
		this.setText(Integer.toString(DayOfMonth));
	}
}