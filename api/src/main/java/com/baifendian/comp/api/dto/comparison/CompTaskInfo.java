package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.dao.postgresql.model.comparison.CompTaskData;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/14 0014.
 */
@Setter
@Getter
public class CompTaskInfo {
  private List<CompTaskData> tasks;
  private int currentPage;
  private int totalCounts;
}
