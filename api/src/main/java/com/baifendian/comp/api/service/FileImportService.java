package com.baifendian.comp.api.service;

import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.api.dto.error.ParameterException;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.dto.fileImport.FieldTypeInfo;
import com.baifendian.comp.api.dto.fileImport.FileSplitData;
import com.baifendian.comp.api.dto.fileImport.FileUploadData;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.DatasourceUtil;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.common.config.BizConfig;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.utils.DataTypeUtil;
import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.common.utils.NameUtil;
import com.baifendian.comp.common.utils.file.ExcelFileUtil;
import com.baifendian.comp.common.utils.file.FileHelper;
import com.baifendian.comp.common.utils.file.FileReaderUtil;
import com.baifendian.comp.common.utils.json.JsonUtil;
import com.baifendian.comp.dao.config.StorageConfig;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.jdbc.BiSQLException;
import com.baifendian.comp.dao.jdbc.DsConnect;
import com.baifendian.comp.dao.jdbc.JDBCExec;
import com.baifendian.comp.dao.jdbc.JDBCPool;
import com.baifendian.comp.dao.postgresql.mapper.FieldNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xuelei on 2017/10/18 0018.
 */
@Service
public class FileImportService {

  @Autowired
  private TableMapperNew tableMapper;

  @Autowired
  private FieldNewMapper fieldMapper;

  @Autowired
  DatasourceUtil datasourceUtil;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static int FILE_OVERVIEW_NUM = 6;

  /**
   * 上传文件
   *
   * @param userId userId
   * @param file the binary data of file
   * @return BaseResponse
   */
  public FileUploadData fileUpload(String userId, MultipartFile file)
      throws IOException {
    if (file == null || file.isEmpty()) {
      logger.error("必选参数 userId, file 存在为空");
      throw new ParameterException("必选参数 userId, file 存在为空");
    }

    //检查大小
    if (file.getSize() > BizConfig.getFileMaxSize()) {
      logger.error("上传文件大小不能超过: {}", BizConfig.getFileMaxM());
      throw new ParameterException("com.bfd.comp.api.fileImportService.fileSize", BizConfig.getFileMaxM());
    }

    String originalFilename = file.getOriginalFilename();
    String type = originalFilename
        .substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length()).toLowerCase();

    if (!type.equals("csv") && !type.equals("xlsx")) {
      throw new ParameterException("com.bfd.comp.api.fileImportService.fileType");
    }

    //生成文件id
    String fileId = GenIdUtil.genFileId();

//    String fileName = fileId + "." + type;

    // 创建文件
    String fileName = FileHelper.createFileImportPath(fileId, userId)+"."+type;

    // 保存文件
    logger.info("Start upload file ...");
    FileHelper.saveFileAndStructSuccessData(fileName, file.getInputStream(),
        file.getSize());
    logger.info("Success upload file ...");

    FileUploadData fileUploadData = new FileUploadData();
    fileUploadData.setFileName(fileName.replace("/", "__"));
    return fileUploadData;
  }


  /**
   * 放弃一个缓存文件
   */
  public void delete(String userId, String fileName) {
    String filePath = FileHelper.createFileImportPath(fileName, userId);
    FileHelper.deleteDir(new File(filePath));
    logger.info("delete file: " + filePath);
  }

//  /**
//   * 按分隔符分割字段并自动识别字段类型
//   */
//  public FileSplitData splitData(String userId, String delimiter, String fileName, String decode) {
//    List<FieldType> typeList = new ArrayList<>();
//
//    //读数据
//    String filePath = FileHelper.createFileImportPath(fileName, userId);
////    String filePath = "F:\\file\\588008540267623043\\studentDatexlsx.xlsx";
//
//    //获取csv或者excel文件前lineNumber条按分隔符分割后的数据
//    List<List<String>> splitData = new ArrayList<>();
//    try {
//      splitData = FileReaderUtil.getSplitData(filePath, fileName, 6, delimiter, decode);
//    } catch (IOException e) {
//      e.printStackTrace();
//      throw new ParameterException("上传文件数据不合法");
//    }
//
//    logger.info("*************splitData***************");
//    logger.info(JsonUtil.toJsonString(splitData));
//
//    if (splitData == null || splitData.size() == 0) {
//      throw new ParameterException("上传文件数据不能为空");
//    }
//    List<FieldTypeInfo> schema = new ArrayList<>();
//
//    //类型识别
//    try {
//      //类型识别
//      int rowSize = splitData.size();  //行数
//      int columnSize = splitData.get(0).size(); //列数
//      for (int i = 0; i < columnSize; i++) {
//        int total = 0;
//        int numSize = 0;
//        int dateSize = 0;
//        for (int j = 1; j < rowSize; j++) {
//          if (splitData.get(j).size() != columnSize){
//            throw new ParameterException("上传文件数据无标准行");
//          }
//          if (splitData.size() < j) {
//            continue;
//          }
//          //判断某一列的类型情况
//          List<String> data = splitData.get(j);
//          if (data.size() > i) {
//            total++;
//            if (DataTypeUtil.isNumeric(data.get(i))) {
//              numSize++;
//            } else if (DataTypeUtil.isDate(data.get(i))) {
//              dateSize++;
//            }
//          }
//        }
//
//        FieldTypeInfo fieldTypeInfo = new FieldTypeInfo();
//        if (total > 0) {
//
//          if (numSize == total) {
//            //100%是数值就认为是数值型
//            fieldTypeInfo.setName(splitData.get(0).get(i));
//            fieldTypeInfo.setType(FieldType.NUM);
//          } else if (dateSize == total) {
//            //100%是日期就认为是数值型
//            fieldTypeInfo.setName(splitData.get(0).get(i));
//            fieldTypeInfo.setType(FieldType.DATE);
//          } else {
//            //其他情况就认为是文本型
//            fieldTypeInfo.setName(splitData.get(0).get(i));
//            fieldTypeInfo.setType(FieldType.TEXT);
//          }
//        } else {
//          //其他情况就认为是文本型
//          fieldTypeInfo.setName(splitData.get(0).get(i));
//          fieldTypeInfo.setType(FieldType.TEXT);
//        }
//
//        schema.add(fieldTypeInfo);
//
//      }
//    } catch (Exception e) {
//      throw new ParameterException("上传文件数据不合法");
//    }
//
//    splitData.remove(0);
//    return new FileSplitData(schema, splitData);
//
//  }

  /**
   * 按分隔符分割字段并自动识别字段类型
   */
  public FileSplitData splitData(String delimiter, String fileName, String decode) {
    fileName = fileName.replace("__", "/");
    File file = new File(fileName);
    if (!file.exists()){
      throw new ParameterException("com.bfd.bi.api.file.data.no.exist");
    }

    //文件类型
    String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
        .toLowerCase();
    if (type.equals("csv")) {
      return getCsv(delimiter, fileName, decode);
    }

    try {
      return excel(fileName);
    } catch (IOException e) {
      throw new ParameterException(e, "com.bfd.bi.api.file.data.invalid");
    }
  }

  /**
   * 导入数据
   */
  @Transactional(rollbackFor = Throwable.class, value = "PostgreSQLTransactionManager")
  public void fileImport(String userId, String fileName, List<FieldTypeInfo> schema,
      String parentId, String name, String delimiter, String decode)
      throws Exception {
    JDBCParam jdbcParam = StorageConfig.getFileJDBCParam();
    //String filePath = FileHelper.createFileImportPath(fileName, userId);
    String newFilePath = fileName.replace("__", "/");

    //文件类型
    String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
        .toLowerCase();
    TableType tableType = type.equals("csv") ? TableType.CSV : TableType.EXCEL;

    //获取csv或者excel文件第一条按分隔符分割后的数据
    List<String> titles = new ArrayList<>();
    try {
      if (type.equals("csv")) {
        List<String> data = FileReaderUtil.readFile(newFilePath, decode, 1);
        for (String line : data) {
          titles = Arrays.asList(line.split(delimiter));
        }
      } else if (type.equals("xlsx")) {
        for (List<String> objectList: ExcelFileUtil.getExcelSplitData(newFilePath, 1)){
          titles.addAll(objectList.stream().map(Object::toString).collect(Collectors.toList()));
        }
      }
    } catch (IOException e) {
      throw new PreFailedException("com.bfd.bi.api.file.data.invalid");
    }

    if (titles.size() != schema.size()) {
      throw new PreFailedException("com.bfd.bi.api.file.data.invalid");
    }

    String genId = GenIdUtil.genFileTableId();
    String originTableName = "\"" + StorageConfig.getSchema() + "\"." + genId;
    //创建表
    String ddl = getDDL(schema, originTableName);

    //生成工作表
    Table table = Table.builder()
        .id(NameUtil.tableName())
        .originName(StorageConfig.getSchema() + "." + genId)
        .name(name)
        .owner(userId)
        .type(tableType)
        .dsId(datasourceUtil.getInnerDsId())
        .parentId(parentId)
        .build();

    try {
      tableMapper.insert(table);
    } catch (DuplicateKeyException e) {
      logger.error("Insert error.", e);
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate", I18nUtil.tableName());
    }

    List<Field> fieldList = new ArrayList<>();

    //生成字段
    for (int i = 0; i < schema.size(); i++) {
      Field field = Field.builder()
          .owner(userId)
          .id(NameUtil.fieldName())
          .originName(schema.get(i).getFieldId())
          .name(schema.get(i).getName())
          .fileOriginName(titles.get(i))
          .tableId(table.getId())
          .genType(FieldGenType.NATIVE)
          .type(schema.get(i).getType())
          .originType("text")
          .sortId(i)
          .build();
      fieldList.add(field);
    }

    try {
      fieldMapper.batchInsert(fieldList);
    } catch (DuplicateKeyException e) {
      logger.error("Insert error.", e);
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate",
          I18nUtil.fieldName());
    }

    JDBCExec.execute(jdbcParam, ddl);

    String tmpPath = newFilePath;
    //导入文件
    if (type.equals("xlsx")) {
      //如果是excel文件需要转成csv文件
      //newFilePath = .replace(".xlsx", ".csv");
      tmpPath = newFilePath + ".csv";
      ExcelFileUtil.xlsx(new File(newFilePath), new File(tmpPath));
      delimiter = ",";
    }

    try {
      logger.info("Start copy file data to postGreSql");
      copyFromFile(tmpPath, originTableName, delimiter, decode);
      logger.info("End copy file data to postGreSql");
    } catch (BiSQLException e) {
      logger.info("Close error.", e);

      // Failed to import, drop tables.
      JDBCExec.dropTable(jdbcParam, originTableName);
      throw e;
    }

    AuditUtil.pushFileImport(AuditPageName.FILE, AuditUtil.getAuditData(table));

  }


//  /**
//   * 导入数据
//   */
//  @Transactional
//  public void fileImport(String userId, String fileName, List<FieldTypeInfo> schema,
//      String parentId, String name, String delimiter, String decode) {
//    JDBCParam jdbcParam = StorageConfig.getFileJDBCParam();
//    String filePath = FileHelper.createFileImportPath(fileName, userId);
//    String newFilePath = filePath;
//
//    //文件类型
//    String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
//        .toLowerCase();
//    TableType tableType = type.equals("csv") ? TableType.CSV : TableType.EXCEL;
//
//    //获取csv或者excel文件第一条按分隔符分割后的数据
//    List<String> titles = new ArrayList<>();
//    try {
//      titles = FileReaderUtil.getSplitData(filePath, fileName, 1, delimiter, decode).get(0);
//      logger.info("titles is :{}", JsonUtil.toJsonString(titles));
//    } catch (IOException e) {
//      throw new PreFailedException("com.bfd.comp.api.fileImportService.dataInvalid");
//    }
//
//    if (titles.size() != schema.size()) {
//      throw new PreFailedException("com.bfd.comp.api.fileImportService.dataInvalid");
//    }
//
//    String genId = GenIdUtil.genFileTableId();
//    String originTableName = "\"" + StorageConfig.getSchema() + "\"." + genId;
//    //创建表
//    String ddl = getDDL(schema, originTableName);
//
//    //生成工作表
//    Table table = Table.builder()
//        .id(NameUtil.tableName())
//        .originName(StorageConfig.getSchema() + "." + genId)
//        .name(name)
//        .owner(userId)
//        .type(tableType)
//        .dsId(datasourceUtil.getInnerDsId())
//        .parentId(parentId)
//        .build();
//    tableMapper.insert(table);
//
//    //生成字段
//    for (int i = 0; i < schema.size(); i++) {
//      Field field = Field.builder()
//          .owner(userId)
//          .id(NameUtil.fieldName())
//          .originName(schema.get(i).getFieldId())
//          .name(schema.get(i).getName())
//          .fileOriginName(titles.get(i))
//          .tableId(table.getId())
//          .genType(FieldGenType.NATIVE)
//          .type(schema.get(i).getType())
//          .originType("text")
//          .sortId(i)
//          .build();
//
//      fieldMapper.insert(field);
//    }
//
//    try {
//      JDBCExec.execute(jdbcParam, ddl);
//    }catch (Exception e){
//      logger.error("SQL 执行异常", e);
//      throw new PreFailedException("数据导入失败，请检查数据是否合法");
//    }
//
//    //导入文件
//    if (type.equals("xlsx")) {
//      //如果是excel文件需要转成csv文件
//      newFilePath = filePath.replace(".xlsx", ".csv");
//      ExcelFileUtil.xlsx(new File(filePath), new File(newFilePath));
//      delimiter = ",";
//    }
//
//    Connection connection = null;
//    try {
//      logger.info("Start copy file data to postGreSql");
//      connection = JDBCPool.getInstance().borrowConn(jdbcParam);
//      copyFromFile(connection, newFilePath, originTableName, delimiter, decode);
//      logger.info("End copy file data to postGreSql");
//    } catch (Exception e) {
//      logger.error("Import error.", e);
//    } finally {
//      JDBCPool.getInstance().returnConn(jdbcParam, connection);
//    }
//
//    AuditUtil.pushFileImport(AuditPageName.FILE, AuditUtil.getAuditData(table));
//
//  }

  public String getDDL(List<FieldTypeInfo> schema, String tableName) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("CREATE TABLE " + tableName + " (\n");
    boolean start = true;
    for (FieldTypeInfo fieldTypeInfo : schema) {
      fieldTypeInfo.setFieldId(GenIdUtil.genFileFieldId());
      if (start) {
        buffer.append("\"" + fieldTypeInfo.getFieldId() + "\" text");
        start = false;
        continue;
      }
      buffer.append(",\n\"" + fieldTypeInfo.getFieldId() + "\" text");
    }
    buffer.append("\n)");
    return buffer.toString();
  }

  private static FileSplitData excel(String fileName) throws IOException {
    List<List<String>> splitData = ExcelFileUtil.getExcelSplitData(fileName, FILE_OVERVIEW_NUM);
    if (splitData.isEmpty()){
      throw new ParameterException("com.bfd.bi.api.file.data.empty");
    }

    List<FieldTypeInfo> schema = new ArrayList<>();

    //类型识别
    int rowSize = splitData.size();  //行数
    int columnSize = splitData.get(0).size(); //列数
    for (int i = 0; i < columnSize; i++) {
      FieldType type = null;
      // 如果文本出现不同类型的情况，则默认为text
      for (int j = 1; j < rowSize; j++) {
//        if (splitData.get(j).size() != columnSize){
//          throw new ParameterException("上传文件数据无标准行");
//        }
        if (splitData.size() < j) {
          continue;
        }
        //判断某一列的类型情况
        List<String> data = splitData.get(j);
        if (data.size() > i) {
          if (DataTypeUtil.isNumeric(data.get(i))) {
            if (type == null){
              type = FieldType.NUM;
            }else if (type != FieldType.NUM){
              type = FieldType.TEXT;
            }
          } else if (DataTypeUtil.isDate(data.get(i))) {
            if (type == null){
              type = FieldType.DATE;
            }else if (type != FieldType.DATE){
              type = FieldType.TEXT;
            }
          }
        }
      }
      if (type == null){
        type = FieldType.TEXT;
      }

      FieldTypeInfo fieldTypeInfo = new FieldTypeInfo();
      fieldTypeInfo.setName(splitData.get(0).get(i).toString());
      fieldTypeInfo.setType(type);
      schema.add(fieldTypeInfo);
    }

    splitData.remove(0);
    return FileSplitData.builder()
        .splitData(splitData)
        .schema(schema)
        .build();
  }

  private FileSplitData getCsv(String delimiter, String fileName, String decode) {
    List<List<String>> splitData = new ArrayList<>();

    List<String> dataList = FileReaderUtil.readFile(fileName, decode, FILE_OVERVIEW_NUM);

    for (String data: dataList ){
      splitData.add(Arrays.asList(data.split(delimiter)));
    }

    if (splitData.isEmpty()){
      throw new ParameterException("com.bfd.bi.api.file.data.empty");
    }
    logger.info("splitData is :");
    logger.info(JsonUtil.toJsonString(splitData));
    List<FieldTypeInfo> schema = new ArrayList<>();

    //类型识别
    int rowSize = splitData.size();  //行数
    int columnSize = splitData.get(0).size(); //列数
    for (int i = 0; i < columnSize; i++) {
      FieldType type = null;
      // 如果文本出现不同类型的情况，则默认为text
      for (int j = 1; j < rowSize; j++) {
//        if (splitData.get(j).size() != columnSize){
//          throw new ParameterException("上传文件数据无标准行");
//        }
        if (splitData.size() < j) {
          continue;
        }
        //判断某一列的类型情况
        List<String> data = splitData.get(j);
        if (data.size() > i) {
          if (DataTypeUtil.isNumeric(data.get(i))) {
            if (type == null){
              type = FieldType.NUM;
            }else if (type != FieldType.NUM){
              type = FieldType.TEXT;
            }
          } else if (DataTypeUtil.isDate(data.get(i))) {
            if (type == null){
              type = FieldType.DATE;
            }else if (type != FieldType.DATE){
              type = FieldType.TEXT;
            }
          }
        }
      }
      if (type == null){
        type = FieldType.TEXT;
      }

      FieldTypeInfo fieldTypeInfo = new FieldTypeInfo();
      fieldTypeInfo.setName(splitData.get(0).get(i).toString());
      fieldTypeInfo.setType(type);
      schema.add(fieldTypeInfo);
    }

    splitData.remove(0);
    return FileSplitData.builder()
        .splitData(splitData)
        .schema(schema)
        .build();
  }

  public void copyFromFile(String filePath, String tableName, String delimiter, String decode) {

    FileInputStream fileInputStream = null;

    try(DsConnect connect = new DsConnect(StorageConfig.getFileJDBCParam())) {
      CopyManager copyManager = new CopyManager((BaseConnection) connect.getConnection());
      fileInputStream = new FileInputStream(filePath);

      String sql = "COPY " + tableName + " FROM STDIN with DELIMITER '" + delimiter
          + "' CSV HEADER encoding '" + (decode.equals("GBK") ? "GBK" : "UTF-8") + "'"
          + "\n escape '\\' \n quote '\"'";
      logger.info("Tmp file name:{} , sql: {}", filePath, sql);
      copyManager.copyIn(sql, fileInputStream);
    } catch (SQLException e) {
      if (e.getMessage().contains("invalid byte sequence")) {
        throw new PreFailedException(e, "com.bfd.bi.api.file.invalid.sequence");
      }
      if (e.getMessage().contains("character with byte sequence") && e.getMessage()
          .contains("has no equivalent in encoding")) {
        throw new PreFailedException(e, "com.bfd.bi.api.file.error.code.sequence");
      }

      throw new BiSQLException(e, "com.bfd.bi.api.file.data.invalid");
    } catch (FileNotFoundException e) {
      throw new BiSQLException(e, "com.bfd.bi.api.file.data.no.exist");
    } catch (IOException e) {
      throw new BiSQLException(e, "com.bfd.bi.api.file.server.error");
    } catch (Exception e) {
      // close error, ignore.
      logger.info("Close error.", e);
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          logger.info("Close error.", e);
        }
      }
    }
  }

//  public void copyFromFile(Connection connection, String filePath, String tableName,
//      String delimiter, String decode)
//      throws SQLException, IOException {
//
//    FileInputStream fileInputStream = null;
//
//    try {
//      CopyManager copyManager = new CopyManager((BaseConnection) connection);
//      fileInputStream = new FileInputStream(filePath);
//      if (delimiter.equals("\\|")) {
//        delimiter = "|";
//      }
//      String sql = "COPY " + tableName + " FROM STDIN with DELIMITER '" + delimiter
//          + "' CSV HEADER encoding '" + (decode.equals("GBK") ? "GBK" : "UTF-8") + "'"
//          + "\n escape '\\' \n quote '\"'";
//      logger.info("Tmp file name:{} , sql: {}", filePath, sql);
//      copyManager.copyIn(sql, fileInputStream);
////      if (decode.equals("GBK")){
////        copyManager.copyIn("COPY " + tableName + " FROM STDIN with DELIMITER '" + delimiter + "' CSV HEADER encoding 'GBK'", fileInputStream);
////      }else {
////        copyManager.copyIn("COPY " + tableName + " FROM STDIN with DELIMITER '" + delimiter + "' CSV HEADER", fileInputStream);
////      }
//    } finally {
//      if (fileInputStream != null) {
//        try {
//          fileInputStream.close();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//  }

}
