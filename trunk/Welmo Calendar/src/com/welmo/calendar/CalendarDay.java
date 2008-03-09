package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class CalendarDay extends TextView{

	Drawable defaultBackground;
	int mOrgWidth;
	int mOrgHeight;
	int flag=0;
	private Paint mTextPaint;
	
	
	public CalendarDay(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams,R.style.clendarfreeday);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
				ShowMessge("Calendar Day Clik Catched: "+id + getMeasuredWidth() + getMeasuredHeight());
				boolean getfocus = arg0.requestFocus();
			}
		});
		setFocusable(true);
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				int id = v.getId();
				//ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b)
					v.setBackgroundColor(0xffedd400);
				else
					v.setBackground(defaultBackground);
			}		
		});
		defaultBackground = this.getBackground();
		mTextPaint = new Paint();
		setFocusableInTouchMode(true);
		setFocusable(true);
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
		//setMeasuredDimension(35,getMeasuredHeight());
	}    

	@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
		//canvas.drawText(getText().toString(), mPaddingLeft, mPaddingTop - (int) mTextPaint.ascent(), mTextPaint);    
	}
}