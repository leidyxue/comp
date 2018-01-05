package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.enums.CompNodeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonNode {
  /**
   * 节点id,作为as name
   */
  private String id;

  private String taskId;

  /**
   * 节点名称
   */
  private String name;

  /**
   * 节点类型（工作表还是结果集）
   */
  private CompNodeType type;

  private String tableId;

  //工作表名称
  private String tableName;

  //表的真实名称
  private String originTableName;


  /**
   * 位置信息
   */
  private Position position;

  public String getSelectName(){
    return id;
  }

  @JsonIgnore
  public String getLeftFromName(){
    return "(select {resultCols} from {leftTable} {type} {rightTable} on {conditions} {filter} {subWhere}) AS " + id;
  }

  @JsonIgnore
  public String getRightFromName(){
    return "(select {resultCols} from {leftTable} {type} {rightTable} on {conditions} {filter} {subWhere}) AS " + id;
  }
}
