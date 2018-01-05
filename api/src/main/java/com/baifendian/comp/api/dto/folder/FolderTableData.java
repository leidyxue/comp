package com.baifendian.comp.api.dto.folder;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.TableType;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
public class FolderTableData {

  private String id;
  private String name;
  private String desc;
  private String originName;
  private String dsId;
  private String dsName;
  private String parentId;
  private DSType dsType;
  private TableType tableType;
  private Date createTime;
  private Date modifyTime;
}
