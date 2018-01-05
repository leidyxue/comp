package com.baifendian.comp.dao.postgresql.model.comparison;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/18 0018.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultTable {
  private String tableName;
  private String nodeName;
  private List<TitleInfo> titles;
}
