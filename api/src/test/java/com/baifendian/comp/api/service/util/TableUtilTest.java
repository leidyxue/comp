package com.baifendian.comp.api.service.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TableUtilTest {

  @Test
  public void idNameTransform() throws Exception {
    Map<String, String> idNameMap = new HashMap<>();
    idNameMap.put("id1", "name1");
    idNameMap.put("id2", "n2");

    String aggregator = "${id1}+${id2}";

    String result = TableUtil.idNameTransform(aggregator, idNameMap);

    assertEquals(result, "${name1}+${n2}");
  }

}