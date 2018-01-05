package com.baifendian.comp.api.dto.folder;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FolderTree {

  private List<FolderData> folders;
  private List<FolderTableData> tables;
}
