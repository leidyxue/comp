package com.baifendian.comp.api.dto.datasource;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.Datasource;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

@Setter
@Getter
public class ConfData {

  /**
   * 数据源ID
   */
  private String id;
  /**
   * 数据源名称
   */
  private String name;

  private String desc;
  /**
   * 数据源类型
   */
  private DSType type;
  /**
   * 参数配置
   */
  private JDBCParam parameter;
  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 修改时间
   */
  private Date modifyTime;

  private List<UseTable> useTables;

  public ConfData(Datasource datasource) {
    this.id = datasource.getId();
    this.name = datasource.getName();
    this.desc = datasource.getDesc();
    this.type = datasource.getType();
    this.parameter = (JDBCParam) datasource.getParameter();
    this.createTime = datasource.getCreateTime();
    this.modifyTime = datasource.getModifyTime();
  }

  public ConfData(Datasource datasource, List<Field> fieldList) {
    this.id = datasource.getId();
    this.name = datasource.getName();
    this.desc = datasource.getDesc();
    this.type = datasource.getType();
    this.parameter = (JDBCParam) datasource.getParameter();
    this.createTime = datasource.getCreateTime();
    this.modifyTime = datasource.getModifyTime();

    if (CollectionUtils.isNotEmpty(fieldList)) {

      Map<String, UseTable> useTableMap = new HashMap<>();
      for (Field field : fieldList) {
        if (!useTableMap.containsKey(field.getTableOrgName())) {
          UseTable useTable = new UseTable();
          useTable.setTableName(field.getTableOrgName());
          useTable.setFields(new ArrayList<>());
          useTable.getFields().add(field.getName());
          useTableMap.put(field.getTableOrgName(), useTable);
        } else {
          useTableMap.get(field.getTableOrgName()).getFields().add(field.getName());
        }
      }

      this.useTables = useTableMap.entrySet().stream().map(Entry::getValue)
          .collect(Collectors.toList());
    }
  }
}
