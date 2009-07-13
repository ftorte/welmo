package com.welmo.travel.tracking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


class CurrencyConv {
	
	// Target Currency
	private String targFlagIDTrg;
	private String trgCountryName; 	// Ex: USA
	private String trgCountryISO2; 	// Ex: US
	private String trgCurrISO3;		// Ex: USD
	private double trgValue;		// Ex: 134 (USD)
	private double trgVSSrcRate;	// Ex:0.7529
	// Source Currency
	private String srcCurrISO3;		// Ex: EUR
	private double srcValue;		// Ex: 100
	private double srcVSTrgRate; 	// Ex:1,34	


	CurrencyConv(){
		targFlagIDTrg="";
		trgCountryName="";
		trgCountryISO2="";
		trgCurrISO3="";
		trgValue=0;
		trgVSSrcRate=0;

		srcCurrISO3="";
		srcValue=0;
		srcVSTrgRate=0;
	}
	CurrencyConv(String CountryName, String CountryISO2, String tCurrISO3){
		trgCountryName=CountryName;
		trgCountryISO2=CountryISO2;
		targFlagIDTrg = CountryISO2.toLowerCase();
		trgCurrISO3=tCurrISO3;
		trgValue=0;
		trgVSSrcRate=0;

		srcCurrISO3="";
		srcValue=0;
		srcVSTrgRate=0;
	}
	int compareTO(CurrencyConv c){
		
		if(targFlagIDTrg.compareTo(c.targFlagIDTrg)!=0) return-1;
		if(trgCountryName.compareTo(c.trgCountryName)!=0) return-1;
		if(trgCountryISO2.compareTo(c.trgCountryISO2)!=0) return-1;
		if(trgCurrISO3.compareTo(c.trgCurrISO3)!=0) return-1;
		if(trgValue != c.trgValue)return-1;
		if(trgVSSrcRate != c.trgVSSrcRate) return -1;
		
		if(srcCurrISO3.compareTo(c.srcCurrISO3) !=0) return -1;
		if(srcValue != c.srcValue) return -1;
		if(srcVSTrgRate!=c.srcVSTrgRate) return -1;
			
		return 0;
	}
	double Round(double d){
		long l = Math.round(d * 100000);
		return (double) l/100000;	
	}
	void setTarget(String CountryName, String CountryISO2, String tCurrISO3, String sCurrISO3, double rateScrVSTrg)
	{
		trgCountryName=CountryName;
		trgCountryISO2=CountryISO2;
		targFlagIDTrg = CountryISO2.toLowerCase();
		trgCurrISO3=tCurrISO3;
		srcCurrISO3=sCurrISO3;

		srcVSTrgRate = rateScrVSTrg;
		trgVSSrcRate = 1/srcVSTrgRate;
		
		trgValue = Round(srcValue*srcVSTrgRate);
	}
	void setTargetValue(double val)
	{
		trgValue=Round(val);
		srcValue = Round(trgValue*trgVSSrcRate);
	}
	void setSource(String sCurrISO3, double rateScrVSTrg)
	{
		srcCurrISO3=sCurrISO3;
		srcVSTrgRate = rateScrVSTrg;
		if(srcVSTrgRate > 0){
			trgVSSrcRate = 1/srcVSTrgRate;
			trgValue = Round(srcValue*srcVSTrgRate);
		}
		else{
			trgVSSrcRate = -1;
			trgValue = -1;
		}
	}
	void setSourceValue(double val)
	{
		srcValue=val;
		if(trgVSSrcRate != -1)
			trgValue = Round(srcValue*srcVSTrgRate);
		else
			trgValue = -1;
	}
	void setSrcVSTrgRate(double newRate){
		if(newRate >= 0){
			srcVSTrgRate = newRate;
			trgVSSrcRate = 1/srcVSTrgRate;
			trgValue = Round(srcValue*srcVSTrgRate);
		}
		else{
			srcVSTrgRate = -1;
			trgVSSrcRate = -1;
			trgValue = -1;
		}
	}
	public String getTargFlagIDTrg() { return targFlagIDTrg;}
	public String getTrgCountryName() {return trgCountryName;}
	public String getTrgCountryISO2() {return trgCountryISO2;}
	public String getTrgCurrISO3() {return trgCurrISO3;}
	public double getTrgValue(){return trgValue;}
	public double getTrgVSSrcRate() {return trgVSSrcRate;}
	public String getSrcCurrISO3() {return srcCurrISO3;}
	public double getSrcValue() {return srcValue;}
		 
	public double getSrcVSTrgRate() {return srcVSTrgRate;}

	
}