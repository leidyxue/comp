package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.common.enums.ExecStatus;
import java.util.Date;
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
public class ComparisonResult {
  @AuditProperty
  private String id;
  @AuditProperty
  private Date execTime;
  @AuditProperty
  private Date finishedTime;
  @AuditProperty
  private String taskId;
  private ResultTables resultTables;
  private ExecStatus status;
}
