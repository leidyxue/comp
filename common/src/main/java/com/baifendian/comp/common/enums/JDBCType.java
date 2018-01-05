package com.baifendian.comp.common.enums;

public enum JDBCType {
  MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql"), POSTGRESQL("org.postgresql.Driver",
      "jdbc:postgresql"), KYLIN(
      "org.apache.kylin.jdbc.Driver", "jdbc:kylin");

  private String driver;
  private String head;

  JDBCType(String driver, String head) {
    this.driver = driver;
    this.head = head;
  }

  public String getDriver() {
    return driver;
  }

  public String getHead() {
    return head;
  }
}
