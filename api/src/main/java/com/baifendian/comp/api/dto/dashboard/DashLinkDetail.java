package com.baifendian.comp.api.dto.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class DashLinkDetail {

  private String chartId;
  private String tableId;
  private List<DashLinkDto> links = new ArrayList<>();

  public DashLinkDetail addLink(String linkChartId, String linkTableId, String fieldId,
      String linkFieldId) {
    Optional<DashLinkDto> exist = links.stream()
        .filter(dl -> StringUtils.equals(dl.getLinkChartId(), linkChartId)).findFirst();
    if (exist.isPresent()){
      exist.get().addField(fieldId, linkFieldId);
    }else{
      DashLinkDto linkDto = new DashLinkDto();
      linkDto.setLinkChartId(linkChartId);
      linkDto.setLinkTableId(linkTableId);
      linkDto.addField(fieldId, linkFieldId);

      links.add(linkDto);
    }

    return this;
  }
}
