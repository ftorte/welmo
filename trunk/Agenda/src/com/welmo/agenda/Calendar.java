package com.welmo.agenda;

import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.*;
import java.io.*;

import com.tft.myoffice.meeting.Meeting;

public class Calendar implements Serializable{
	public static final long serialVersionUID = 1;

	// hash map containing the month of the agenda
	// a month is present if in the month at least a day
	// has a meeting
	private Map WeekList  = new HashMap();
	
	//private static final long serialVersionUID = ;

	public Map getWeekList() {
		return WeekList;
	}

	public void setWeekList(Map monthList) {
		this.WeekList = monthList;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}	
	
	// Custom Interface of Calendar
	// Retun all miting on one day
	/**
	 * return the list of meeting planned fo a given day
	 * @author ebbp178
	 * 
	 * @param day contains the day on we want the list of meeting*
	 * 
	 */
	void GetDayMeetings(int day){}
	/**
	 * return the list of meeting planned for a given week
	 * @author ebbp178
	 * 
	 * @param week contains the day on we want the list of meeting*
	 * 
	 */
	void GetWeekMitingList(int year, int month, int day){}
	void InsertMeeting(int yaer, int month, int day,Meeting meeting)
	{
		//TO DO test if week still empty  in the calendar. In this case
		// the insert must create a new week
		///if ()
		long weekUID=0;
		if(!WeekList.containsKey(weekUID))
		{
			//Week theWeek = new Week();
			//WeekList.put(weekUID,theWeek);
		}
		//Week theWeek = (Week) WeekList.get(weekUID);
	}
	void DelMeeting(int mUID){}
}
