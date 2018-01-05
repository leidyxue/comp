package com.baifendian.bi.engine.util;

import com.baifendian.bi.engine.enums.FieldType;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtil {
  static public String sqlAddQuote(String field, String sqlQuote) {
    return sqlQuote + field.replaceAll("\\.", sqlQuote + "\\." + sqlQuote) + sqlQuote;
  }
  private final static String INNER_SQL_QUOTE = "\"";

  static public String fieldAddQuote(String field) {
    return INNER_SQL_QUOTE + field + INNER_SQL_QUOTE;
  }

  public static FieldType createValueType(String type) {
    String upperType = type.toUpperCase();
    if (upperType.startsWith("INT") || upperType.startsWith("BIGINT")
        || upperType.startsWith("FLOAT") || upperType.startsWith("DOUBLE")
        || upperType.startsWith("DECIMAL")){
      return FieldType.NUM;
    }
    switch (upperType) {
      /** mysql */
      case "TINYINT":
      case "SMALLINT":
      case "MEDIUMINT":
      case "INT":
      case "BIGINT":
      case "FLOAT":
      case "DOUBLE":
      case "DECIMAL":
        /** PostgreSQL */
      case "INTEGER":
      case "NUMERIC":
      case "REAL":
      case "SERIAL":
      case "BIGSERIAL":
        return FieldType.NUM;

      case "DATE":
      case "TIME":
      case "YEAR":
      case "DATETIME":
      case "TIMESTAMP":
      case "INTERVAL":
        return FieldType.DATE;

      default:
        return FieldType.TEXT;
    }
  }

  public static FieldType createValueType(int type) {
    switch (type) {
      case Types.BIT:
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
      case Types.BIGINT:
      case Types.FLOAT:
      case Types.DOUBLE:
      case Types.NUMERIC:
      case Types.DECIMAL:
        return FieldType.NUM;

      case Types.DATE:
      case Types.TIME:
      case Types.TIMESTAMP:
      case Types.TIMESTAMP_WITH_TIMEZONE:
      case Types.TIME_WITH_TIMEZONE:
        return FieldType.DATE;
    }

    return FieldType.TEXT;
  }

  public static String typeClean(String orgType) {
    Matcher matcher = baseTypePattern.matcher(orgType);
    if (matcher.matches()) {
      return matcher.group(1);
    }

    return orgType;
  }

  static private Pattern baseTypePattern = Pattern.compile("^(\\w*)(?:\\((\\d*)(?:,(\\d*))?\\))?$");

}
