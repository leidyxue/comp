package com.baifendian.bi.engine.element.table;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.element.column.SimpleColumn;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;
import com.baifendian.bi.engine.meta.SQLTable;
import com.baifendian.bi.engine.util.SqlUtil;

public class SimpleTable implements SQLTable {

  private int alisaCount = 0;

  private String name;

  private String alisa;
  private CustomHandle handle;

  public SimpleTable(CustomHandle handle , String name, String alisa){
    this.name = name;
    this.alisa = alisa;
    this.handle = handle;
  }

  @Override
  public String toString() {
    return SqlUtil.sqlAddQuote(name, handle.getQuote())+" as "+alisa;
  }

  @Override
  public String getAlisa() {
    return alisa;
  }

  @Override
  public Column createColumn(String name, String id, ColumnType columnType) {
    return new SimpleColumn(id, name, "c_"+alisaCount++, this, columnType);
  }

  @Override
  public CustomHandle getCustomHandle() {
    return handle;
  }

}
