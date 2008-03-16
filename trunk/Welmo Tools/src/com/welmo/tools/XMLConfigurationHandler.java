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

import android.util.Log;

public class XMLConfigurationHandler implements ContentHandler {

	private Locator locator;
	private static final String TAG = "XMLConfigurationHandler";

	public XMLConfigurationHandler() {
		super();
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
	public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException {
		Log.v(TAG, "Fermeture de la balise : " + localName);

		if ( ! "".equals(nameSpaceURI)) { // name space non null
			Log.v(TAG, "appartenant a l'espace de nommage : " + localName);
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
		Log.v(TAG, "Ouverture de la balise : " + localName);

		if ( ! "".equals(nameSpaceURI)) { // espace de nommage particulier
			Log.v(TAG, "  appartenant a l'espace de nom : "  + nameSpaceURI);
		}

		Log.v(TAG, "  Attributs de la balise : ");

		for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
			Log.v(TAG, "     - " 
					+ attributs.getLocalName(index) + " = " 
					+ attributs.getValue(index));
		}


	}
	@Override
	public void startPrefixMapping(String prefix, String URI)
	throws SAXException {
		// TODO Auto-generated method stub
		Log.v(TAG, "Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);		
	}
}
