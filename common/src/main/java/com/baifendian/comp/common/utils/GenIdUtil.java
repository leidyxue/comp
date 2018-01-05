package com.baifendian.comp.common.utils;


import java.util.UUID;

public class GenIdUtil {

  /**
   * 生成一个图表的id
   */
  public static String genChartId() {
    return "chart_" + genGeneralId();
  }

   /**
   * 生成一个控制面板的id
   */
  public static String genDashId() {
    return "dash_" + genGeneralId();
  }

  public static String genShareId() {
    return "share_" + genGeneralId();
  }
  public static String genShareChartId() {
    return "share_c_" + genGeneralId();
  }
  /**
   * 生成一个projectid
   */
  public static String genProjectId() {
    return "project_" + genGeneralId();
  }


  /**
   * 生成通用id后缀
   */
  public static String genGeneralId() {
    return MD5Util.getMD5(UUID.randomUUID().toString());
  }

  public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
      "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
      "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
      "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
      "W", "X", "Y", "Z" };


  public static String generateShortUuid() {
    StringBuffer shortBuffer = new StringBuffer();
    String uuid = UUID.randomUUID().toString().replace("-", "");
    for (int i = 0; i < 8; i++) {
      String str = uuid.substring(i * 4, i * 4 + 4);
      int x = Integer.parseInt(str, 16);
      shortBuffer.append(chars[x % 0x3E]);
    }
    return shortBuffer.toString();

  }

  public static void main(String[] args) {
    System.out.println(generateShortUuid());
    System.out.println(genGeneralId());
  }

  /**
   * sql模板 result id
   */
  public static String genSQLResultId() {
    return "sql_r_" + genGeneralId();
  }

  /**
   * 获取sql模板 id 字段
   * @return
   */
  public static String genSQLFieldId(){
    return "sql_f_" + genGeneralId();
  }

  /**
   * 获取上传file的 id
   * @return
   */
  public static String genFileId(){
    return "file_" + genGeneralId();
  }

  /**
   * 获取上传file的字段 id
   * @return
   */
  public static String genFileFieldId(){
    return "file_f_" + genGeneralId();
  }

  /**
   * 获取比对任务结果表名
   * @return
   */
  public static String genResultTable(){
    return "comp_t_" + generateShortUuid();
  }

  /**
   * 获取上传file的字段 id
   * @return
   */
  public static String genFileTableId(){
    return "file_t_" + genGeneralId();
  }

  /**
   * 获取比对任务 id
   * @return
   */
  public static String genTaskId(){
    return "task_" + generateShortUuid();
  }

  /**
   * 获取比对节点 id
   * @return
   */
  public static String genNodeId(){
    return "node_" + genGeneralId();
  }

  /**
   * 获取比对结果 id
   * @return
   */
  public static String genResultId(){
    return "result_" + generateShortUuid();
  }
}
