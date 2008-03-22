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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.welmo.R;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class CalendarDayHour extends RelativeLayout{

	Drawable defaultBackground;
	int mOrgWidth;
	int mOrgHeight;
	int flag=0;
	int waitFocus=500; // wait for focus fixed for a minimu time of xMS to shot details
	private Paint mTextPaint;
	protected CalendarMonthView mCalendarMonthView = null;
	private Context mContext;
	
	//manage dimension
	protected boolean	fixedDimension	= false;
	protected int 		mWidth			=0;
	protected int		mHeigth			=0;
	//manage colors
	protected int 		mFocusedBackground		=0xFFFFFFFF;
	protected int 		mLongSelectedBackground	=0xFFFFFFFF;
	//manage day constants
	protected int 		mTheDay			=0;
	//manage occupation map
	protected Paint 	mPaint 			= new Paint(); 
	
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
		View v1 = new View(context);
		v1.setBackgroundColor(0x5F0F5F0F);
		//v1.setLayoutParams(new LayoutParams(10, 10));
		v1.setClickable(true);
		defaultBackground = this.getBackground();
		
		addView(v1, new RelativeLayout.LayoutParams(
				20, 100));
		
		mTextPaint = new Paint();
		setFocusableInTouchMode(true);
		setFocusable(true);
		setEnabled(true);
	}
	public CalendarDayHour(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight() +10*48);
		mWidth = getMeasuredWidth();
		mHeigth = getMeasuredHeight();		
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
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
	/*public int getMTheMonth() {
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
	}*/
	
	public void setDay(CalendarMonthView v,int day){
		mCalendarMonthView = v;
		mTheDay = day;
	}
}