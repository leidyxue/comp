package com.baifendian.comp.api.service.util;

import com.baifendian.comp.api.dto.error.NotFoundException;
import com.baifendian.comp.api.dto.error.UnAuthorizedException;
import com.baifendian.comp.api.service.ShareData;
import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.utils.AggregatorUtil;
import com.baifendian.comp.dao.postgresql.mapper.FieldNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.baifendian.comp.dao.postgresql.model.table.TableAllData;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableUtilNew {

  public static Table getTable(List<Table> fieldList, String tableId) {
    return fieldList.stream().filter(f -> StringUtils.equals(f.getId(), tableId))
        .findFirst().orElse(null);
  }

  public static List<Field> getDepField(List<Field> fieldList) {
    return fieldList.stream()
        .filter(f -> f.nonDeleted() && f.isGenerate())
        .flatMap(f -> AggregatorUtil.getFieldIds(f.getAggregator()).stream())
        .map(id -> getField(fieldList, id))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public static Field getField(List<Field> fieldList, String fieldId) {
    return fieldList.stream().filter(f -> StringUtils.equals(f.getId(), fieldId))
        .findFirst().orElse(null);
  }

  @Autowired
  private TableMapperNew tableMapper;

  @Autowired
  private FieldNewMapper fieldMapper;
  @Autowired
  private ShareData shareData;

  public void checkTableId(String tableId) {
    Table table = tableMapper.selectById(tableId);

    if (table == null) {
      throw new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.tableName());
    }
  }

  public Table createSimpleTable(String owner, String tableId) {
    Table table = tableMapper.selectById(tableId);

    if (table == null || table.isDeleted()) {
      throw new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.tableName());
    }
    if (!StringUtils.equals(owner, table.getOwner())) {
      throw new ParamException("com.baifendian.bi.api.common.notOwner", I18nUtil.tableName(),
          tableId);
    }
    return table;
  }

  public List<Table> createSimpleTable(String owner) {
    List<Table> tableList = tableMapper.selectByUser(owner);

    if (CollectionUtils.isEmpty(tableList)) {
      // User do not have any table, add share
      shareData.insertUserShare(owner);
      return new ArrayList<>();
    }

    if (tableList.stream().noneMatch(Table::isShare)) {
      // User do not have any share table, add share
      shareData.insertUserShare(owner);
    }

    return tableList.stream().filter(t -> !t.isDeleted()).collect(Collectors.toList());
  }

  public TableAllData createTable(String owner, String tableId) {
    TableAllData tableAllData = createTable(tableId);
    if (!StringUtils.equals(owner, tableAllData.getTable().getOwner())) {
      throw new UnAuthorizedException("com.baifendian.bi.api.common.notOwner", I18nUtil.tableName(),
          tableId);
    }
    return tableAllData;
  }

  public List<TableAllData> createTables(Set<String> tableIds) {
    List<TableAllData> result = new ArrayList<>();
    if (CollectionUtils.isEmpty(tableIds)){
      return result;
    }
    List<Table> tableList = tableMapper.selectByIds(tableIds);
    if (CollectionUtils.isEmpty(tableList)){
      return result;
    }

    List<Field> fieldList = fieldMapper.findByTIds(tableIds);

    return tableList.stream().map(TableAllData::new).map(f -> f.addFieldList(fieldList))
        .collect(Collectors.toList());
  }

  public TableAllData createTable(String tableId) {
    Table table = tableMapper.selectById(tableId);

    if (table == null || table.isDeleted()) {
      throw new ParamException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.tableName());
    }

    TableAllData tableAllData = new TableAllData();
    tableAllData.setTable(table);
    tableAllData.setFieldList(fieldMapper.findByTId(tableId));
    return tableAllData;
  }

  public List<Field> createFields(Set<String> fieldIdSet) {
    if (CollectionUtils.isEmpty(fieldIdSet)) {
      return new ArrayList<>();
    }

    List<Field> fieldList = fieldMapper.selectByIds(fieldIdSet);
    if (fieldList.size() != fieldIdSet.size()) {
      // TODO invalid field id
      //throw
    }

    // TODO check oner

    // TODO check whether native field

    return fieldList;
  }

  public Field createField(String owner, String tableId, String fieldId) {
    Field field = fieldMapper.selectById(fieldId);

    if (field == null) {
      throw new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.fieldName());
    }

    if (!StringUtils.equals(owner, field.getOwner())) {
      throw new ParamException("com.baifendian.bi.api.common.notOwner", I18nUtil.fieldName(),
          fieldId);
    }

    if (!field.getTableId().equals(tableId)) {
      throw new ParamException("com.baifendian.bi.api.common.notOwner", I18nUtil.tableName(),
          tableId);
    }
    field.setId(fieldId);
    return field;
  }


  public List<Field> nonDeletedFields(String owner, String tableId) {
    checkTableId(tableId);

    List<Field> fieldList = fieldMapper.findByTId(tableId);
    if (CollectionUtils.isEmpty(fieldList)) {
      return new ArrayList<>();
    }

    return fieldList.stream().filter(Field::nonDeleted).collect(Collectors.toList());
  }

  public void cleanDeleteField(String tableId, List<Field> fieldList){
    //List<Field> fieldList = nonDeletedFields(userId, tableId);

    // 字段修改后，假删的字段可能已经不被使用了
    List<Field> delFieldList = fieldList.stream().filter(Field::isDeleted).collect(Collectors.toList());

  }
}
