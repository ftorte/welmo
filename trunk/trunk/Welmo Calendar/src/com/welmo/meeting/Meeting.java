package com.welmo.meeting;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.welmo.contacts.Attends;
import com.welmo.dbhelper.AgendaDBHelper;

public class Meeting implements Parcelable, Serializable {

	public static final int RESPONSE_OK 			= 1;
	public static final int RESPONSE_REFUSE 		= 2;
	public static final int NO_RESPONSE 			= 3;
	
	//Constant for SQL handling of Meetings
	public static final String MEETING_TABLE_CREATE_CONDITIONS = 
        "(UID INTEGER PRIMARY KEY," 
		+ " Object TEXT, Description TEXT, Owner TEXT, Duration INTEGER, Timestamp INTEGER)";
	
	public static final long serialVersionUID = 1;
	
	private MeetingUID 	MeetingID 		= new MeetingUID();
	private String 		MtgObject		="";
	private String 		MtgDescription	="";
	private String 		Owner			="";
	private long 		Timestamp		=0L;
	private short 		Duration		=0;

	public static final String ATTENDS_TABLE_CREATE_CONDITIONS = 
        "(UID INTEGER," 
		+ " ID INTEGER, Name TEXT, Response INTEGER, Message TEXT, isMe INTEGER, PRIMARY KEY(UID,ID))";
	private final static String LOG_TAG = "Meeting";
	public Vector<Attends> attendlist = new Vector<Attends>(0);
	
	public static final String CLSID = "00a6e802-b8b8-488f-8e29-db69bc0ba6fc";
	//------------------------------------------------------------------
	//Parcelable Handling
	//------------------------------------------------------------------
	
	/*public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>
	{
        public Meeting createFromParcel(Parcel in) {return new Meeting(in);}
        public Meeting[] newArray(int size) {return new Meeting[size];}
    };*/
    

    public void writeToParcel(Parcel out, int flag){
        out.writeLong(MeetingID.UID);
        out.writeString(MtgObject);
        out.writeString(MtgDescription);
        out.writeString(Owner);
        out.writeLong(Timestamp);
        out.writeInt(attendlist.size());
        if (attendlist.size()!=0){
        	Iterator<Attends> it = attendlist.listIterator();
        	while(it.hasNext()){
        		Object o = it.getClass(); 
        		Attends theAttend = (Attends)o; 
        		out.writeString(theAttend.ID);
        		out.writeString(theAttend.Name);
        		out.writeInt(theAttend.Response);
        		out.writeString(theAttend.Message);
        		out.writeInt(theAttend.isMe);
        	}		
        }
    }
    public int describeContents() {
    	return 0;
    }


    public void readFromParcel(Parcel in) {
    	attendlist.clear();
    	MeetingID.UID = in.readLong();
    	MtgObject = in.readString();
    	MtgDescription = in.readString();
    	Owner = in.readString();
    	Timestamp = in.readLong();
    	int nAttends = in.readInt();
    	for (int index = 0; index < nAttends; index ++){
    		Attends theAttend = new Attends();
    		theAttend.ID = in.readString();
    		theAttend.Name = in.readString();
    		theAttend.Response = in.readInt();
    		theAttend.Message = in.readString();
    		theAttend.isMe = in.readInt();
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
		this.Owner	= copy.getOwner();
		this.Timestamp	= copy.getTimestamp();
	}
	@Override
	public boolean equals(Object o) {
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
	public void setStartTimeFrame(short start_h, short start_m, boolean fixed)
	{
		int DurationMin = MeetingID.getDurationInMinutes();
		if(start_h > 23 || start_h <0)
			throw new IllegalArgumentException ("Invalid start or end hours parameter");
		if(start_m > 59 || start_m <0)
			throw new IllegalArgumentException ("Invalid start or end minutes parameter");
		//if fixed set end equal to start + duration in minutes up to 23:59
		int End =0;
		if(fixed)
			End = java.lang.Math.min(1439, start_h*60 + start_m + DurationMin);
		else
			End = java.lang.Math.max(start_h*60 + start_m+1, 
					MeetingID.getEndHour()*60 +MeetingID.getEndMin());
		MeetingID.setStartHour(start_h);
		MeetingID.setStartMin(start_m);
		MeetingID.setEndHour((short)(End/60));
		MeetingID.setEndMin((short)(End %60));
		
	}
	public void setEndTimeFrame(short end_h, short end_m, boolean fixed)
	{
		int DurationMin = java.lang.Math.max(1,MeetingID.getDurationInMinutes());
		if(end_h > 23 || end_h <0)
			throw new IllegalArgumentException ("Invalid start or end hours parameter");
		if(end_m > 59 || end_m <0)
			throw new IllegalArgumentException ("Invalid start or end minutes parameter");
		int Start =0;
		if(fixed)
			Start = java.lang.Math.max(0, end_h*60 + end_m - DurationMin);
		else
			Start = java.lang.Math.min(end_h*60 + end_m-1, 
					MeetingID.getEndHour()*60 +MeetingID.getEndMin());
		
		MeetingID.setStartHour((short)(Start/60));
		MeetingID.setStartMin((short)(Start %60));
		MeetingID.setEndHour(end_h);
		MeetingID.setEndMin(end_m);
	}
	public void setObject(String Object) {this.MtgObject = Object;}
	public void setDescription(String Description) {this.MtgDescription = Description;}
	public void setMeetingID(MeetingUID mID) {this.MeetingID.UID =  mID.UID;}
	public void setMeetingIDDay(MeetingUID mIDDay){
		MeetingID.UID = ((MeetingID.UID&MeetingUID.MASK_TIMETYPE) | (mIDDay.UID & MeetingUID.MASK_DATE));
	}
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
		String str = "";
		str = str + MeetingID.UID;
		str = str + "\t" + (MtgObject.replace("\t"," ")).toString();
		str = str + "\t" + (MtgDescription.replace("\t"," ")).toString();
		str = str + "\t" + (Owner.replace("\t"," ")).toString();
		str = str + "\t" + Timestamp;
		if (attendlist.size()!=0){
         	Iterator<Attends> it = attendlist.listIterator();
         	str = str + "\t" + attendlist.size();
         	while(it.hasNext()){ 
        		Attends theAttend = it.next(); 
        		str = str + ","+ theAttend.ID;
         		str = str +"," + theAttend.Name.replace(","," ").toString();
         	}		
         }
		return str;
	}
	public void fromString(String str) {
		try{
			String[] tokens = str.split("\t");
			Log.i("Meeting", "[fromString] n tokens  =" + tokens.length);
			this.Clear();
			for(int index=0; index < tokens.length; index++){
				switch(index){
				case 0:
					Log.i("Meeting", "[fromString] case 0=" + tokens[0]);
					MeetingID.UID = Long.parseLong(tokens[0]);
					break;
				case 1:
					Log.i("Meeting", "[fromString] case 1=" + tokens[1]);
					this.MtgObject = tokens[1];
					break;	
				case 2:
					Log.i("Meeting", "[fromString] case 2=" + tokens[2]);
					this.MtgDescription = tokens[2];
					break;
				case 3:
					Log.i("Meeting", "[fromString] case 3=" + tokens[3]);
					this.Owner = tokens[3];
					break;
				case 4:
					Log.i("Meeting", "[fromString] case 4=" + tokens[4]);
					this.Timestamp = Long.parseLong(tokens[4]);
					break;
				case 5:
					String[] tokensII = tokens[5].split(",");
					int nAttends = (int)Long.parseLong(tokensII[0]);
					Log.i("Meeting", "[fromString] Nattend =" + nAttends);
					for (int attentIndex = 0; attentIndex < nAttends; attentIndex++){
						Attends theAttend = new Attends();
						theAttend.ID = tokensII[attentIndex*2+1];
						theAttend.Name = tokensII[attentIndex*2+2];
						theAttend.Response = Meeting.NO_RESPONSE;
						attendlist.add(theAttend);
						Log.i("Meeting", "[fromString] attend["+ theAttend.ID  +"]"+  theAttend.Name);
					}
					break;
				default:
					break;
				}	
				
			}
		}
		catch(IndexOutOfBoundsException indexEx){
			Log.e(LOG_TAG, "IndexOutOfBoundsException"+ indexEx.getMessage());
		}
		catch(NumberFormatException NbEx){
			Log.e(LOG_TAG,"NumberFormatException"+ NbEx.getMessage());
		}
	}
	
	private ContentValues getCntMeetingDescription()
	{
		ContentValues theContent = new ContentValues();
		theContent.put("UID",MeetingID.UID);
		theContent.put("Object",MtgObject);
		theContent.put("Description",MtgDescription);
		theContent.put("Owner",Owner);
		theContent.put("Timestamp",Timestamp);
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
		theContent.put("Message",theAttend.Message);
		theContent.put("isMe",theAttend.isMe);
		return theContent;
	}
	public void UpdateToDatabase(AgendaDBHelper db)
	{
		Log.i(LOG_TAG, "[UpdateToDatabase] entry function");   
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
			Log.i(LOG_TAG, "[UpdateToDatabase] update attends:" + attendlist.size());
	        Iterator<Attends> it = attendlist.listIterator();
	        while(it.hasNext()){
	        		Object o = it.next(); 
	        		Attends theAttend = (Attends)o; 
	        		content = this.getCntMeetingAttnds(theAttend);
	        		nRows = db.createAttendsRow(content);
	        	}		
	        }
	}
	public boolean RestoreFromDatabase (AgendaDBHelper db)
	{
		String[] columns;
		Cursor cur;
		
		columns = new String[]{"UID","Object","Description","Owner", "Duration", "Timestamp"};
		cur = db.fetchMeetingsRowsByID(MeetingID.UID,columns);
		if(cur != null)
		{
			cur.moveToFirst();    
			this.MtgObject = cur.getString(1);
			this.MtgDescription = cur.getString(2);
			this.Owner = cur.getString(3);
			this.Duration = cur.getShort(4);
			this.Timestamp = cur.getLong(5);
			cur.close();
			//free(columns);
			attendlist.clear();
			columns = new String[]{"UID","ID","Name","Response","Message","isMe"};
			cur = db.fetchAttendsRowsByWhere("UID="+MeetingID.UID,columns);
			if(cur != null)
			{
				cur.moveToFirst();
				while(!cur.isAfterLast()){
				    Attends theAttend = new Attends();
				    theAttend.ID = cur.getString(1);
				    theAttend.Name = cur.getString(2);
				    theAttend.Response = cur.getInt(3);
				    theAttend.Message = cur.getString(4);
				    theAttend.isMe = cur.getInt(5);
				    attendlist.add(theAttend);
				    cur.moveToNext();
				}
				cur.close();
			}
			return true;
		}
		else{
			attendlist.clear();
			MtgObject = "";
			MtgDescription = "";
			Owner = "";
			Timestamp=0L;
			Duration = 0;	
			attendlist.clear();
			return false;
		}	
	}
	public void DeleteFromDatabase(AgendaDBHelper db)
	{
			db.deleteMeetingsRowByID(MeetingID.UID);
			db.deleteAttendsRowByWhere("UID="+MeetingID.UID);
	}
	public String getOwner() {
		return Owner;
	}
	public void setOwner(String from) {
		this.Owner = from;
	}
	public long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}
	public void Clear(){
		this.attendlist.clear();
		this.Duration=0;
		this.MeetingID.UID=0L;
		this.MtgDescription="";
		this.MtgObject="";
		this.Owner="";
		this.Timestamp=0L;
	}
	public Attends findAttend(String AttendName){
		Attends at = null;
		Iterator<Attends> it = attendlist.iterator();
		while (it.hasNext())
		{
			if((at=it.next()).ID.compareTo(AttendName)==0)
				return at;
		}
		return null;
	}
	public void setIsMe(String Attend){
		Attends at = null;
		Iterator<Attends> it = attendlist.iterator();
		while (it.hasNext())
		{
			if((at=it.next()).ID.compareTo(Attend)==0)
				at.isMe=1;
			else
				at.isMe=0;
		}		
	}
	public String whoIsMe(){
		Attends at = null;
		Iterator<Attends> it = attendlist.iterator();
		while (it.hasNext())
		{
			if((at=it.next()).isMe==1)
				return at.ID;
		}	
		return "";
	}
}

