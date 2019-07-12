package com.jarvissoft.begardobebar.utils;

public class SolarDate {
	 public SolarDate()
     {
     }
     public SolarDate(int year , int month , int day, int hour , int minute , int second)
     {
         Year = year;
         Month = month;
         Day = day;
         Hour = hour;
         Minute = minute;
         Second = second;
     }
     public Integer Year;
     public Integer Month;
     public Integer Day;
     public Integer Hour;
     public Integer Minute;
     public Integer Second;
    
 	public static String padleft(String s, int len, char c)
 	{
 		 if (s.length() > len)
 			 return s; 
 		 StringBuilder d = new StringBuilder(len);
 		int fill = len - s.length();
 		while (fill-- > 0)
 			d.append(c);
 		d.append(s);
 		return d.toString();
 	}
     

 	public String  getFullText()
     {
         return  toString("YYYY/MM/dd HH:mm:ss");
     }
     @Override
		public  String toString()
     {
         return  toString("YYYY/MM/dd");
     }
     public  String toString(String pattern)
     {
         String rVal = pattern;
     	if(pattern.contains("YYYY"))
         {
         	rVal = rVal.replace("YYYY", padleft(Year.toString(), 4, '0'));
         }
     	if(pattern.contains("yyyy"))
         {
         	rVal = rVal.replace("yyyy", padleft(Year.toString(), 4, '0'));
         }
     	if(pattern.contains("MM"))
         {
         	rVal = rVal.replace("MM", padleft(Month.toString(), 2, '0'));
         }
     	if(pattern.contains("dd"))
         {
         	rVal = rVal.replace("dd", padleft(Day.toString(), 2, '0'));
         }
     	if(pattern.contains("HH"))
         {
         	rVal = rVal.replace("HH", padleft(Hour.toString(), 2, '0'));
         }
     	if(pattern.contains("hh"))
         {
         	rVal = rVal.replace("hh", padleft(Hour>=12 ? ((Integer)(Hour-12)).toString():Hour.toString(), 2, '0'));
         }

     	if(pattern.contains("mm"))
         {
         	rVal = rVal.replace("mm", padleft(Minute.toString(), 2, '0'));
         }
     	if(pattern.contains("ss"))
         {
         	rVal = rVal.replace("ss", padleft(Second.toString(), 2, '0'));
         }
     	return rVal;
     }
     
}

