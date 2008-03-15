package com.welmo.meeting;

import java.util.Iterator;
import java.util.Vector;

import com.welmo.contacts.Attends;
import com.welmo.dbhelper.AgendaDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Meeting implements Parcelable {

	public static final int RESPONSE_OK 			= 1;
	public static final int RESPONSE_REFUSE 		= 2;
	public static final int NO_RESPONSE 			= 3;
	
	//Constant for SQL handling of Meetings
	public static final String MEETING_TABLE_CREATE_CONDITIONS = 
        "(UID INTEGER PRIMARY KEY," 
		+ " Object TEXT, Description TEXT, Duration INTEGER)";
	
	public static final long serialVersionUID = 1;
	
	private MeetingUID 	MeetingID 		= new MeetingUID();
	private String 		MtgObject		="";
	private String 		MtgDescription	="";
	private short 		Duration		=0;

	public static final String ATTENDS_TABLE_CREATE_CONDITIONS = 
        "(UID INTEGER," 
		+ " ID INTEGER, Name TEXT, Response INTEGER, PRIMARY KEY(UID,ID))";
	
	public Vector<Attends> attendlist = new Vector<Attends>(0);
	
	//------------------------------------------------------------------
	//Parcelable Handling
	//------------------------------------------------------------------
	
	/*public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>
	{
        public Meeting createFromParcel(Parcel in) {return new Meeting(in);}
        public Meeting[] newArray(int size) {return new Meeting[size];}
    };*/
    
    private Meeting(Parcel in) {
        readFromParcel(in);
    }
    public void writeToParcel(Parcel out){
        out.writeLong(MeetingID.UID);
        out.writeString(MtgObject);
        out.writeString(MtgDescription);
        out.writeInt(attendlist.size());
        if (attendlist.size()!=0){
        	Iterator<Attends> it = attendlist.listIterator();
        	while(it.hasNext()){
        		Object o = it.getClass(); 
        		Attends theAttend = (Attends)o; 
        		out.writeString(theAttend.ID);
        		out.writeString(theAttend.Name);
        		out.writeInt(theAttend.Response);
        	}		
        }
    }

    public void readFromParcel(Parcel in) {
    	attendlist.clear();
    	MeetingID.UID = in.readLong();
    	MtgObject = in.readString();
    	MtgDescription = in.readString();
    	int nAttends = in.readInt();
    	for (int index = 0; index < nAttends; index ++){
    		Attends theAttend = new Attends();
    		theAttend.ID = in.readString();
    		theAttend.Name = in.readString();
    		theAttend.Response = in.readInt();
    	}
    }
    
	//-------------------------------------------------------
	// Constructors
	//-------------------------------------------------------
	public Meeting(long DayID, 	short start_h, short start_m, short end_h, short end_m, 
								String mtgObject, String description) {
		super();
		MeetingID.UID = DayID;
		MtgObject = mtgObject;
		MtgDescription = description; 
		setTimeFrame(start_h, start_m, end_h, end_m);
	}
	public Meeting() {
		super();
	}
	public void CopyDBInfo(Meeting mtg)
	{
		
	}
	public Meeting(Meeting copy)
	{
		this.MtgDescription = new String(copy.MtgDescription);
		this.MtgObject = new String(copy.MtgObject); 
		this.MeetingID = new MeetingUID(copy.MeetingID);
		this.Duration = copy.Duration;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	
	//-------------------------------------------------------
	// Setter functions
	//-------------------------------------------------------
	public void setTimeFrame(short start_h, short start_m,short end_h, short end_m)
	{
		if((start_h > 23 || start_h <0)||(end_h > 23 || end_h <0 ))
			throw new IllegalArgumentException ("Invalid start or end hours parameter");
		if((start_m > 59 || start_m <0)||(end_m > 59 || end_m <0 ))
			throw new IllegalArgumentException ("Invalid start or end minutes parameter");
		int Start = (short)(start_h*100 + start_m);
		int End = (short)(end_h*100 + end_m);
		if(End < Start)
			throw new IllegalArgumentException ("Invalid end time parameter < than Start");
		MeetingID.setStartHour(start_h);
		MeetingID.setStartMin(start_m);
		MeetingID.setEndHour(end_h);
		MeetingID.setEndMin(end_m);
	}
	public void setStartTimeFrame(short start_h, short start_m)
	{
		if(start_h > 23 || start_h <0)
			throw new IllegalArgumentException ("Invalid start or end hours parameter");
		if(start_m > 59 || start_m <0)
			throw new IllegalArgumentException ("Invalid start or end minutes parameter");
		int Start = (short)(start_h*100 + start_m);
		int End = (short)(MeetingID.getEndHour()*100 + MeetingID.getEndMin());
		if(End < Start)
			throw new IllegalArgumentException ("Invalid end time parameter < than Start");
		MeetingID.setStartHour(start_h);
		MeetingID.setStartMin(start_m);
	}
	public void setEndTimeFrame(short end_h, short end_m)
	{
		if(end_h > 23 || end_h <0)
			throw new IllegalArgumentException ("Invalid start or end hours parameter");
		if(end_m > 59 || end_m <0)
			throw new IllegalArgumentException ("Invalid start or end minutes parameter");
		int Start = (short)(MeetingID.getStartHour()*100 + MeetingID.getStartMin());
		int End = (short)(end_h*100 + end_m);
		if(End < Start)
			throw new IllegalArgumentException ("Invalid end time parameter < than Start");
		MeetingID.setEndHour(end_h);
		MeetingID.setEndMin(end_m);
	}
	public void setObject(String Object) {this.MtgObject = Object;}
	public void setDescription(String Description) {this.MtgDescription = Description;}
	public void setMeetingID(MeetingUID mID) {this.MeetingID =  mID;}
	public void setMeetingIDDay(MeetingUID mIDDay){MeetingID.UID = (MeetingID.UID | (mIDDay.UID & MeetingUID.MASK_DATE));}
	public void setType(short m){ MeetingID.setType(m);}
	//-------------------------------------------------------
	// Special Setter functions
	//-------------------------------------------------------
	public void setAsFreeTime()
	{
		MeetingID.setType(MeetingUID.TYPE_FREE_TYME);
		MtgObject = "Free Time";
		MtgDescription = "...";
		attendlist.clear();
	}
	//-------------------------------------------------------
	// Getter functions
	//-------------------------------------------------------
	public short getEnd_h(){return MeetingID.getEndHour();}
	public short getEnd_m(){return MeetingID.getEndMin();}
	public short getStart_h(){return MeetingID.getStartHour();}
	public short getStart_m(){return MeetingID.getStartMin();}
	public MeetingUID  getMeetingID() {return MeetingID;}
	public String getObject() {return MtgObject;}
	public String getDescription() {return MtgDescription;}
	public short getType(){return MeetingID.getType();}
	public int getDurationInM()
	{
		int duration = 60*(getEnd_h() - getStart_h()) + getEnd_m() - getStart_m();
		return duration;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	private ContentValues getCntMeetingDescription()
	{
		ContentValues theContent = new ContentValues();
		theContent.put("UID",MeetingID.UID);
		theContent.put("Object",MtgObject);
		theContent.put("Description",MtgDescription);
		theContent.put("Duration",Duration);
		return theContent;
	}
	private ContentValues getCntMeetingAttnds(Attends theAttend)
	{
		ContentValues theContent = new ContentValues();
		theContent.put("UID",MeetingID.UID);
		theContent.put("ID",theAttend.ID);
		theContent.put("Name",theAttend.Name);
		theContent.put("Response",theAttend.Response);
		return theContent;
	}
	public void UpdateToDatabase(AgendaDBHelper db)
	{
		ContentValues content;
		long nRows;
		
		//Update Meeting description
		content = this.getCntMeetingDescription();
		nRows= db.updateMeetingsRowByID(MeetingID.UID, content);
		if (nRows == 0L)
			nRows = db.createMeetingsRow(content);
		
		//Update attend list 
		nRows = db.deleteAttendsRowByWhere("UID="+MeetingID.UID);
		if (attendlist.size()!=0){
	        Iterator it = attendlist.listIterator();
	        while(it.hasNext()){
	        		Object o = it.next(); 
	        		Attends theAttend = (Attends)o; 
	        		content = this.getCntMeetingAttnds(theAttend);
	        		nRows = db.createAttendsRow(content);
	        	}		
	        }
	}
	public void RestoreFromDatabase (AgendaDBHelper db)
	{
		String[] columns;
		Cursor cur;
		
		columns = new String[]{"UID","Object","Description","Duration"};
		cur = db.fetchMeetingsRowsByID(MeetingID.UID,columns);
		if(cur != null)
		{
			cur.first();    
			this.MtgObject = cur.getString(1);
			this.MtgDescription = cur.getString(2);
			this.Duration = cur.getShort(3);	
			//free(columns);
			attendlist.clear();
			columns = new String[]{"UID","ID","Name","Response"};
			cur = db.fetchAttendsRowsByWhere("UID="+MeetingID.UID,columns);
			if(cur != null)
			{
				cur.first();
				while(!cur.isAfterLast()){
				    Attends theAttend = new Attends();
				    theAttend.ID = cur.getString(1);
				    theAttend.Name = cur.getString(2);
				    theAttend.Response = cur.getInt(3);
				    attendlist.add(theAttend);
				    cur.next();   
				}
			}
		}
		else{
			attendlist.clear();
			MtgObject = "";
			MtgDescription = "";
			Duration = 0;	
			attendlist.clear();
		}
	}
	public void DeleteFromDatabase(AgendaDBHelper db)
	{
			db.deleteMeetingsRowByID(MeetingID.UID);
			db.deleteAttendsRowByWhere("UID="+MeetingID.UID);
	}
}

