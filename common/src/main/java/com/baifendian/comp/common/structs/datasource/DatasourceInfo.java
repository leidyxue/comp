package com.baifendian.comp.common.structs.datasource;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DatasourceInfo {

  private List<TableMeta> tables;
}
