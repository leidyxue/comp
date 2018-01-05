package com.baifendian.comp.api.dto.datasource;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DsMeta {

  private Map<String, Object> meta = new HashMap<>();
}
