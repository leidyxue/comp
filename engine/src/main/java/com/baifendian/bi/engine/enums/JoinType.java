package com.baifendian.bi.engine.enums;

public enum JoinType {
  LEFT_JOIN(" LEFT JOIN "), RIGHT_JOIN(" RIGHT JOIN "), INNER_JOIN("INNER JOIN");

  private String val;

  JoinType(String val){
    this.val = val;
  }

  @Override
  public String toString() {
    return val;
  }
}
