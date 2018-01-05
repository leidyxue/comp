package com.baifendian.comp.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * 表达式工具类
 */
public class AggregatorUtil {

  private final static Pattern p = Pattern.compile("\\$\\{[^\\}]*\\}");

  public static List<String> getFieldIds(String aggregator) {
    if (StringUtils.isEmpty(aggregator)){
      return new ArrayList<>();
    }
    Matcher matcher = p.matcher(aggregator);
    List<String> fieldIds = new ArrayList<>();

    while (matcher.find()) {
      String fieldId = matcher.group(0);
      fieldId = fieldId.substring(2, fieldId.length() - 1);
      fieldIds.add(fieldId);
    }

    return fieldIds;
  }
}
