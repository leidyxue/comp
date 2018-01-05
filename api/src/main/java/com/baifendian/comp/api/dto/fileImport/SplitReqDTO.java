package com.baifendian.comp.api.dto.fileImport;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xuelei on 2017/10/24 0024.
 */
@Setter
@Getter
public class SplitReqDTO {
  private String delimiter;
  private List<String> data;
}
