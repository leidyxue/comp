package com.baifendian.comp.common.enums;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public enum  Operator {
  EQU(" = "), NEQ(" != "), LSS(" < "), LEQ(" <= "), GTR(" > "), GEQ(" >= "), NULL(" is null "), NOT_NULL(" is not null "), CON("CON"), NCON("NCON"), BETWEEN("BETWEEN");

  private String val;

  Operator(String val){
    this.val = val;
  }

  @Override
  public String toString() {
    return val;
  }
}
