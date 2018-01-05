package com.baifendian.comp.api.dto;

import com.baifendian.comp.api.dto.dashboard.DashUsedTable;
import com.baifendian.comp.api.dto.table.SchemaData;
import com.baifendian.comp.api.service.util.FieldId2Name;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.TableAllData;
import java.util.List;
import java.util.stream.Collectors;

public class DtoUtil {
  public static SchemaData createSchemaData(Field field, FieldId2Name fieldId2Name) {
    return SchemaData.builder()
        .id(field.getId())
        //.param(field.getParameter())
        .type(field.getType())
        .createTime(field.getCreateTime())
        .modifyTime(field.getModifyTime())
        .aggregator(fieldId2Name.apply(field.getAggregator()))
        .formula(field.getAggregator())
        .originName(field.getOriginName())
        .name(field.getName())
        .genType(field.getGenType())
        .build();
  }

  public static List<SchemaData> createSchemaList(List<Field> fieldList) {
    FieldId2Name fieldId2Name = FieldId2Name.create(fieldList);


    return fieldList.stream().map(f -> createSchemaData(f, fieldId2Name)).collect(Collectors.toList());
  }

  public static DashUsedTable createDashUsedTable(String dashId, List<TableAllData> tables){
    DashUsedTable dashUsedTable = new DashUsedTable();
    dashUsedTable.setDashId(dashId);

    for (TableAllData tableAllData: tables){
      DashUsedTable.TableInfo tableInfo = new DashUsedTable.TableInfo();
      tableInfo.setId(tableAllData.tableId());
      tableInfo.setName(tableAllData.tableName());
      tableInfo.setSchema(createSchemaList(tableAllData.getFieldList()));

      dashUsedTable.getTables().add(tableInfo);
    }

    return dashUsedTable;
  }
}
