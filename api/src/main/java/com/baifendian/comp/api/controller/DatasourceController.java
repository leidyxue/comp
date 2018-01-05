package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.datasource.ConfData;
import com.baifendian.comp.api.dto.datasource.DataSourceData;
import com.baifendian.comp.api.dto.datasource.DsMeta;
import com.baifendian.comp.api.dto.datasource.UseTable;
import com.baifendian.comp.api.dto.folder.FolderTableData;
import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.structs.datasource.DatasourceInfo;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.api.service.DatasourceService;
import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.utils.HttpUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatasourceController {

  @Autowired
  private DatasourceService datasourceService;

  @GetMapping("/datasources-test/connect")
  public void datasourceTest(HttpServletRequest request,
      @RequestParam(value = "type") DSType type) {

    JDBCParam jdbcParam = HttpUtil.objectRequiredParam(request, "parameter", JDBCParam.class);
    jdbcParam.setType(type);
    datasourceService.dsTest(jdbcParam);
  }

  @GetMapping("/datasources-test/info")
  public DatasourceInfo getTableAndData(HttpServletRequest request,
      @RequestParam(value = "type") DSType type) {
    JDBCParam jdbcParam = HttpUtil.objectRequiredParam(request, "parameter", JDBCParam.class);

    jdbcParam.setType(type);

    DatasourceInfo datasourceInfo = new DatasourceInfo();
    datasourceInfo.setTables(datasourceService.getTableAndData(jdbcParam));

    return datasourceInfo;
  }

  /**
   * 查看所有数据源
   */
  @GetMapping(value = "/datasources")
  public DataSourceData findAll(@RequestAttribute(value = "userId") String userId,
      @RequestParam(value = "type", required = false) String type) {
    return datasourceService.findAll(userId, type);
  }

  /**
   * 添加数据源
   */
  @PostMapping(value = "/datasources")
  public ConfData createObject(@RequestAttribute(value = "userId") String userId,
      @RequestBody DataSourceCreateObj obj) {
    JDBCParam jdbcParam = obj.getParameter();
    jdbcParam.setType(obj.getType());

    return datasourceService.createDS(userId, obj.getName(), obj.getDesc(), obj.getType()
        , jdbcParam, obj.getUseTables());
  }

  /**
   * 查询一个数据源的具体配置
   */
  @GetMapping("/datasources/{datasourceId}")
  public ConfData queryConf(@RequestAttribute(value = "userId") String userId,
      @PathVariable("datasourceId") String datasourceId) {
    return datasourceService.queryConf(userId, datasourceId);
  }

  @GetMapping("/datasources/{datasourceId}/tables")
  public List<FolderTableData> queryDsTables(@RequestAttribute(value = "userId") String userId,
      @PathVariable("datasourceId") String datasourceId) {
    return datasourceService.getDsTables(userId, datasourceId);
  }

  @PutMapping("/datasources/{datasourceId}")
  public ConfData modify(@RequestAttribute(value = "userId") String userId,
      @PathVariable("datasourceId") String datasourceId, @RequestBody ModifyDataSourceObj modifyDataSourceObj) {
    List<UseTable> useTables = modifyDataSourceObj.getUseTables();
    if (useTables == null){
      useTables = new ArrayList<>();
    }
    return datasourceService
        .modifyDS(userId, datasourceId, modifyDataSourceObj.getName(),
            modifyDataSourceObj.getDesc(),
            modifyDataSourceObj.getParameter(), useTables);
  }

  @DeleteMapping("/datasources/{datasourceId}")
  public void delete(@RequestAttribute(value = "userId") String userId,
      @PathVariable("datasourceId") String datasourceId) {
    datasourceService.delete(userId, datasourceId);
  }

  @GetMapping("/datasource-meta")
  public DsMeta getDSMeta(@RequestAttribute(value = "userId") String userId) {
    return datasourceService.getMeta(userId);
  }

  @PostMapping("/datasources/public-data/{id}")
  public void addPublicData(@RequestAttribute(value = "userId") String userId, @PathVariable("id") String pubId) {
    datasourceService.addPublicData(userId, pubId);
  }
}

@Setter
@Getter
@ParamRequest
class DataSourceCreateObj {

  @ParamProperty(maxLen = 128, name = {"com.baifendian.bi.common.name.ds", "com.bfd.bi.common.ele.name"})
  private String name;
  @ParamProperty(required = false, maxLen = 256, name = {"com.baifendian.bi.common.name.ds", "com.bfd.bi.common.ele.desc"})
  private String desc;
  @ParamProperty
  private DSType type;
  @ParamProperty(recursive = true)
  private JDBCParam parameter;
  private List<UseTable> useTables;
}

@Setter
@Getter
@ParamRequest
class ModifyDataSourceObj {

  @ParamProperty(maxLen = 128, name = {"com.baifendian.bi.common.name.ds", "com.bfd.bi.common.ele.name"})
  private String name;
  @ParamProperty(required = false, maxLen = 256, name = {"com.baifendian.bi.common.name.ds", "com.bfd.bi.common.ele.desc"})
  private String desc;
  @ParamProperty(recursive = true)
  private JDBCParam parameter;
  private List<UseTable> useTables;
}
