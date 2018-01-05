package com.baifendian.comp.common.utils.date;

import com.baifendian.bi.engine.enums.FuncDateType;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateI18nUtil {

  private static Map<Locale, DateFormatUtil> dateFormatUtilMap = new HashMap<>();

  static {
    dateFormatUtilMap.put(Locale.SIMPLIFIED_CHINESE, new DateFormatZhCn());
    dateFormatUtilMap.put(Locale.US, new DateFormatEnUs());
    dateFormatUtilMap.put(Locale.FRANCE, new DateFormatFrFr());
  }

  public static Object dateFormat(Object date, FuncDateType dateType){
    DateFormatUtil dateFormatUtil = dateFormatUtilMap.get(LocaleContextHolder.getLocale());
    if (dateFormatUtil == null){
      return date;
    }

    switch (dateType){
      case YEAR:
      case DAY:
      case HOUR:
      case MINUTE:
      case MONTH:
      case QUARTERLY:
      case WEEK:
      case SECOND:
        if (date instanceof Double){
          Double d = (Double) date;
          return dateFormatUtil.format(d.intValue(), dateType);
        }else {
          Integer integer = (Integer) date;
          return dateFormatUtil.format(integer, dateType);
        }

      case TO_YEAR:
        return dateFormatUtil.toYearFormat((Date) date);
      case TO_QUARTERLY:
        return dateFormatUtil.toQuarterlyFormat((Date) date);
      case TO_MONTH:
        return dateFormatUtil.toMonthFormat((Date) date);
      case TO_WEEK:
        return dateFormatUtil.toWeekFormat((Date) date);

      case TO_DAY:
        return dateFormatUtil.toDayFormat((Date) date);
      case TO_HOUR:
        return dateFormatUtil.toHourFormat((Date) date);
      case TO_MINUTE:
        return dateFormatUtil.toMinuteFormat((Date) date);

      case TO_SECOND:
        return dateFormatUtil.toSecondFormat((Date) date);
    }

    return date;
  }

}
