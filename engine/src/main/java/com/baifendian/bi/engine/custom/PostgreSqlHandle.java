package com.baifendian.bi.engine.custom;

import com.baifendian.bi.engine.consts.SqlConstants;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.meta.Column;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;

public class PostgreSqlHandle implements CustomHandle {

  @Override
  public String escape(String orgValue) {
    if (StringUtils.isEmpty(orgValue)){
      return orgValue;
    }

    return orgValue.replace("'", "''");
  }

  @Override
  public String dateFormat(Column field, FuncDateType dateType) {
    String fieldName = field.cast(FieldType.DATE);

    switch (dateType) {
      case YEAR:
      case MONTH:
      case WEEK:
      case DAY:
      case MINUTE:
      case HOUR:
      case SECOND:
        return MessageFormat.format(DATE_FORMAT_MSG, dateType.name(), fieldName);

      case QUARTERLY:
        return MessageFormat.format(DATE_FORMAT_MSG, "QUARTER", fieldName);

      case TO_QUARTERLY:
        return MessageFormat
            .format(TO_DATE_FORMAT_MSG, "'quarter'", fieldName );

      default:

        return MessageFormat
            .format(TO_DATE_FORMAT_MSG, "'"+dateType.name().substring(3)+"'", fieldName );
    }
  }

  @Override
  public String castText(String field) {
    return "cast(" + field + " as text)";
  }

  @Override
  public String castNum(String field) {
    return "bi_num_func(" + field + ")";
  }

  @Override
  public String castDate(String field) {
    return "bi_date_func(" + field + ")";
  }

  @Override
  public String getQuote() {
    return SqlConstants.POSTGRE_SQL_QUOTE;
  }

  private static final String DATE_FORMAT_MSG = " EXTRACT( {0} from {1} )";
  private static final String TO_DATE_FORMAT_MSG = " date_trunc({0}, {1} )";

  public static void main(String[] args) {
    System.out.println(MessageFormat
        .format(TO_DATE_FORMAT_MSG, "TO_YEAR".substring(3), "afds"));
    System.out.println("TO_YEAR".substring(2));
  }
}
