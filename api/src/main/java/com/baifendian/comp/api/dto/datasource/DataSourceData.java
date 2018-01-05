package com.baifendian.comp.api.dto.datasource;

import com.baifendian.comp.common.enums.ds.DsMetaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class DataSourceData {

  List<DsInfo> datasources = new ArrayList<>();
  List<DsSum> summary = new ArrayList<>();

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DsInfo{
    private String id;
    private String name;
    private String desc;
    private String type;
    @Default
    private DsMetaType metaType = DsMetaType.DATABASE;
    private Date createTime;
    private Date modifyTime;
  }

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class  DsSum{
    /**
     * 数据源类型
     */
    private String type;
    private String name;

    /**
     * 出现次数
     */
    private int count;
  }
}
