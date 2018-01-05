package com.baifendian.comp.api.service.util;

import com.baifendian.comp.dao.postgresql.model.table.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FieldName2Id implements Function<String, String> {
  private Map<String, String> idNameMap = new HashMap<>();

  public FieldName2Id(List<Field> fieldList){
    fieldList.forEach(field -> idNameMap.put(field.getName(), field.getId()));
  }

  @Override
  public String apply(String org) {
    try {
      return TableUtil.idNameTransform(org, idNameMap);
    }catch (Exception e){
      return org;
    }
  }
}
