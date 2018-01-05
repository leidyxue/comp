package com.baifendian.comp.common.enums;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public enum  FilterType {
  ALL(" and "), ANY(" or ");

  private String val;

  FilterType(String val){
    this.val = val;
  }

  @Override
  public String toString() {
    return val;
  }
}
