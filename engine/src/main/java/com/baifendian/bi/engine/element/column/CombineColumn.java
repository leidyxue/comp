package com.baifendian.bi.engine.element.column;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;
import java.util.List;

public class CombineColumn implements Column{
  private String id;
  private String name;
  private ColumnType columnType;
  private List<Column> columnList;

  public CombineColumn(String name, String id, FieldType tagType, List<Column> columnList){
    this.name = name;
    this.id = id;
    this.columnType = new ColumnType(FieldType.TEXT, tagType);
    this.columnList = columnList;
  }

  @Override
  public ColumnType getType() {
    return columnType;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Column modifyId(String newId) {
    return new CombineColumn(name, newId, columnType.getType(), columnList);
  }

  @Override
  public String getName() {
    String result = name;
    for (Column c: columnList){
      result = result.replace("${"+c.getId()+"}", c.getName());
    }
    return result;
  }

  @Override
  public CustomHandle getCustomHandle() {
    return columnList.get(0).getCustomHandle();
  }
}
