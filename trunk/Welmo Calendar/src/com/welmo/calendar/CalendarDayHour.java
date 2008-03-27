package com.welmo.calendar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.welmo.dbhelper.AgendaDBHelper;
import com.welmo.meeting.Meeting;
import com.welmo.meeting.MeetingDay;
import com.welmo.meeting.MeetingUID;
public class CalendarDayHour extends AbsoluteLayout{

	
	
	Vector<MeetingBarView> 	mMeetings 	= new Vector<MeetingBarView>();	
	protected int 			mWidth		=0;
	protected int			mHeigth		=0;

	private MeetingDay 			theDay		= null;
	private Context 			mContext	= null;
	private AgendaDBHelper		dbAgenda 	= null;
	
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
		
		//connect with the adapter
		dbAgenda 	= new AgendaDBHelper(context,"Agenda","Meetings","Attends");
		setDay(2008,3,16);
	}
	public CalendarDayHour(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}

	public void Update(){
		Iterator<MeetingBarView> it = mMeetings.iterator();
		while(it.hasNext()){
			UpdateMeetingBarConflict(it.next());
		}
	}
	public void UpdateMeetingBarConflict(MeetingBarView mb){
		int position = 0;
		int occupationMap = 0;
		int MaxOccupationPos = 0;
		Vector<MeetingBarView> coflicts = new Vector<MeetingBarView>();
		//---------------------------------------------------
		//exams all meeting and detect conflicts
		Iterator<MeetingBarView> it = mMeetings.iterator();
		while(it.hasNext()){
			MeetingBarView MeetBar = it.next();
			if(MeetBar.HasConflict(mb)){
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
		mb.setConflictLevel(MaxOccupationPos);
		mb.setColumnPosition(position);
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
	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	public void setDay(int year, int month, int day)
	{
		theDay = new MeetingDay(year, month, day,dbAgenda);	
		if (theDay != null){
			theDay.RestoreFromDatabase();
		}
		
		//--------------------------------
		mMeetings.clear();
		this.removeAllViews();
		
		MeetingUID noFreeTimeMask = new MeetingUID();
		noFreeTimeMask.setType(MeetingUID.TYPE_FREE_TYME);
		
		ArrayList<MeetingUID> keys = theDay.GetMeetingsUIDs(noFreeTimeMask);
		Iterator<MeetingUID> it = keys.iterator();
		while(it.hasNext())
		{	
			Meeting mtg = theDay.getMeeting(it.next());
			MeetingBarView newMeetingBar = new MeetingBarView(mContext,mWidth,mHeigth);
			newMeetingBar.setPeriod(mtg.getStart_h(),mtg.getStart_m(),mtg.getEnd_h(),mtg.getEnd_m());
			UpdateMeetingBarConflict(newMeetingBar);
			mMeetings.add(newMeetingBar);
			addView(newMeetingBar);
		}
	}
}