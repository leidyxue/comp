package com.baifendian.comp.api.dto.datasource;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class UseTable {

  private String tableName;

  private List<String> fields;

  @Override
  public boolean equals(Object obj) {
    if (obj == this){
      return true;
    }
    if (obj instanceof UseTable){
      return StringUtils.equals(((UseTable)obj).getTableName(),tableName);
    }
    return false;
  }
}
