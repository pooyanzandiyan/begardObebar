package com.jarvissoft.begardobebar.utils;

import java.util.Calendar;

public class DateTimeUtil 
{
        static int[] KHAYYAM_TABLE = new int[]{
          5,       9,     13,     17,     21,     25,     29,
         34,      38,     42,     46,     50,     54,     58,     62,
         67,      71,     75,     79,     83,     87,     91,     95,
        100,     104,    108,    112,    116,    120,    124
        };
        static short[] month = new short[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        public static Calendar solarToGeorgian(SolarDate solarDate){
            int aYear = solarDate.Year;
            int aMonth = solarDate.Month;
            int aDay = solarDate.Day;
            int aHour = solarDate.Hour;
            int aMin = solarDate.Minute;
            int aSec = solarDate.Second;

            int Year = aYear, Month, Day;

            int EY = ExceptionYear(Year);

            int yDay = aMonth < 7 ? ((aMonth - 1) * 31 + aDay) : (186 + (aMonth - 7) * 30 + aDay);
            if (yDay > 286 - EY)
            {
                Year += 622;
                yDay -= 286 - EY;
            }
            else
            {
                Year += 621;
                yDay += 79 + EY + (georgianLeap(Year) == KWYearTypeEnum.KW_LEAP ? 1 : 0);
            }

            short MK = georgianLeap(Year) == KWYearTypeEnum.KW_LEAP ? (short)1 : (short)0;
            short myDay = (short)(MK + 28);
            short[] newMonthArray = new short[month.length];
            System.arraycopy(month, 0, newMonthArray, 0, newMonthArray.length);
            newMonthArray[1] = myDay;

            for (Month = 0; yDay > newMonthArray[Month]; Month++)
                yDay -= newMonthArray[Month];

            Month++;
            Day = yDay;
            Calendar cl =  Calendar.getInstance();
            cl.set(Year, Month-1, Day, aHour, aMin, aSec);
            return cl;
        }
        /*
         * dateSolar in yyyyMMddhhmmss format
         */
        public static long convertSolarDateTimeStringToTime(String dateSolar) {
    		int year = Integer.parseInt(dateSolar.substring(0,4));
    		int month = Integer.parseInt(dateSolar.substring(4,6));
    		int day = Integer.parseInt(dateSolar.substring(6,8));
    		int hour = Integer.parseInt(dateSolar.substring(8,10));
    		int minute = Integer.parseInt(dateSolar.substring(10,12));
    		int second = Integer.parseInt(dateSolar.substring(12,14));
    		long responseDateLong = DateTimeUtil.solarToGeorgian(new SolarDate(year, month, day, hour, minute, second)).getTimeInMillis();
    		return responseDateLong;
    	}
        
        
        public static String convertLongToSolarDateTime(long time,String pattern) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTimeInMillis(time);
    		SolarDate solarDate = DateTimeUtil.georgianToSolar(cal);
    		
    		//return solarDate.toString("yyyy/MM/dd HH:mm:ss");
    		return solarDate.toString(pattern);
    	}
        
        public static int daysOfSolarYear(int aYear)
        {
            return solarLeap(aYear) == KWYearTypeEnum.KW_NATURAL ? 365 : 366;
        }
        private static Calendar decDate(Calendar baseDateTime, int aYear, int aMonth, int aDay)
        {
            int t_Year, t_Month, t_Day;
            SolarDate shDateTime = georgianToSolar(baseDateTime);
            t_Year = shDateTime.Year;
            t_Month = shDateTime.Month;
            t_Day = shDateTime.Day;

            boolean bMnthLastDay = false;
            if (t_Day == daysOfSolarMonth(t_Year, t_Month))
                bMnthLastDay = true;

            t_Year -= aYear;
            t_Month -= aMonth;
            while (t_Month < 1)
            {
                t_Month += 12;
                t_Year--;
            }
            if (t_Day > daysOfSolarMonth(t_Year, t_Month) || bMnthLastDay)
                t_Day = daysOfSolarMonth(t_Year, t_Month);

            while (aDay >= t_Day)
            {
                aDay -= t_Day;
                if (--t_Month < 1)
                {
                    t_Year--;
                    t_Month = 12;
                }
                t_Day = daysOfSolarMonth(t_Year, t_Month);
            }
            t_Day -= aDay;
            SolarDate result = new SolarDate();
            result.Year = t_Year;
            result.Month = t_Month;
            result.Day = t_Day;
            result.Hour = baseDateTime.get(Calendar.HOUR_OF_DAY);
            result.Minute = baseDateTime.get(Calendar.MINUTE);
            result.Second = baseDateTime.get(Calendar.SECOND);
            return solarToGeorgian(result);
        }
        private static Calendar incDate(Calendar baseDateTime, int aYear, int aMonth, int aDay)
        {

            int t_Year, t_Month, t_Day;
            SolarDate shDateTime = georgianToSolar(baseDateTime);
            t_Year = shDateTime.Year;
            t_Month = shDateTime.Month;
            t_Day = shDateTime.Day;

            boolean bMnthLastDay = false;
            if (t_Day == daysOfSolarMonth(t_Year, t_Month))
                bMnthLastDay = true;

            t_Year += aYear;
            t_Month += aMonth;
            while (t_Month > 12)
            {
                t_Month -= 12;
                t_Year++;
            }
            if (t_Day > daysOfSolarMonth(t_Year, t_Month) || bMnthLastDay)
                t_Day = daysOfSolarMonth(t_Year, t_Month);

            while (aDay > (daysOfSolarMonth(t_Year, t_Month) - t_Day))
            {
                aDay -= daysOfSolarMonth(t_Year, t_Month) - t_Day + 1;
                if (++t_Month > 12)
                {
                    t_Year++;
                    t_Month = 1;
                }
                t_Day = 1;
            }
            t_Day += aDay;
            SolarDate resultDateTime = new SolarDate();
            resultDateTime.Year = t_Year;
            resultDateTime.Month = t_Month;
            resultDateTime.Day = t_Day;
            resultDateTime.Hour = baseDateTime.get(Calendar.HOUR_OF_DAY);
            resultDateTime.Minute = baseDateTime.get(Calendar.MINUTE);
            resultDateTime.Second = baseDateTime.get(Calendar.SECOND);
            return solarToGeorgian(resultDateTime);
        }
        private static int Calc_DM(int ydays, char aDM)
        {
            //ASSERT( aDM == 'D' || aDM == 'M' );

            int k;
            if (aDM == 'D')
            {
                if (ydays < 187)
                {
                    k = ydays % 31;
                    if (k == 0) k = 31;
                }
                else
                {
                    ydays -= 186;
                    k = (ydays % 30);
                    if (k == 0) k = 30;
                }
                return k;
            }

            // month number calculation
            if (ydays < 187)
            {
                return (ydays % 31) != 0 ? (1 + ydays / 31) : (ydays / 31);
            }

            ydays -= 186;
            return (ydays % 30) != 0 ? (7 + ydays / 30) : (6 + ydays / 30);
        }
        public static Calendar incrementDateTime(Calendar baseDateTime, int year, int month, int day)
        {
            return incDate(baseDateTime, year, month, day);
        }
        public static Calendar decrementDateTime(Calendar baseDateTime, int year, int month, int day)
        {
            return decDate(baseDateTime, year, month, day);
        }
        public static int daysOfSolarMonth(int aYear, int aMonth)
        {
            return aMonth <= 6 ? 31 : aMonth < 12 ? 30 : solarLeap(aYear) == KWYearTypeEnum.KW_NATURAL ? 29 : 30;
        }
        public static Calendar getFirstDateTimeOf(Calendar baseDate)
        {
            SolarDate solarDateTime = georgianToSolar(baseDate);
            solarDateTime.Month = 1;
            solarDateTime.Day = 1;
            solarDateTime.Hour = 0;
            solarDateTime.Minute = 0;
            solarDateTime.Second = 0;
            return solarToGeorgian(solarDateTime);
        }
        public static Calendar getLastDateTimeOf(Calendar baseDate)
        {
            SolarDate solarDateTime = georgianToSolar(baseDate);
            solarDateTime.Year = solarDateTime.Year + 1;
            solarDateTime.Month = 1;
            solarDateTime.Day = 1;
            solarDateTime.Hour = 23;
            solarDateTime.Minute = 59;
            solarDateTime.Second = 59;
            Calendar dateTime = solarToGeorgian(solarDateTime);
            return decrementDateTime(dateTime, 0, 0, 1);
        }
        double   getDays(Calendar  aToDate , Calendar  aFromDate)
        {
        	return 0;
        }
        public static double monthsBetween(Calendar aFromDate, Calendar aToDate, boolean aNeedRound)
        {
        	aFromDate = Calendar.getInstance();
        	aFromDate.set(aFromDate.get(Calendar.YEAR),aFromDate.get(Calendar.MONTH),aFromDate.get(Calendar.DATE));

        	aToDate = Calendar.getInstance();
        	aToDate.set(aToDate.get(Calendar.YEAR),aToDate.get(Calendar.MONTH),aToDate.get(Calendar.DATE));

            if (aToDate.compareTo(aFromDate)==0)
                return 0;
            if (aToDate.compareTo(aFromDate)<0)
                return -monthsBetween(aToDate, aFromDate, aNeedRound);
            
            
          	 long totalDay = (aToDate.getTimeInMillis()-aFromDate.getTimeInMillis())/1000/3600/24;
            double dMonths = Math.round((float) totalDay / 30);

            Calendar tmpToDate1, tmpToDate2;

            while (true)
            {

                tmpToDate1 = incDate(aFromDate, 0, (int)dMonths, 0);

                tmpToDate2 = incDate(aFromDate, 0, (int)(dMonths) + 1, 0);

                if (tmpToDate1.compareTo(aToDate)<=0 && aToDate.compareTo(tmpToDate2)<0)

                    break;

                if (aToDate.compareTo(tmpToDate1)<0)

                    dMonths -= 1;

                else

                    dMonths += 1;

            }

        	long totalDay2 = (aToDate.getTimeInMillis()-aFromDate.getTimeInMillis())/1000/3600/24;

            
            dMonths += (double)(totalDay2) / 31;

            if (!aNeedRound)

                return dMonths;



            int fday = georgianToSolar(aFromDate).Day;

            int tday = georgianToSolar(aToDate).Day;

            if (fday >= 29 && fday <= tday)

                return Math.round(dMonths);

            return dMonths;

        }
        public static SolarDate georgianToSolar(int iyear , int iMonth , int iDay , int iHour , int iMinute , int iSecond)
        {
            SolarDate solarDateTime = new SolarDate();
            int Year = iyear;
            int Month = iMonth;
            int Day = iDay;

            int MK = georgianLeap(Year) == KWYearTypeEnum.KW_LEAP ? 1 : 0;
            int SK = solarLeap(Year - 622) == KWYearTypeEnum.KW_LEAP ? 1 : 0;
            int EY = ExceptionYear(Year - 622);


            short myDay = (short)(MK + 28);
            short[] newMonthArray = new short[month.length];
            System.arraycopy(month, 0, newMonthArray, 0, newMonthArray.length);
            newMonthArray[1] = myDay;

            int Sday = 0;
            for (int i = 0; i < Month - 1; i++)
                Sday += newMonthArray[i];
            Sday += Day;

            if (Sday > (79 + EY + SK))
            {
                solarDateTime.Year = Year - 621;
                Sday -= 79 + EY + SK;
            }
            else
            {
                solarDateTime.Year = Year - 622;
                Sday += 286 - EY;
            }

            solarDateTime.Month = Calc_DM(Sday, 'M');
            solarDateTime.Day = Calc_DM(Sday, 'D');
            solarDateTime.Hour = iHour;
            solarDateTime.Minute = iMinute;
            solarDateTime.Second = iSecond;
            return solarDateTime;
        	
        }
        public static SolarDate georgianToSolar(Calendar dateTime)
        {
        	return georgianToSolar(dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH)+1,dateTime.get(Calendar.DATE),dateTime.get(Calendar.HOUR_OF_DAY),dateTime.get(Calendar.MINUTE),dateTime.get(Calendar.SECOND));
        }

        private static KWYearTypeEnum solarLeap(int year)
        {
            int dd;
            if (year >= 474)
            {
                dd = (year - 474) % 128;
                if (dd == 0)
                    return KWYearTypeEnum.KW_LEAP;
            }
            else
                dd = (year >= 342) ? (year - 342) : (128 - (374 - year) % 128);

            for (int i = 0; i < 30; i++)
            {
                if (KHAYYAM_TABLE[i] == dd)
                    return KWYearTypeEnum.KW_LEAP;
            }
            return KWYearTypeEnum.KW_NATURAL;
        }
        private static KWYearTypeEnum georgianLeap(int year)
        {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
                return KWYearTypeEnum.KW_LEAP;
            return KWYearTypeEnum.KW_NATURAL;
        }
        private static int ExceptionYear(int Year)
        {
            if (Year == 1380)
                return 0;

            int MK = 0, SK = 0;
            if (Year > 1380)
            {
                for (int i = 1380; i < Year; i++)
                {
                    if (solarLeap(i) == KWYearTypeEnum.KW_LEAP)
                        SK++;
                    if (georgianLeap(i + 622) == KWYearTypeEnum.KW_LEAP)
                        MK++;
                }
                return SK - MK;
            }
            // Year < 1380
            for (int i = Year; i < 1380; i++)
            {
                if (solarLeap(i) == KWYearTypeEnum.KW_LEAP)
                    SK++;
                if (georgianLeap(i + 622) == KWYearTypeEnum.KW_LEAP)
                    MK++;
            }
            return MK - SK;
        }


}
enum KWYearTypeEnum
{
    KW_NATURAL, 
    KW_LEAP ,
}
