package com.baifendian.comp.api.dto.chart;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartLinkParam {

  private String chartId;
  private List<LinkField> linkFields;

  @Setter
  @Getter
  public static class LinkField{

    private String fieldId;
    private String value;
  }
}
