package com.baifendian.comp.common.utils.date;

import com.baifendian.bi.engine.enums.FuncDateType;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatZhCn implements DateFormatUtil {

  private static Map<FuncDateType, String> formatMap = new HashMap<>();

  static {
    formatMap.put(FuncDateType.YEAR, "{0}年");
    formatMap.put(FuncDateType.QUARTERLY, "{0}季度");
    formatMap.put(FuncDateType.MONTH, "{0}月");
    formatMap.put(FuncDateType.DAY, "{0}日");
    formatMap.put(FuncDateType.WEEK, "{0}周");
    formatMap.put(FuncDateType.HOUR, "{0}时");
    formatMap.put(FuncDateType.MINUTE, "{0}分");
    formatMap.put(FuncDateType.SECOND, "{0}秒");
  }

  @Override
  public String format(int value, FuncDateType dateType) {
    return MessageFormat.format(formatMap.get(dateType), value);
  }

  @Override
  public String toYearFormat(Date date){
    return YEAR_FORMAT_ZH_CN.format(date);
  }

  @Override
  public String toQuarterlyFormat(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int month = cal.get(Calendar.MONTH);
    int year = cal.get(Calendar.YEAR);

    return MessageFormat.format(QUARTERLY_TEXT, String.valueOf(year), (month-1)/3+1);
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
    return MessageFormat.format(WEEK_TEXT, String.valueOf(year), week, BiDateUtil.getWeekRanger(date));
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

  private final static FastDateFormat YEAR_FORMAT_ZH_CN = FastDateFormat.getInstance("yyyy年");
  private final static FastDateFormat TO_MONTH_FORMAT = FastDateFormat.getInstance("yyyy年MM月");
  private final static FastDateFormat TO_DAY_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日");
  private final static FastDateFormat TO_HOUR_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日 HH时");
  private final static FastDateFormat TO_MINUTE_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日 HH时mm分");
  private final static FastDateFormat TO_SECOND_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日 HH时mm分SS秒");

  private final static String QUARTERLY_TEXT = "{0}年{1}季度";
  private final static String WEEK_TEXT = "{0}年{1}周({2})";

  public static void main(String[] args) {

  }
}
