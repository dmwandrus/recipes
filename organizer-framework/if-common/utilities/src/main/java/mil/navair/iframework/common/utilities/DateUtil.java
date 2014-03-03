/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.navair.iframework.common.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mweigel
 */
public class DateUtil {
    // 2012-07-23T13:32:04.394-05:00

    public static final String FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    // 2009-09-24T17:07:21.622Z
    public static final String FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    // 20120704120856
    public static final String FORMAT3 = "yyyyMMddHHmmss";
    // 20120704120856-0700
    public static final String FORMAT4 = "yyyyMMddHHmmssZ";
    // 2001-07-04 12:08:56
    public static final String FORMAT5 = "yyyy-MM-dd HH:mm:ss";
    // 2001-07-04 12:08:56-0700
    public static final String FORMAT6 = "yyyy-MM-dd HH:mm:ssZ";
    // 2012-07-23 13:32:04.394-05:00
    public static final String FORMAT7 = "yyyy-MM-dd HH:mm:ss.SSSZ";
    // 2001-07-04
    public static final String FORMAT8 = "yyyy-MM-dd";
    // 13:32:04
    public static final String FORMAT9 = "HH:mm:ss";
    // 13:32:04-05:00
    public static final String FORMAT10 = "HH:mm:ssZ";
    // 13:32:04.394-05:00
    public static final String FORMAT11 = "HH:mm:ss.SSSZ";
    // 201207041208
    public static final String FORMAT12 = "yyyyMMddHHmm";
    // 2012070412
    public static final String FORMAT13 = "yyyyMMddHH";
    private static SimpleDateFormat formatter = new SimpleDateFormat();
    private static final Logger logger = Logger.getLogger(DateUtil.class.getName());
    private static final Level LOG_LEVEL = Level.INFO;

    @SuppressWarnings("CallToThreadDumpStack")
    public static Date getDate(String sourceDateString, String format) {
        Date date = null;

        formatter.applyPattern(format);

        try {
            date = formatter.parse(sourceDateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
            logger.log(LOG_LEVEL, " DateUtil::ParseException ", ex.toString());
        }

        return (date);
    }

    public static String getDateString(String sourceDateString, String format) {
        Date date = getDate(sourceDateString, format);

        return (date.toString());
    }

    public static String getDateString(Date date, String format) {
        formatter.applyPattern(format);

        String dateString = formatter.format(date);

        return (dateString);
    }

    public static String getDateString(long milliseconds, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        Date date = cal.getTime();
        return (getDateString(date, format));
    }

    public static Date getDate(long milliseconds, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        Date date = cal.getTime();
        return (date);
    }

    public static long getDateMilliSeconds(String sourceDateString, String format) {
        Date date = getDate(sourceDateString, format);

        return (date.getTime());
    }
}
