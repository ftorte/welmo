package com.welmo.communication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;


public class DualService extends Service{
	private static final String LOG_TAG = "DUALSERVICE";
	private int counter = 0;
	private Handler serviceHandler = null;

    @Override
    public void onStart(int startId, Bundle arguments) {
	  super.onStart( startId, arguments );
      Log.d( LOG_TAG, "onStart" );
	  serviceHandler = new Handler();
	  serviceHandler.postDelayed( new RunTask(),1000L );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
      super.onCreate();
	  Log.d( LOG_TAG,"onCreate" );
    }

    @Override
    protected void onDestroy() {
	  super.onDestroy();
	  Log.d( LOG_TAG,"onDestroy" );
    }

    /**
     * The IAdderService is defined through IDL
     */
    private final ICounterService.Stub binder = 
			new ICounterService.Stub() {
      public int getCounterValue() {
		return counter;
      }
    };


    class RunTask implements Runnable {
	  public void run() {
		++counter;
		serviceHandler.postDelayed( this, 1000L );
	  }
	}
}