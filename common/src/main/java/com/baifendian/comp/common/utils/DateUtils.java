package com.baifendian.comp.common.utils;

/**
 * Created by xuelei on 2017/10/18 0018.
 */

import com.baifendian.bi.engine.enums.FuncDateType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * 时间操作工具类
 * <p>
 *
 * @author : dsfan
 * @date : 2016年8月23日
 */
public class DateUtils {

  /** 1 Day in Millis */
  public static final long DAY = 24L * 60L * 60L * 1000L;

  /** 1 Week in Millis */
  public static final long WEEK = 7 * DAY;

  /* An array of custom date formats */
  private static final DateFormat[] CUSTOM_DATE_FORMATS;

  /* The Default Timezone to be used */
  private static final TimeZone TIMEZONE = TimeZone.getTimeZone("UTC"); //$NON-NLS-1$


  public static boolean isValidDate(String str) {
    boolean convertSuccess = false;
    int i = 0;
    while (i < CUSTOM_DATE_FORMATS.length) {
      try {
        // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        CUSTOM_DATE_FORMATS[i].setLenient(false);
        CUSTOM_DATE_FORMATS[i].parse(str);
        convertSuccess = true;
        i++;
      } catch (ParseException e) {
        // e.printStackTrace();
        // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
        i++;
      }
    }

    return convertSuccess;
  }

  /**
   * Tries different date formats to parse against the given string
   * representation to retrieve a valid Date object.
   *
   * @param strdate
   *            Date as String
   * @return Date The parsed Date
   */
  public static Date parseDate(String strdate) {

        /* Return in case the string date is not set */
    if (strdate == null || strdate.length() == 0) return null;

    Date result = null;
    strdate = strdate.trim();
    if (strdate.length() > 10) {

            /* Open: deal with +4:00 (no zero before hour) */
      if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate.substring(strdate.length() - 5).indexOf("-") == 0) && strdate.substring(strdate.length() - 5).indexOf(":") == 2) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String sign = strdate.substring(strdate.length() - 5, strdate.length() - 4);
        strdate = strdate.substring(0, strdate.length() - 5) + sign + "0" + strdate.substring(strdate.length() - 4); //$NON-NLS-1$
      }

      String dateEnd = strdate.substring(strdate.length() - 6);

            /*
             * try to deal with -05:00 or +02:00 at end of date replace with -0500 or
             * +0200
             */
      if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0) && dateEnd.indexOf(":") == 3) { //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        if (!"GMT".equals(strdate.substring(strdate.length() - 9, strdate.length() - 6))) { //$NON-NLS-1$
          String oldDate = strdate;
          String newEnd = dateEnd.substring(0, 3) + dateEnd.substring(4);
          strdate = oldDate.substring(0, oldDate.length() - 6) + newEnd;
        }
      }
    }

        /* Try to parse the date */
    int i = 0;
    while (i < CUSTOM_DATE_FORMATS.length) {
      try {

                /*
                 * This Block needs to be synchronized, because the parse-Method in
                 * SimpleDateFormat is not Thread-Safe.
                 */
        synchronized (CUSTOM_DATE_FORMATS[i]) {
          return CUSTOM_DATE_FORMATS[i].parse(strdate);
        }
      } catch (ParseException e) {
        i++;
      } catch (NumberFormatException e) {
        i++;
      }
    }
    return result;
  }

  /** Initialize the array of common date formats and formatter */
  static {

        /* Create Date Formats */
    final String[] possibleDateFormats = {
                /* RFC 1123 with 2-digit Year */"EEE, dd MMM yy HH:mm:ss z",
                /* RFC 1123 with 4-digit Year */"EEE, dd MMM yyyy HH:mm:ss z",
                /* RFC 1123 with no Timezone */"EEE, dd MMM yy HH:mm:ss",
                /* Variant of RFC 1123 */"EEE, MMM dd yy HH:mm:ss",
                /* RFC 1123 with no Seconds */"EEE, dd MMM yy HH:mm z",
                /* Variant of RFC 1123 */"EEE dd MMM yyyy HH:mm:ss",
                /* RFC 1123 with no Day */"dd MMM yy HH:mm:ss z",
                /* RFC 1123 with no Day or Seconds */"dd MMM yy HH:mm z",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ssZ",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss'Z'",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:sszzzz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss z",
                /* ISO 8601 */"yyyy-MM-dd'T'HH:mm:ssz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss.SSSz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HHmmss.SSSz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss",
                /* ISO 8601 w/o seconds */"yyyy-MM-dd'T'HH:mmZ",
                /* ISO 8601 w/o seconds */"yyyy-MM-dd'T'HH:mm'Z'",
                "yyyy-MM-dd HH:mm:ss",
                "HH:mm:ss",
                /* RFC 1123 without Day Name */"dd MMM yyyy HH:mm:ss z",
                /* RFC 1123 without Day Name and Seconds */"dd MMM yyyy HH:mm z",
                /* Simple Date Format */"yyyy-MM-dd",
        /* Simple Date Format */"yyyy/MM/dd",
        /* Simple Date Format */"yyyy年MM月dd日",
                /* Simple Date Format */"MMM dd, yyyy" };

        /* Create the dateformats */
    CUSTOM_DATE_FORMATS = new SimpleDateFormat[possibleDateFormats.length];

    for (int i = 0; i < possibleDateFormats.length; i++) {
      CUSTOM_DATE_FORMATS[i] = new SimpleDateFormat(possibleDateFormats[i], Locale.ENGLISH);
      CUSTOM_DATE_FORMATS[i].setTimeZone(TIMEZONE);
    }
  }


  public static Date millisecond2Date(Long ms){
    return ms==null? null:new Date(ms);
  }

  /**
   * 获取格式化的日期字符串
   *
   * @return 日期字符串
   */
  public static String format(Date date, String formatString) {
    FastDateFormat format = FastDateFormat.getInstance(formatString);
    return format.format(date);
  }

  public static String formatBaseDate(Date date) {
    return TODAY_END2.format(date);
  }

  private static DateFormat TODAY_START = new SimpleDateFormat("yyyy-MM-dd");
  private static DateFormat TODAY_END = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
  private static DateFormat TODAY_END2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


  public static final FastDateFormat BASE_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

  public static String getTodayDate() {
    return BASE_DATE_FORMAT.format(new Date());
  }



  public static long getTodayEnd() {
    try {
      return TODAY_END2.parse(TODAY_END.format(new Date())).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return 0;
  }

  public static long getDayEnd(Date date){
    try {
      return TODAY_END2.parse(TODAY_END.format(date)).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return 0;
  }

  public static long getTodayStart() {
    try {
      return TODAY_START.parse(TODAY_START.format(new Date())).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return 0;
  }

  public static long getDayStart(Date date){
    try {
      return TODAY_START.parse(TODAY_START.format(date)).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return 0;
  }

  /**
   * 获取系统默认时区
   * <p>
   */
  public static String getDefaultTimeZome() {
    return TimeZone.getDefault().getID();
  }


  /**
   * 获取当前时间指定格式的日期字符串
   * <p>
   *
   * @return 日期字符串
   */
  public static String now(String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(new Date());
  }



  /**
   * 通过字符串获取日期
   *
   * @return 日期
   */

  public static Date parse(String dateStr, String formatString) {
    try {
      DateFormat formatter = new SimpleDateFormat(formatString);

      return formatter.parse(dateStr);
    } catch (ParseException e) {
      throw new RuntimeException("时间转换失败异常", e);
    }
  }

  public static Date parseUtc(String dateStr, String formatString) {
    try {
      DateFormat formatter = new SimpleDateFormat(formatString);
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

      return formatter.parse(dateStr);
    } catch (ParseException e) {
      throw new RuntimeException("时间转换失败异常", e);
    }
  }


  public static int dayDiff(Date startDate, Date endDate) {
    String startStr = TODAY_START.format(startDate);
    String endStr = TODAY_START.format(endDate);

    DateTime start = null;
    try {
      start = new DateTime(TODAY_START.parse(startStr));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    DateTime end = null;
    try {
      end = new DateTime(TODAY_START.parse(endStr));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return Days.daysBetween(start, end).getDays();
  }

  public String dateFormat(Date date, FuncDateType funcDateType){
    switch (funcDateType){
      case TO_YEAR:
        return "";
    }

    return null;
  }

  public static void main(String[] args) {
    String dateStr = "2017/11/21";
//    Date d = DateUtils.parseDate("Tue, 9 Nov 2010 12:45:00 GMT");
//    Date d = DateUtils.parseDate(dateStr);
//    Date d = DateUtils.parseDate("2017/09/08 11:45:09");
//    Date d = DateUtils.parseDate("2017年09月09号");
//    System.out.println(formatBaseDate(d));
    if (DateUtils.isValidDate(dateStr)){
      System.out.println("YES");
    }else {
      System.out.println("NO");
    }

  }
}
