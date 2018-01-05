package com.baifendian.comp.api.service;

import com.baifendian.comp.api.dto.ParamUtil;
import com.baifendian.comp.api.dto.datasource.ConfData;
import com.baifendian.comp.api.dto.datasource.DataSourceData;
import com.baifendian.comp.api.dto.datasource.DataSourceData.DsInfo;
import com.baifendian.comp.api.dto.datasource.DataSourceData.DsSum;
import com.baifendian.comp.api.dto.datasource.DsDbMeta;
import com.baifendian.comp.api.dto.datasource.DsMeta;
import com.baifendian.comp.api.dto.datasource.DsPubMeta;
import com.baifendian.comp.api.dto.datasource.UseTable;
import com.baifendian.comp.api.dto.error.ParameterException;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.dto.folder.FolderTableData;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.DatasourceUtil;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.common.ds.DsConfUtil;
import com.baifendian.comp.common.ds.DsMetaConf;
import com.baifendian.comp.common.ds.PublicDataConf;
import com.baifendian.comp.common.ds.PublicDataConf.PublicFieldMeta;
import com.baifendian.comp.common.ds.PublicDataConf.PublicTableMeta;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.common.enums.ds.DsMetaType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.structs.datasource.Summary;
import com.baifendian.comp.common.structs.datasource.TableMeta;
import com.baifendian.comp.common.utils.HttpUtil;
import com.baifendian.comp.common.utils.NameUtil;
import com.baifendian.comp.dao.config.StorageConfig;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.jdbc.DsConnect;
import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.dao.jdbc.JDBCExec;
import com.baifendian.comp.dao.postgresql.mapper.DatasourceMapper;
import com.baifendian.comp.common.structs.datasource.Datasource;
import com.baifendian.comp.dao.postgresql.mapper.DsDirRefMapper;
import com.baifendian.comp.dao.postgresql.mapper.FieldNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import com.baifendian.comp.dao.postgresql.mapper.ds.UserPubDataMapper;
import com.baifendian.comp.dao.postgresql.model.ds.UserPubData;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.baifendian.comp.dao.structs.sql.DbFieldMeta;
import com.baifendian.comp.dao.structs.sql.DbMeta;
import com.baifendian.comp.dao.structs.sql.DbTableMeta;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DependsOn("StorageConfig")
public class DatasourceService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DatasourceMapper datasourceMapper;

  @Autowired
  private TableMapperNew tableMapper;

  @Autowired
  private FieldNewMapper fieldMapper;

  @Autowired
  private DatasourceUtil datasourceUtil;

  @Autowired
  private FolderService folderService;

  @Autowired
  private UserPubDataMapper userPubDataMapper;

  @Autowired
  private ShareData shareData;
  @Autowired
  private DsDirRefMapper dsDirRefMapper;

  @PostConstruct
  public void createInnerDs() {
    datasourceMapper.updateInner(Datasource.builder().id(datasourceUtil.getInnerDsId())
        .type(StorageConfig.getJdbcType())
        .name("InnerDs")
        .parameter(StorageConfig.getJDBCParam())
        .build());

    for (PublicDataConf publicDataConf : DsConfUtil.getPubConf()) {
      datasourceMapper.updateInner(Datasource.builder().id(publicDataConf.getId())
          .type(StorageConfig.getJdbcType())
          .name(publicDataConf.getId())
          .parameter(StorageConfig.getJDBCParam())
          .build());
    }
  }

  private static DataSourceData createDataSourceData(List<Datasource> dsList, List<Summary> sums) {
    DataSourceData dataSourceData = new DataSourceData();

    if (CollectionUtils.isNotEmpty(dsList)) {
      dataSourceData.setDatasources(dsList
          .stream().map(ds -> DataSourceData.DsInfo
              .builder()
              .id(ds.getId())
              .type(DsConfUtil.getDsName(ds.getType().toString()))
              .createTime(ds.getCreateTime())
              .desc(ds.getDesc())
              .modifyTime(ds.getModifyTime())
              .name(ds.getName())
              .build())
          // 按修改时间倒序排列
          .sorted(Comparator.comparing(DsInfo::getModifyTime).reversed())
          .collect(Collectors.toList()));
    }
    if (CollectionUtils.isNotEmpty(sums)) {
      dataSourceData.setSummary(
          sums.stream().map(sum -> DataSourceData.DsSum.builder()
              .count(sum.getCount())
              .name(DsConfUtil.getDsName(sum.getType().toString()))
              .type(sum.getType().name())
              .build()).collect(Collectors.toList())
      );
    }

    return dataSourceData;
  }

  public DataSourceData findAll(String userId, String type) {
    List<Summary> summaryList = datasourceMapper.findSummary(userId);
    List<Datasource> datasourceList = new ArrayList<>();

    if (type != null) {
      try {
        datasourceList = datasourceMapper.findAll(userId, DSType.valueOf(type));
      } catch (Exception e) {
        // 不是数据源类型，忽略
      }
    } else {
      datasourceList = datasourceMapper.findAll(userId, null);
    }
    DataSourceData dataSourceData = createDataSourceData(datasourceList, summaryList);

    List<UserPubData> userPubDatas = userPubDataMapper.findByUserId(userId);
    if (!CollectionUtils.isEmpty(userPubDatas)) {
      for (UserPubData userPubData : userPubDatas) {
        PublicDataConf pubConf = DsConfUtil.getPubConf(userPubData.getId());
        if (pubConf == null) {
          // TODO
          throw new PreFailedException("Error pub id");
        }
        dataSourceData.getSummary().add(DsSum.builder()
            .count(1)
            .name(pubConf.i18nName())
            .type(pubConf.getId())
            .build());
        if (type != null) {
          if (!StringUtils.equals(type, pubConf.getId())) {
            continue;
          }
        }

        dataSourceData.getDatasources().add(
            DsInfo.builder()
                .name(pubConf.i18nName())
                .modifyTime(userPubData.getCreateTime())
                .createTime(userPubData.getCreateTime())
                .id(userPubData.getId())
                .metaType(DsMetaType.PUBLIC_DATA)
                .type(pubConf.i18nName())
                .build());
      }
    }

    return dataSourceData;
  }

  /**
   * 创建一个数据源
   */
  public Datasource createDS(Datasource datasource) {
    datasource.setId(NameUtil.dataSourceName());
    datasourceMapper.insert(datasource);
    return datasource;
  }

  public DbTableMeta findTableMeta(List<DbTableMeta> tableMetaList, String tableName) {
    Optional<DbTableMeta> metaOptional = tableMetaList.stream().filter(
        tableMeta -> StringUtils.equals(tableMeta.getTableName(), tableName))
        .findFirst();

    if (!metaOptional.isPresent()) {
      throw new ParameterException("com.baifendian.bi.api.datasource.table.error", tableName);
    }

    return metaOptional.get();
  }

  DbFieldMeta findFieldMeta(DbTableMeta tableMeta, String fieldName) {
    Optional<DbFieldMeta> fieldMetaOptional = tableMeta.getSchema().stream()
        .filter(fieldMeta -> StringUtils.equals(fieldMeta.getName(), fieldName)).findFirst();
    if (!fieldMetaOptional.isPresent()) {
      throw new ParameterException("com.baifendian.bi.api.datasource.table.field.error",
          fieldName, tableMeta.getTableName());
    }

    return fieldMetaOptional.get();
  }

  @Transactional
  public ConfData createDS(String userId, String name, String desc, DSType type,
      JDBCParam parameter, List<UseTable> useTables) {
    ParamUtil.checkName(name);
    // 数据源同名的目录，如果不存在就创建一个, 目录已经存在，则忽略
    String folderId = folderService.getFolderId(userId, name);

    Datasource datasource = Datasource.builder()
        .name(name)
        .desc(desc)
        .type(type)
        .parameter(parameter)
        .id(NameUtil.dataSourceName())
        .owner(userId)
        .build();

    try {
      datasourceMapper.insert(datasource);
    } catch (DuplicateKeyException e) {
      logger.info("Ds name duplicate", e);
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate", I18nUtil.dsName());
    }

    if (CollectionUtils.isNotEmpty(useTables)) {
      //
      List<DbTableMeta> tableMetaList = JDBCExec
          .dsExec(datasource.getParameter(), DsConnect::getTableMetas);

      List<Table> insertTables = new ArrayList<>();
      List<Field> insetField = new ArrayList<>();

      for (UseTable useTable : useTables) {
        Table table = Table.builder()
            .id(NameUtil.tableName())
            .originName(useTable.getTableName())
            .name(useTable.getTableName())
            .owner(userId)
            .type(TableType.NATIVE)
            .dsId(datasource.getId())
            .parentId(folderId)
            .build();
        insertTables.add(table);
        DbTableMeta tableMeta = findTableMeta(tableMetaList, useTable.getTableName());

        for (String fieldName : useTable.getFields()) {
          DbFieldMeta fieldMeta = findFieldMeta(tableMeta, fieldName);

          Field field = Field.builder()
              .owner(userId)
              .id(NameUtil.fieldName())
              .originName(fieldName)
              .name(fieldName)
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

    ConfData confData = new ConfData(datasource);
    confData.setUseTables(useTables);
    AuditUtil.pushInsert(AuditPageName.DATASOURCE, AuditUtil.getAuditData(datasource));
    return confData;
  }

  @Transactional
  public ConfData modifyDS(String userId, String dsId, String name, String desc,
      JDBCParam parameter, List<UseTable> useTables) {
    ParamUtil.checkName(name);
    HttpUtil.checkParam(parameter);

    Datasource datasource = datasourceUtil.createDatasource(dsId, userId);
    String oldData = AuditUtil.getAuditData(datasource);
    //folderService.modifyFolder(userId, datasource.getDirId(), datasource.getName());
    String folderId = null;

    parameter.setType(datasource.getType());

    datasource.setName(name);
    datasource.setDesc(desc);
    datasource.setModifyTime(new Date());
    datasource.setParameter(parameter);

    try {
      datasourceMapper.update(datasource);
    } catch (DuplicateKeyException e) {
      logger.info("Ds name duplicate", e);
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate", I18nUtil.dsName());
    }

    DbMeta dbMeta = JDBCExec.dsExec(datasource.getParameter(), DsConnect::getAllTableMeta);

    List<Table> existList = tableMapper.selectByDs(userId, dsId);

    List<Table> canBeDeleted = new ArrayList<>();

    List<Table> removedTableList = CollectionUtils.isEmpty(useTables)
        ? existList.stream().collect(Collectors.toList())
        : existList.stream()
            .filter(t -> useTables.stream()
                .noneMatch(ut -> StringUtils.equals(t.getOriginName(), ut.getTableName())))
            .collect(Collectors.toList());

    List<Field> chartUseFields = fieldMapper.selectChartFieldByUser(userId);

    if (CollectionUtils.isNotEmpty(chartUseFields)){
      chartUseFields = chartUseFields.stream()
          .filter(f -> null != TableUtilNew.getTable(existList, f.getTableId()))
          .collect(Collectors.toList());
    }

    if (CollectionUtils.isNotEmpty(removedTableList)){
      Set<String> chartUsedTableIds = chartUseFields.stream().map(Field::getTableId)
          .collect(Collectors.toSet());
      if (CollectionUtils.isEmpty(chartUsedTableIds)){
        canBeDeleted = removedTableList;
      }else{
        // 取消的表，有三种处理
        for (Table table: removedTableList){
          if (chartUsedTableIds.contains(table.getId())){
            // 工作表已经被图表使用
            if (dbMeta.hasTable(table.getOriginName())){
              // 数据源里，表依然存在
              throw new PreFailedException("com.baifendian.bi.api.table.delete.error2",
                  table.getName());
            }

            // 表虽然被引用，但数据源内的原始表，已经被删除了
            tableMapper.updateDeleteById(table.getId());
          }else{
            canBeDeleted.add(table);
          }
        }
      }
    }

    if (CollectionUtils.isNotEmpty(canBeDeleted)){
      canBeDeleted.forEach(t -> tableMapper.deleteByTableId(t.getId()));
    }

    if (CollectionUtils.isNotEmpty(useTables)) {

      List<Field> fieldExistList = fieldMapper.findByDsId(dsId);

      List<Table> insertTables = new ArrayList<>();
      List<Field> insetField = new ArrayList<>();

      for (UseTable useTable : useTables) {
        Optional<Table> existTable = existList.stream()
            .filter(table -> StringUtils.equals(table.getName(), useTable.getTableName()))
            .findFirst();
        Table table;
        if (!existTable.isPresent()) {
          //
          folderId = folderService.getFolderId(userId, name);

          table = Table.builder()
              .id( NameUtil.tableName())
              .originName(useTable.getTableName())
              .name(useTable.getTableName())
              .owner(datasource.getOwner())
              .type(TableType.NATIVE)
              .dsId(datasource.getId())
              .parentId(folderId)
              .build();
          insertTables.add(table);
        }else{
          table = existTable.get();

          List<Field> tableField = fieldExistList
              .stream().filter(f -> StringUtils.equals(f.getTableId(), table.getId()))
              .collect(Collectors.toList());
          List<Field> columnUsedList = TableUtilNew.getDepField(tableField);

          List<Field> deletedFieldList = tableField.stream()
              .filter(f -> !useTable.getFields().contains(f.getOriginName()))
              .collect(Collectors.toList());
          for (Field field: deletedFieldList){
            if (columnUsedList.stream().anyMatch(f -> StringUtils.equals(f.getId(), field.getId()))){
              // 被其它字段使用
              throw new PreFailedException("com.baifendian.bi.api.table.field.used.error",
                  field.getName());
            }

            if (chartUseFields.stream().anyMatch(f -> StringUtils.equals(f.getId(), field.getId()))){
              fieldMapper.updateDeleteById(field.getId());
            }else {
              fieldMapper.deleteById(field.getId());
            }
          }
        }

        // TODO native field name and user field name is the same
        for (String fieldName : useTable.getFields()) {
          Optional<Field> fieldOptional = fieldExistList.stream()
              .filter(
                  field -> StringUtils.equals(fieldName, field.getName()) && useTable.getTableName()
                      .equals(field.getTableName()))
              .findFirst();
          if (fieldOptional.isPresent()){
            continue;
          }
          DbFieldMeta fieldMeta = dbMeta.fieldMeta(table.getOriginName(), fieldName);

          Field field = Field.builder()
              .owner(datasource.getOwner())
              .id(NameUtil.fieldName())
              .originName(fieldName)
              .name(fieldName)
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

    ConfData confData = new ConfData(datasource);
    confData.setUseTables(useTables);
    AuditUtil.pushUpdate(AuditPageName.DATASOURCE, oldData, AuditUtil.getAuditData(datasource));
    return confData;
  }


  /**
   * 测试一个数据连接
   */
  public void dsTest(JDBCParam jdbcParam) {
    HttpUtil.checkParam(jdbcParam);
    JDBCExec.dsExec(jdbcParam, dsConnect -> true);
  }

  public List<TableMeta> getTableAndData(JDBCParam jdbcParam) {
    HttpUtil.checkParam(jdbcParam);
    return JDBCExec.dsExec(jdbcParam, DsConnect::tableMetaAndData);
  }

  public ConfData queryConf(String userId, String dataSourceId) {
    Datasource datasource = datasourceUtil.createDatasource(dataSourceId, userId);
    List<Field> fieldList = fieldMapper.findByDsId(dataSourceId);
    if (fieldList != null){
      fieldList = fieldList.stream().filter(Field::nonDeleted).collect(Collectors.toList());
    }

    return new ConfData(datasource, fieldList);
  }

  public List<FolderTableData> getDsTables(String userId, String dataSourceId) {
    List<Table> tableList = null;
    // check public data
    PublicDataConf publicDataConf = DsConfUtil.getPubConf(dataSourceId);
    if (publicDataConf != null) {
      tableList = tableMapper.selectByDs(userId, dataSourceId);
    } else {
      datasourceUtil.checkDatasource(dataSourceId, userId);
      tableList = tableMapper.selectByDs(userId, dataSourceId);
    }

    if (CollectionUtils.isEmpty(tableList)) {
      return new ArrayList<>();
    }

    return tableList.stream()
        .map(table -> FolderTableData.builder()
            .id(table.getId())
            .originName(table.getOriginName())
            .desc(table.getDesc())
            .tableType(table.getType())
            .createTime(table.getCreateTime())
            .dsId(table.getDsId())
            .dsName(table.getDsName())
            .parentId(table.getParentId())
            .dsType(table.getDsType())
            .modifyTime(table.getModifyTime())
            .name(table.getName())
            .build()).collect(Collectors.toList());
  }

  @Transactional
  public void delete(String userId, String dataSourceId) {
    // check public data
    List<Table> useTableList = tableMapper.findChartTableByUser(userId, dataSourceId);

    if (CollectionUtils.isNotEmpty(useTableList)) {
      throw new PreFailedException("com.baifendian.bi.api.table.delete.error2",
          useTableList.get(0).getName());
    }

    PublicDataConf publicDataConf = DsConfUtil.getPubConf(dataSourceId);
    if (publicDataConf != null) {
      // Public data
      userPubDataMapper.deleteById(userId, dataSourceId);
      tableMapper.deleteByDsId(userId, dataSourceId);

      dsDirRefMapper.deleteByDsId(userId, dataSourceId);
      AuditUtil.pushDel(AuditPageName.DATASOURCE, "name=" + dataSourceId);
      return;
    }

    Datasource datasource = datasourceUtil.createDatasource(dataSourceId, userId);
    datasourceMapper.deleteById(dataSourceId);
    AuditUtil.pushDel(AuditPageName.DATASOURCE, AuditUtil.getAuditData(datasource));
  }

  public DsMeta getMeta(String userId) {
    List<UserPubData> userPubDatas = userPubDataMapper.findByUserId(userId);

    DsMetaConf dsMetaConf = DsConfUtil.getDsMeta();
    Map<String, Object> resultMap = new HashMap<>();

    if (dsMetaConf.getDatabase() != null) {
      for (DsMetaConf.DbMeta dbMeta : dsMetaConf.getDatabase()) {
        DsDbMeta dsDbMeta = DsDbMeta.builder()
            .name(dbMeta.getName())
            .extendData(dbMeta.getExtendsData())
            .build();
        resultMap.put(dbMeta.getDsType().toString(), dsDbMeta);
      }
    }

    if (dsMetaConf.getPubData() != null) {
      for (DsMetaConf.PubDataMeta pubDataMeta : dsMetaConf.getPubData()) {
        boolean isUsed = userPubDatas.stream()
            .anyMatch(up -> StringUtils.equals(up.getId(), pubDataMeta.getId()));

        DsPubMeta dsPubMeta = DsPubMeta.builder()
            .name(pubDataMeta.getName())
            .isUsed(isUsed)
            .build();
        resultMap.put(pubDataMeta.getId(), dsPubMeta);

      }
    }

    return new DsMeta(resultMap);
  }

  @Transactional
  public void addPublicData(String userId, String pubId) {
    List<UserPubData> userPubDatas = userPubDataMapper.findByUserId(userId);
    if (CollectionUtils.isNotEmpty(userPubDatas)) {
      if (userPubDatas.stream().anyMatch(up -> StringUtils.equals(up.getId(), pubId))) {
        // User have already add this public data.
        return;
      }
    }
    PublicDataConf pubConf = DsConfUtil.getPubConf(pubId);
    if (pubConf == null) {
      // TODO
      throw new PreFailedException("Error pub id");
    }
    List<Field> fieldList = new ArrayList<>();
    List<Table> tableList = new ArrayList<>();

    String folderId = folderService.getPubFolderId(userId, pubConf.getId());

    for (PublicTableMeta meta : pubConf.getMeta()) {

      Table table = Table.builder()
          .id(NameUtil.tableName())
          .originName(StorageConfig.getPublicSchema() + "." + meta.getTable())
          .name(meta.getName())
          .owner(userId)
          .type(TableType.PUBLIC)
          .dsId(pubConf.getId())
          .parentId(folderId)
          .build();

      tableList.add(table);

      int i = 1;
      for (PublicFieldMeta fieldMeta : meta.getFields()) {
        Field field = Field.builder()
            .owner(userId)
            .id(NameUtil.fieldName())
            .originName(fieldMeta.getOrgName())
            .name(fieldMeta.getName())
            .tableId(table.getId())
            .genType(FieldGenType.NATIVE)
            .type(fieldMeta.getType())
            .sortId(i++)
            .build();
        fieldList.add(field);
      }
    }
    tableMapper.batchInsert(tableList);
    fieldMapper.batchInsert(fieldList);

    userPubDataMapper.insert(new UserPubData(pubId, userId, new Date()));
    AuditUtil.pushInsert(AuditPageName.DATASOURCE, "name=" + pubId);
  }

  @Scheduled(fixedDelay = 1000 * 60 * 10)
  public void scanShareData() {
    logger.info("Begin scan share data");
    shareData.updateShareData();
    logger.info("End scan share data");
  }
}
