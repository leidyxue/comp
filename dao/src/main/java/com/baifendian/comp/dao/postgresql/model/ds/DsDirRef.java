package com.baifendian.comp.dao.postgresql.model.ds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DsDirRef {

  private String dsId;
  private String dirId;
  private String owner;
}
