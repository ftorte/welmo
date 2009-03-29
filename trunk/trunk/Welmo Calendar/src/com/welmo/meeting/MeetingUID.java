package com.welmo.meeting;

import java.util.Calendar;

public class MeetingUID implements Comparable<MeetingUID> {

	@Override
	public int compareTo(MeetingUID arg0) {
		// TODO Auto-generated method stub
		Long uid;
		int val=0;

		if(!(arg0 instanceof MeetingUID))
			throw new ClassCastException("Invalid type for MeetingUID"); 
		else
			uid = arg0.UID; 
		val = ((Long)UID).compareTo(uid);
		return val;
	}
	public static final int FIRST_YEAR					=1970; 
	public static final short TYPE_FREE_TYME	 		=0; 
	public static final short TYPE_WORING_MEETING		=1;
	public static final short TYPE_WORKING_PROJECT		=2;
	public static final short TYPE_PERSONAL_GENARIC		=4;
	public static final short TYPE_PERSONAL_LESURE		=8;

	// year 3 bytes
	public static final long MASK_DATE		=0xFFFFFFFF00000000L; 
	public static final long MASK_TIMETYPE	=0x00000000FFFFFFFFL; 
	public static final long MASK_YEAR		=0xFF00000000000000L; 
	public static final long MASK_MONTH		=0x00F0000000000000L;
	public static final long MASK_WEEK		=0x000FF00000000000L;
	public static final long MASK_DAY_NB 	=0x00000FF000000000L;
	public static final long MASK_DAY_OW 	=0x0000000F00000000L;
	
	public static final long MASK_START_HOUR =0x0000000FC000000L;
	public static final long MASK_START_MIN =0x0000000003F00000L;
	public static final long MASK_END_HOUR 	=0x00000000000FC000L;
	public static final long MASK_END_MIN 	=0x0000000000003F00L;
	public static final long MASK_TYPE  	=0x00000000000000F0L;
	public static final long MASK_COUNT 	=0x000000000000000FL;
	
	public static final int MASK_YEAR_POS 			= 56;
	public static final int MASK_MONTH_POS 			= 52;
	public static final int MASK_WEEK_POS 			= 44;
	public static final int MASK_DAY_NB_POS 		= 36;
	public static final int MASK_DAY_OW_POS 		= 32;
	
	public static final int MASK_START_HOUR_POS 	= 26;
	public static final int MASK_START_MIN_POS 		= 20;
	public static final int MASK_END_HOUR_POS 		= 14;
	public static final int MASK_END_MIN_POS 		= 8;
	public static final int MASK_TYPE_POS 			= 4;
	public static final int MASK_COUNT_POS 			= 0;
	
	public long UID;
	
	//**************************************************************
	// Constructors
	//**************************************************************
	public MeetingUID() {
		UID=0x0;
	}
	public MeetingUID(long uid) {
		UID=uid;
	}
	public MeetingUID(MeetingUID copy) {
		UID=copy.UID;
	}
	
	//**************************************************************
	// getter functions
	//**************************************************************
	public short getYear()		{return (short)(FIRST_YEAR +((UID & MASK_YEAR) >>> MASK_YEAR_POS));}
	public short getMonth()		{return (short)((UID & MASK_MONTH) >>> MASK_MONTH_POS);}
	public short getWeek()		{return (short)((UID & MASK_WEEK) >>> MASK_WEEK_POS);}
	public short getDayOW()		{return (short)((UID & MASK_DAY_OW) >>> MASK_DAY_OW_POS);}
	public short getDayNB()		{return (short)((UID & MASK_DAY_NB) >>> MASK_DAY_NB_POS);}
	public short getStartHour()	{return (short)((UID & MASK_START_HOUR) >>> MASK_START_HOUR_POS);}
	public short getStartMin()	{return (short)((UID & MASK_START_MIN) >>> MASK_START_MIN_POS);}
	public short getEndHour()	{return (short)((UID & MASK_END_HOUR) >>> MASK_END_HOUR_POS);}
	public short getEndMin()	{return (short)((UID & MASK_END_MIN) >>> MASK_END_MIN_POS);}
	public short getCount()		{return (short)((UID & MASK_COUNT) >>> MASK_COUNT_POS);}
	public short getType()		{return (short)((UID & MASK_TYPE) >>> MASK_TYPE_POS);}
	public int getStart()		{return (short)((UID & (MASK_START_HOUR | MASK_START_MIN)) >>> MASK_START_MIN_POS);}
	public int getEnd()			{return (short)((UID & (MASK_END_HOUR | MASK_END_MIN)) >>> MASK_END_MIN_POS);}
	public long getMinUID()		{return (long) (UID & (~MASK_COUNT));}
	public long getMaxUID()		{return (long) (UID | (MASK_COUNT));}
	//**************************************************************
	// print to string function
	//**************************************************************
	public String toStringStartTime(){
			return Integer.toString(getStartHour())+"h"+Integer.toString(getStartMin());
	}
	public String toStringEndTime(){
		return Integer.toString(getEndHour())+"h"+Integer.toString(getEndMin());
	}
	public String toStringDuration(){
		return Integer.toString(getEndHour() - getStartHour()) + "h" + 
			Integer.toString(getEndMin() - getStartMin());
	}
	//**************************************************************
	// Setter functions
	//**************************************************************
	public void setYear(short y)		{UID = (UID & ~MASK_YEAR)  |((((long)y) << MASK_YEAR_POS) & MASK_YEAR);}
	public void setMonth(short m)		{UID = (UID & ~MASK_MONTH) |((((long)m) << MASK_MONTH_POS) & MASK_MONTH);}
	public void setWeek(short w)		{UID = (UID & ~MASK_WEEK)  |((((long)w) << MASK_WEEK_POS) & MASK_WEEK);}
	public void setDayOW(short d)		{UID = (UID & ~MASK_DAY_OW)|((((long)d) << MASK_DAY_OW_POS) & MASK_DAY_OW);}
	public void setDayNB(short d)		{UID = (UID & ~MASK_DAY_NB)|((((long)d) << MASK_DAY_NB_POS) & MASK_DAY_NB);}
	public void setStartHour(short h)	{UID = (UID & ~MASK_START_HOUR)  |((((long)h) << MASK_START_HOUR_POS) & MASK_START_HOUR);}
	public void setStartMin(short m)	{UID = (UID & ~MASK_START_MIN)   |((((long)m) << MASK_START_MIN_POS) & MASK_START_MIN);}
	public void setEndHour(short h)		{UID = (UID & ~MASK_END_HOUR)  |((((long)h) << MASK_END_HOUR_POS) & MASK_END_HOUR);}
	public void setEndMin(short m)		{UID = (UID & ~MASK_END_MIN)   |((((long)m) << MASK_END_MIN_POS) & MASK_END_MIN);}
	public void setCount(short m)		{UID = (UID & ~MASK_COUNT)   |((((long)m) << MASK_COUNT_POS) & MASK_COUNT);}
	public void setType(short m)		{UID = (UID & ~MASK_TYPE)   |((((long)m) << MASK_TYPE_POS) & MASK_TYPE);}
	
	
	public void setUID (short year, short month, short day)
	{
		if (month > 12 || month < 0)
			throw new IllegalArgumentException ("Invalid month parameter");
		if (year > FIRST_YEAR+255 || year < FIRST_YEAR)
			throw new IllegalArgumentException ("Invalid year parameter");
		if (day > 31 || day < 1)
			throw new IllegalArgumentException ("Invalid day parameter");
	
		setYear((short)(year-FIRST_YEAR));
		setMonth(month);
		//calculate the week
		Calendar cl = Calendar.getInstance();
		cl.set(year, month-1, day);
		// set ISO standard calendar
		cl.setFirstDayOfWeek(Calendar.MONDAY);
		cl.setMinimalDaysInFirstWeek(4);
		setWeek((short)cl.get(Calendar.WEEK_OF_YEAR));
		setDayOW((short)cl.get(Calendar.DAY_OF_WEEK));
		setDayNB((short)day);
	}
	
	public boolean isSameDate(MeetingUID theUID)
	{
		
		if ((theUID.UID & MASK_DATE) == (UID & MASK_DATE))
			return true;
		else
			return false;
	}
	public CharSequence getMyDate(){

		Calendar cl = Calendar.getInstance();
		cl.set(this.getYear(),this.getMonth()-1,this.getDayNB());
		cl.setFirstDayOfWeek(Calendar.MONDAY);
		cl.setMinimalDaysInFirstWeek(4);
		
		java.util.Date now = cl.getTime(); // <-- think same as 
		System.currentTimeMillis();
		java.text.DateFormat dateFormat = 
			java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT); 
		String dateString = dateFormat.format(now); 
		return dateString;	 
	}
	public void addNbOfDay(int nDay){
		Calendar cl = Calendar.getInstance();
		cl.set(this.getYear(),this.getMonth()-1,this.getDayNB());
		cl.add(Calendar.DAY_OF_MONTH, nDay);
		this.setUID((short)cl.get(Calendar.YEAR),
				(short)(cl.get(Calendar.MONTH)+1),
				(short)cl.get(Calendar.DAY_OF_MONTH));
	}
	public int getDurationInMinutes(){
		int duration = (getEndHour() - getStartHour())*60 +  (getEndMin() - getStartMin());
		return duration;
	}
	public boolean isOfType(short types){
			if((((UID & MASK_TYPE) >>> MASK_TYPE_POS)& types) != 0)
				return true;
			return false;
	}
}
