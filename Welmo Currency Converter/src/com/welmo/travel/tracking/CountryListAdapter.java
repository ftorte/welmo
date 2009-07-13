package com.welmo.travel.tracking;

import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.welmo.contents.ExpensesManager.CurrenciesColumn;
import com.welmo.contents.ExpensesManager.CurrenciesISO;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


class CountryListAdapter extends BaseAdapter implements Filterable{
	
	protected class Country {
		public String name;
		public String flag;
		public String countrycode;
		public boolean active;
		public boolean selected;
		public boolean changed;
		Country(String theName,String theISO2Code,boolean isSelected){
			name = theName;
			countrycode = theISO2Code;
			flag = theISO2Code.toLowerCase();
			selected =isSelected;
			changed = false;
			active = true;
		}
		void copy(Country other){
			name = other.name;
			flag = other.flag;
			selected= other.selected;
			countrycode=other.countrycode;
			changed = true;
			active = other.active;
		}
	}
	
	private LayoutInflater mInflater;
	public ArrayList<Country> mCountryList = new ArrayList<Country>();
	
	// Varaibles to implement filterable
	private final Object mLock = new Object();
	private ArrayList<Country> mOriginalValues;
	private CountryListAdapterFilter mFilter;
	//-------------------------------------------------
	private Context mContext;
	
	public CountryListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return mCountryList.size();
	}
	public Object getItem(int position) {
		if(position < mCountryList.size())
			return mCountryList.get(position);
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
			convertView = mInflater.inflate(R.layout.imagelistmultiplecheck, null);
			holder.countryName = (TextView) convertView.findViewById(R.id.countryname);
			holder.flag = (ImageView) convertView.findViewById(R.id.flag);
			holder.selectedYesNo = (ImageView) convertView.findViewById(R.id.selected);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		Country country = mCountryList.get(position);

		//Country Name	
		holder.countryName.setText(country.name);
		//Select Check Box
		if(country.selected)
			holder.selectedYesNo.setImageResource(android.R.drawable.checkbox_on_background);
		else
			holder.selectedYesNo.setImageResource(android.R.drawable.checkbox_off_background);
		//Country_flag
		holder.flag.setImageResource(mContext.getResources().getIdentifier(country.flag, "drawable","com.welmo.travel.tracking"));
		//Change Backgroud if not active
		if(!country.active)
			convertView.setBackgroundResource(R.color.grey_clear);
		else
			convertView.setBackgroundResource(R.color.grey_dark);
		return convertView;
	}
	class ViewHolder {
        ImageView flag=null;
        TextView countryName=null;
        ImageView selectedYesNo=null;
    }
	public void clear() {
		mCountryList.clear();
		notifyDataSetChanged();
	}
	public void addItem(Country cr) {
		if(cr == null)
			throw (new IllegalArgumentException("Invalid Currency Object null pointer")); 
		mCountryList.add(cr);
		notifyDataSetChanged();
	}
	public void changeItem(Country cr, int postion) {
		if(cr == null || postion <0 || postion  > mCountryList.size()-1)
			throw (new IllegalArgumentException("Invalid Currency Object null pointer")); 
		mCountryList.get(postion).copy(cr);
		notifyDataSetChanged();
	}
	void readCurrencyFromCursor(Cursor cur){
		String CountryName;
		String CountryISO2;
		boolean ContrySelected;
		
		mCountryList.clear();
		if (cur.getCount() <= 0)
			return;
		cur.moveToFirst();
		while (!cur.isAfterLast())
		{
			String val;
			CountryName=cur.getString(cur.getColumnIndex(CurrenciesISO.COUNTRYNAME));
			CountryISO2=cur.getString(cur.getColumnIndex(CurrenciesISO.COUNTRYCODE));
			ContrySelected = (cur.getInt(cur.getColumnIndex(CurrenciesISO.SELECTED))==1?true:false);
			Country theCountry = new Country(CountryName,CountryISO2,ContrySelected);
			if((val = cur.getString(cur.getColumnIndex(CurrenciesColumn.CURRSOURCE))) == null)
				theCountry.active = false;
			else
				theCountry.active = true;
			addItem(theCountry);
			cur.moveToNext();
		}
	}
	
	void filterComplex(String [] filter){
		if(filter[0] == "yes")
			((CountryListAdapterFilter)getFilter()).setFilterNotActive(true);
		else
			((CountryListAdapterFilter)getFilter()).setFilterNotActive(false);
		getFilter().filter(filter[1]);
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new CountryListAdapterFilter();
		}
		return mFilter;
	}

	/**
	 * <p>An array filters constrains the content of the array adapter with
	 * a prefix. Each item that does not start with the supplied prefix
	 * is removed from the list.</p>
	 */
	private class CountryListAdapterFilter extends Filter {
		// custom specific filters
		protected boolean filterNotActive = false;

		public void  setFilterNotActive(boolean yes_no){
			filterNotActive = yes_no;
		}
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<Country>(mCountryList);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					ArrayList<Country> list = new ArrayList<Country>(mOriginalValues);
					results.values = list;
					results.count = list.size();
					if(filterNotActive){
						for (int i = list.size()-1; i >= 0; i--) {
							if(!(list.get(i).active))
								list.remove(i);
						}
						results.values = list;
						results.count = list.size();
					}
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();

				final ArrayList<Country> values = mOriginalValues;
				final int count = values.size();
				final ArrayList<Country> newValues = new ArrayList<Country>(count);

				for (int i = 0; i < count; i++) {
					final Country value = values.get(i);
					final String valueText = value.name.toString().toLowerCase();

					// filter not active country if filter is active
					if(filterNotActive && !(value.active))
						continue;

					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				// filter not active country if filter is active
				if(filterNotActive){
					for (int i = newValues.size()-1; i >= 0; i--) {
						if(!(newValues.get(i).active))
							newValues.remove(i);
					}
					results.values = newValues;
					results.count = newValues.size();
				}
			}
			return results;
		}

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
        	mCountryList = (ArrayList<Country>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}