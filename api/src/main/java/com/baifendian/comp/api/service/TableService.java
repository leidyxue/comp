package com.baifendian.comp.api.service;

import com.baifendian.comp.api.dto.DtoUtil;
import com.baifendian.comp.api.dto.error.NotFoundException;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.api.dto.ParamUtil;
import com.baifendian.comp.api.dto.datasource.SimpleDatasource;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.dto.table.ChartDash;
import com.baifendian.comp.api.dto.table.RangeData;
import com.baifendian.comp.api.dto.table.SchemaData;
import com.baifendian.comp.api.dto.table.TableDetailData;
import com.baifendian.comp.api.dto.table.TableField;
import com.baifendian.comp.api.dto.table.TableFieldParam;
import com.baifendian.comp.api.dto.table.TableRelation;
import com.baifendian.comp.api.dto.table.TableSingleField;
import com.baifendian.comp.api.service.util.DashboardUtil;
import com.baifendian.comp.api.service.util.DatasourceUtil;
import com.baifendian.comp.api.service.util.FieldId2Name;
import com.baifendian.comp.api.service.util.FieldName2Id;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.api.service.util.TableUtil;
import com.baifendian.comp.common.ds.DsConfUtil;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.common.structs.datasource.Datasource;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.jdbc.JDBCExec;
import com.baifendian.comp.dao.jdbc.SqlResultData;
import com.baifendian.comp.dao.postgresql.mapper.FieldNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.NodeMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.TaskMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashFilterMapper;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonTask;
import com.baifendian.comp.dao.postgresql.model.dash.DashFilter;
import com.baifendian.comp.dao.postgresql.model.share.ShareDash;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.baifendian.comp.dao.postgresql.model.table.TableAllData;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.common.utils.NameUtil;
import com.baifendian.comp.dao.jdbc.SQLContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TableMapperNew tableMapperNew;

  @Autowired
  private ChartNewMapper chartMapper;

  @Autowired
  private TableUtilNew tableUtilNew;

  @Autowired
  private DashboardUtil dashboardUtil;
  @Autowired
  private DatasourceUtil datasourceUtil;
  @Autowired
  private DashFilterMapper dashFilterMapper;
  @Autowired
  private FieldNewMapper fieldNewMapper;

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private NodeMapper nodeMapper;

  public void deleteTableField(String userId, String tableId, String fieldId) {
    TableAllData tableAllData = tableUtilNew.createTable(userId, tableId);

    Optional<Field> fieldOpt = tableAllData.getField(fieldId);
    if (!fieldOpt.isPresent()) {
      throw new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.fieldName());
    }

    Field field = fieldOpt.get();
    if (field.isNative()){
      throw new PreFailedException("com.baifendian.bi.api.common.nativeError");
    }
    Set<String> usedField = tableAllData.usedFieldIds();
    if (usedField.contains(fieldId)){
      throw new PreFailedException("com.baifendian.bi.api.table.field.used.error",
          field.getName());
    }

    // 判断字段有没有被chart使用
    int num = fieldNewMapper.findUsedById(fieldId);
    if (num > 0){
      fieldNewMapper.updateDeleteById(fieldId);
    }else{
      fieldNewMapper.deleteById(fieldId);
    }

    AuditUtil.pushDel(AuditPageName.FIELD, AuditUtil.getAuditData(field));
  }

  @Transactional
  public TableSingleField modifyTableField(String userId, String tableId, String fieldId,
      String name, String desc, String aggregator, FieldType type) {
    ParamUtil.checkName(name);
    List<Field> fieldList = tableUtilNew.nonDeletedFields(userId, tableId);

    Field field = fieldList.stream().filter(f -> StringUtils.equals(f.getId(), fieldId))
        .findFirst().orElseThrow((Supplier<BiException>) () ->
            new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.fieldName()));

    FieldName2Id fieldName2Id = new FieldName2Id(fieldList);

    String old = AuditUtil.getAuditData(field);

    field.setModifyTime(new Date());
    field.setDesc(desc);
    field.setType(type);
    field.setName(name);
    if (field.isGenerate()) {
      field.setAggregator(fieldName2Id.apply(aggregator));
    }

    fieldNewMapper.updateById(field);

    AuditUtil.pushUpdate(AuditPageName.FIELD, old, AuditUtil.getAuditData(field));
    return TableSingleField.builder()
        .originName(field.getOriginName())
        .name(name)
        .aggregator(aggregator)
        .formula(field.getAggregator())
        .createTime(field.getCreateTime())
        .modifyTime(field.getModifyTime())
        .genType(field.getGenType())
        .type(field.getType())
        .id(fieldId)
        .tableId(tableId)
        .desc(desc)
        .build();
  }

  public TableField getTableField(String userId, String tableId) {
    TableAllData tableAllData = tableUtilNew.createTable(userId, tableId);
    Table table = tableAllData.getTable();

    TableField.TableFieldBuilder builder = TableField.builder().id(tableId)
        .name(table.getName())
        .createTime(table.getCreateTime())
        .modifyTime(table.getModifyTime())
        .dsId(table.getDsId());

    FieldId2Name fieldId2Name = FieldId2Name.create(tableAllData.getFieldList());

    builder.schema(tableAllData.nonDeletedField().stream()
        .filter(Field::nonDeleted)
        .map(field -> DtoUtil.createSchemaData(field, fieldId2Name))
        .collect(Collectors.toList()));

    return builder.build();
  }

  public void modifyTable(String userId, String tableId, String name, String desc) {
    ParamUtil.checkName(name);
    Table table = tableUtilNew.createSimpleTable(userId, tableId);

    String oldData = AuditUtil.getAuditData(table);
    table.setName(name);
    table.setDesc(desc);

    try {
      tableMapperNew.updateById(table);
    } catch (DuplicateKeyException e) {
      logger.error("Insert error.", e);
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate",
          new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.table"));
    }
    AuditUtil.pushUpdate(AuditPageName.WORKSHEET, oldData, AuditUtil.getAuditData(table));
  }

  public SchemaData createTableField(String userId, String tableId, String name, String desc,
      String aggregator, FieldType type, FieldGenType genType) {
    ParamUtil.checkName(name);
    TableUtil.checkFieldGenType(genType);

    List<Field> fieldList = tableUtilNew.nonDeletedFields(userId, tableId);

    FieldName2Id fieldName2Id = new FieldName2Id(fieldList);

    Field field = Field.builder()
        .id(NameUtil.fieldName())
        .aggregator(fieldName2Id.apply(aggregator))
        .name(name)
        .desc(desc)
        .owner(userId)
        .tableId(tableId)
        .genType(genType)
        .type(type)
        .build();

    fieldNewMapper.insert(field);

    AuditUtil.pushInsert(AuditPageName.FIELD, AuditUtil.getAuditData(field));

    return SchemaData.builder()
        .name(name)
        .id(field.getId())
        .aggregator(aggregator)
        .formula(field.getAggregator())
        .createTime(field.getCreateTime())
        .modifyTime(field.getModifyTime())
        .genType(genType)
        .type(type)
        .originType(field.getOriginType())
        .build();
  }

  public TableDetailData getTableDetail(String userId, String tableId) {
    TableAllData tableAllData = tableUtilNew.createTable(userId, tableId);
    Table table = tableAllData.getTable();
    TableDetailData.TableDetailDataBuilder builder = TableDetailData.builder()
        .createTime(table.getCreateTime())
        .desc(table.getDesc())
        .dsId(table.getDsId())
        .dsName(table.getDsName())
        .dsType(table.getDsType())
        .genType(table.getType())
        .id(tableId)
        .modifyTime(table.getModifyTime())
        .name(table.getName())
        .originName(table.getOriginName());

    FieldId2Name fieldId2Name = FieldId2Name.create(tableAllData.getFieldList());
    List<SchemaData> schema = new ArrayList<>();
    for (Field field : tableAllData.getFieldList()) {
      if (field.isDeleted()) {
        continue;
      }
      SchemaData.SchemaDataBuilder schemaDataBuilder = SchemaData.builder()
          .id(field.getId())
          .name(field.getName())
          .originName(field.getOriginName())
          .fileOriginName(field.getFileOriginName())
          .createTime(field.getCreateTime())
          .genType(field.getGenType())
          .modifyTime(field.getModifyTime())
          .type(field.getType())
          .originType(field.getOriginType())
          .desc(field.getDesc())
          .aggregator(fieldId2Name.apply(field.getAggregator()))
          .formula(field.getAggregator());

      schema.add(schemaDataBuilder.build());
    }

    SqlResultData sqlResultData = SQLContext.getTableData(tableAllData);

    builder.titles(sqlResultData.getTitles())
        .data(sqlResultData.getDataList())
        .dataStatus(sqlResultData.dataStatus())
        .dataMessage(sqlResultData.getDataMessage())
        .schema(schema);

    builder.number(SQLContext.getTableSize(table.getJdbcParameter(), table.getOriginName()));

    return builder.build();
  }

  public void delete(String usrId, String tableId) {
    // check condition manually
//    if (0 < tableMapperNew.selectNumByTId(tableId)) {
//      throw new PreFailedException("com.baifendian.bi.api.table.delete.error");
//    }

    List<ComparisonTask> tasks = taskMapper.findByTableId(tableId);
    if (tasks.size() > 0) {
      throw new PreFailedException("com.bfd.comp.api.tableService.tableInTask");
    }

    Table table = tableUtilNew.createSimpleTable(usrId, tableId);
    if (table.isFile()) {
      // Drop table, if import.
      // 删除文件导入的表
      String dropTable = "DROP table IF EXISTS " + table.getOriginName();
      logger.info("drop table, sql:{}", dropTable);
      JDBCExec.execute(table.getJdbcParameter(), dropTable);
    }

    tableMapperNew.deleteByTableId(tableId);
    AuditUtil.pushDel(AuditPageName.WORKSHEET, AuditUtil.getAuditData(table));
  }

  private RangeData getRangeDataNew(List<Field> fieldList) {
    Optional<Field> delField = fieldList.stream()
        .filter(Field::isDeleted)
        .findFirst();
    if (delField.isPresent()){
      // 列已经删除
      throw new NotFoundException("com.bfd.bi.api.common.field.del", delField.get().getName());
    }

    Set<String> tableIds = fieldList.stream().map(Field::getTableId)
        .collect(Collectors.toSet());
    List<Table> tableList = tableMapperNew.selectByIds(tableIds);

    FieldType fieldType = fieldList.get(0).getType();
    boolean sameType = fieldList.stream()
        .allMatch(field -> field.getType() == fieldType);
    if (!sameType) {
      throw new PreFailedException("com.baifendian.bi.api.table.range.type.diff");
    }

    Set<String> result = new HashSet<>();

    for (Field field : fieldList) {
      Optional<Table> tableOptional = tableList.stream()
          .filter(t -> StringUtils.equals(t.getId(), field.getTableId())).findFirst();
      if (!tableOptional.isPresent()) {
        continue;
      }
      TableAllData tableAllData = tableUtilNew.createTable(field.getTableId());
      result.addAll(SQLContext.getRangeSql(tableAllData, field));
    }

    RangeData resultData = new RangeData();
    resultData.setRange(result.stream().limit(100)
        .sorted(String::compareToIgnoreCase)
        .collect(Collectors.toList()));

    return resultData;
  }

  public RangeData rangeDate(List<TableFieldParam> tableFieldParamList) {
    Set<String> fieldIdSet = tableFieldParamList.stream().map(TableFieldParam::getFieldId)
        .collect(Collectors.toSet());

    List<Field> fieldList = tableUtilNew.createFields(fieldIdSet);

    return getRangeDataNew(fieldList);
  }

  public RangeData shareFilterData(String shareId, String filterId) {
    ShareDash shareDash = dashboardUtil.createShareDash(shareId);

    List<DashFilter> tmp = dashFilterMapper.queryByDashId(shareDash.getDashId());
    if (CollectionUtils.isNotEmpty(tmp)) {
      Set<String> fieldIds = tmp.stream().filter(df -> StringUtils.equals(df.getId(), filterId))
          .map(DashFilter::getFieldId).collect(Collectors.toSet());
      if (CollectionUtils.isEmpty(fieldIds)) {
        throw new PreFailedException("com.baifendian.bi.api.common.invalid.Id",
            I18nUtil.fieldName());
      }

      List<Field> fieldList = tableUtilNew.createFields(fieldIds);

      return getRangeDataNew(fieldList);
    }

    throw new PreFailedException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.shareName());
  }

  public TableRelation queryRelation(String usrId, String tableId) {
    Table table = tableUtilNew.createSimpleTable(usrId, tableId);

    Datasource datasource = datasourceUtil.createDatasource(table.getDsId(), usrId);
    SimpleDatasource simpleDatasource = SimpleDatasource.builder()
        .createTime(datasource.getCreateTime())
        .desc(datasource.getDesc())
        .id(datasource.getId())
        .modifyTime(datasource.getModifyTime())
        .type(datasource.getType())
        .build();

    switch (table.getType()) {
      case SHARE:
        simpleDatasource.setName(DsConfUtil.getShareDsName());
        break;

      case PUBLIC:
        simpleDatasource.setName(DsConfUtil.getDsName(datasource.getId()));
        break;
      default:
        simpleDatasource.setName(datasource.getName());
    }

    TableRelation.TableRelationBuilder builder = TableRelation.builder();

    builder.charts(chartMapper.findByTId(tableId).stream()
        .map(c -> ChartDash.builder()
            .createTime(c.getCreateTime())
            .dashId(c.getDashId())
            .dashName(c.getDashName())
            .id(c.getId())
            .modifyTime(c.getModifyTime()).name(c.getName())
            .projectId(c.getProjectId())
            .projectName(c.getProjectName()).build()).collect(Collectors.toList()))
        .datasource(simpleDatasource);

    return builder.build();
  }
}
