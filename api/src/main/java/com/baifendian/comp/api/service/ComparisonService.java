package com.baifendian.comp.api.service;

import com.baifendian.comp.api.dto.comparison.ExecData;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.common.enums.CompNodeType;
import com.baifendian.comp.common.enums.ExecStatus;
import com.baifendian.comp.common.enums.ScheduleStatus;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.common.utils.json.JsonUtil;
import com.baifendian.comp.dao.config.StorageConfig;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.jdbc.JDBCExec;
import com.baifendian.comp.dao.postgresql.mapper.FieldMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.NodeMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.RelationMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.ResultMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.ScheduleMapper;
import com.baifendian.comp.dao.postgresql.mapper.comparison.TaskMapper;
import com.baifendian.comp.dao.postgresql.model.comparison.CompTaskData;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonNode;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonResult;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonSchedule;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonTask;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonsRelation;
import com.baifendian.comp.dao.postgresql.model.comparison.Crontab;
import com.baifendian.comp.dao.postgresql.model.comparison.ResultTable;
import com.baifendian.comp.dao.postgresql.model.comparison.ResultTables;
import com.baifendian.comp.dao.postgresql.model.comparison.SelectedField;
import com.baifendian.comp.api.dto.comparison.CompTaskInfo;
import com.baifendian.comp.api.dto.comparison.ResultInfo;
import com.baifendian.comp.api.dto.comparison.TableCompRelationDTO;
import com.baifendian.comp.api.dto.comparison.TaskDTO;
import com.baifendian.comp.api.dto.comparison.TaskResultData;
import com.baifendian.comp.api.dto.comparison.TaskResultsDTO;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.service.util.TaskSQLUtil;
import com.baifendian.comp.api.thrift.QuartzService;
import com.baifendian.comp.api.thrift.client.ThriftClient;
import com.baifendian.comp.dao.postgresql.model.comparison.TitleInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.TException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xuelei on 2017/11/13 0013.
 */
@Service
public class ComparisonService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private NodeMapper nodeMapper;

  @Autowired
  private RelationMapper relationMapper;

  @Autowired
  private FieldMapper fieldMapper;

  @Autowired
  private TableMapper tableMapper;

  @Autowired
  private TaskSQLUtil taskSQLUtil;

  @Autowired
  private ResultMapper resultMapper;

  @Autowired
  private ScheduleMapper scheduleMapper;

  static String sql = "select {resultCols} from {leftTable} {type} {rightTable} on {conditions} {filter} {subWhere}";

  public CompTaskInfo getTasks(String userId, String userName,
      int currentPage, int pageSize, String keyWord, ExecStatus status, String timeOrder,
      Date startDate, Date endDate) {
    if (keyWord !=null && keyWord.contains("'")){
      keyWord = keyWord.replace("'", "''");
    }

    List<CompTaskData> compTaskDatas = taskMapper
        .findAll(userId, (currentPage - 1) * pageSize, pageSize, keyWord, status, timeOrder, startDate, endDate);
    CompTaskInfo compTaskInfo = new CompTaskInfo();

    for (CompTaskData compTaskData : compTaskDatas){
      if (compTaskData.getExecTime() != null && compTaskData.getFinishedTime() != null){
        Long spendSecends = (compTaskData.getFinishedTime().getTime()-compTaskData.getExecTime().getTime())/1000 +1;
        compTaskData.setSpendTime(spendSecends + "s");
      }

    }

    parseTasks(compTaskDatas, userName);
    compTaskInfo.setTasks(compTaskDatas);
    compTaskInfo.setCurrentPage(currentPage);

    int total = taskMapper.totalCount(userId,keyWord, status, startDate, endDate);
    compTaskInfo.setTotalCounts(total);

    //结果表

    return compTaskInfo;
  }

  public List<CompTaskData> parseTasks(List<CompTaskData> compTaskDatas, String userName) {
    for (CompTaskData compTaskData : compTaskDatas) {
      compTaskData.setOwner(userName);

    }
    return compTaskDatas;
  }

  /**
   * 创建比对任务
   */
  @Transactional
  public TaskDTO createTask(String userId, String name, List<ComparisonNode> nodes,
      List<ComparisonsRelation> relations) {
    //先创建任务
    ComparisonTask comparisonTask = new ComparisonTask();
    String taskId = GenIdUtil.genTaskId();
    comparisonTask.setId(taskId);
    comparisonTask.setName(name);
    comparisonTask.setOwner(userId);
    comparisonTask.setCreateTime(new Date());
    comparisonTask.setModifyTime(new Date());
    taskMapper.insert(comparisonTask);

    //再创建节点
    if (nodes != null) {
      for (ComparisonNode comparisonNode : nodes) {
        comparisonNode.setTaskId(taskId);
        nodeMapper.insert(comparisonNode);
      }
    }

    //最后创建关系
    if (relations != null) {
      for (ComparisonsRelation relation : relations) {
        relation.setTaskId(taskId);
        relationMapper.insert(relation);
      }
    }

    //创建结果
    TaskDTO taskDTO = new TaskDTO(taskId, name, nodes, relations);
    AuditUtil.pushInsert(AuditPageName.COMPTASK, AuditUtil.getAuditData(taskDTO));
    return taskDTO;
  }

  /**
   * 查看任务
   */
  public TaskDTO getTask(String taskId) {
    return parseTask(taskId);
  }

  public TaskDTO modifyTask(String userId, String taskId, String name, List<ComparisonNode> nodes,
      List<ComparisonsRelation> relations) {

    //修改名称
    ComparisonTask comparisonTask = taskMapper.findById(taskId);
    String oldData = AuditUtil.getAuditData(comparisonTask);
    comparisonTask.setName(name);
    comparisonTask.setModifyTime(new Date());
    taskMapper.update(comparisonTask);

    //删除内容
    relationMapper.deleteByTaskId(taskId);
    nodeMapper.deleteByTaskId(taskId);

    //添加内容
    //再创建节点
    if (nodes != null) {
      for (ComparisonNode comparisonNode : nodes) {
        comparisonNode.setTaskId(taskId);
        nodeMapper.insert(comparisonNode);
      }
    }

    //最后创建关系
    if (relations != null) {
      for (ComparisonsRelation relation : relations) {
        relation.setTaskId(taskId);
        relationMapper.insert(relation);
      }
    }
    AuditUtil.pushUpdate(AuditPageName.COMPTASK, oldData, AuditUtil.getAuditData(comparisonTask));

    return null;
  }

  /**
   * 任务重命名
   */
  public void modifyName(String userId, String taskId, String name) {
    ComparisonTask task = taskMapper.findById(taskId);
    task.setName(name);
    task.setModifyTime(new Date());
    taskMapper.update(task);
  }

  public void deleteTask(String userId, String taskId) {
    ComparisonTask task = taskMapper.findById(taskId);
    String name = task.getName();
    //删除任务（级联删除节点、关系、任务结果）
    taskMapper.deleteById(taskId);

    //删除结果数据
    ComparisonTask comparisonTask = taskMapper.findById(taskId);

    List<ComparisonResult> comparisonResults = resultMapper.findAll(taskId);

    JDBCParam jdbcParam = StorageConfig.getFileJDBCParam();
    for (ComparisonResult comparisonResult : comparisonResults){
      List<ResultTable> resultTableList = comparisonResult.getResultTables().getResultTableList();
      if (CollectionUtils.isNotEmpty(resultTableList)){
        for (ResultTable resultTable : resultTableList){
          String tableName = resultTable.getTableName();
          String sql = "drop table if exists " + tableName;
          JDBCExec.execute(jdbcParam, sql);
        }
      }

    }

    AuditUtil.pushDel(AuditPageName.COMPTASK, "name=" + name);

  }

  public TaskDTO parseTask(String taskId) {
    ComparisonTask comparisonTask = taskMapper.findById(taskId);
    if (comparisonTask == null){
      logger.error("comparisonTask is null");
    }
    List<ComparisonNode> nodes = nodeMapper.findAll(taskId);
    List<ComparisonsRelation> relations = relationMapper.findAll(taskId);

    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setId(taskId);
    taskDTO.setName(comparisonTask.getName());
    taskDTO.setNodes(nodes);
    taskDTO.setRelations(relations);
    taskSQLUtil.parseTaskDTO(taskDTO);
    return taskDTO;
  }

  /**
   * 根据taskId生成sql
   */
  public String generateSQL(String taskId) {
    TaskDTO taskDTO = parseTask(taskId);
    ComparisonsRelation rootRelation = taskSQLUtil.getRootRelations(taskDTO.getRelations()).get(0);

    String id = GenIdUtil.genResultId();
    String resultTableName = "\"" + StorageConfig.getCompResultSchema() + "\"." + id;
    String SQL = taskSQLUtil.getTaskSQL(taskDTO, rootRelation, sql);
    SQL = "CREATE TABLE " + resultTableName + " AS " + SQL;
    return SQL;
  }

  /**
   * 校验task
   */
  public void checkTask(TaskDTO taskDTO) {
    //验证
    List<ComparisonsRelation> comparisonsRelations = taskSQLUtil
        .getRootRelations(taskDTO.getRelations());
    if (comparisonsRelations.size() != 1) {
      throw new PreFailedException("com.bfd.comp.api.comparisonService.oneResult");
    }

    //验证节点
    List<String> tableNodeIds = new ArrayList<>();
    for (ComparisonNode node : taskDTO.getNodes()) {
      if (node.getType().equals(CompNodeType.TABLE)) {
        tableNodeIds.add(node.getId());
      }

    }

    for (ComparisonsRelation relation : taskDTO.getRelations()) {
      if (tableNodeIds.contains(relation.getLeftId())) {
        tableNodeIds.remove(relation.getLeftId());
      }

      if (tableNodeIds.contains(relation.getRightId())) {
        tableNodeIds.remove(relation.getRightId());
      }
    }

    if (tableNodeIds.size() > 0) {
      throw new PreFailedException("com.bfd.comp.api.comparisonService.oneResult");
    }
  }

  /**
   * api方法，webservice专用
   */
  public String exec(String userId, String taskId) {

    TaskDTO taskDTO = parseTask(taskId);

    //校验
    checkTask(taskDTO);

    String message = "";

    //请求rpc执行
    ThriftClient thriftClient = new ThriftClient();
    try {
      message = thriftClient.getClient().execSQL(taskId);
      logger.info("exec message :{}", message);
    } catch (TException e) {
      throw new PreFailedException("com.bfd.comp.api.comparisonService.rpcException");
    }
//    execTask(taskId);
    return message;
  }


  /**
   * rpc使用
   */
  public ExecData execTask(String taskId) {
    ExecData execData = new ExecData();
    //执行结果记录
    ExecStatus status = ExecStatus.RUNNING;
    String id = GenIdUtil.genResultId();
    ComparisonResult comparisonResult = new ComparisonResult(id, new Date(), null, taskId, null,
        status);
    resultMapper.insert(comparisonResult);

    TaskDTO taskDTO = parseTask(taskId);

    List<ComparisonsRelation> rootRelations = taskSQLUtil.getRootRelations(taskDTO.getRelations());

    ResultTables resultTables = new ResultTables();
    //resultTables.
    List<ResultTable> resultTableList = new ArrayList<>();
    for (ComparisonsRelation rootRelation : rootRelations){
      String resultTableName = "\"" + StorageConfig.getCompResultSchema() + "\"." + id;
//    String resultTableName = "\"" + StorageConfig.getSchema() + "\".file_t_d46158fc264449fddeeb4c3bad6ba066";

      //解析sql
      String SQL = taskSQLUtil.getTaskSQL(taskDTO, rootRelation, sql);
      SQL = "CREATE TABLE " + resultTableName + " AS " + SQL;

      //执行sql
      logger.info("**********start exec task sql*********");
      JDBCParam jdbcParam = StorageConfig.getFileJDBCParam();
      try {
        JDBCExec.execute(jdbcParam, SQL);
        status = ExecStatus.SUCCESS;
      }catch (Exception e){
        status = ExecStatus.FAILED;
        logger.error("SQL 执行异常", e);
      }

      logger.info("**********end exec task sql*********");

      List<TitleInfo> titles = new ArrayList<>();
      List<List<Object>> data = new ArrayList<>();

      //真数据
      for (SelectedField selectedField : rootRelation.getSelectedLeftFields().getSelectedFields()) {
        TitleInfo titleInfo = new TitleInfo(selectedField.getAlias(), selectedField.getType());
        titles.add(titleInfo);
      }
      for (SelectedField selectedField : rootRelation.getSelectedRightFields().getSelectedFields()) {
        TitleInfo titleInfo = new TitleInfo(selectedField.getAlias(), selectedField.getType());
        titles.add(titleInfo);
      }


      ResultTable resultTable = new ResultTable();
      resultTable.setTitles(titles);
      resultTable.setNodeName(rootRelation.getResultId());
      resultTable.setTableName(resultTableName);

      resultTableList.add(resultTable);
    }

    //执行成功更新执行状态
    comparisonResult.setFinishedTime(new Date());
    comparisonResult.setStatus(status);
    resultTables.setResultTableList(resultTableList);
    comparisonResult.setResultTables(resultTables);
    resultMapper.update(comparisonResult);

    execData.setStatus(status);
    execData.setTaskName(taskDTO.getName());
    execData.setEndTime(new Date());

    return execData;
  }

  /**
   * 创建离线执行
   */
  public ComparisonSchedule saveSchedule(String userId, String taskId, String warnEmail,
      ScheduleStatus scheduleStatus, Crontab crontab) {
    //请求rpc执行
    ThriftClient thriftClient = new ThriftClient();
    QuartzService.Client client = null;

    ComparisonSchedule scheduleBefor = scheduleMapper.findById(taskId);
    ComparisonSchedule scheduleNow = new ComparisonSchedule(taskId, new Date(), scheduleStatus,
        warnEmail, crontab);
    if (scheduleBefor == null) {
      scheduleMapper.insert(scheduleNow);
    } else {
      scheduleMapper.update(scheduleNow);
      }

      String message = "";
    try {
      client = thriftClient.getClient();
      if (scheduleBefor != null) {
        //下线情况
        if (scheduleNow.getStatus().equals(ScheduleStatus.OFFLINE)) {
          //之前不是上线状态，
          if (scheduleBefor.getStatus().equals(ScheduleStatus.ONLINE)) {
            //请求下线
            //TODO
            message = client.removeTask(taskId);
            logger.info("offline task: ", taskId);
          }
        } else if (scheduleBefor.getStatus().equals(ScheduleStatus.OFFLINE)) {
          //请求上线
          //TODO
          message = client.cronTrigger(taskId, JsonUtil.toJsonString(crontab), warnEmail);
          logger.info("online task: ", taskId);
        }else if (scheduleBefor.getStatus().equals(ScheduleStatus.ONLINE)){
          //TODO
          //先下线再上线
          message = client.removeTask(taskId);
          logger.info("offline task: ", taskId);

          message = client.cronTrigger(taskId, JsonUtil.toJsonString(crontab), warnEmail);
          logger.info("online task: ", taskId);
        }
      }else {
        if (scheduleNow.getStatus().equals(ScheduleStatus.ONLINE)){
          //请求上线
          message = client.cronTrigger(taskId, JsonUtil.toJsonString(crontab), warnEmail);
          logger.info("online task: ", taskId);
        }
      }

    } catch (TException e) {
      e.printStackTrace();
      throw new PreFailedException("com.bfd.comp.api.fileImportService.rpcException");
    }

    return scheduleNow;

  }

  /**
   * 查看调度信息
   * Query schedule info
   */
  public ComparisonSchedule querySchedule(String userId, String taskId) {
    return scheduleMapper.findById(taskId);
  }


  /**
   * 查看比对结果首页
   * Query comp result
   */
  public TaskResultsDTO queryResults(String userName, String taskId) {
    ComparisonTask comparisonTask = taskMapper.findById(taskId);

    List<ComparisonResult> comparisonResults = resultMapper.findAll(taskId);

    TaskResultsDTO taskResultsDTO = new TaskResultsDTO(comparisonTask.getId(),
        comparisonTask.getName(),
        comparisonTask.getCreateTime(), comparisonTask.getModifyTime(), userName,
        getResultInfo(comparisonResults));

    return taskResultsDTO;
  }

  /**
   * 预览数据
   * Preview result data
   */
  public TaskResultData previewResult(String userId, String resultId) {
    return getResultData(resultId, 1000);
  }


  public List<ResultInfo> getResultInfo(List<ComparisonResult> comparisonResults) {
    List<ResultInfo> resultInfos = new ArrayList<>();
    for (ComparisonResult comparisonResult : comparisonResults) {
      ResultInfo resultInfo = new ResultInfo(comparisonResult.getId(),
          comparisonResult.getExecTime(), comparisonResult.getFinishedTime(),
          comparisonResult.getStatus());

      resultInfos.add(resultInfo);
    }
    return resultInfos;
  }


  /**
   * 查看工作表和比对任务的关系
   */
  public TableCompRelationDTO queryTableCompInfo(String userId, String tableId) {
    List<ComparisonTask> tasks = taskMapper.findByTableId(tableId);
    TableCompRelationDTO dto = new TableCompRelationDTO(tasks.size(), tasks);
    return dto;
  }

  public void deleteResult (String userId, String resultId){
    ComparisonResult result = resultMapper.findById(resultId);
    if (result.getResultTables() != null){
      for (ResultTable resultTable : result.getResultTables().getResultTableList()){
        String tableName = resultTable.getTableName();
        String dropTable = "DROP table IF EXISTS " + tableName;
        logger.info("drop table, sql:{}", dropTable);
        JDBCExec.execute(StorageConfig.getFileJDBCParam(), dropTable);
      }

    }

    resultMapper.deleteById(resultId);
    AuditUtil.pushDel(AuditPageName.COMPRESULT, AuditUtil.getAuditData(result));
  }


  public TaskResultData getResultData(String resultId, int limit) {
    ComparisonResult comparisonResult = resultMapper.findById(resultId);
    if (comparisonResult.getStatus().equals(ExecStatus.RUNNING)){
      throw new PreFailedException("com.bfd.comp.api.comparisonService.taskRunning");
    }

    //真实数据
    List<TitleInfo> titles = comparisonResult.getResultTable().getTitles();
    String resultTableName = comparisonResult.getResultTable().getTableName();

    //查询预览数据
    JDBCParam jdbcParam = StorageConfig.getFileJDBCParam();
    String sql = "select * from " + resultTableName + " limit " + limit;

    List<List<Object>> data = JDBCExec.dsExec(jdbcParam, sql);

    //查看结果
    TaskResultData.TaskResultDataBuilder builder = TaskResultData.builder()
        .titles(titles)
        .data(data)
        .status(comparisonResult.getStatus())
        .startExecTime(comparisonResult.getExecTime())
        .endExecTime(comparisonResult.getFinishedTime())
        .resultId(resultId)
        .taskId(comparisonResult.getTaskId());

    return builder.build();

  }

}
