package com.welmo.tools;

/*
 * Created on 2 nov. 03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

import android.content.ContentValues;
import android.content.Context;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.util.Log;
import com.welmo.meeting.*;
import com.welmo.dbhelper.*;

public class XMLContentAgendaHandler implements ContentHandler {

	//---------------------------------------------------------
	Context mContext = null;
	
	//---------------------------------------------------------
	//content handler class
	private Meeting 		mMeeting 		= new Meeting();
	private MeetingUID		mMeetingUID		= new MeetingUID();
	private AgendaDBHelper	dbAgenda	= null;
	
	class MeetingRawInfo {
		public int			Year	=	0;
		public int			Day		=	0;
		public int			Month	=	0;
		public int 			Start_h =	0;
		public int 			Start_m =	0;
		public int 			End_h 	=	0;
		public int 			End_m 	=	0;
		public String		Desc	=	"";
		public String 		Obj		=	"";  
		
		public void clear(){
			Year	=	0;
			Day		=	0;
			Month	=	0;
			Start_h =	0;
			Start_m =	0;
			End_h 	=	0;
			End_m 	=	0;
			Desc	=	"";
			Obj		=	"";  
		}
	}
	private MeetingRawInfo mMeetingInfo = new MeetingRawInfo();
	
	//---------------------------------------------------------
	private Locator locator;
	private static final String TAG = "XMLConfigurationHandler";

	public XMLContentAgendaHandler(Context ctx) {
		super();
		mContext = ctx;
		// On definit le locator par defaut.
		locator = new LocatorImpl();
		dbAgenda = new AgendaDBHelper(mContext,"Agenda","Meetings","Attends");
		dbAgenda.deleteMeetingsRowByWhere(null);
		dbAgenda.deleteMeetingsRowByWhere(null);
	}
	public void setDocumentLocator(Locator value) {
		locator =  value;
	}
	public void startDocument() throws SAXException {
		Log.v(TAG, "Debut de l'analyse du document");
	}
	@Override
	public void characters(char[] ch, int start, int end) throws SAXException {
		Log.v(TAG, "#PCDATA : " + new String(ch, start, end));
	}
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Fin de l'analyse du document" );
	}
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Fin de traitement de l'espace de nommage : " + prefix);


	}
	@Override
	public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
		Log.v(TAG, "espaces inutiles rencontres : ..." + new String(ch, start, end) +  "...");
	}
	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		Log.v(TAG, "Instruction de fonctionnement : " + target);
		Log.v(TAG, "  dont les arguments sont : " + data);
	}
	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// Je ne fais rien, ce qui se passe n'est pas franchement normal.
		// Pour eviter cet evenement, le mieux est quand meme de specifier une dtd pour vos
		// documents xml et de les faire valider par votre parser.              
	}
	
	@Override
	public void startElement(String nameSpaceURI, String localName, String rawName, 
			Attributes attributs) throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Ouverture de la balise : " + localName);
		if(localName.compareTo("meeting")==0){
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("year")==0){	
					mMeetingInfo.Year=Integer.parseInt(attributs.getValue(index));
				}
				else if(attributs.getLocalName(index).compareTo("month")==0){	
					mMeetingInfo.Month = Integer.parseInt(attributs.getValue(index));
				}
				else if(attributs.getLocalName(index).compareTo("day")==0){	
					mMeetingInfo.Day = Integer.parseInt(attributs.getValue(index));
				}
				else if(attributs.getLocalName(index).compareTo("start")==0){	
					String start = attributs.getValue(index);
					String[] start_tokens = start.split(":");
					mMeetingInfo.Start_h = Integer.parseInt(start_tokens[0]);
					mMeetingInfo.Start_m = Integer.parseInt(start_tokens[1]);
				}
				else if(attributs.getLocalName(index).compareTo("end")==0){	
					String start = attributs.getValue(index);
					String[] start_tokens = start.split(":");
					mMeetingInfo.End_h = Integer.parseInt(start_tokens[0]);
					mMeetingInfo.End_m = Integer.parseInt(start_tokens[1]);
				}
				else if(attributs.getLocalName(index).compareTo("subject")==0){	
					mMeetingInfo.Obj = attributs.getValue(index);
				}
				else if(attributs.getLocalName(index).compareTo("description")==0){	
					mMeetingInfo.Desc = attributs.getValue(index);
				}
				else{
					Log.e(TAG, "Wrong XML format file");
				}
			}
		}
	}
	@Override
	public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException {
		if(localName.compareTo("meeting")==0){
			mMeeting.setTimeFrame((short)mMeetingInfo.Start_h, (short)mMeetingInfo.Start_m,
					(short)mMeetingInfo.End_h, (short)mMeetingInfo.End_m);
			mMeeting.setDescription(mMeetingInfo.Desc);
			mMeeting.setObject(mMeetingInfo.Obj);
			mMeeting.setType(MeetingUID.TYPE_WORING_MEETING);
			mMeetingUID.setUID((short)mMeetingInfo.Year,(short) mMeetingInfo.Month, (short)mMeetingInfo.Day);
			mMeeting.setMeetingIDDay(mMeetingUID);
			mMeeting.UpdateToDatabase(dbAgenda);
		}
	}
	public ContentValues CreatePeople(String adress)
	{
		ContentValues theContent = new ContentValues();
		String[] tokens = adress.split(",");
		theContent.put(People.NAME,tokens[0]);
		theContent.put(People.NOTES,tokens[1]);

		return theContent;
	}
	public ContentValues CreatePhones(String adress)
	{
		ContentValues theContent = new ContentValues();
		String[] tokens = adress.split(",");
		theContent.put(Phones.PERSON_ID,tokens[0] );
		theContent.put(Phones.NUMBER, tokens[1]);
		return theContent;
	}
	@Override
	public void startPrefixMapping(String prefix, String URI)
	throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);		
	}
}
