package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.common.structs.dash.Project;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class DashProjectTreeDTO {
  List<DashInfo> dashboads;
  List<Project> projects;
}
