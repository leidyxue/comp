package com.baifendian.comp.dao.structs.sql;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@AllArgsConstructor
public class DbMeta {

  private List<DbTableMeta> tableMetaList;

  public DbTableMeta tableMeta(String tableOrgName){
    return tableMetaList.stream()
        .filter(mt -> StringUtils.equals(tableOrgName, mt.getTableName()))
        .findFirst().orElse(null);
  }

  public DbFieldMeta fieldMeta(String tableOrgName, String fieldName){
    return tableMetaList.stream()
        .filter(mt -> StringUtils.equals(tableOrgName, mt.getTableName()))
        .flatMap(mt -> mt.getSchema().stream())
        .filter(f -> StringUtils.equals(f.getName(), fieldName))
        .findFirst().orElse(null);
  }

  public boolean hasTable(String tableOrgName){
    return tableMetaList.stream()
        .anyMatch(mt -> StringUtils.equals(tableOrgName, mt.getTableName()));
  }

}
