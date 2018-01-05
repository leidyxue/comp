package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.api.dto.table.SchemaData;
import com.baifendian.comp.common.enums.ExecStatus;
import com.baifendian.comp.dao.postgresql.model.comparison.TitleInfo;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/19 0019.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResultData {
  private String resultId;
  private String taskId;
  private String taskName;
  private Date startExecTime;
  private Date endExecTime;
  private ExecStatus status;
  private List<TitleInfo> titles;
  private List<List<Object>> data;
}
