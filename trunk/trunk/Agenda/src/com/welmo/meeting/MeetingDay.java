/**
 * {@docRoot}
 *  This class contains and handle the meeting lis of one day
 */
package com.welmo.meeting;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import com.tft.myoffice.dbhelper.AgendaDBHelper;

/**
 * @author ebbp178
 * Class containing a day of the agenda
 */
public class MeetingDay implements Serializable {
	public static final long serialVersionUID = 1;
	
	// ----------------------------------------------------------------
	// constant for day occupation management
	// Quarter management constants
	public static final byte MASK_1THQ		=0x1;
	public static final byte MASK_2NDQ		=0x2;
	public static final byte MASK_3THQ		=0x2;
	public static final byte MASK_4THQ		=0x4;
	// Hours management constants
	public static final byte ONEHOUR_SHIFT	=4; // (5-4)*4
	public static final byte START_BEGDAY	=0; // (4-4)*4
	public static final byte END_BEGDAY		=3; // (19-4)*4=15*4=60
	public static final byte START_MIDDAY	=4; // (4-4)*4
	public static final byte END_MIDDAY		=19; // (19-4)*4=15*4=60
	public static final byte START_ENDDAY	=20; // (4-4)*4
	public static final byte END_ENDDAY		=23; // (19-4)*4=15*4=60
	// ----------------------------------------------------------------

	
	//private variables for day occupation Management
	public class Occupation extends Object {
		private int dayBeg	=0x0;
		private int dayMid	=0x0;
		private int dayEnd	=0x0;		
	}
	private MeetingUID 			dayUID 				= new MeetingUID();
	private Occupation 			dayOccupation 		= new Occupation();
	private Occupation[] 		dayConflicts 		= new Occupation[5];
	private	SortedMap 			MeetingsList  		= new TreeMap();
	private AgendaDBHelper 		dbAgenda 			= null;
	   
	public MeetingDay(int year, int month , int day,AgendaDBHelper dbh) {
		super();
		dbAgenda = dbh;
		dayUID.setUID((short)year,(byte)month,(byte)day); 
		Meeting NullMeeting = new Meeting();
		NullMeeting.setMeetingIDDay(dayUID);
		NullMeeting.setObject("Null Meting");
		NullMeeting.setType(MeetingUID.TYPE_FREE_TYME);
		NullMeeting.setTimeFrame((short)0, (short)0, (short)23, (short)59);
		AddFreeTime(NullMeeting);
		for (int i=0; i < 5; i++)
			dayConflicts[i] = new Occupation();
	}
	
	public long getDayUID(){
		return  dayUID.UID;
	}
	
	public CharSequence getMyDate(){
		return  dayUID.getMyDate();
	}
	
	public void AddMeeting(Meeting theMeeting)
	{
		// SMT Start Meeting
		// SFT Start Free Time
		// EMT End Meeting Time
		// EFT End Free Time 
		Vector newMeetings = new Vector();
		long deletedKeys[] = new long [MeetingsList.size()];
		
		if (theMeeting.getMeetingID().getType() == MeetingUID.TYPE_FREE_TYME)
			throw new IllegalArgumentException ("Free tile cannot be create by the user");

		theMeeting.setMeetingIDDay(dayUID);
		int count=0;
		while(MeetingsList.containsKey(theMeeting.getMeetingID().UID))
		{
			count++;
			int newMeetingCount = theMeeting.getMeetingID().getCount()+1;
			theMeeting.getMeetingID().setCount((short)newMeetingCount);
			if(count > 3)
				throw new IllegalArgumentException ("UID already present: conflict with multiple ");
		}	
		int SMT,EMT,SFT,EFT;
		SMT = theMeeting.getMeetingID().getStart();
		EMT = theMeeting.getMeetingID().getEnd();
		
		count=0;
		LookForKey:	
		for (Object keyItem: MeetingsList.keySet()){
			long key = Long.parseLong(keyItem.toString());
			Meeting currMeeting = (Meeting) MeetingsList.get(key);
			if (currMeeting.getMeetingID().getType() == MeetingUID.TYPE_FREE_TYME){
				SFT = currMeeting.getMeetingID().getStart();
				EFT = currMeeting.getMeetingID().getEnd();
				if (SMT <= SFT){
					if (SFT <= EMT){
						if(EMT <= EFT){
							// change start free time equal to end meeting time SFT = EMT;
							// case 1
							currMeeting.setStartTimeFrame((short)(EMT >> 6),(short)(EMT & 0x3F));
							//if time frame is reduced to 0 deleted the meeting
							if(currMeeting.getDurationInM() < 1)
								deletedKeys[count++]=key;
						}
						else{
							// free time covered by the new meeting free time equal to end meeting time SFT = EMT;
							// case 2 delete the meeting
							deletedKeys[count++]=key;
						}
					}
					// case 3
					//else{ do nothing; }
				}
				else{
					if (SMT <= EFT){
						if(EMT<=EFT){
							//free time divided in two free times by the meeting
							// case 1
							Meeting newMeeting = new Meeting(currMeeting);
							currMeeting.setEndTimeFrame((short)(SMT >> 6),(short)(SMT & 0x3F));
							newMeeting.setStartTimeFrame((short)(EMT >> 6),(short)(EMT & 0x3F));
							// if new free time meeting is more than 0 M add it to the agenda
							if (newMeeting.getDurationInM()>0)
								newMeetings.add(newMeeting);
							// if new free time is less than 1 minute delete it from the agenda
							if (currMeeting.getDurationInM()<1)
								deletedKeys[count++]=key;
							// since we fond a free time that included the meeting we can exit and avoit
							// and exception because of the insertion of a meeting that change the set used in
							// the cycle for
							break LookForKey;
						}
						else{
							//free time divided in two free times by the meeting
							currMeeting.setEndTimeFrame((short)(SMT >> 6),(short)(SMT & 0x3F));
							// if new free time is less than 1 minute delete it from the agenda
							if (currMeeting.getDurationInM()<1)
								deletedKeys[count++]=key;
						}
					}
					//else{ do nothing; }
				}
			}
			
		}
		while (!newMeetings.isEmpty()){
			Meeting mtg = (Meeting)newMeetings.firstElement();
			MeetingsList.put(mtg.getMeetingID().UID,mtg);
			if(dbAgenda != null)
				mtg.UpdateToDatabase(dbAgenda);
			newMeetings.remove(0);

		}
		for (int i =0; i < count; i++)
			MeetingsList.remove(deletedKeys[i]);

		MeetingsList.put(theMeeting.getMeetingID().UID, theMeeting);
		//save/update the new meeting in the database
		if(dbAgenda != null)
			theMeeting.UpdateToDatabase(dbAgenda);

	}
	public void AddFreeTime(Meeting theMeeting)
	{
		int SMT,EMT,SFT,EFT;
		//,count;
		if (theMeeting.getMeetingID().getType() != MeetingUID.TYPE_FREE_TYME)
			throw new IllegalArgumentException ("A meeting cannot be added using this function");

		Vector newMeetings = new Vector();
		//long deletedKeys[] = new long [MeetingsList.size()];
		
		//count = 0;
		LookForKey:		
		for (Object keyItem: MeetingsList.keySet()){
			long key = Long.parseLong(keyItem.toString());
			Meeting currMeeting = (Meeting)MeetingsList.get(key);
			// read start and end meeting time
			SMT = currMeeting.getMeetingID().getStart();
			EMT = currMeeting.getMeetingID().getEnd();
			// read start and end free time
			SFT = theMeeting.getMeetingID().getStart();
			EFT = theMeeting.getMeetingID().getEnd();
			if (SMT <= SFT){
				if (SFT < EMT){
					if(EMT <= EFT){
						// change start free time equal to end meeting time SFT = EMT;
						theMeeting.setStartTimeFrame((short)(EMT >> 6),(short)(EMT & 0x3F));
						//if time frame is reduced to 0 deleted the meeting
						if (theMeeting.getDurationInM()<1)
							break LookForKey;
					}
					else{
						// free time full covered by an existing meeting
						break LookForKey;
					}
				}
				//else{ do nothing; }
			}
			else{
				if (SMT <= EFT){
					if(EMT <=EFT){
						//free time divided in two free times by the meeting
						Meeting newMeeting = new Meeting(theMeeting);
						newMeeting.setEndTimeFrame((short)(SMT >> 6),(short)(SMT & 0x3F));
						if (newMeeting.getDurationInM()>0)
							newMeetings.add(newMeeting);
						theMeeting.setStartTimeFrame((short)(EMT >> 6),(short)(EMT & 0x3F));
						if (theMeeting.getDurationInM()<1)
							break LookForKey;
					}
					else{
						//free time divided in two free times by the meeting
						theMeeting.setEndTimeFrame((short)(SMT >> 6),(short)(SMT & 0x3F));
						if (theMeeting.getDurationInM()<1)
							break LookForKey;
					}
				}
				//else{ do nothing; }
			}
		}
		if(theMeeting.getDurationInM()>0 ){
			MeetingsList.put(theMeeting.getMeetingID().UID, theMeeting);
			theMeeting.UpdateToDatabase(dbAgenda);
		}
			
		while (!newMeetings.isEmpty()){
			Meeting mtg = (Meeting)newMeetings.firstElement();
			MeetingsList.put(mtg.getMeetingID().UID,mtg);
			mtg.UpdateToDatabase(dbAgenda);
			newMeetings.remove(0);
		}
	}
	public void CompactFreeMeetings()
	{
		long deletedKeys[] = new long [MeetingsList.size()];
		int count = 0;
		Meeting currMeeting,nextMeeting = null;
		currMeeting = (Meeting)MeetingsList.get(MeetingsList.firstKey());
		for (Object keyItem: MeetingsList.keySet()){
			long key = Long.parseLong(keyItem.toString());
			nextMeeting = (Meeting)MeetingsList.get(key);
			if((currMeeting.getType() == MeetingUID.TYPE_FREE_TYME) && 
					(nextMeeting.getType() == MeetingUID.TYPE_FREE_TYME)){
				if((currMeeting.getEnd_h() == nextMeeting.getStart_h()) &&
						(currMeeting.getEnd_m() == nextMeeting.getStart_m())){
					currMeeting.setEndTimeFrame(nextMeeting.getEnd_h(), nextMeeting.getEnd_m());
					deletedKeys[count++]=key;
				}
				else
					currMeeting = nextMeeting;
			}
			else
				currMeeting = nextMeeting;
		}
		for (int i =0; i < count; i++){
			Meeting theMeeting = (Meeting) MeetingsList.remove(deletedKeys[i]);
			theMeeting.DeleteFromDatabase(dbAgenda);
		}
	}
	public void DelMeeting(Long mUID)
	{
		if (!MeetingsList.containsKey(mUID))
			throw new IllegalArgumentException ("Invalid meetingUID parameter");
		Meeting theMtng = (Meeting) MeetingsList.remove(mUID);
		theMtng.DeleteFromDatabase(dbAgenda);
		theMtng.setAsFreeTime();
		AddFreeTime(theMtng);
		CompactFreeMeetings();
		theMtng=null;
	}
	public void ChangeMeeting(Long mUID,Meeting theMeeting)
	{
		if (!MeetingsList.containsKey(mUID))
			throw new IllegalArgumentException ("Invalid meetingUID parameter");
		DelMeeting(mUID);
		if(dayUID.isSameDate(theMeeting.getMeetingID()))
		{
			AddMeeting(theMeeting);
		}
		if(dbAgenda != null)
			theMeeting.UpdateToDatabase(dbAgenda);
	}
	public Meeting getMeeting(long mUID)
	{
		if (!MeetingsList.containsKey(mUID))
			throw new IllegalArgumentException ("Invalid meetingUID parameter");
		return (Meeting)MeetingsList.get(mUID);
	}
	public int GetNbOfMeeting(){
		return MeetingsList.keySet().size();
	}
	public Object[] GetMeetingsUIDs(){
		Object[] keys = MeetingsList.keySet().toArray();
		return keys;
	}
	public void RestoreFromDatabase(){
		long key;
		Meeting 	currMeeting = null;
		MeetingUID	currMeetingUID = null;
		//Check that a database helper is available
		if(dbAgenda == null)
			throw new IllegalArgumentException ("no database helper");
		/*Read all keys from the database and delete the ones that not anymore 
		 in the MeetingList*/
		List<Long> listDB_Ids = dbAgenda.fetchMeetingsRowIdListByID(getDayUID()&MeetingUID.MASK_DATE, 
				(getDayUID()&MeetingUID.MASK_DATE)| ~ MeetingUID.MASK_DATE);
		//Read all Meeting and add to Map
		for (int index = 0; index <listDB_Ids.size(); index ++){
			key = listDB_Ids.get(index).longValue();
			currMeeting = new Meeting();
			currMeetingUID = new MeetingUID(key);
			currMeeting.setMeetingID(currMeetingUID);
			currMeeting.RestoreFromDatabase(dbAgenda);
			MeetingsList.put(key, currMeeting);
		}
	}
	public void UpdateToDatabase(){
		long key;
		Meeting currMeeting = null;
		//Check that a database helper is available
		if(dbAgenda == null)
			throw new IllegalArgumentException ("no database helper");
		/*Read all keys from the database and delete the ones that not anymore 
		 in the MeetingList*/
		List<Long> listDB_Ids = dbAgenda.fetchMeetingsRowIdListByID(getDayUID()&MeetingUID.MASK_DATE, 
				(getDayUID()&MeetingUID.MASK_DATE)| ~ MeetingUID.MASK_DATE);
		
		for (int index = 0; index <listDB_Ids.size(); index ++){
			key = listDB_Ids.get(index).longValue();
			if (MeetingsList.containsKey(key) == false){
				MeetingsList.remove(key);
			}
		}
		//Update all meeting in the database
		for (Object keyItem: MeetingsList.keySet()){
			key = Long.parseLong(keyItem.toString());
			currMeeting = (Meeting)MeetingsList.get(key);
			currMeeting.UpdateToDatabase(dbAgenda);
		}
	}
	void AddToOccupationMap(Meeting theMeeting)
	{
		Occupation MeetingOcc = CalculateOccupationMap(theMeeting);
		
		// Update Occupation Map
		dayOccupation.dayBeg = dayOccupation.dayBeg | dayOccupation.dayBeg & MeetingOcc.dayBeg;
		dayOccupation.dayMid = dayOccupation.dayMid | dayOccupation.dayMid & MeetingOcc.dayMid;
		dayOccupation.dayEnd = dayOccupation.dayEnd | dayOccupation.dayEnd & MeetingOcc.dayEnd;
		
		// Update Conflict Maps
		for (int i = 4; i > 0; i--)
		{
			dayConflicts[i].dayBeg = dayConflicts[i].dayBeg | dayConflicts[i-1].dayBeg & MeetingOcc.dayBeg; 
			dayConflicts[i].dayMid = dayConflicts[i].dayMid | dayConflicts[i-1].dayMid & MeetingOcc.dayMid; 
			dayConflicts[i].dayEnd = dayConflicts[i].dayEnd | dayConflicts[i-1].dayEnd & MeetingOcc.dayEnd; 
		}
		dayConflicts[0].dayBeg = dayConflicts[0].dayBeg | dayOccupation.dayBeg & MeetingOcc.dayBeg; 
		dayConflicts[0].dayMid = dayConflicts[0].dayMid | dayOccupation.dayEnd & MeetingOcc.dayMid; 
		dayConflicts[0].dayEnd = dayConflicts[0].dayEnd | dayOccupation.dayEnd & MeetingOcc.dayEnd; 

	};
	private void DelFromOccupationMap(Meeting theMeeting)
	{
		Occupation MeetingOcc = null;
		MeetingOcc = CalculateOccupationMap(theMeeting);

		// Update Conflict Maps
		for (int i = 4; i > 0; i--)
		{
			dayConflicts[i].dayBeg 	= dayConflicts[i].dayBeg & (~(dayConflicts[i].dayBeg | MeetingOcc.dayBeg));
			dayConflicts[i].dayMid 	= dayConflicts[i].dayMid & (~(dayConflicts[i].dayMid | MeetingOcc.dayMid));
			dayConflicts[i].dayEnd 	= dayConflicts[i].dayEnd & (~(dayConflicts[i].dayEnd | MeetingOcc.dayEnd));
			MeetingOcc.dayBeg 		= MeetingOcc.dayBeg & (~(dayConflicts[i].dayBeg | MeetingOcc.dayBeg));
			MeetingOcc.dayMid 		= MeetingOcc.dayMid & (~(dayConflicts[i].dayMid | MeetingOcc.dayMid));
			MeetingOcc.dayEnd 		= MeetingOcc.dayEnd & (~(dayConflicts[i].dayEnd | MeetingOcc.dayEnd));
		}
		dayOccupation.dayBeg 		= dayOccupation.dayBeg & (~(MeetingOcc.dayBeg));
		dayOccupation.dayMid 		= dayOccupation.dayMid & (~(MeetingOcc.dayMid));
		dayOccupation.dayEnd 		= dayOccupation.dayEnd & (~(MeetingOcc.dayEnd));			
	};
	private Occupation CalculateOccupationMap(Meeting theMeeting)
	{
		Occupation 	ocpn = new Occupation();
		int 		start 	=theMeeting.getStart_h()*4 + theMeeting.getStart_m()/15;
		int 		end 	=theMeeting.getEnd_h()*4 + theMeeting.getEnd_m()/15;
		
		ocpn.dayBeg	=0xFFFF;
		ocpn.dayMid	=0xFFFF;
		ocpn.dayEnd	=0xFFFF;
		
		if (start < 32){
			ocpn.dayBeg = ocpn.dayBeg & (0xFFFF << start);
		}
		else{
			if (start < 64){ 
				ocpn.dayBeg = 0; 
				ocpn.dayMid = ocpn.dayMid & (0xFFF << (start - 32));
			}
			else{
				ocpn.dayBeg = ocpn.dayMid = 0;	
				ocpn.dayEnd = ocpn.dayEnd & (0xFFF << (start - 64));
			}
		}
		
		if (end < 32){
			ocpn.dayEnd = ocpn.dayMid = 0; 
			ocpn.dayBeg= ocpn.dayBeg & (0xFFFF >> (31-end));
		}
		else{
			if (end  < 64)
			{ 
				ocpn.dayEnd = 0; 
				ocpn.dayMid = ocpn.dayMid & (0xFFF >> (31 - (end - 32)));
			}
			else{
				ocpn.dayEnd = ocpn.dayEnd  & (0xFFF >> (31 -(end - 64)));
			}
		}
		return ocpn;
	};
}
