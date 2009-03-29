package com.welmo.contacts;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.welmo.R;

public class ContactsList extends  ListActivity{

	private class Contact{
		public boolean Selected;
		public String ID;
		public String Name;
		public int 	_pos;
	}
	
	public static String CONTACTS_IDS ="CONTACTS_ID";
	public static String CONTACTS_NAMES ="CONTACTS_NAMES";
	private ListView theListView;
	
	private String strExcludedContacts;
	protected ArrayList<Contact> theContactList = new ArrayList<Contact>();
	protected static final String TAG = "[ContactsList]";

	void ShowMessage(String Msg){
        Toast.makeText(this,Msg,Toast.LENGTH_SHORT).show();
	}
	
	/** Called when the activity is first created. */ 
	@Override 
	public void onCreate(Bundle icicle) { 
		super.onCreate(icicle); 
		setContentView(R.layout.contactslistview);
		
		theListView = (ListView)findViewById(android.R.id.list);
        theListView.setOnItemLongClickListener(new OnItemLongClickListener (){ 
			@Override
			public boolean onItemLongClick(AdapterView arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v(TAG,"Long item Click: ");
    			return true;
			}
    	});
		
		//Buttons 
		ImageButton confirmButton 	= (ImageButton) findViewById(R.id.confirm);
		ImageButton cancelButton 	= (ImageButton) findViewById(R.id.cancel);
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			strExcludedContacts = extras.getString(this.CONTACTS_IDS);
			strExcludedContacts = "people._id NOT IN (" + strExcludedContacts+")";
		}
		else{
			strExcludedContacts=new String("");
		}
		
		Cursor c = getContentResolver().query(People.CONTENT_URI, null, strExcludedContacts, null, null); 

		int nContactPositon=0;
		if(c!= null){
			if(c.getCount()>0){
				c.moveToFirst();
				while (!c.isAfterLast())
				{
					Contact theContact = new Contact();
					theContact.ID = c.getString(c.getColumnIndex(People._ID));
					theContact.Name = c.getString(c.getColumnIndex(People.NAME));
					theContact.Selected = false;
					theContact._pos = nContactPositon;
					theContactList.add(theContact);
					c.moveToNext();
					nContactPositon++;
				}
			}
		}
		//Close cursor if necessary
		if(c != null) c.close();
		
		ContactListAdapter mAdapter = new ContactListAdapter(this);
		
		this.setListAdapter(mAdapter); 
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString(CONTACTS_IDS, getSelectedContactsID());
				bundle.putString(CONTACTS_NAMES, getSelectedContactsNames());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});	
		
		
	} 

	String getSelectedContactsID(){
		String strContactsID = "";
		for(int pos =0; pos < theContactList.size(); pos ++){
			if (((Contact)theContactList.get(pos)).Selected){
				strContactsID = ((Contact)theContactList.get(pos)).ID + "," + strContactsID; 
			}	
		}
		if (strContactsID.length() > 0){
			String newString = strContactsID.substring(0, strContactsID.length()-1);
			return newString;
		}
		return strContactsID;
	}
	String getSelectedContactsNames(){
		String strContactsNames = "";
		for(int pos =0; pos < theContactList.size(); pos ++){
			if (((Contact)theContactList.get(pos)).Selected){
				strContactsNames = ((Contact)theContactList.get(pos)).Name + "," + strContactsNames; 
			}	
		}
		if (strContactsNames.length() > 0){
			String newString = strContactsNames.substring(0, strContactsNames.length()-1);
			return newString;
		}
		return strContactsNames;
	}
	
	@Override 
	protected void onListItemClick(ListView l, View v, int position, long id){ 
		super.onListItemClick(l, v, position, id); 
		CheckBox cbItem = (CheckBox)v.findViewById(R.id.cbxSelectItem);
		Contact item = (Contact)theContactList.get(position);
		if(item.Selected){
			cbItem.setChecked(false);
		}
		else{
			cbItem.setChecked(true);
		}
	}
	
	private class PeopleList extends LinearLayout {

		public 		CheckBox 	mCbContact;	
		public 		TextView 	mTVName;	
		
		private 	View 		mTheView;
		private 	Context 	mContext;
		private 	int 		mPositon=0;
        
        public PeopleList(Context context){
        	super(context);
        	Log.v(TAG,"PeopleList Constructor");
        	mContext = context;
        	LayoutInflater  inf =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	mTheView = inf.inflate(R.layout.contactlistrow, null, false); 
        	mCbContact = (CheckBox) mTheView.findViewById(R.id.cbxSelectItem);
        	mTVName	= (TextView) mTheView.findViewById(R.id.txtView);
        	mCbContact.setOnClickListener(mCboxListener);
        	addView(mTheView, new LinearLayout.LayoutParams(
        			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
        public void setContactName(String title) {
        	mTVName.setText(title);
        }
        public void setID(int thepositon) {
        	mPositon=thepositon;
        }
        // Catch the checkbox click and update the item in the adapter.
        private OnClickListener mCboxListener = new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		Log.v(TAG,"check box onClick: ");
        		if(mCbContact.isChecked()){ 
        			((Contact)theContactList.get(mPositon)).Selected=true;
        		}
        		else
        		{
        			((Contact)theContactList.get(mPositon)).Selected=false;
        		}
        	}
        };
    }
	public class ContactListAdapter extends BaseAdapter {
		
		private Context mContext;

		public ContactListAdapter(Context ctx){
			mContext = ctx;
		}
		@Override
		public int getCount() {
			Log.v(TAG,"adaptor getCount");
			return theContactList.size();
		}
		@Override
		public Object getItem(int index) {
			Log.v(TAG,"adaptor getItem:" + index);
			return theContactList.get(index);
		}
		@Override
		public long getItemId(int position){
			Log.v(TAG,"adaptor getItemId:" + position);
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PeopleList sv;
            if (convertView == null) {
            	Log.v(TAG,"getView(new):" + position);
                sv = new PeopleList(mContext);
                Contact currItem = theContactList.get(position);
                sv.setContactName(currItem.Name);
                sv.mCbContact.setChecked(currItem.Selected);
                sv.mPositon = currItem._pos;
            } 
            else {
            	Log.v(TAG,"getView:" + position + " " + convertView.toString());
                sv = (PeopleList) convertView;
                Contact currItem = theContactList.get(position);
                sv.setContactName(currItem.Name);
                sv.mCbContact.setChecked(currItem.Selected);
                sv.mPositon = currItem._pos;
            }
            return sv;
		}
		
	}
	
}