package com.welmo.util;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tft.myoffice.R;

public class SetupContent extends Activity{

	private Thread syncThread;
    private Timer timer;
	private ImageView arrowsImage;
	private Handler mHandler = new Handler();


	/**
	 * Called with the activity is first created.
	 */
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.setupcontent);

		((ImageView) findViewById(R.id.contacts_shape))
		.setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.contacts_shape))
		.setOnClickListener(mSyncListener);

		((ImageView) findViewById(R.id.calendar_shape))
		.setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.calendar_shape))
		.setOnClickListener(mSyncListener);

		((ImageView) findViewById(R.id.tasks_shape))
		.setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.tasks_shape))
		.setOnClickListener(mSyncListener);

		((ImageView) findViewById(R.id.notes_shape))
		.setOnFocusChangeListener(mFocusChangeListener);
		((ImageView) findViewById(R.id.notes_shape))
		.setOnClickListener(mSyncListener);

	}

	void ShowMessge(String Msg){
        Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}
	OnClickListener mSyncListener = new OnClickListener() {

		public void onClick(View v) {
			
	            // This class extends Thread
	            class SyncThread extends Thread {
	                // This method is called when the thread runs
	                public void run() {
	                    try {
	                        CreateContent();
	                    } catch (Exception e) {
	                        Log.e("Erroe" , e.toString());
	                        ShowMessge("Exception: " + e.toString());
	                    }
	                }
	            }

	            class ArrowsTask extends TimerTask {
	                int count = 0;

	                public void run() {
	                    mHandler.post(new Runnable() {
	                        public void run() {
	                            switch (count) {
	                            case 0:
	                                arrowsImage.setImageResource(R.drawable.icon_sync3_dark);
	                                break;
	                            case 1:
	                                arrowsImage.setImageResource(R.drawable.icon_sync4_dark);
	                                break;
	                            case 2:
	                                arrowsImage.setImageResource(R.drawable.icon_sync1_dark);
	                                break;
	                            case 3:
	                                arrowsImage.setImageResource(R.drawable.icon_sync2_dark);
	                                count = -1;
	                                break;
	                            }
	                            count++;
	                        }
	                    });
	                }
	            }

	            // Create and start the sync thread
	            syncThread = new SyncThread();
	            syncThread.setName("FunambolSyncClient");

	            ArrowsTask arrowTask = new ArrowsTask();
	            timer = new Timer("fnblArrows");

	            //
	            // Set the correct arrow to animate
	            //
	         
	            if (v.getId() == R.id.contacts_shape) {
	                arrowsImage = (ImageView) findViewById(R.id.contacts_arrow);
	            } else {
	                ShowMessge("Not supported (yet), try later...");
	                return;
	            }
			
	            //Start the sync thread
	            syncThread.start();
	            timer.schedule(arrowTask, 1, 150);
		}
	};

	OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
		public void onFocusChanged(View v, boolean b) {


			if (b) {
				((ImageView) v).setImageResource(R.drawable.sync_shape_over);
				
				if (v.getId() == R.id.contacts_shape) {
					((ImageView) findViewById(R.id.contacts_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				} else if (v.getId() == R.id.calendar_shape) {
					((ImageView) findViewById(R.id.calendar_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				} else if (v.getId() == R.id.tasks_shape) {
					((ImageView) findViewById(R.id.tasks_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				} else if (v.getId() == R.id.notes_shape) {
					((ImageView) findViewById(R.id.notes_arrow))
					.setImageResource(R.drawable.icon_sync2_dark);
				}
			} else {
				((ImageView) v).setImageResource(R.drawable.sync_shape);
				
				if (v.getId() == R.id.contacts_shape) {
					((ImageView) findViewById(R.id.contacts_arrow))
					.setImageResource(0);
				} else if (v.getId() == R.id.calendar_shape) {
					((ImageView) findViewById(R.id.calendar_arrow))
					.setImageResource(0);
				} else if (v.getId() == R.id.tasks_shape) {
					((ImageView) findViewById(R.id.tasks_arrow))
					.setImageResource(0);
				} else if (v.getId() == R.id.notes_shape) {
					((ImageView) findViewById(R.id.notes_arrow))
					.setImageResource(0);
				}
			}
		}
	};


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

	void SetupContactContent()
	{
		android.net.Uri URI;
		//--------------------------------------------------------------
		//clear contact content
		getContentResolver().delete(People.CONTENT_URI, null, null);
		getContentResolver().delete(Phones.CONTENT_URI, null, null);
		//--------------------------------------------------------------
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Pluto,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
		
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Pippo,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Minni,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Paperina,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Topolino,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Paperone,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Qui,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Quo,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Qua,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Nonna Papera,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Gastone,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Carlo,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Carla,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Andrea,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Antonio,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Mauro,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Pitro,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Paolo,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
		URI = getContentResolver().insert(People.CONTENT_URI,CreatePeople("Gigi,notes"));
		URI = getContentResolver().insert(Phones.CONTENT_URI,CreatePhones(URI.getLastPathSegment()+ ",0474473287"));
	
	}

	 // Sync Client ----------------------------------------------------------
    public void CreateContent() {

        mHandler.post(new Runnable() {
            public void run() {
                ((TextView) findViewById(R.id.sync_message_contacts))
                        .setText("Synchronizing Contacts...");
            }
        });
        // Start sync
        try {
        	long i= 100000;
            while(i > 0)
        			i= i-1;
        	SetupContactContent();
        } catch (Exception e) {
            Log.e("Exception: ", e.toString());
            Toast.makeText(this,"Exception: " + e.toString(),Toast.LENGTH_LONG).show();
        }
        
        timer.cancel();
        mHandler.post(new Runnable() {
            public void run() {
                arrowsImage.setImageResource(R.drawable.icon_complete);
            }
        });
    }
}
