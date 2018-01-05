package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonNode;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonsRelation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/15 0015.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
  private String id;
  @AuditProperty
  private String name;
  private List<ComparisonNode> nodes;
  private List<ComparisonsRelation> relations;
}
