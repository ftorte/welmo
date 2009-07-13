package com.welmo.travel.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpView extends Activity {
	
	@Override
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.help);

        final String mimeType = "text/html";
        final String encoding = "utf-8";

        WebView browser=(WebView) findViewById(R.id.helpview);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.loadUrl("file:///android_asset/help.html");

    }
}
