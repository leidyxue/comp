package com.baifendian.comp.common.structs.folder;

import com.baifendian.comp.common.annotation.AuditProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Folder {

  private String id;
  @AuditProperty
  private String name;
  @AuditProperty
  private String parentId;
  @Default
  private Date createTime = new Date();
  @Default
  private Date modifyTime = new Date();
  private String owner;

  public static Folder getRoot(String userId){
    return Folder.builder().owner(userId)
        .name("root")
        .build();
  }

  public boolean isFolderId(String fId){
    return StringUtils.equals(fId, this.getId());
  }

  public boolean isInnerModify(){
    return !StringUtils.equals(id, name);
  }
}
