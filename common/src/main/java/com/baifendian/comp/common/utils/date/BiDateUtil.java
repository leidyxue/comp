package com.baifendian.comp.common.utils.date;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class BiDateUtil {
  private final static FastDateFormat WEEK_FORMAT = FastDateFormat.getInstance("MM/dd");

  public static String getWeekRanger(Date date){
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    Date monday = DateUtils.addDays(date, 0-dayOfWeek);
    Date sunday = DateUtils.addDays(date, 7 - dayOfWeek);

    return WEEK_FORMAT.format(monday) +"~"+WEEK_FORMAT.format(sunday);
  }
}
