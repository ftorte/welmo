package com.welmo.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.welmo.R;

public class DialogListSelector extends Dialog {

	ListView 			theList = null;
	Context 			mContext;
	OnSelectionListener	mSelectionListener = null;

	public interface OnSelectionListener{
		void setSelection(int position);
	};
	
	OnItemClickListener mListListener = new OnItemClickListener() {
    	public void onItemClick(AdapterView parent, View v, int position, long id){
    		doEnd(true, position);
    	}
	};
    
	public DialogListSelector(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public DialogListSelector(Context context, int theme) {
		super(context, theme);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public DialogListSelector(Context context) {
		super(context);
		mContext = context;
		setCancelable(true);
		setContentView(R.layout.dialoglistselector);
    	theList = (ListView)findViewById(R.id.DialogList);
    	theList.setOnItemClickListener(mListListener);
	}
	public void setContent(int listlayout, int listtextitem, String[] content){
		ArrayAdapter<String> adapter = new ArrayAdapter(mContext, listlayout,listtextitem,content);
        theList.setAdapter(adapter);
	}
	public void SelectItem(int position){
		//if(position <= theList.getCount())
			theList.setSelection(position);
	}
	public void setSelctionListener(OnSelectionListener listener){
		mSelectionListener = listener;
	}
    public void doEnd(boolean succeded, int position){
    	if(succeded)
    		if(mSelectionListener != null)
    			mSelectionListener.setSelection(position);
    	cancel();
    }
}
