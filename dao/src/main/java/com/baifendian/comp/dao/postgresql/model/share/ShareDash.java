package com.baifendian.comp.dao.postgresql.model.share;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareDash {

  private String id;
  @Default
  private Date createTime = new Date();
  private String dashId;

}
