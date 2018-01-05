package com.baifendian.comp.api.service.util;

import com.baifendian.comp.dao.postgresql.model.table.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FieldId2Name implements Function<String, String> {
  private Map<String, String> idNameMap = new HashMap<>();

  public FieldId2Name(List<Field> fieldList){
    fieldList.forEach(field -> idNameMap.put(field.getId(), field.getName()));
  }

  public static FieldId2Name create(List<Field> fieldList){
    FieldId2Name fieldId2Name = new FieldId2Name();
    fieldList.forEach(field -> fieldId2Name.idNameMap.put(field.getId(), field.getName()));

    return fieldId2Name;
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
