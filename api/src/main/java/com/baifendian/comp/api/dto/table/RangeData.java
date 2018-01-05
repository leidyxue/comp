package com.baifendian.comp.api.dto.table;

import com.baifendian.bi.engine.enums.FieldType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RangeData {

  private FieldType type;

  private List<String> range;
}
