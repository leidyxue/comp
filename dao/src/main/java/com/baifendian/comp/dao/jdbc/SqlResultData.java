package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.consts.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SqlResultData {
  public int dataStatus(){
    return dataStatus.ordinal();
  }
  @Singular("title")
  private List<String> titles;
  @Singular("data")
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private List<List<Object>> dataList;

  @Default
  private SqRunStatus dataStatus = SqRunStatus.SUCCESS;
  private String dataMessage;
}
