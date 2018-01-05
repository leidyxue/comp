package com.baifendian.bi.engine.custom;

import com.baifendian.bi.engine.consts.SqlConstants;
import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.meta.Column;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;

public class MysqlHandle implements CustomHandle{

  static private MysqlHandle instance = new MysqlHandle();

  static public MysqlHandle createInstance(){
    return instance;
  }


  @Override
  public String escape(String orgValue) {
    if (StringUtils.isEmpty(orgValue)){
      return orgValue;
    }
    return orgValue.replace("'", "\\'");
  }

  @Override
  public String dateFormat(Column field, FuncDateType dateType) {
    String fieldName = field.getName();

    switch (dateType){
      case MINUTE:
      case HOUR:
      case MONTH:
      case YEAR:
      case SECOND:
        return MessageFormat.format(GET_DATE_FORMAT_MSG, dateType.name(), fieldName);
      case QUARTERLY:
      return MessageFormat.format(GET_DATE_FORMAT_MSG, "QUARTER", fieldName);
      case DAY:
        return "DAYOFMONTH(" + fieldName + ")";

      case TO_QUARTERLY:
        return MessageFormat.format(TO_QUARTER_FORMAT, fieldName);
      case TO_WEEK:
        return MessageFormat.format(TO_WEER_FORMAT_MSG, fieldName);

      case TO_YEAR:
        return MessageFormat.format(TO_DATE_FORMAT_MSG, "%Y-01-01 00:00:00", fieldName);
      case TO_MONTH:
        return MessageFormat.format(TO_DATE_FORMAT_MSG, "%Y-%m-01 00:00:00", fieldName);
      case TO_DAY:
        return MessageFormat.format(TO_DATE_FORMAT_MSG, "%Y-%m-%d 00:00:00", fieldName);
      case TO_HOUR:
        return MessageFormat.format(TO_DATE_FORMAT_MSG, "%Y-%m-%d %H:00:00", fieldName);
      case TO_MINUTE:
        return MessageFormat.format(TO_DATE_FORMAT_MSG, "%Y-%m-%d %H:%i:00", fieldName);
      case TO_SECOND:
        return MessageFormat.format(TO_DATE_FORMAT_MSG, "%Y-%m-%d %H:%i:%s", fieldName);

    }
    return fieldName;
  }
  private final static String GET_DATE_FORMAT_MSG = " {0}({1})";
  private final static String TO_DATE_FORMAT_MSG = "cast(DATE_FORMAT({1}, \"{0}\") as DATETIME)";
  private final static String TO_WEER_FORMAT_MSG = "cast(DATE_FORMAT(subdate({1}, weekday({1})),\"%Y-%m-%d 00:00:00\")AS DATETIME)";
  private final static String TO_QUARTER_FORMAT = "DATE_FORMAT({0}, CONCAT(\"%Y-\",QUARTER({0})*3-2,\"-01 00:00:00\"))";

  @Override
  public String castNum(String field) {
    /**
     * max 10^15 .99999
     */
    return "CAST("+field+" AS decimal(20, 5))";
  }

  @Override
  public String castDate(String field) {
    return "CAST("+field+" AS DATETIME)";
  }

  @Override
  public String getQuote() {
    return SqlConstants.MYSQL_SQL_QUOTE;
  }
}
