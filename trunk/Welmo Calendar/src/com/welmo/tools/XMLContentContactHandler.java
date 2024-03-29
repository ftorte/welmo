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
import android.provider.Contacts;
import android.provider.Contacts.ContactMethods;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.util.Log;

public class XMLContentContactHandler implements ContentHandler {

	//---------------------------------------------------------
	Context mContext = null;
	
	//---------------------------------------------------------
	//data structure 
	private class contactName{
		public String name	= new String();
		public String notes = new String();
	}
	private class contactTypePhone{
		public String phone_nuber = new String();
	}
	private class contactTypeEmals{
		public String XMPP = new String();
	}
	contactName 		theContact 	= new contactName();
	contactTypePhone	thePhones 	= new contactTypePhone();
	contactTypeEmals	theEmails 	= new contactTypeEmals();
	//---------------------------------------------------------
	Locator locator;
	private static final String TAG = "XMLConfigurationHandler";

	public XMLContentContactHandler(Context ctx) {
		super();
		mContext = ctx;
		// On definit le locator par defaut.
		locator = new LocatorImpl();
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
		
	}
	
	@Override
	public void startElement(String nameSpaceURI, String localName, String rawName, 
			Attributes attributs) throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Ouverture de la balise : " + localName);
		if(localName.compareTo("contact")==0){
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("name")==0){	
					theContact.name = attributs.getValue(index);
				}
				if(attributs.getLocalName(index).compareTo("notes")==0){	
					theContact.notes = attributs.getValue(index);
				}
				else{
					Log.e(TAG, "Wrong XML format file");
				}
			}
		}
		if(localName.compareTo("Phone")==0){
			Log.v(TAG, "Close of tag: " + localName);
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("number")==0){	
					thePhones.phone_nuber= attributs.getValue(index);
				}
				else{
					Log.e(TAG, "Wrong XML format file");
				}
			}
		}
		if(localName.compareTo("XMPP")==0){
			Log.v(TAG, "Close of tag: " + localName);
			for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
				if(attributs.getLocalName(index).compareTo("email")==0){	
					theEmails.XMPP= attributs.getValue(index);
				}
				else{
					Log.e(TAG, "Wrong XML format file");
				}
			}
		}
	}
	@Override
	public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException {
		if(localName.compareTo("contact")==0){
			android.net.Uri newPerson = mContext.getContentResolver().insert(People.CONTENT_URI,CreatePeople(theContact.name + "," + theContact.notes));
			String personID = newPerson.getLastPathSegment();
			if(thePhones.phone_nuber.compareTo("")!=0){
				String strPhoneContent = personID+ ",0474473287";
				mContext.getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(strPhoneContent));
				thePhones.phone_nuber="";
			}
			if(theEmails.XMPP.compareTo("")!=0){
				String strCtMethod=personID + "," + theEmails.XMPP; 
				newPerson = mContext.getContentResolver().insert(
						newPerson.buildUpon().appendEncodedPath(Contacts.ContactMethods.CONTENT_URI.getPath()).build()
						,CreateXMPP(strCtMethod));
				Log.v(TAG, "Traitement de l'espace de nommage : ");		
				theEmails.XMPP="";
			}
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
	public ContentValues CreateXMPP(String adress)
	{
		ContentValues theContent = new ContentValues();
		String[] tokens = adress.split(",");
		theContent.put(ContactMethods.PERSON_ID,tokens[0]);
		theContent.put(ContactMethods.LABEL,"XMPP");
		theContent.put(ContactMethods.DATA,tokens[1]);
		//[FT ver 0.51] theContent.put(ContactMethods.KIND,ContactMethods.EMAIL_KIND);
		//[FT ver 0.51] theContent.put(ContactMethods.TYPE,ContactMethods.EMAIL_KIND_OTHER_TYPE);
		return theContent;
	}
	@Override
	public void startPrefixMapping(String prefix, String URI)
	throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);		
	}
}
