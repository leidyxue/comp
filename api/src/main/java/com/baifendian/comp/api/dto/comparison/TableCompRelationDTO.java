package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonTask;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableCompRelationDTO {
  private int taskNum;
  private List<ComparisonTask> tasks;
}
