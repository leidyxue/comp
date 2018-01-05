package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.table.RangeData;
import com.baifendian.comp.api.dto.table.SchemaData;
import com.baifendian.comp.api.dto.table.TableDetailData;
import com.baifendian.comp.api.dto.table.TableField;
import com.baifendian.comp.api.dto.table.TableFieldParam;
import com.baifendian.comp.api.dto.table.TableRelation;
import com.baifendian.comp.api.dto.table.TableSingleField;
import com.baifendian.comp.api.service.TableService;
import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.common.utils.HttpUtil;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableController {

  @Autowired
  private TableService tableService;

  /**
   * 得到工作表详细信息
   */
  @GetMapping("/tables/{tableId}")
  public TableDetailData getTable(@RequestAttribute("userId") String userId,
      @PathVariable("tableId") String tableId) {
    return tableService.getTableDetail(userId, tableId);
  }

  @PutMapping("/tables/{tableId}")
  public void modify(@RequestAttribute("userId") String userId,
      @PathVariable("tableId") String tableId,
      @RequestBody ModifyTableParam modifyTableParam) {
    tableService
        .modifyTable(userId, tableId, modifyTableParam.getName(), modifyTableParam.getDesc());
  }

  @DeleteMapping("/tables/{tableId}")
  public void deleteTable(@RequestAttribute("userId") String userId,
      @PathVariable("tableId") String tableId) {
    tableService.delete(userId, tableId);
  }

  @PostMapping("/tables/{tableId}/schema")
  public SchemaData createTableField(@PathVariable("tableId") String tableId,
      @RequestAttribute("userId") String userId,
      @RequestBody CreateTableFieldParam createTableFieldParam) {
    return tableService
        .createTableField(userId, tableId, createTableFieldParam.getName(),
            createTableFieldParam.getDesc(), createTableFieldParam.getAggregator(),
            createTableFieldParam.getType(), createTableFieldParam.getGenType());
  }

  @GetMapping("/tables/{tableId}/schema")
  public TableField getTableField(@RequestAttribute("userId") String userId,
      @PathVariable("tableId") String tableId) {
    return tableService.getTableField(userId, tableId);
  }

  @PutMapping("/tables/{tableId}/schema/{fieldId}")
  public TableSingleField modifyTableField(@PathVariable("tableId") String tableId,
      @PathVariable("fieldId") String fieldId, @RequestAttribute(value = "userId") String userId,
      @RequestBody ModifyTableFieldParam fieldParam) {
    return tableService
        .modifyTableField(userId, tableId, fieldId, fieldParam.getName(), fieldParam.getDesc(),
            fieldParam.getAggregator(), fieldParam.getType());
  }

  @DeleteMapping("/tables/{tableId}/schema/{fieldId}")
  public void deleteTableField(@PathVariable("tableId") String tableId,
      @PathVariable("fieldId") String fieldId,
      @RequestAttribute(value = "userId") String userId) {
    tableService.deleteTableField(userId, tableId, fieldId);
  }

  @GetMapping("/tables/{tableId}/relation")
  public TableRelation getTableRelation(@RequestAttribute(value = "userId") String user,
      @PathVariable("tableId") String tableId) {
    return tableService.queryRelation(user, tableId);
  }

  @GetMapping("/tables-range")
  public RangeData getRange(HttpServletRequest request) {
    List<TableFieldParam> tableFieldParamList = HttpUtil
        .listRequiredParam(request, "selectedTables", TableFieldParam.class);
    return tableService.rangeDate(tableFieldParamList);
  }
}

@Setter
@Getter
@ParamRequest
class ModifyTableParam {

  @ParamProperty
  private String name;

  private String desc;
}

@Setter
@Getter
@ParamRequest
class CreateTableFieldParam {

  @ParamProperty
  private String name;

  private String desc;

  @ParamProperty
  private String aggregator;

  @ParamProperty
  private FieldType type;

  @ParamProperty(range = {"C_GENERATE", "T_GENERATE"})
  private FieldGenType genType;
}

@Setter
@Getter
@ParamRequest
class ModifyTableFieldParam {

  @ParamProperty
  private String name;

  private String desc;

  //@ParamProperty
  private String aggregator;

  @ParamProperty
  private FieldType type;

}
