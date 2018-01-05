package com.baifendian.comp.api.service;

import com.baifendian.comp.api.service.util.DatasourceUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.common.utils.NameUtil;
import com.baifendian.comp.dao.config.StorageConfig;
import com.baifendian.comp.dao.jdbc.PostgreSqlConnect;
import com.baifendian.comp.dao.postgresql.mapper.FieldNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.baifendian.comp.dao.structs.sql.DbFieldMeta;
import com.baifendian.comp.dao.structs.sql.DbTableMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ShareData {
  @Autowired
  private FolderService folderService;
  @Autowired
  private DatasourceUtil dsUtil;

  @Autowired
  private TableMapperNew tableMapper;

  @Autowired
  private FieldNewMapper fieldMapper;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private List<DbTableMeta> oldTableMeta = new ArrayList<>();

  public void updateShareData(){
    if (!StorageConfig.isShareOpen()){
      return;
    }
    String schema = StorageConfig.getShareSchema();

    try(PostgreSqlConnect connect = new PostgreSqlConnect(StorageConfig.getJDBCParam())){
      updateTableMeta(connect.getTableMetas(schema));
      List<String> userIdList = tableMapper.getUserList();
      if (CollectionUtils.isNotEmpty(userIdList)){
        userIdList.forEach(this::updateUserShare);
      }
    } catch (Exception e) {
      logger.error("Share data error.", e);
    }
  }

  public static void main(String[] args) {
    ShareData shareData = new ShareData();
    shareData.updateShareData();
  }

  synchronized private void updateTableMeta(List<DbTableMeta> tableMetas){
    if (!StorageConfig.isShareOpen()){
      return;
    }
    this.oldTableMeta = tableMetas;
  }

  public static boolean hasTable(List<DbTableMeta> oldTableMeta, Table field){
    if (CollectionUtils.isNotEmpty(oldTableMeta)){
      return oldTableMeta.stream()
          .anyMatch(dbTableMeta -> StringUtils
              .equals(dbTableMeta.getTableName(), field.getOriginName()));
    }

    return false;
  }

  public static boolean hasField(List<DbTableMeta> oldTableMeta, Field field){
    if (CollectionUtils.isNotEmpty(oldTableMeta)){
      return oldTableMeta.stream()
          .filter(dbTableMeta -> StringUtils
              .equals(dbTableMeta.getTableName(), field.getTableOrgName()))
          .flatMap(dbTableMeta -> dbTableMeta.getSchema().stream())
          .anyMatch(schema -> StringUtils.equals(schema.getName(), field.getOriginName()));
    }

    return false;
  }

  @Transactional
  synchronized public void updateUserShare(String userId){
    if (!StorageConfig.isShareOpen() || CollectionUtils.isEmpty(oldTableMeta)){
      return;
    }

    List<Table> tableList = tableMapper.selectByType(userId, TableType.SHARE);

    if (CollectionUtils.isEmpty(tableList)){
      insertUserShare(userId);
      return;
    }

    // remove table
    tableList.stream()
        .filter(t -> !hasTable(oldTableMeta, t))
        .forEach(table -> tableMapper.deleteByTableId(table.getId()));

    List<Field> existFieldList = fieldMapper.findByTIds(tableList.stream().map(Table::getId).collect(
        Collectors.toSet()));

    // 被其他使用的列
    List<Field> columnUsedField = TableUtilNew.getDepField(existFieldList);

    List<Field> chartUseFieldList = fieldMapper.selectChartFieldByUser(userId);

    if (CollectionUtils.isNotEmpty(chartUseFieldList)){
      columnUsedField.addAll(chartUseFieldList);
    }

    // 被删除的列
    List<Field> deletedFieldList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(existFieldList)) {
      deletedFieldList = existFieldList.stream()
          .filter(f -> !hasField(oldTableMeta, f))
          .collect(Collectors.toList());

      for (Field field: deletedFieldList){
        Field usedField = TableUtilNew.getField(columnUsedField, field.getId());
        if (usedField != null){
          fieldMapper.updateDeleteById(usedField.getId());
        }else{
          // 未使用的列，直接删除
          fieldMapper.deleteById(field.getId());
        }
      }
    }

    List<Table> insertTables = new ArrayList<>();
    List<Field> insetField = new ArrayList<>();

    // 数据源同名的目录，如果不存在就创建一个, 目录已经存在，则忽略
    String folderId = folderService.getShareFolderId(userId);

    for (DbTableMeta dbTableMeta: oldTableMeta){
      Optional<Table> tableOptional = tableList.stream()
          .filter(t -> StringUtils.equals(t.getOriginName(), dbTableMeta.getTableName()))
          .findFirst();
      if (tableOptional.isPresent()){
        // table exist
        Table table = tableOptional.get();

        for (DbFieldMeta fieldMeta: dbTableMeta.getSchema()){
          Optional<Field> fieldOpt = existFieldList.stream()
              .filter(f -> StringUtils.equals(f.getOriginName(), fieldMeta.getName()))
              .filter(f -> StringUtils.equals(f.getTableId(), table.getId()))
              .findFirst();
          if (fieldOpt.isPresent()){
            // 已经存在的列，则跳过
            continue;
          }
          Field field = Field.builder()
              .owner(userId)
              .id(NameUtil.fieldName())
              .originName(fieldMeta.getName())
              .name(fieldMeta.getName())
              .tableId(table.getId())
              .genType(FieldGenType.NATIVE)
              .type(fieldMeta.getType())
              .originType(fieldMeta.getOrgType())
              .sortId(fieldMeta.getSortId())
              .build();
          insetField.add(field);
        }

        continue;
      }
      Table table = Table.builder()
          .id(NameUtil.tableName())
          .originName(dbTableMeta.getTableName())
          .name(dbTableMeta.getTableName())
          .owner(userId)
          .type(TableType.SHARE)
          .dsId(dsUtil.getInnerDsId())
          .parentId(folderId)
          .build();

      insertTables.add(table);

      for (DbFieldMeta fieldMeta: dbTableMeta.getSchema()){
        Field field = Field.builder()
            .owner(userId)
            .id(NameUtil.fieldName())
            .originName(fieldMeta.getName())
            .name(fieldMeta.getName())
            .tableId(table.getId())
            .genType(FieldGenType.NATIVE)
            .type(fieldMeta.getType())
            .originType(fieldMeta.getOrgType())
            .sortId(fieldMeta.getSortId())
            .build();
        insetField.add(field);
      }
    }

    if (CollectionUtils.isNotEmpty(insertTables)) {
      tableMapper.batchInsert(insertTables);
    }
    if (CollectionUtils.isNotEmpty(insetField)) {
      fieldMapper.batchInsert(insetField);
    }
  }

  @Transactional
  synchronized public void insertUserShare(String userId){
    if (!StorageConfig.isShareOpen() || CollectionUtils.isEmpty(oldTableMeta)){
      return;
    }

    List<Table> insertTables = new ArrayList<>();
    List<Field> insetField = new ArrayList<>();

    // 数据源同名的目录，如果不存在就创建一个, 目录已经存在，则忽略
    String folderId = folderService.getShareFolderId(userId);

    for (DbTableMeta dbTableMeta: oldTableMeta){
      Table table = Table.builder()
          .id(NameUtil.tableName())
          .originName(dbTableMeta.getTableName())
          .name(dbTableMeta.getTableName())
          .owner(userId)
          .type(TableType.SHARE)
          .dsId(dsUtil.getInnerDsId())
          .parentId(folderId)
          .build();

      insertTables.add(table);

      for (DbFieldMeta fieldMeta: dbTableMeta.getSchema()){
          Field field = Field.builder()
              .owner(userId)
              .id(NameUtil.fieldName())
              .originName(fieldMeta.getName())
              .name(fieldMeta.getName())
              .tableId(table.getId())
              .genType(FieldGenType.NATIVE)
              .type(fieldMeta.getType())
              .originType(fieldMeta.getOrgType())
              .sortId(fieldMeta.getSortId())
              .build();
          insetField.add(field);
      }
    }

    tableMapper.batchInsert(insertTables);
    fieldMapper.batchInsert(insetField);
  }
}
