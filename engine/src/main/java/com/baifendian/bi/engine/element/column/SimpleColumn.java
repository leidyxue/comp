package com.baifendian.bi.engine.element.column;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;
import com.baifendian.bi.engine.meta.SQLTable;
import com.baifendian.bi.engine.util.SqlUtil;

public class SimpleColumn implements Column{

  private SQLTable table;
  private String name;
  private String alisa;
  private String key;
  private ColumnType columnType;

  public SimpleColumn(String name, String key, SQLTable table, ColumnType type){
    this.name = name;
    this.alisa = "c_"+key;
    this.table = table;
    this.key = key;
    this.columnType = type;
  }

  public SimpleColumn(String key, String name, String alisa, SQLTable table, ColumnType type){
    this.key = key;
    this.name = name;
    this.alisa = alisa;
    this.table = table;
    this.columnType = type;
  }

  @Override
  public ColumnType getType() {
    return columnType;
  }

  @Override
  public String cast(FieldType type) {
    if (type == columnType.getOrgType()){
      return tmp();
    }

    switch (type){
      case TEXT:
        return tmp();

      case NUM:
        return table.getCustomHandle().castNum(tmp());

      case DATE:
        return table.getCustomHandle().castDate(tmp());
    }
    return tmp();
  }

  @Override
  public String getId(){
    return key;
  }

  @Override
  public Column modifyId(String newId) {
    return new SimpleColumn(newId, name, alisa, table, getType());
  }

  @Override
  public String getAlisa() {
    return alisa;
  }

  @Override
  public String getName() {
    return cast(columnType.getType());
  }

  public String tmp(){
    return table.getAlisa() + "." + SqlUtil.sqlAddQuote(name, table.getCustomHandle().getQuote());
  }

  @Override
  public CustomHandle getCustomHandle() {
    return table.getCustomHandle();
  }
}
