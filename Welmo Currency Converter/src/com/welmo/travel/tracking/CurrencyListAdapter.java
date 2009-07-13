package com.welmo.travel.tracking;

import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.welmo.contents.ExpensesManager.CurrenciesISO;
import com.welmo.contents.ExpensesManager.Currency;

class CurrencyListAdapter extends BaseAdapter {
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		super.registerDataSetObserver(observer);
	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		super.unregisterDataSetObserver(observer);
	}

	public static final int CHANGED_VALUE_TARGET = 1;
	public static final int CHANGED_VALUE_SOURCE = 2;
	public static final int CHANGED_RATE_TRG_VS_SRC = 3;
	public static final int CHANGED_RATE_SRC_VS_TRG = 4;
	
	private LayoutInflater mInflater;
	private ArrayList<CurrencyConv> mCurrencyList = new ArrayList<CurrencyConv>();
	static String FLAGS_PATH = "android.resource://com.welmo.travel.tracking/R.drawable.";
	private Context mContext;
	
	public CurrencyListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return mCurrencyList.size();
	}

	public Object getItem(int position) {
		if(position < mCurrencyList.size())
			return mCurrencyList.get(position);
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		// A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.currencynew, null);
			holder.CurrCode = (TextView) convertView.findViewById(R.id.currencycode);
			holder.TargetValue = (TextView) convertView.findViewById(R.id.valuetarget);
			holder.SurceRate=(TextView) convertView.findViewById(R.id.sourcetargetrate);
			holder.CountryName = (TextView) convertView.findViewById(R.id.countryname);
			holder.flag = (ImageView) convertView.findViewById(R.id.flag);
			convertView.setTag(holder);

		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		CurrencyConv currency = mCurrencyList.get(position);

		holder.CountryName.setText(currency.getTrgCountryName());
		holder.CurrCode.setText(" " + currency.getTrgCurrISO3());
		
		 NumberFormat f = NumberFormat.getInstance(Locale.US);
		 if (f instanceof DecimalFormat) {
		     ((DecimalFormat) f).setGroupingUsed(true);
		 }
		 
		if(currency.getTrgValue() != -1)
			holder.TargetValue.setText(""+f.format(currency.getTrgValue()));
		else
			holder.TargetValue.setText("--");
		
		holder.SurceRate.setText("[1 "+currency.getTrgCurrISO3()+" = " + (currency.getTrgVSSrcRate()!=-1?f.format(currency.getTrgVSSrcRate()):"--") 
				+" "+ currency.getSrcCurrISO3() + "]");
		holder.flag.setImageResource(mContext.getResources().getIdentifier(currency.getTargFlagIDTrg(), "drawable","com.welmo.travel.tracking"));
		return convertView;
	}
	class ViewHolder {
        TextView CountryName=null;
        TextView CurrCode=null;
        TextView TargetValue=null;
        TextView SurceValue=null;
        TextView SurceRate=null;
        ImageView flag=null;
    }

	public void clearCurrency() {
		mCurrencyList.clear();
		notifyDataSetChanged();
	}
	public void addCurrency(CurrencyConv cr) {
		if(cr == null)
			throw (new IllegalArgumentException("Invalid Currency Object null pointer")); 
		mCurrencyList.add(cr);
		notifyDataSetChanged();
	}
	void readCurrencyFromCursor(Cursor cur){
		String CountryName;
		String CountryISO2;
		String CountryCurrISO3;
		
		mCurrencyList.clear();
		if (cur == null)
			return;
		if (cur.getCount() <= 0)
			return;
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			CountryName=cur.getString(cur.getColumnIndex(CurrenciesISO.COUNTRYNAME));
			CountryISO2=cur.getString(cur.getColumnIndex(CurrenciesISO.COUNTRYCODE));
			CountryCurrISO3=cur.getString(cur.getColumnIndex(CurrenciesISO.CURRCODE));
			CurrencyConv theCurr = new CurrencyConv(CountryName,CountryISO2,CountryCurrISO3);
			theCurr.setSource(cur.getString(cur.getColumnIndex(Currency.CURRSOURCE)),cur.getDouble(cur.getColumnIndex(Currency.RATESRC_VS_TGT)));
			addCurrency(theCurr);
			cur.moveToNext();
		}
	}
	
	double ChangeValueInArray(int ChangeType ,double newValue,int position){
		double source=0.0;
		switch(ChangeType){
		case CHANGED_VALUE_TARGET:
			mCurrencyList.get(position).setTargetValue(newValue);
			source = mCurrencyList.get(position).getSrcValue();
			for(int index = 0; index < mCurrencyList.size(); index++ )
				mCurrencyList.get(index).setSourceValue(source);
			break;
		case CHANGED_RATE_SRC_VS_TRG:
			mCurrencyList.get(position).setSrcVSTrgRate(newValue);
			source = mCurrencyList.get(position).getSrcValue();
			for(int index = 0; index < mCurrencyList.size(); index++ )
				mCurrencyList.get(index).setSourceValue(source);
			break;
		case CHANGED_RATE_TRG_VS_SRC:
			break;
		case CHANGED_VALUE_SOURCE:
			source = newValue;
			for(int index = 0; index < mCurrencyList.size(); index++ )
				mCurrencyList.get(index).setSourceValue(source);
			break;
		default:
			break;
		}
		this.notifyDataSetChanged();
		return source;
	}
}