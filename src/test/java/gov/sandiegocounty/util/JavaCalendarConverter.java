package gov.sandiegocounty.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 
 * @author David Parker (dfparker@aemintegrators.com)
 * 
 * ...................
 * .. HISTORY
 * ...................
 *  2018-10-15 add ASP simple date parse version
 * 
 * ...................
 * .. REFS
 * ...................
 * https://stackoverflow.com/questions/36639154/convert-java-util-date-to-what-java-time-type#36639155
 * https://stackoverflow.com/questions/16336643/parsing-iso-8601-datetime-in-java
 *
 */
public class JavaCalendarConverter {

	public final static String asp_date = "7/8/2018 12:30:00 PM PDT";
	public final static String asp_pattern = "M/d/yyyy HH:mm:ss a z";

	public final static String str_date = "2018-11-01T00:04:00.000-07:00";//"2018-10-03T00:33:37.409-07:00";
	public final static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";


	public static Calendar stringToCalendar(String stringDate, String datePattern) {

		if (stringDate == null) {
			return null;
		}

		Calendar calendar = new GregorianCalendar();
////////////
//System.err.println( " JavaCalendarConverter :: stringToCalendar " + stringDate);
//System.err.println( " JavaCalendarConverter :: datePattern " + datePattern);
////////////
		ZonedDateTime odt = ZonedDateTime.parse( stringDate );
//System.out.println( " JavaCalendarConverter :: stringToCalendar zdt " + odt);
		calendar = java.util.GregorianCalendar.from( odt ); // Produces an instant of `GregorianCalendar` which implements `Calendar` interface.
		calendar.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));


		return calendar;
	}

	public static Calendar stringToSimpleDate(String stringDate, String datePattern) throws ParseException {

		DateFormat df = new SimpleDateFormat( asp_pattern );
		Calendar cal  = Calendar.getInstance();
		cal.setTime(df.parse(stringDate));
		return cal;
	}

	/**
	 * @param calendar
	 * @param datePattern
	 * @return
	 */
	public static String calendarToString(Calendar calendar, String datePattern) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
		String calendarString = simpleDateFormat.format(calendar.getTime());

		return calendarString;
	}

/**
 * @param args
 */
	public static void main(String[] args) {
//		./datetime: 2018-10-13T09:34:00.000-07:00
//		./datetime@TypeHint: Date
		Calendar c = JavaCalendarConverter.stringToCalendar("2018-10-13T09:34:00.000-07:00", pattern);
		System.out.println( " JavaCalendarConverter :: main " + c);
		System.out.println( " JavaCalendarConverter :: main " + JavaCalendarConverter.calendarToString(c, pattern));

//		Calendar c = null;
		try {
			c = JavaCalendarConverter.stringToSimpleDate(asp_date, asp_pattern);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf( " JavaCalendarConverter :: main %s", c.getTime());
		c=null;
	}
}