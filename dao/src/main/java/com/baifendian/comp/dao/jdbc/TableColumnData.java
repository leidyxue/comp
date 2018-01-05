package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.bi.engine.meta.Column;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class TableColumnData {

  public Column findColumn(String id){
    Column column = columns.get(id);
    Field field = fieldMap.get(id);
    if (field.isDeleted()){
      throw new BiException("com.baifendian.bi.api.datasource.table.field.name.error"
          , HttpStatus.PRECONDITION_FAILED, field.getName());
    }
    return column;
  }

  public TableColumnData(Map<String, Column> columns, Map<String, Field> fieldMap){
    this.columns = columns;
    this.fieldMap = fieldMap;
  }

  private Map<String, Column> columns;
  private Map<String, Field> fieldMap;
}
