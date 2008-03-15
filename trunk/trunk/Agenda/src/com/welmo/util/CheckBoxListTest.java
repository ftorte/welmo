package com.welmo.util;

import java.util.ArrayList;
import java.util.List;
import com.welmo.R;

import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.util.Log;
import android.widget.TextView; 
import java.io.FileReader;
import java.io.File;
import java.io.InputStream;  
import java.io.FileInputStream;
import android.content.Context;
import android.content.Intent;
import android.view.Menu.Item;
import android.widget.CheckBox;



public class CheckBoxListTest extends ListActivity {
    /** Called when the activity is first created. */
	
    private String logCat = "MY ACTIVITY";
	private CheckBoxifiedTextListAdapter itla;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.contactslistview);
        
        /* Set up list adapter and check boxes */
        itla = new CheckBoxifiedTextListAdapter(this);

        itla.addItem(new CheckBoxifiedText("Box 1", true));
        itla.addItem(new CheckBoxifiedText("Box 2", false));
        itla.addItem(new CheckBoxifiedText("Box 3", true));

        // Display it
        setListAdapter(itla);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
		/* Add 2 itesm to menu: "select all" and "unselect all" are the values of the 
		* two strings */
        menu.add(0, 0, "Select All");
        menu.add(0, 1, "Select None");
        return true;
    }  
    @Override
    public boolean onMenuItemSelected(int featureId, Item item) {
        super.onMenuItemSelected(featureId, item);
        switch(item.getId()) {
        case 0:
        	// Select All
        	selectAll();
            break;
        case 1:
        	// Deselect All
        	deselectAll();
            break;
        }
        
        return true;
    } 	
	
    private void selectAll()
    {
    	for(int i = 0 ; i<itla.getCount(); i++)
    	{
    		itla.setChecked(true, i);
    	}
    }
    private void deselectAll()
    {
    	for(int i = 0 ; i<itla.getCount(); i++)
    	{
    		itla.setChecked(false, i);
    	}
    }
}