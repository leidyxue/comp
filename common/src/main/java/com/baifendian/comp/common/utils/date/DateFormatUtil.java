package com.baifendian.comp.common.utils.date;

import com.baifendian.bi.engine.enums.FuncDateType;
import java.util.Date;

public interface DateFormatUtil {

  Object format(int value, FuncDateType dateType);

  String toYearFormat(Date date);
  String toQuarterlyFormat(Date date);
  String toMonthFormat(Date date);
  String toWeekFormat(Date date);
  String toDayFormat(Date date);
  String toHourFormat(Date date);
  String toMinuteFormat(Date date);
  String toSecondFormat(Date date);
}
