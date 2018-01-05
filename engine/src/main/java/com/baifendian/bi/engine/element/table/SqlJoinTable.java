package com.baifendian.bi.engine.element.table;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.element.condition.Condition;
import com.baifendian.bi.engine.enums.CombineLogic;
import com.baifendian.bi.engine.enums.JoinType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;
import com.baifendian.bi.engine.meta.GroupBy;
import com.baifendian.bi.engine.meta.OrderBy;
import com.baifendian.bi.engine.meta.SQL;
import com.baifendian.bi.engine.meta.SQLTable;
import java.util.ArrayList;
import java.util.List;

public class SqlJoinTable implements SQLTable, SQL{

  private SQLTable left;
  private SQLTable right;
  private JoinType joinType;
  private Condition onCause;

  private int alisaCount = 0;
  private String name;
  private String alisa;

  private List<Column> columns = new ArrayList<>();

  public SqlJoinTable(SQLTable left, SQLTable right, JoinType joinType, Condition onCause){
    this.left = left;
    this.right = right;
    this.joinType = joinType;
    this.onCause = onCause;
  }

  @Override
  public CustomHandle getCustomHandle() {
    return null;
  }

  @Override
  public String getAlisa() {
    return alisa;
  }

  @Override
  public Column createColumn(String name, String id, ColumnType columnType) {
    return null;
  }

  @Override
  public List<String> getTitle() {
    return null;
  }

  @Override
  public List<Column> getColumns() {
    return null;
  }

  @Override
  public SQL setDistinct() {
    return null;
  }

  @Override
  public SQL addColumn(Column column) {
    return null;
  }

  @Override
  public SQL addColumn(List<Column> column) {
    return null;
  }

  @Override
  public SQL table(SQLTable table) {
    return null;
  }

  @Override
  public SQL setCondition(Condition condition) {
    return null;
  }

  @Override
  public SQL setCondition(CombineLogic logic, Condition condition) {
    return null;
  }

  @Override
  public SQL setGroupBy(GroupBy groupBy) {
    return null;
  }

  @Override
  public SQL setOrderBy(OrderBy orderBy) {
    return null;
  }

  @Override
  public SQL limit(int num) {
    return null;
  }

  @Override
  public String build() {
    return null;
  }
}
