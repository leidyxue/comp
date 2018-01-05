package com.baifendian.bi.engine.enums;

public enum MatchType {
  EQUAL("="),
  GREATER(">"),
  GREATER_EQUAL(">="),
  LESS("<");

  private final String operator;
  MatchType(String ope){
    this.operator = ope;
  }

  public String toString(){
    return operator;
  }
}
