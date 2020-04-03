package com.ous.poc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author abdulhafeez
 *
 */
public class DateUtils {

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date parseDate(String dateString) throws ParseException {
		return DATE_FORMATTER.parse(dateString);
	}
}
