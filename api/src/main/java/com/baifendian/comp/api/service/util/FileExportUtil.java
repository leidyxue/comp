package com.baifendian.comp.api.service.util;

import com.baifendian.comp.common.exception.BiException;
import com.google.common.base.Joiner;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Component
public class FileExportUtil {

  public String createCsv(List<String> titles, List<List<Object>> data) {
    StringBuilder resultData = new StringBuilder();
    if (CollectionUtils.isEmpty(titles)) {
      return "";
    }
    Joiner joiner = Joiner.on(",").useForNull("");
    resultData.append(joiner.join(titles));
    resultData.append("\n");
    for (List<Object> result : data) {
      if (CollectionUtils.isEmpty(result)) {
        continue;
      }
      resultData.append(joiner.join(result));
      resultData.append("\n");
    }

    return resultData.toString();
  }

  public byte[] createExcel(List<String> titles, List<List<Object>> data) {
    try (XSSFWorkbook wb = new XSSFWorkbook()) {
      //第二步创建sheet
      XSSFSheet sheet = wb.createSheet("result-data");
      //第三步创建行row:添加表头0行
      Row row0 = sheet.createRow(0);
      for (int i = 0; i < titles.size(); ++i) {
        row0.createCell(i).setCellValue(titles.get(i));
      }

      for (int i = 0; i < data.size(); ++i) {
        Row row = sheet.createRow(i + 1);
        List<Object> result = data.get(i);
        for (int j = 0; j < result.size(); ++j) {
          Object obj = result.get(j);
          if (obj == null) {
            row.createCell(j).setCellValue("");
          } else {
            row.createCell(j).setCellValue(obj.toString());
          }
        }
      }

      try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
        wb.write(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
      } catch (IOException e) {
        throw new BiException(e, "com.bfd.dw.api.service.adhoc.AdHocService.common.downloadError");
      }
    } catch (IOException e) {
      throw new BiException(e, "com.bfd.dw.api.service.adhoc.AdHocService.common.downloadError");
    }
  }
}
