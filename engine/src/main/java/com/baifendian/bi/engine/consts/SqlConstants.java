package com.baifendian.bi.engine.consts;

import com.google.common.base.Joiner;

public class SqlConstants {

  public static final Joiner ColumnJoiner = Joiner.on(",");
  public final static String INNER_SQL_QUOTE = "\"";
  public final static String MYSQL_SQL_QUOTE = "`";
  public final static String POSTGRE_SQL_QUOTE = "\"";
  public final static String KYLIN_SQL_QUOTE = "\"";

}
