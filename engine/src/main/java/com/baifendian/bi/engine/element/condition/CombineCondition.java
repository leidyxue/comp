package com.baifendian.bi.engine.element.condition;

import com.baifendian.bi.engine.enums.CombineLogic;

public class CombineCondition implements Condition{
  private Condition left;
  private Condition right;
  private CombineLogic logic;

  public static Condition criteriaAnd(Condition left, Condition right){
    if (left == null){
      return right;
    }
    if (right == null){
      return left;
    }
    return new CombineCondition(left, right, CombineLogic.AND);
  }

  public static Condition criteriaOr(Condition left, Condition right){
    if (left == null){
      return right;
    }
    if (right == null){
      return left;
    }
    return new CombineCondition(left, right, CombineLogic.OR);
  }

  @Override
  public String toString(){
    return "(" + left.toString() + ")" + logic + "(" + right + ")";
  }

  public CombineCondition(Condition left, Condition right, CombineLogic logic){
    this.logic = logic;
    this.left = left;
    this.right = right;
  }
}
