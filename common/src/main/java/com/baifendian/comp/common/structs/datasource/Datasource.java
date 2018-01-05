package com.baifendian.comp.common.structs.datasource;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.common.enums.DSType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class Datasource {

  /**
   * 数据源ID
   */
  private String id;
  /**
   * 数据源名称
   */
  @AuditProperty
  private String name;
  private String desc;
  /**
   * 数据源类型
   */
  private DSType type;
  /**
   * 参数配置
   */
  private JDBCParam parameter;
  /**
   * 创建时间
   */
  @Default
  private Date createTime = new Date();
  /**
   * 修改时间
   */
  @Default
  private Date modifyTime = new Date();
  /**
   * 所有者ID
   */
  private String owner;
}
