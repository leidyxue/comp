package com.baifendian.comp.api.service.util;

import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.api.dto.comparison.TaskDTO;
import com.baifendian.comp.common.enums.CompNodeType;
import com.baifendian.comp.common.enums.CompType;
import com.baifendian.comp.common.structs.field.FieldOld;
import com.baifendian.comp.common.structs.table.TableOld;
import com.baifendian.bi.engine.util.FieldCastUtil;
import com.baifendian.comp.dao.postgresql.mapper.FieldMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapper;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonNode;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonsRelation;
import com.baifendian.comp.dao.postgresql.model.comparison.Condition;
import com.baifendian.comp.dao.postgresql.model.comparison.FilterConfig;
import com.baifendian.comp.dao.postgresql.model.comparison.SelectedField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
@Component
public class TaskSQLUtil {
  @Autowired
  private FieldMapper fieldMapper;

  @Autowired
  private TableMapper tableMapper;

  /**
   * 处理任务节点数据
   * @param taskDTO
   */
  public void parseTaskDTO(TaskDTO taskDTO){
    //根据字段id获取字段信息
    Set<String> fieldIds = new HashSet<>();
    for (ComparisonsRelation relation : taskDTO.getRelations()){
      for (SelectedField selectedField : relation.getSelectedLeftFields().getSelectedFields()){
        fieldIds.add(selectedField.getId());
      }
      for (SelectedField selectedField : relation.getSelectedRightFields().getSelectedFields()){
        fieldIds.add(selectedField.getId());
      }

      for (Condition condition : relation.getConditions().getConditions()){
        fieldIds.add(condition.getLeftFieldId());
        fieldIds.add(condition.getRightFieldId());
      }

      if (relation.getFilter() != null){
        for (FilterConfig filterConfig : relation.getFilter().getConfigs()){
          if (filterConfig.getFieldId() != null){
            fieldIds.add(filterConfig.getFieldId());
          }

        }
      }

    }

    //所有列的集合
    List<FieldOld> fields = fieldMapper.selectByIds(fieldIds);

    //处理node
    for (ComparisonNode node : taskDTO.getNodes()){
      if (node.getType().equals(CompNodeType.TABLE) && node.getTableId() != null){
        TableOld table = tableMapper.selectById(node.getTableId());
        node.setTableName(table.getName());
        node.setOriginTableName(table.getOriginName());
      }
    }

    //处理关系
    for (ComparisonsRelation relation : taskDTO.getRelations()){
      //处理conditions
      for (Condition condition : relation.getConditions().getConditions()){
        FieldOld leftField = findField(condition.getLeftFieldId(), fields);
        FieldOld rightField = findField(condition.getRightFieldId(), fields);
        condition.setLeftName(leftField.getName());
        condition.setLeftOriginName(leftField.getOriginName());
        condition.setRightName(rightField.getName());
        condition.setRightOriginName(rightField.getOriginName());
        condition.setLeftFieldType(leftField.getType());
        condition.setRightFieldType(rightField.getType());
        condition.setLeftOrgType(leftField.getOriginType());
        condition.setRightOrgType(rightField.getOriginType());
      }

      //处理filter config
      if (relation.getFilter() != null){
        for (FilterConfig filterConfig : relation.getFilter().getConfigs()){
          if (filterConfig.getFieldId() != null){
            FieldOld field = findField(filterConfig.getFieldId(), fields);
            filterConfig.setFieldName(field.getName());
            filterConfig.setFieldOriginName(field.getOriginName());
            filterConfig.setFieldType(field.getType());
            filterConfig.setFieldTOrgType(field.getOriginType());
          }
        }
      }


      //处理selectLefFields和selectedRightFields
      for (SelectedField selectedField : relation.getSelectedLeftFields().getSelectedFields()){
        FieldOld field = findField(selectedField.getId(), fields);
        selectedField.setName(field.getName());
        selectedField.setOriginName(field.getOriginName());
        selectedField.setType(field.getType());
        selectedField.setOrgType(field.getOriginType());
      }

      for (SelectedField selectedField : relation.getSelectedRightFields().getSelectedFields()){
        FieldOld field = findField(selectedField.getId(), fields);
        selectedField.setName(field.getName());
        selectedField.setOriginName(field.getOriginName());
        selectedField.setType(field.getType());
        selectedField.setOrgType(field.getOriginType());
      }

    }


  }

  public FieldOld findField(String fieldId, List<FieldOld> fields){
    for (FieldOld field : fields){
      if (field.getId().equals(fieldId)){
        return field;
      }
    }
    return null;
  }

  public void getRoot(TaskDTO taskDTO){
    List<ComparisonNode> nodes = taskDTO.getNodes();
    List<ComparisonNode> tableNodes = new ArrayList<>();
    List<ComparisonNode> resultNodes = new ArrayList<>();
    List<String> tableNodeIds = new ArrayList<>();
    List<String> resultNodeIds = new ArrayList<>();

    //生成表节点和结果集节点
    for (ComparisonNode node : nodes){
      if (node.getType().equals(CompNodeType.TABLE)){
        tableNodes.add(node);
        tableNodeIds.add(node.getId());
      }else {
        resultNodes.add(node);
        resultNodeIds.add(node.getId());
      }
    }

    for (ComparisonNode node : resultNodes){

    }

    for (ComparisonsRelation relation : taskDTO.getRelations()){
      //验证
      //TODO

      for (ComparisonsRelation relation1 : taskDTO.getRelations()){
        if (tableNodeIds.contains(relation1.getLeftId()) && tableNodeIds.contains(relation1.getRightId())){
          //如果结果集节点的左右节点都在表节点中，就将表节点的两个元素删除，将结果集节点加入表节点集合中
          tableNodeIds.remove(relation1.getLeftId());
          tableNodeIds.remove(relation1.getRightId());
          tableNodeIds.add(relation1.getResultId());
        }
      }

    }
  }

  public String generateSQL(TaskDTO taskDTO){
    return null;
  }


  public String getTaskSQL(TaskDTO taskDTO, ComparisonsRelation rootRelation, String sql){
//    ComparisonsRelation rootRelation = getRootRelation(taskDTO);
    //左节点
    ComparisonsRelation leftRelation = null;
    ComparisonNode leftNode = null;
    for (ComparisonsRelation relation : taskDTO.getRelations()){
      if (rootRelation.getLeftId().equals(relation.getResultId())){
        leftRelation = relation;
      }
    }

    for (ComparisonNode node : taskDTO.getNodes()){
      if (rootRelation.getLeftId().equals(node.getId())){
        leftNode = node;
      }
    }

    //右节点
    ComparisonsRelation rightRelation = null;
    ComparisonNode rightNode = null;
    for (ComparisonsRelation relation : taskDTO.getRelations()){
      if (rootRelation.getRightId().equals(relation.getResultId())){
        rightRelation = relation;
      }
    }

    for (ComparisonNode node : taskDTO.getNodes()){
      if (rootRelation.getRightId().equals(node.getId())){
        rightNode = node;
      }
    }

    //组装sql
    sql = parseSQL(rootRelation, leftNode, rightNode, sql);

    //左节点递归
    if (leftRelation != null){
      sql = getTaskSQL(taskDTO, leftRelation, sql);
    }


    //右节点递归
    if (rightRelation != null){
      sql = getTaskSQL(taskDTO, rightRelation, sql);
    }

    return sql;
  }


  /**
   * 组装sql
   * @param relation
   * @param leftNode
   * @param rightNode
   * @param sql
   * @return
   */
  public String parseSQL(ComparisonsRelation relation, ComparisonNode leftNode, ComparisonNode rightNode, String sql){
    String resultCols = "";
    String leftName = "";
    String rightName = "";
    String joinType = "";
    String conditions = "";
    String filters = "";
    String subWhere = "";


    //leftName
    if (leftNode.getType().equals(CompNodeType.TABLE)){
      leftName = getNodeSQL(leftNode);
    }else {
      leftName = leftNode.getLeftFromName();
    }


    //rightName
    if (rightNode.getType().equals(CompNodeType.TABLE)){
      rightName = getNodeSQL(rightNode);
    }else {
      rightName = rightNode.getRightFromName();
    }

    String leftSubStr = "";
    String rightSubStr = "";
    //conditions
    for (Condition condition : relation.getConditions().getConditions()){
      String leftCol = leftNode.getSelectName() + "." + condition.getLeftFieldId();
//      String leftCol = FieldCastUtil.castValue(leftCol1, condition.getLeftFieldType(), condition.getLeftOrgType());
      String rightCol = rightNode.getSelectName() + "." + condition.getRightFieldId();
//      String rightCol = FieldCastUtil.castValue(rightCol1, condition.getRightFieldType(), condition.getRightOrgType());

      String empty = "";
      if (condition.getLeftFieldType().equals(FieldType.TEXT)){
        empty = leftCol + "!='' and ";
      }
      conditions += leftCol + "=" + rightCol + " and " + leftCol + " is not null and " + empty;

      //并集处理
      if (relation.getCompType().equals(CompType.FULL_JOIN)){
        condition.setUnionStr(FieldCastUtil.unionField(leftCol, rightCol));
      }

      //差集条件
      if (relation.getCompType().equals(CompType.LEFT_SUBTRACT)){
        //左差
        if (leftSubStr.equals("")){
          leftSubStr = rightCol + " is null";
        }else {
          leftSubStr += " and " + rightCol + " is null";
        }
      }

      if (relation.getCompType().equals(CompType.RIGHT_SUBTRACT)){
        //右差
        if (leftSubStr.equals("")){
          rightSubStr = leftCol + " is null";
        }else {
          rightSubStr += " and " + leftCol + " is null";
        }
      }

    }

    conditions = conditions.substring(0, conditions.length()-5);

    //resultCols
    for (SelectedField selectedField : relation.getSelectedLeftFields().getSelectedFields()){
      String resultCol = leftNode.getSelectName() + "." + selectedField.getSelectName() + ",";
      if (relation.getCompType().equals(CompType.FULL_JOIN)){
        //如果是并集会对比对字段做归一处理
        for (Condition condition : relation.getConditions().getConditions()){
          if (condition.getLeftFieldId().equals(selectedField.getId())){
            resultCol = condition.getUnionStr() + " as " + selectedField.getSelectName() + ",";
          }
        }
      }
        resultCols += resultCol;
    }

    for (SelectedField selectedField : relation.getSelectedRightFields().getSelectedFields()){
      String resultCol = rightNode.getSelectName() + "." + selectedField.getSelectName() + ",";
      if (relation.getCompType().equals(CompType.FULL_JOIN)){
        for (Condition condition : relation.getConditions().getConditions()){
          if (condition.getRightFieldId().equals(selectedField.getId())){
            resultCol = condition.getUnionStr() + " as " + selectedField.getSelectName() + ",";
          }
        }
      }
      resultCols += resultCol;
    }

    resultCols = resultCols.substring(0, resultCols.length()-1);


    //joinType
    CompType type = relation.getCompType();
    if (type.equals(CompType.JOIN)){
      joinType = "JOIN";
    }else if (type.equals(CompType.FULL_JOIN)){
      joinType = "FULL JOIN";
    }else if (type.equals(CompType.LEFT_SUBTRACT)){
      joinType = "FULL JOIN";
      subWhere = leftSubStr;
    }else if (type.equals(CompType.RIGHT_SUBTRACT)){
      joinType = "FULL JOIN";
      subWhere = rightSubStr;
    }


    //filters
    filters = relation.getFilter().getFilters();

    if (!subWhere.equals("") && filters.equals("")){
      subWhere = "where " + subWhere;
    }else if (!subWhere.equals("")){
      subWhere = "and " + subWhere;
    }

    sql = sql.replaceFirst("\\{resultCols\\}", resultCols)
//        .replace("${type}", joinType)
        .replaceFirst("\\{conditions\\}", conditions)
        .replaceFirst("\\{filter\\}", filters)
        .replaceFirst("\\{subWhere\\}", subWhere);
    sql = sql
        .replaceFirst("\\{leftTable\\}\\s\\{type\\}\\s\\{rightTable\\}", leftName + " " + joinType + " " + rightName);

    return sql;

  }

  /**
   * select表的处理
   * @param node
   * @return
   */
  public String getNodeSQL(ComparisonNode node){
    StringBuffer buf = new StringBuffer();
    buf.append("(select ");
    if (node.getType().equals(CompNodeType.TABLE)){
      List<FieldOld> fieldOlds = fieldMapper.findByTId(node.getTableId());
      for (FieldOld fieldOld : fieldOlds){
        String orgName = FieldCastUtil.castValue(fieldOld.getOriginName(), fieldOld.getType(), fieldOld.getOriginType());
        buf.append(orgName + " as " + fieldOld.getId() + ",");
      }
      return buf.substring(0, buf.length()-1) + " from " + node.getOriginTableName() + ") as " + node.getId();
    }else {
      return null;
    }
  }


  /**
   * 获取根节点
   * @param relations
   * @return
   */
  public List<ComparisonsRelation> getRootRelations(List<ComparisonsRelation> relations){
    List<ComparisonsRelation> comparisonsRelations = new ArrayList<>();
    for (ComparisonsRelation relation : relations){
      boolean isRoot = true;
      for (ComparisonsRelation relation1 : relations){
        if (relation1.getLeftId().equals(relation.getResultId()) || relation1.getRightId().equals(relation.getResultId())){
          isRoot = false;
        }
      }
      if (isRoot){
        comparisonsRelations.add(relation);
      }

    }
    return comparisonsRelations;
  }

}
