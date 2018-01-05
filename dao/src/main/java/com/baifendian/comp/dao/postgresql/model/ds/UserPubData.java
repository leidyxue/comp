package com.baifendian.comp.dao.postgresql.model.ds;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPubData {

  private String id;
  private String userId;
  private Date createTime = new Date();
}
