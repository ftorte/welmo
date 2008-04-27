/**
 * {@docRoot}
 *  This class contains and handle the meeting lis of one day
 */
package com.welmo.meeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import com.welmo.dbhelper.AgendaDBHelper;

/**
 * @author ebbp178
 * Class containing a day of the agenda
 */
public class MeetingList implements Serializable,IMeetingList{
	public static final long serialVersionUID = 1;
	
	private	SortedMap<MeetingUID,Meeting> 			MeetingsList  		= new TreeMap<MeetingUID,Meeting>();
	private AgendaDBHelper 		dbAgenda 								= null;
	   
	public MeetingList(AgendaDBHelper dbh) {
		super();
		dbAgenda = dbh;
	}
	
	public void AddMeeting(Meeting theMeeting)
	{
		int count=0;
		while(MeetingsList.containsKey(theMeeting.getMeetingID()))
		{
			count++;
			int newMeetingCount = theMeeting.getMeetingID().getCount()+1;
			theMeeting.getMeetingID().setCount((short)newMeetingCount);
			if(count > 3)
				throw new IllegalArgumentException ("UID already present: conflict with multiple ");
		}	
		MeetingsList.put(theMeeting.getMeetingID(), theMeeting);
		if(dbAgenda != null)
			theMeeting.UpdateToDatabase(dbAgenda);

	}
	public void DelMeeting(MeetingUID mUID)
	{
		if (!MeetingsList.containsKey(mUID))
			throw new IllegalArgumentException ("Invalid meetingUID parameter");
		Meeting theMtng = MeetingsList.remove(mUID);
		theMtng.DeleteFromDatabase(dbAgenda);
		theMtng.setAsFreeTime();
		theMtng=null;
	}
	public void ChangeMeeting(MeetingUID mUID,Meeting theMeeting)
	{
		if (!MeetingsList.containsKey(mUID))
			throw new IllegalArgumentException ("Invalid meetingUID parameter");
		DelMeeting(mUID);
		AddMeeting(theMeeting);
		if(dbAgenda != null)
			theMeeting.UpdateToDatabase(dbAgenda);
	}
	public Meeting getMeeting(MeetingUID mUID)
	{
		if (!MeetingsList.containsKey(mUID))
			throw new IllegalArgumentException ("Invalid meetingUID parameter");
		return MeetingsList.get(mUID);
	}
	public int GetNbOfMeeting(){
		return MeetingsList.keySet().size();
	}
	public ArrayList<MeetingUID> GetMeetingsUIDs(){
		ArrayList<MeetingUID> keys = new ArrayList<MeetingUID>(MeetingsList.keySet());
		return keys;
	}
	public int GetNbOfMeeting(short ValidTypes){
		ArrayList<MeetingUID> keys = new ArrayList<MeetingUID>(MeetingsList.keySet());
		Iterator<MeetingUID> it = keys.iterator();
		while(it.hasNext()){
			if(!it.next().isOfType(ValidTypes))
			{	
				it.remove();
			}
		}
		return keys.size();
	}
	public ArrayList<MeetingUID> GetMeetingsUIDs(short ValidTypes){
		ArrayList<MeetingUID> keys = new ArrayList<MeetingUID>(MeetingsList.keySet());
		Iterator<MeetingUID> it = keys.iterator();
		while(it.hasNext()){
			if(!it.next().isOfType(ValidTypes))
			{	
				it.remove();
			}
		}
		return keys;
	}
	public void RestoreFromDatabase(){
		Meeting tmpMeeting=null;
		MeetingUID key = new MeetingUID();
		Meeting 	currMeeting = null;
		MeetingUID	currMeetingUID = null;
		//Check that a database helper is available
		if(dbAgenda == null)
			throw new IllegalArgumentException ("no database helper");
		/*Read all keys from the database and delete the ones that not anymore 
		 in the MeetingList*/
		long min = 0L;
		long max = 0x7FFFFFFFFFFFFFFFL;
		
		List<Long> listDB_Ids = dbAgenda.fetchMeetingsRowIdListByID(min,max);
		//Read all Meeting and add to Map
		MeetingsList.clear();
		for (int index = 0; index <listDB_Ids.size(); index ++){
			key.UID = listDB_Ids.get(index).longValue();
			currMeeting = new Meeting();
			currMeetingUID = new MeetingUID(key);
			currMeeting.setMeetingID(currMeetingUID);
			currMeeting.RestoreFromDatabase(dbAgenda);
			tmpMeeting = MeetingsList.put(currMeetingUID, currMeeting);
		}
	}
	public void UpdateToDatabase(){
		MeetingUID key=new MeetingUID();
		Meeting currMeeting = null;
		//Check that a database helper is available
		if(dbAgenda == null)
			throw new IllegalArgumentException ("no database helper");
		/*Read all keys from the database and delete the ones that not anymore 
		 in the MeetingList*/
		long min = 0L;
		long max = 0xFFFFFFFFFFFFFFFFL;
		
		List<Long> listDB_Ids = dbAgenda.fetchMeetingsRowIdListByID(min,max);

		for (int index = 0; index <listDB_Ids.size(); index ++){
			key.UID = listDB_Ids.get(index).longValue();
			if (MeetingsList.containsKey(key) == false){
				MeetingsList.remove(key);
			}
		}
		//Update all meeting in the database
		for (MeetingUID keyUID: MeetingsList.keySet()){
			currMeeting = (Meeting)MeetingsList.get(keyUID);
			currMeeting.UpdateToDatabase(dbAgenda);
		}
	}
}
