package com.baifendian.comp.api.dto.dashboard;


import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class DashLinkDto {
  private String linkChartId;
  private String linkTableId;
  private List<LinkField> linkFields = new ArrayList<>();

  public DashLinkDto addField(String fieldId, String linkFieldId){
    linkFields.add(new LinkField(fieldId, linkFieldId));
    return this;
  }

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LinkField{

    private String fieldId;
    private String linkFieldId;
  }
}
