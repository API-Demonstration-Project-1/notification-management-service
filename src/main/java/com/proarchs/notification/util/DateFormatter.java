package com.proarchs.notification.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {

	public static Date format(Long unixTimestamp) throws ParseException {
	   //convert seconds to milliseconds
	   Date date = new Date(unixTimestamp * 1000L); 
	   // format of the date
	   SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	   jdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
	   
	   return jdf.parse(jdf.format(date));
	}
}
