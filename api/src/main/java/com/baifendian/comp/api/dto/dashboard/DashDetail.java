package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.common.structs.dash.DashElementParam;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashDetail {
  private String id;
  private String name;
  private String desc;
  private String projectId;
  private String projectName;
  private Date createTime;
  private Date modifyTime;
  private DashMeta meta;

  @Setter
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DashMeta{

    private List<DashFilterParam> filters;
    private JsonNode styleConf;
    @Default
    private List<DashElementParam> elements = new ArrayList<>();
    @Default
    private List<DashLinkDetail> linkInfos = new ArrayList<>();
  }
}
