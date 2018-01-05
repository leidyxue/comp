package com.baifendian.comp.dao.postgresql.model.table;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.utils.AggregatorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@NoArgsConstructor
public class TableAllData {
  public Set<String> usedFieldIds(){
    return fieldList.stream()
        .filter(Field::isGenerate)
        .flatMap(f -> AggregatorUtil.getFieldIds(f.getAggregator()).stream())
        .collect(Collectors.toSet());
  }
  public List<Field> nonDeletedField(){
    if (CollectionUtils.isEmpty(fieldList)){
      return new ArrayList<>();
    }

    return fieldList.stream()
        .filter(f -> !f.isDeleted()).collect(Collectors.toList());
  }
  public List<Field> tableDataField(){
    if (CollectionUtils.isEmpty(fieldList)){
      return new ArrayList<>();
    }

    return fieldList.stream().filter(f -> f.getGenType() != FieldGenType.C_GENERATE)
        .filter(f -> !f.isDeleted()).collect(Collectors.toList());
  }

  public Optional<Field> getField(String id){
    return fieldList.stream().filter(f -> StringUtils.equals(id, f.getId()))
        .findFirst();
  }
  public String tableId(){
    return table.getId();
  }
  public String tableOrgName(){
    return table.getOriginName();
  }
  public String tableName(){
    return table.getName();
  }
  public DSType getDSType(){
    return table.getDsType();
  }

  public JDBCParam jdbcParam(){
    return table.getJdbcParameter();
  }

  public TableAllData addFieldList(List<Field> fields){
    if (CollectionUtils.isEmpty(fields)){
      return this;
    }

    if (this.fieldList == null){
      fieldList = new ArrayList<>();
    }

    fieldList.addAll(
        fields.stream().filter(f -> StringUtils.equals(f.getTableId(), table.getId())).collect(
            Collectors.toList()));

    return this;
  }

  public TableAllData(Table table){
    this.table = table;
    fieldList = new ArrayList<>();
  }

  public boolean isDeletedTable(){
    return table.isDeleted();
  }

  private Table table;
  private List<Field> fieldList;
}
