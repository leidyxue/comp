package com.baifendian.comp.dao.postgresql.model.comparison;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2018/1/3 0003.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultTables {
  private List<ResultTable> resultTableList;
}
