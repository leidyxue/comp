package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.bi.engine.enums.FieldType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashFilterParam {
  private String id;
  private String name;
  private FieldType type;
  @Singular("chart")
  private List<String> charts;
  @Singular("table")
  private List<DashFilterTable> tables;

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DashFilterTable{
    private String tableId;
    private String fieldId;
  }
}
