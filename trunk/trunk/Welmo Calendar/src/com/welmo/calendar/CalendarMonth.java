package com.welmo.calendar;

import java.util.Map;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class CalendarMonth extends TableLayout{

	public CalendarMonth(Context context, AttributeSet attrs, Map inflateParams) {
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

	public CalendarMonth(Context context) {
		this(context,null,null);
		// TODO Auto-generated constructor stub
	}

	void ShowMessge(String Msg){
        Toast.makeText(mContext,Msg,Toast.LENGTH_SHORT).show();
	}
	    
	/* (non-Javadoc)
	 * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
	}

	/* (non-Javadoc)
	 * @see android.view.View#setOnFocusChangeListener(android.view.View.OnFocusChangeListener)
	 */
	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		// TODO Auto-generated method stub
		super.setOnFocusChangeListener(l);
	}

	/* (non-Javadoc)
	 * @see android.view.View#setOnLongClickListener(android.view.View.OnLongClickListener)
	 */
	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		// TODO Auto-generated method stub
		super.setOnLongClickListener(l);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onFocusChanged(boolean, int, android.graphics.Rect)
	 */
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
}
