package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
	private Paint mTextPaint;
	
	public CalendarDay(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int id = arg0.getId();
				ShowMessge("Calendar Day Clik Catched: "+id);
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
		setAlignment(android.text.Layout.Alignment.ALIGN_CENTER);
		defaultBackground = this.getBackground();
		mTextPaint = new Paint();
	}

	public CalendarDay(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}

	/* (non-Javadoc)
	 * @see android.widget.TextView#onMeasure(int, int)
	 */
	

	
	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight()*2);
	}    
	/**     * Determines the width of this view     * 
	 * @param measureSpec A measureSpec packed into an int     
	 * @return The width of the view, honoring constraints from measureSpec    
	 * */   
	/*private int measureWidth(int measureSpec){        
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);   
		int specSize = MeasureSpec.getSize(measureSpec);   
		if (specMode == MeasureSpec.EXACTLY) {    
			// We were told how big to be          
			result = specSize;    
		}
		else
		{           
			// Measure the text  
			//
			result = (int) mTextPaint.measureText(getText().toString()) + mPaddingLeft                    
				+ mPaddingRight;
			if (specMode == MeasureSpec.AT_MOST) {  
				// Respect AT_MOST value if that was what is called for by measureSpec   
				result = Math.min(result, specSize);  
			}        
		}        
		return result;    
	} 
	/**     * Determines the height of this view    
	 *  * @param measureSpec A measureSpec packed into an int   
	 *    * @return The height of the view, honoring constraints from measureSpec    
	 *     */  
	/*private int measureHeight(int measureSpec) {   
		int result = 0;   
		int specMode = MeasureSpec.getMode(measureSpec);      
		int specSize = MeasureSpec.getSize(measureSpec);   
		int mAscent = (int) mTextPaint.ascent();      
		if (specMode == MeasureSpec.EXACTLY) {       
			// We were told how big to be          
			result = specSize;        } else {     
				// Measure the text (beware: ascent is a negative number)      
				result = (int) (-mAscent + mTextPaint.descent()) + mPaddingTop    
				+ mPaddingBottom;            if (specMode == MeasureSpec.AT_MOST) {  
					// Respect AT_MOST value if that was what is called for by measureSpec     
					result = Math.min(result, specSize);        
				}      
			}     
		return result;    
	}
	/*@Override    
	protected void onDraw(Canvas canvas) {        
		super.onDraw(canvas);        
		//canvas.drawText(getText().toString(), mPaddingLeft, mPaddingTop - (int) mTextPaint.ascent(), mTextPaint);    
	}*/
}
