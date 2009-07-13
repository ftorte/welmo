package com.welmo.travel.tracking;

public class CustomStringBuilder {
 
	public CustomStringBuilder() {
		// TODO Auto-generated constructor stub
	}
	public String compileString(String str, String[] params){
		StringBuilder strBuilder = new StringBuilder(str);
		int index=0;
		int parnum=0;
		while((index = strBuilder.indexOf("?")) != -1){
			if(params.length < parnum)
				break;
			strBuilder.deleteCharAt(index);
			strBuilder.insert(index, params[parnum]);
			parnum++;
		}
		return strBuilder.toString();
	}
	
}
