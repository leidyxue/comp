package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.enums.FilterType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

  /**
   * 过滤条件类型
   */
  private FilterType type;

  /**
   * 过滤条件配置
   */
  private List<FilterConfig> configs;

  @JsonIgnore
  public String getFilters(){
    String result = "";
    for (FilterConfig filterConfig : configs){
      if (result.equals("")){
        result += "where" + filterConfig.getFilterSQL();
      }else {
        result += type.toString() + filterConfig.getFilterSQL();
      }
    }

    return result;
  }
}
