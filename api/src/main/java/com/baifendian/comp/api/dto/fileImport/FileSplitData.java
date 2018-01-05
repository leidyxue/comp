package com.baifendian.comp.api.dto.fileImport;

import com.baifendian.comp.common.consts.Constants;
import com.baifendian.comp.common.enums.SqRunStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/10/20 0020.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileSplitData {
  @Default
  private SqRunStatus status = SqRunStatus.SUCCESS;
  private String message;
  private List<FieldTypeInfo> schema;
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private List<List<String>> splitData;
}
