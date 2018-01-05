package com.baifendian.comp.common.utils.file;

import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.utils.DataTypeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by Administrator on 2017/10/25 0025.
 */
public class ExcelFileUtil {

  public static List<List<String>> getExcelSplitData(String filePath, int lineNum)
      throws IOException {
    File file = new File(filePath);

    List<List<String>> splitData = new ArrayList<>();

    XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(file));

    // Get first sheet from the workbook
    XSSFSheet sheet = wBook.getSheetAt(0);
    Row row;
    // Iterate through each rows from first sheet
    Iterator<Row> rowIterator = sheet.iterator();
    int n = 0;
    int fieldNum = getTitleNum(sheet.getRow(0));

    while (rowIterator.hasNext() && n < lineNum) {
      n++;
      row = rowIterator.next();
      List<String> lineSplitData = new ArrayList<>();

      for (int i=0; i<fieldNum; ++i) {
        lineSplitData.add(getCellValue(row.getCell(i)).toString());
      }
      splitData.add(lineSplitData);
    }

    wBook.close();

    return splitData;
  }

  public static int getTitleNum(Row titleRow){
    if (titleRow == null) {
      throw new ParamException("com.bfd.bi.api.file.data.empty");
    }
    int num = titleRow.getFirstCellNum();
    if (num != 0) {
      throw new ParamException("com.bfd.bi.api.file.data.emptyTitle");
    }

    return titleRow.getLastCellNum();
  }

  public static Object getCellValue(Cell cell){
    if (cell == null){
      return "";
    }
    switch (cell.getCellTypeEnum()) {
      case BOOLEAN:
        return cell.getBooleanCellValue();
      case NUMERIC:
        Short code = cell.getCellStyle().getDataFormat();
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
          SimpleDateFormat sdf = null;
          if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
              .getBuiltinFormat("h:mm")) {
            sdf = new SimpleDateFormat("HH:mm");
          } else {// 日期
            sdf = new SimpleDateFormat("yyyy-MM-dd");
          }
          Date date = cell.getDateCellValue();
          return sdf.format(date);
        } else if (code == 14 || code == 31 || code == 58 || code == 57) {
          // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          double value = cell.getNumericCellValue();
          Date date = org.apache.poi.ss.usermodel.DateUtil
              .getJavaDate(value);
          return sdf.format(date);
        } else {
          double value = cell.getNumericCellValue();
          CellStyle style = cell.getCellStyle();
          DecimalFormat format = new DecimalFormat();
//          String temp = style.getDataFormatString();
////          // 单元格设置成常规
//          if (!temp.equals("General")) {
//            format.applyPattern("#");
//          }
          String v = format.format(value);
          v = v.replace(",","");
          return v;
        }
      case STRING: {
        String val = cell.getStringCellValue();
        if (StringUtils.isNotEmpty(val)){
          val = val.replace("\"", "\\\"");
        }
        return val;
      }

      case BLANK:
        return "";
      default: {
        String val = cell.toString();
        if (StringUtils.isNotEmpty(val)) {
          val = val.replace("\"", "\\\"");
        }
        return val;
      }

    }
  }

  public static void xlsx(File inputFile, File outputFile) {
    // For storing data into CSV files
    StringBuffer data = new StringBuffer();
    try {
      FileOutputStream fos = new FileOutputStream(outputFile);

      // Get the workbook object for XLSX file
      XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(inputFile));

      // Get first sheet from the workbook
      XSSFSheet sheet = wBook.getSheetAt(0);
      Row row;
      int fieldNum = getTitleNum(sheet.getRow(0));

      // Iterate through each rows from first sheet
      Iterator<Row> rowIterator = sheet.iterator();
      while (rowIterator.hasNext()) {
        row = rowIterator.next();
        if (data.length() > 1) {
          data.append("\r\n");
        }

        // For each row, iterate through each columns
        for (int i=0; i<fieldNum; ++i){
          if (i != 0){
            data.append(",");
          }
          data.append("\"").append(getCellValue(row.getCell(i))).append("\"");
        }
        //每行数据做最后处理
//        data.deleteCharAt(data.length() - 1);
      }

      fos.write(data.toString().getBytes());
      fos.close();

      wBook.close();

    } catch (Exception ioe) {
      ioe.printStackTrace();
    }
  }



  public static void main(String[] args) throws IOException{
//    getExcelSplitData("C:\\Users\\Administrator\\Desktop\\文件上传\\“遇见•爱”信息登记表(1).xlsx", 6);

    List<FieldType> test = new ArrayList<>();
    test.add(FieldType.TEXT);
    test.add(FieldType.NUM);
    test.add(FieldType.NUM);
    test.add(FieldType.DATE);
    xlsx(new File("C:\\Users\\Administrator\\Desktop\\文件上传\\时间下钻测试.xlsx"), new File("C:\\Users\\Administrator\\Desktop\\文件上传\\sCsv.csv"));
    System.out.println("OK");
  }
}
