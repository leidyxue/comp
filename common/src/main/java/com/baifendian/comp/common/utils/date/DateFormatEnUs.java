package com.baifendian.comp.common.utils.date;

import com.baifendian.bi.engine.enums.FuncDateType;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatEnUs implements DateFormatUtil{

  private final static Map<Integer, String> monthMap = new HashMap<>();

  static {
    monthMap.put(1, "January");
    monthMap.put(2, "February");
    monthMap.put(3, "March");
    monthMap.put(4, "April");
    monthMap.put(5, "May");
    monthMap.put(6, "June");
    monthMap.put(7, "July");
    monthMap.put(8, "August");
    monthMap.put(9, "September");
    monthMap.put(10, "October");
    monthMap.put(11, "November");
    monthMap.put(12, "December");
  }

  private String quarterly(int value){
    switch (value){
      case 1:
        return "The first quarter";

      case 2:
        return "The second quarter";

      case 3:
        return "The third quater";

      default:
        return "The fourth quarter";
    }
  }

  @Override
  public Object format(int value, FuncDateType dateType) {
    switch (dateType){
      case YEAR:
        return value + " year";

      case QUARTERLY:
        return quarterly(value);

      case MONTH:
        return monthMap.get(value);

      case WEEK:
        return "Week " + value;

      case HOUR:
        return value + " o'clock";
    }
    return value;
  }

  @Override
  public String toYearFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    return year + " year";
  }

  @Override
  public String toQuarterlyFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    return quarterly((month-1)/3+1)+ " of " + year;
  }

  @Override
  public String toMonthFormat(Date date) {
    return TO_MONTH_FORMAT.format(date);
  }

  @Override
  public String toWeekFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int week = cal.get(Calendar.WEEK_OF_YEAR);
    int year = cal.get(Calendar.YEAR);

    return "Week " + week + " " + year + "(" + BiDateUtil.getWeekRanger(date) + ")";
  }

  @Override
  public String toDayFormat(Date date) {
    return TO_DAY_FORMAT.format(date);
  }

  @Override
  public String toHourFormat(Date date) {
    return TO_HOUR_FORMAT.format(date);
  }

  @Override
  public String toMinuteFormat(Date date) {
    return TO_MINUTE_FORMAT.format(date);
  }

  @Override
  public String toSecondFormat(Date date) {
    return TO_SECOND_FORMAT.format(date);
  }

  private final static FastDateFormat TO_MONTH_FORMAT = FastDateFormat.getInstance("MMM YYYY", Locale.US);
  private final static FastDateFormat TO_DAY_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY", Locale.US);
  private final static FastDateFormat TO_HOUR_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY à HH'h'", Locale.US);
  private final static FastDateFormat TO_MINUTE_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY à HH:mm", Locale.US);
  private final static FastDateFormat TO_SECOND_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY à HH:mm:ss", Locale.US);
}
