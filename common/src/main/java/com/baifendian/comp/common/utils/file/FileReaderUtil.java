package com.baifendian.comp.common.utils.file;

import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.utils.DataTypeUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * Created by Administrator on 2017/10/18 0018.
 */
public class FileReaderUtil {
  private final static Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);
  public static int fileSize(String fileName) {
    int size = 0;

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(fileName)))) {
      while (br.readLine() != null) {
        ++size;
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return size;
  }

  /**
   * 避免数据失真对读取的数据暂时不作任何处理.直接返回
   * 因为是本地文件读取所以目前暂不考虑重试的情况
   *
   * @param lineNumber -1为全部读取
   */
  public static List<String> readFile(String filePath, String encode, int lineNumber) {
    int size = fileSize(filePath);
    if (size == 0){
      throw new ParamException("com.bfd.bi.api.file.data.empty");
    }
    int[] random = DataTypeUtil.randomNumber(lineNumber, size);

    List<String> contents = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(filePath), encode))) {
      String line;
      int index = 0;
      for (int i=0; i<random.length; ++i) {
        while ((line = br.readLine()) != null) {
          if (i ==0){
            contents.add(line);
            index++;
            break;
          }
          if (index++ == random[i]) {
            contents.add(line);
            break;
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return contents;
  }

  public static void main(String[] args) {
    readFile("C:\\Users\\Administrator\\Desktop\\文件上传\\task_4Jxn1mLE_result_1t2duWP0.csv", "utf-8", 6);
  }

}
