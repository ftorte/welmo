package com.welmo.contents.synchronizers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;



import org.xml.sax.Attributes; 
import org.xml.sax.InputSource;
import org.xml.sax.SAXException; 
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler; 

import com.welmo.contents.ExpensesManager;
import com.welmo.contents.ExpensesManager.Currency;
import com.welmo.contents.providers.ExpensesProvider;

public class SynchCurrenciesRates  {
	
	
	
	public final static int SERVICE_YOO = 1;
	public final static int SERVICE_BCE = 2;
	public final static int SERVICE_FED = 3;
	public final static int SERVICE_DEFAULT = SERVICE_BCE;

	final private static String YAHOO = "http://finance.yahoo.com/d/quotes.csv";
	final private static String FED = "http://finance.yahoo.com/d/quotes.csv";
	final private static String BCE = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

	final private static Uri theUri = ExpensesManager.Currency.CONTENT_URI; 
    final private static String TAG ="SynchCurrenciesRates";
    
	private int service=SERVICE_DEFAULT;
	private String masterCurrSoruce="";
	private String queryString="";
	private String synchDate = "0/0/0000";

	private  ArrayList<ContentValues> theContentValues = new ArrayList<ContentValues>();	
	private ArrayList<String> validSources = new ArrayList<String>(Arrays.asList("EUR","USD","JPY","CNY","GBP"));
	private double validSourcesRates[] = {1.0,1.0,1.0,1.0,1.0};
	
	private double Round(double d){
		long l = Math.round(d * 100000);
		return (double) l/100000;	
	}
	
	private void setupService(int synch_service){
		service = synch_service;
		switch(synch_service){
		case SERVICE_YOO:
			queryString = YAHOO;
			break;
		case SERVICE_BCE:
			queryString = BCE;
			masterCurrSoruce = "EUR";
			break;
		case SERVICE_FED:
			break;		
		default:
			service = SERVICE_BCE;
		}
		theContentValues.clear();
	}
	private void CompleteParsedCurrencies(){
		ArrayList<ContentValues> othervalues = new ArrayList<ContentValues>();
		for ( Iterator<String> sourceCurr = validSources.iterator(); sourceCurr.hasNext();) {
			String newSource = sourceCurr.next();
			if(! newSource.contentEquals(masterCurrSoruce)){
				double convfactore = validSourcesRates[validSources.indexOf(newSource)];
				for ( Iterator<ContentValues> targetCurr = theContentValues.iterator(); targetCurr.hasNext();) {
					ContentValues org = targetCurr.next();
					ContentValues newcv = new ContentValues();		
					newcv.put(Currency.CURRSOURCE,newSource);
					newcv.put(Currency.CURRTARGET,org.getAsString(Currency.CURRTARGET));
					newcv.put(Currency.RATESRC_VS_TGT,Round(org.getAsDouble(Currency.RATESRC_VS_TGT)/convfactore));
					newcv.put(Currency.RATEDATE,synchDate);
					othervalues.add(newcv);
				}
			}
		}
		theContentValues.addAll(othervalues);
		othervalues.clear();
	}
	

	private final class BCEHandler extends DefaultHandler{ 
		@Override 
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {  
			double rate =0.0;
			String curr = "";
			int index;
			if(localName.compareTo("Cube")==0){
				if (atts.getLength()==2){
					if((atts.getLocalName(0).compareTo("currency")==0) && (atts.getLocalName(1).compareTo("rate")==0)){	
						ContentValues cv = new ContentValues();
						curr = atts.getValue(0);
						rate = Double.parseDouble(atts.getValue(1));
						if((index = validSources.indexOf(curr)) != -1)
							validSourcesRates[index] =  rate;
						cv.put(Currency.CURRSOURCE,masterCurrSoruce);
						cv.put(Currency.CURRTARGET,curr);
						cv.put(Currency.RATESRC_VS_TGT,Round(rate));
						cv.put(Currency.RATEDATE,synchDate);
						theContentValues.add(cv);
					}
				}
				else{ 
					if (atts.getLength()==1){
						if((atts.getLocalName(0).compareTo("time")==0)){	
							synchDate = atts.getValue(0);
						}
					} 
				}
			}
		} 
		private void ParseBCEQuery(InputSource reader) throws SAXException {
			try { 
				SAXParserFactory spf = SAXParserFactory.newInstance(); 
				SAXParser sp = spf.newSAXParser(); 
				XMLReader xr = sp.getXMLReader(); 
				BCEHandler mywBCEHandler = new BCEHandler(); 
				xr.setContentHandler(mywBCEHandler); 
				xr.parse(reader); 				
			} catch (Exception e) { 
				Log.e("Provider", "WeatherQueryError", e);
				throw new SAXException("Synch: error parsing source data");
			} 
		}
	}

	public void synch(ContentResolver content, int synch_service)throws IOException {
		setupService(synch_service);
		// Create an instance of HttpClient.
		HttpClient client = new DefaultHttpClient();

		try {
			// Create a method instance.
			HttpGet httpget = new HttpGet(queryString);
			// Execute the method.
			HttpResponse response = client.execute(httpget);
			StatusLine statusCode = response.getStatusLine();

			if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
				Log.e(TAG, "Method failed: " + statusCode.getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			InputSource reader = new InputSource( new InputStreamReader(entity.getContent()));
			parseResult(reader);
			if(theContentValues.size() > 0){
				ContentValues []values = new ContentValues[theContentValues.size()];
				content.bulkInsert(theUri,theContentValues.toArray(values));
			}
		}
		catch (SAXException err){
			Log.e("synch", "Invalid query string: " + err.getMessage()); 
			throw new IOException("Synch: error parsing source data");
		}
		catch (IllegalArgumentException err){
			Log.e("synch", "Invalid query string: " + err.getMessage()); 
			throw new IOException("Synch: error parsing source data");
		}
		catch (ClientProtocolException err){
			Log.e("synch", "Fatal HTTP error: " + err.getMessage());
			throw new IOException("Synch: connection problem");
		} 
		catch (IOException e) {
			Log.e("synch", "Fatal transport error: " + e.getMessage());
			throw new IOException("Synch: impossible to get synchronizaton");
		} 
		finally {
			client.getConnectionManager().shutdown();
		}
	}
	private void parseResult(InputSource reader)throws SAXException {
		switch(service){
		case SERVICE_YOO:
			queryString = YAHOO;
			break;
		case SERVICE_BCE:
			BCEHandler bceHand = new BCEHandler();
			bceHand.ParseBCEQuery(reader);
			CompleteParsedCurrencies();
			break;
		case SERVICE_FED:
			break;		
		}

	}
}