package com.baifendian.comp.api.dto.table;

import com.baifendian.comp.common.consts.Constants;
import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDetailData {

  private String id;

  private String dsId;

  private String name;

  private String dsName;

  private String originName;

  private String desc;

  private DSType dsType;

  private TableType genType;

  private long number;

  @Default
  private int dataStatus = 0;

  private String dataMessage;

  @Default
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private List<List<Object>> data = new ArrayList<>();

  private List<String> titles;

  private List<SchemaData> schema;

  private Date createTime;

  private Date modifyTime;

  public TableDetailData(Table table, List<SchemaData> schemaDataList){

  }
}
