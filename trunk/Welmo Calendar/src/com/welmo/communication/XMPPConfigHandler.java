package com.welmo.communication;
import java.util.Vector;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.welmo.R;
import com.welmo.dbhelper.WelmoConfigDBHelper;
import com.welmo.tools.WelmoConfig;
import com.welmo.tools.WelmoConfig.ConfigEntry;
  
/**  
 * Gather the xmpp settings and create an XMPPConnection  
 */  
public class XMPPConfigHandler extends Dialog implements android.view.View.OnClickListener {   
    
	WelmoConfig 		theConfig 		= new WelmoConfig();
	WelmoConfigDBHelper	dbConfig		= null;
	ArrayAdapter<String> adapter		= null;
	EditText  			Host 			= null;
	EditText 			Port 			= null;
	EditText 			Service 		= null; 
	EditText 			Username 		= null;  
	EditText 			Password 		= null;
	
    public XMPPConfigHandler(Context ctx) {   
        super(ctx);   
    }   
  
    protected void onStart() {   
        super.onStart();   
        setContentView(R.layout.xmppconfig);   
        getWindow().setFlags(4, 4);   
        setTitle("XMPP Settings");  

        //Init Edit View pointers
        Host 			= (EditText)findViewById(R.id.host);;
        Port 			= (EditText)findViewById(R.id.port);;
        Service 		= (EditText)findViewById(R.id.service);; 
        Username 		= (EditText)findViewById(R.id.userid);;  
        Password 		= (EditText)findViewById(R.id.password);;

        //Read Current Configuration
    	dbConfig = new WelmoConfigDBHelper(this.getContext(),"Welmo","Config");
    	theConfig.RestoreFromDatabase(dbConfig);
    	Vector<ConfigEntry> cfgs = theConfig.GetEntries("/XMPP/Login","");
    	Vector<String> cfgitems = new Vector<String>();
    	cfgitems.add("...");
    	for(int index =0; index < cfgs.size(); index++)
    		cfgitems.add(cfgs.get(index).Parameter);
    	Spinner s1 = (Spinner) findViewById(R.id.spinner1);
    	
    	//setup adapter
    	adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item,cfgitems);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);

        //config selector event
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
        	public void onItemSelected(AdapterView parent, View v, int position, long id){
				//((MeetingDisplayShort)v).mMeetingLongDescription.setVisibility(VISIBLE);
        		UpdateSelection(position);
    			Log.i("XMPPRcvService", "[Connect] Signaled Presence");   
			}
        	public void onNothingSelected(AdapterView parent) {
				//((MeetingDisplayShort)v).mMeetingLongDescription.setVisibility(VISIBLE);
        		ClearSelection();
    			Log.i("XMPPRcvService", "[Connect] Signaled Presence");   
			}
        });
        Button ok = (Button) findViewById(R.id.ok);   
        ok.setOnClickListener(this);   
    }   
  
    public void onClick(View v) {  
    	theConfig.UpdateToDatabase(dbConfig);
    	dismiss();   
    }   
  
    private String getText(int id) {   
        EditText widget = (EditText) this.findViewById(id);   
        return widget.getText().toString();   
    }   
    public void ClearSelection()
    {
    	Host.setText("");
    	Port.setText(""); 	
    	Service.setText(""); 
    	Username.setText(""); 	
    	Password.setText(""); 

    }
    public void UpdateSelection(int id)
    {
    	if(adapter != null){
    		String loginvaue=(String)adapter.getItem(id);
    		Vector<ConfigEntry> cfgs = theConfig.GetEntries("/XMPP/Login/"+loginvaue,"");
    		if(cfgs.size()>0){
    			for(int index =0; index < cfgs.size(); index++){
    				String par = cfgs.get(index).Parameter;
    				if(par.compareTo("host")==0){
    					Host.setText(cfgs.get(index).ParValue);
    					continue;
    				}
    				if(par.compareTo("port")==0){
    					Port.setText(cfgs.get(index).ParValue);
    					continue;
    				}
    				if(par.compareTo("service")==0){
    					Service.setText(cfgs.get(index).ParValue);
    					continue;
    				}
    				if(par.compareTo("username")==0){
    					Username.setText(cfgs.get(index).ParValue);
    					continue;
    				}
    				if(par.compareTo("password")==0){
    					Password.setText(cfgs.get(index).ParValue);
    					continue;
    				}
    			}
    			theConfig.DelEntries("/XMPP/Login/Default", "value");
    			theConfig.AddEntries("/XMPP/Login/Default", "value", loginvaue);
    		}
    		else{
    			ClearSelection();
    		}

    	}
    }
}  
