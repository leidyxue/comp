package com.baifendian.comp.common.utils.date;

import com.baifendian.bi.engine.enums.FuncDateType;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatFrFr implements DateFormatUtil{

  private final static Map<Integer, String> monthMap = new HashMap<>();

  static {
    monthMap.put(1, "Janvier");
    monthMap.put(2, "Février");
    monthMap.put(3, "Mars");
    monthMap.put(4, "Avril");
    monthMap.put(5, "Mai");
    monthMap.put(6, "Juin");
    monthMap.put(7, "Juillet");
    monthMap.put(8, "Août");
    monthMap.put(9, "Septembre");
    monthMap.put(10, "Octobre");
    monthMap.put(11, "Novembre");
    monthMap.put(12, "Décembre");
  }

  private String quarterly(int value){
    switch (value){
      case 1:
        return "le premier trimestre";

      case 2:
        return "le deuxième trimestre";

      case 3:
        return "le troisième trimestre";

        default:
          return "le quatrième trimestre";
    }
  }

  @Override
  public Object format(int value, FuncDateType dateType) {
    switch (dateType){
      case YEAR:
        return "en " + value;

      case QUARTERLY:
        return quarterly(value);

      case MONTH:
        return monthMap.get(value);

      case HOUR:
        return value + "h";
    }
    return value;
  }

  @Override
  public String toYearFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);

    return "en "+year;
  }

  @Override
  public String toQuarterlyFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int month = cal.get(Calendar.MONTH);
    int year = cal.get(Calendar.YEAR);

    return MessageFormat.format(QUARTERLY_TEXT, String.valueOf(year), quarterly((month-1)/3+1));
  }

  @Override
  public String toMonthFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int month = cal.get(Calendar.MONTH);
    int year = cal.get(Calendar.YEAR);
    return MessageFormat.format(TO_MONTH_TEXT, String.valueOf(year), monthMap.get(month).toLowerCase());
  }

  @Override
  public String toWeekFormat(Date date) {
    // TODO
    return date.toString();
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
  private final static String QUARTERLY_TEXT = "{1} {0}";
  private final static String TO_MONTH_TEXT = "en {1} {0}";
 // private final static String TO_DAY_TEXT = "en {1} {0}";

  private final static FastDateFormat TO_DAY_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY", Locale.FRANCE);
  private final static FastDateFormat TO_HOUR_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY à HH'h'", Locale.FRANCE);
  private final static FastDateFormat TO_MINUTE_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY à HH:mm", Locale.FRANCE);
  private final static FastDateFormat TO_SECOND_FORMAT = FastDateFormat.getInstance("dd/MM/YYYY à HH:mm:ss", Locale.FRANCE);
  public static void main(String[] args) {
    System.out.println(String.valueOf(2017));
    System.out.println(new DateFormatFrFr().toMonthFormat(new Date()));
    System.out.println(TO_HOUR_FORMAT.format(new Date()));
  }
}
