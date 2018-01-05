package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.api.dto.table.SchemaData;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashUsedTable {

  private String dashId;
  private List<TableInfo> tables = new ArrayList<>();

  @Setter
  @Getter
  public static class TableInfo{

    private String id;
    private String name;
    private List<SchemaData> schema;
  }
}
