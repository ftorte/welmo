package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.welmo.R;

public class MeetingBarView extends View{

	Drawable defaultBackground;
	Drawable focusBackground;
	private int mPosX;
	private int mPosY;
	private int mWidth;
	private int mHeight;
	private static int daydimension = 2400;
    private int mStart=0;
    private int mEnd=0;
    private int mConflictLevel=0;
    private int mColumnPosition=0;
    private float mTickRatio = 1;
    private long mOccupationMap = 0;
    
	String mTestMessge= new String("8:30-12:30\n Meeting With Mister Gingle");
	
	
	public MeetingBarView(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);
		mContext = context;
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				boolean getfocus = arg0.requestFocus();
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChanged(View v, boolean b){
				//ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b){
					v.setBackground(focusBackground);
					ShowMessge("mTestMessge");	
					ReDrawView();
				}
				else{
					v.setBackground(defaultBackground);
				}
			}		
		});
		setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0){
				ShowMessge("MeetingBarView \n Long Clik Catched: ");
				return true;
			}
		});
	
		setBackground(R.drawable.sync_shape);
		focusBackground = getBackground();
		setBackground(R.drawable.sync_all_shape);
		defaultBackground = getBackground();
		setFocusableInTouchMode(true);
		setFocusable(true);
		setEnabled(true);
	}

	public MeetingBarView(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	public boolean HasConflict(int start, int end){
		if ((mStart <= start)  && (mEnd >= start))
			return true;
		if ((mStart <= end )  && (mEnd >= end))
			return true;
		if ((mStart >= start) && (mEnd <= end))
			return true;
		return false;
	}
	public int getStart() {
		return mStart;
	}
	public int getEnd() {
		return mEnd;
	}
	public void setPeriod(int start, int end) {
		mStart = start;
		if (start > 2400 || start < 0)
			throw new IllegalArgumentException ("Invalid parameter");
		if (end > 2400 || end < 0)
			throw new IllegalArgumentException ("Invalid parameter");
		if (start > end)
			throw new IllegalArgumentException ("Invalid parameter");
		mStart = start;
		mEnd = end;
	}
	public int getConflictLevel() {
		return mConflictLevel;
	}

	public void setConflictLevel(int conflicLevel) {
		mConflictLevel = conflicLevel;
		UpdateTickness();
	}
	public void IncrementConflictLevel(){
		mConflictLevel++;
		UpdateTickness();
	}
	public void DecrementConflictLevel(){
		if(mConflictLevel>0)
			mConflictLevel--;
		UpdateTickness();
	}
	private void UpdateTickness(){
		if(mConflictLevel >=0)
			mTickRatio = 1/(1+mConflictLevel);
		//TODO change dimension of the window
	}

	public int getColumnPosition() {
		return mColumnPosition;
	}

	public void setColumnPosition(int position) {
		mColumnPosition = position;
	}
	public void ReDrawView(){
		int theWidth;
		int theHeight;
		theWidth = ((View)getParent()).getMeasuredWidth();
		theHeight = ((View)getParent()).getMeasuredHeight();
		
		mWidth = (int)(theWidth * mTickRatio) - mPaddingLeft - mPaddingRight;
		mHeight = theHeight * (mEnd-mStart)/daydimension;
		mPosX = mColumnPosition*mWidth;
		mPosY = theHeight * mStart/daydimension;		
		setLayoutParams(new AbsoluteLayout.LayoutParams(mWidth,mHeight,mPosX,mPosY));
	}
}