package com.baifendian.bi.engine.custom;

import com.baifendian.bi.engine.consts.SqlConstants;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.meta.Column;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;

public class KylinHandle implements CustomHandle{

  @Override
  public String escape(String orgValue) {
    // TODO
    if (StringUtils.isEmpty(orgValue)){
      return orgValue;
    }
    return orgValue.replace("'", "\\'");
  }

  @Override
  public String dateFormat(Column field, FuncDateType dateType) {
    String fieldName = field.cast(FieldType.DATE);

    switch (dateType) {
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

      default:

        return MessageFormat
            .format(TO_DATE_FORMAT_MSG, dateType.name().substring(2), fieldName);
    }
  }

  @Override
  public String castNum(String field) {
    return "CAST(" + field + " AS DOUBLE)";
  }


  @Override
  public String castDate(String field) {
    return "CAST(" + field + " AS TIMESTAMP)";
  }

  @Override
  public String getQuote() {
    return SqlConstants.KYLIN_SQL_QUOTE;
  }

  private final static String GET_DATE_FORMAT_MSG = " {0}({1})";
  private static final String TO_DATE_FORMAT_MSG = " date_trunc('{0}', {1} )";
}
