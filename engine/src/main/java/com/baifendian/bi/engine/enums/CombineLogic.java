package com.baifendian.bi.engine.enums;

public enum CombineLogic {
  AND(" and "),
  OR(" or ");

  CombineLogic(String logicString){
    this.logicString = logicString;
  }

  private final String logicString;
  @Override
  public String toString(){
    return logicString;
  }
}
