package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.bi.engine.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/30 0030.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TitleInfo {
  private String title;
  private FieldType type;
}
