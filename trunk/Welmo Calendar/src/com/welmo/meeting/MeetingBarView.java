package com.welmo.meeting;

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
	private int mParentWidth =0;
	private int	mParentHeigth =0;
	private int MaxLentgthDescription=20;
	
	private static int daydimension = 24*60;
    
	private int mStart=0;
    private int mEnd=0;
    private int mStart_h=0;
    private int mStart_m=0;
    private int mEnd_h=0;
    private int mEnd_m=0;
    private String mSubject="";
    private String mDescription="";
    
    private int mConflictLevel=0;
    private int mColumnPosition=0;
    private float mTickRatio = 1;
   
	String mMsgDescription = "";
	
	public MeetingBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(new OnClickListener (){ 
			@Override
			public void onClick(View arg0) {
				boolean getfocus = arg0.requestFocus();
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener (){
			@Override
			public void onFocusChange(View v, boolean b){
				//ShowMessge("Calendar Day On Focus Catched: "+id);
				if(b){
					v.setBackgroundDrawable(focusBackground);
					ShowMessge(mMsgDescription);	
				}
				else{
					v.setBackgroundDrawable(defaultBackground);
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
		setBackgroundResource(R.drawable.mtg_bar_small);
		focusBackground = getBackground();
		setBackgroundResource(R.drawable.mtg_bar_focus);
		defaultBackground = getBackground();
		setFocusableInTouchMode(true);
		setFocusable(true);
		setEnabled(true);
	}

	public MeetingBarView(Context context, int w, int h) {
		this(context,null);
		mParentWidth = w;
		mParentHeigth = h;
		// TODO Auto-generated constructor stub
	}
	public MeetingBarView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	void ShowMessge(String Msg){
        Toast.makeText(this.getContext(),Msg,Toast.LENGTH_SHORT).show();
	}
	public boolean HasConflict(int start_h, int start_m, int end_h,int end_m){
		
		if(!IsValidTimeFrame(start_h, start_m, end_h,end_m))
			throw new IllegalArgumentException ("Invalid parameter");

		int start 	= start_h*60 + start_m;
		int end 	= end_h*60 + end_m;
		
		if ((mStart < start)  && (mEnd > start))
			return true;
		if ((mStart < end )  && (mEnd > end))
			return true;
		if ((mStart > start) && (mEnd < end))
			return true;
		return false;
	}
	public boolean HasConflict(MeetingBarView mb){
		return HasConflict(mb.getStart_h(),mb.getStart_m(),mb.getEnd_h(),mb.getEnd_h());
	}
	public void setPeriod(int start_h, int start_m, int end_h,int end_m){
		if(!IsValidTimeFrame(start_h, start_m, end_h,end_m))
			throw new IllegalArgumentException ("Invalid parameter");
		mStart_h 	= start_h;
		mStart_m 	= start_m;
		mStart 		= start_h*60 + start_m;
		mEnd_h 		= end_h;
		mEnd_m 		= end_m;
		mEnd 		= end_h*60 + end_m;
		UpdateDescription();
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
			mTickRatio = (float)(1/(1+(float)mConflictLevel));
		ReDrawView();
		//TODO change dimension of the window
	}

	public int getColumnPosition() {
		return mColumnPosition;
	}

	public void setColumnPosition(int position) {
		mColumnPosition = position;
		ReDrawView();
	}
	public int getStart_h() {
		return mStart_h;
	}
	public int getStart_m() {
		return mStart_m;
	}
	public int getEnd_h() {
		return mEnd_h;
	}
	public int getEnd_m() {
		return mEnd_m;
	}
	//Private methods
	private void UpdateDescription(){
		mMsgDescription = 	"["+mStart_h+":"+mStart_m+"] To ["+mEnd_h+":"+mEnd_m+"]";
		if(mSubject.length()<=MaxLentgthDescription){
			mMsgDescription = mMsgDescription+"\nSb: "+mSubject;
		}
		else{
			mMsgDescription = mMsgDescription+"\nSb: "+mSubject.substring(0, MaxLentgthDescription)+"...";
		}
		if(mDescription.length()<=MaxLentgthDescription){
			mMsgDescription = mMsgDescription+"\nDs: "+mDescription;
		}
		else{
			mMsgDescription = mMsgDescription+"\nDs: "+mDescription.substring(0, MaxLentgthDescription)+"...";
		}
	}
	private boolean IsValidTimeFrame(int start_h, int start_m, int end_h,int end_m){
		if (start_h > 24 || start_h < 0 || end_h > 24 || end_h < 0 ||
				start_m > 59 || start_m < 0 || end_m > 59 || end_m < 0)
			return false;
		int start 	= start_h*60 + start_m;
		int end 	= end_h*60 + end_m;
		if (start > end)
			return false;
		return true;
	}
	public void ReDrawView(){
		mWidth = (int)(mParentWidth * mTickRatio);
		mHeight = mParentHeigth * (mEnd-mStart)/daydimension;
		mPosX = mColumnPosition*(mWidth) ;
		mPosY = mParentHeigth * mStart/daydimension;		
		setLayoutParams(new AbsoluteLayout.LayoutParams(mWidth,mHeight,mPosX,mPosY));
	}
	public void setSubject(String subject) {
		mSubject = subject;
		UpdateDescription();
	}
	public String getDescription() {
		UpdateDescription();
		return mDescription;
	}
	public String getSubject() {
		return mSubject;
	}
	public void setDescription(String description) {
		mDescription = description;
		UpdateDescription();
	}
}