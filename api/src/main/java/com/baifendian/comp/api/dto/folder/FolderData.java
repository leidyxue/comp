package com.baifendian.comp.api.dto.folder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
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
@JsonInclude(Include.NON_NULL)
public class FolderData {

  private String id;
  private String name;
  private Date createTime;
  private Date modifyTime;
  private String parentId;
}
