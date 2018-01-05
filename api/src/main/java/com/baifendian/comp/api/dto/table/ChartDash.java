package com.baifendian.comp.api.dto.table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class ChartDash {

  private String id;
  private String dashId;
  private String projectId;
  private String name;
  private String dashName;
  private String projectName;
  private Date createTime;
  private Date modifyTime;
}
