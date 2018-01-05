package com.baifendian.comp.common.structs.table;

import com.baifendian.comp.common.structs.table.combine.TableJoin;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableParam {

  private List<TableJoin> tableJoins;
  // tableDatas 只需要fieldId列表就可以了
  private List<String> tableDatas;
}
