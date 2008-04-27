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

import android.content.Context;
import android.util.Log;

import com.welmo.dbhelper.AgendaDBHelper;
import com.welmo.dbhelper.WelmoConfigDBHelper;
import com.welmo.meeting.Meeting;
import com.welmo.meeting.MeetingUID;

public class XMLConfigurationHandler implements ContentHandler {

	private Locator locator;
	private static final String TAG = "XMLConfigurationHandler";
	//---------------------------------------------------------
	Context mContext = null;
	//---------------------------------------------------------
	//content handler class
	private WelmoConfig 		theConfig 		= new WelmoConfig();
	private WelmoConfigDBHelper	dbConfig		= null;
	//---------------------------------------------------------	
	//handle tags
	String CurrentURI = "";
	String CurrentParameter = "";
	String CurrentParValue = "";

	//---------------------------------------------------------
	public XMLConfigurationHandler(Context ctx) {
		super();
		// On definit le locator par defaut.
		mContext = ctx;
		locator = new LocatorImpl();
		dbConfig = new WelmoConfigDBHelper(mContext,"Welmo","Config");
		dbConfig.deleteConfigAllRows();
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
		theConfig.UpdateToDatabase(dbConfig);
		Log.v(TAG, "Fin de l'analyse du document" );
	}
	@Override
	public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException {

		if ( ! "".equals(nameSpaceURI)) { // name space non null
			Log.v(TAG, "appartenant a l'espace de nommage : " + localName);
		}
		if(localName.compareTo("Service")==0){
			CurrentURI="/";
			return;
		}	
		if(localName.compareTo("Component")==0){
			String[] tokens = CurrentURI.split("/");
			CurrentURI="";
			for (int index = 1; index < tokens.length && index < 2; index++)
				CurrentURI= CurrentURI + "/" + tokens[index];
		}
		if(localName.compareTo("ID")==0){
			String[] tokens = CurrentURI.split("/");
			CurrentURI="";
			for (int index = 1; index < tokens.length && index < 3; index++)
				CurrentURI= CurrentURI + "/" + tokens[index];
		}
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
		String id = "";
		String value = "";		
		Log.v(TAG, "Ouverture de la balise : " + localName);
		if(localName.compareTo("Service")==0){
			CurrentURI="/";
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("value")==0){	
					value = attributs.getValue(index);
				}
			}
			theConfig.AddEntries(CurrentURI, value, "");
			CurrentURI = CurrentURI + value;
			return;
		}	
		if(localName.compareTo("Component")==0){
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("value")==0){
					value = attributs.getValue(index);
				}
			}
			theConfig.AddEntries(CurrentURI, value, "");
			CurrentURI = CurrentURI + "/" + value;
			return;
		}	
		if(localName.compareTo("ID")==0){
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("value")==0){
					value = attributs.getValue(index);
				}
			}
			theConfig.AddEntries(CurrentURI, value, "");
			CurrentURI = CurrentURI + "/" + value;
			return;
		}	
		if(localName.compareTo("Parameters")==0){
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("id")==0){
					id=attributs.getValue(index);
				}
				if(attributs.getLocalName(index).compareTo("value")==0){
					value=attributs.getValue(index);
				}
			}
			theConfig.AddEntries(CurrentURI, id, value);
			return;
		}
	}
	
	@Override
	public void startPrefixMapping(String prefix, String URI)
	throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);		
	}
}
