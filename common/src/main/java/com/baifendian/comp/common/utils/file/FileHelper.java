package com.baifendian.comp.common.utils.file;

import com.baifendian.comp.common.config.BizConfig;
import com.baifendian.comp.common.exception.LocalFileException;
import com.baifendian.comp.common.utils.DateUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/10/18 0018.
 */
public class FileHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

  private static DateFormat pathFormatter = new SimpleDateFormat("yyyy-MM-dd");

  public static void tmpPathClean(String rootPath) {
    File directory = new File(rootPath);

    if (directory.isDirectory()) {
      File[] fileArr = directory.listFiles();
      if (fileArr != null) {
        for (File file : fileArr) {
          fileClean(file);
        }
      }
    }
  }

  public static void fileClean(File filePath){

    if (filePath.isDirectory()) {
      File[] fileArr = filePath.listFiles();
      if (fileArr != null) {
        for (File file : fileArr) {
          //文件最后修改的日期
          Date fileDate = new Date(file.lastModified());

          // 两天之前的缓存文件，可以删除
          if (DateUtils.dayDiff(fileDate, new Date()) > 1) {
            LOGGER.warn("Remove file: {}.", file.getName());
            deleteDir(file);
          }
        }
      }
    }
  }

  public static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      if (children != null) {
        //递归删除目录中的子目录下
        for (int i = 0; i < children.length; i++) {
          boolean success = deleteDir(new File(dir, children[i]));
          if (!success) {
            return false;
          }
        }
      }
    }
    // 目录此时为空，可以删除
    return dir.delete();
  }

  public static void mkdir(String path) {
    File file = new File(path);
    if (!file.exists() && !file.mkdirs()) {
      // 目录不存在，且创建失败
      // TODO
    }
  }

  public static String createFileName(String importType, String orgName) {
    String path = BizConfig.getTmpPath() + "/" + DateUtils.getTodayDate() + "/" + importType + "/";

    File file = new File(path);
    if (!file.exists() && !file.mkdirs()) {
//      throw new LocalFileException("com.bfd.dw.api.localFile.mkdirError");
    }

    return path + System.currentTimeMillis() +
        (int) (Math.random() * 100000) + orgName;
  }

  /**
   * 根据fileId和用户id创建路径和返回路径
   * @param fileName
   * @param userId
   * @return
   */
  public static String createFileImportPath(String fileName, String userId) {

    String filePath = String.format("%s/%s"
        , BizConfig.getTmpPath(), userId);

    File uploadDir = new File(filePath);
    if (!uploadDir.exists()) {
      if (!uploadDir.mkdirs()) {
        LOGGER.error("Mkdirs failed, path name: {}", filePath);
      }
    }

    return String.format("%s/%s", filePath, fileName);
  }

  public static String createFileImportName() {
    String name = String.format("FI_FileImport_s_%s_%s/"
        , System.currentTimeMillis(),
        (int) (Math.random() * 100000));
    if (name.length() > 64) {
      return String.format("FI_s_%s_%s/"
          , System.currentTimeMillis(),
          (int) (Math.random() * 100000));
    }

    return name;
  }

  /**
   * 保存文件
   */
  public static void saveFileAndStructSuccessData(String filePath, InputStream input,
      long fileSize) {
    //写数据
    File file = new File(filePath);
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      int copySize = IOUtils.copy(input, fileOutputStream);
      if (copySize != fileSize) {
        throw LocalFileException.buildSaveException();
      }
      fileOutputStream.flush();
    } catch (FileNotFoundException e) {
      LOGGER.error("File not found, file name: {}", filePath, e);
      throw LocalFileException.buildFileNotFoundException();
    } catch (IOException e) {
      LOGGER.error("File IOException, file name: {}", filePath, e);
      throw LocalFileException.buildIOException();
    } finally {
      try {
        input.close();
      } catch (IOException e) {
        LOGGER.error("File IOException, file name: {}", filePath, e);
         throw LocalFileException.buildIOException();
      }
    }

    //返回数据
//    return structSuccessData(filePath, BizConfig.getFileImportCoding(), 3);
  }

  public static void main(String[] args) {
    File file = new File("F:\\file\\test\\2017-10-19\\FI_FileImport_s_1508377439553_47243\\tac.csv");
    Long time =file.lastModified();
    Long now = System.currentTimeMillis();
//    System.out.println(time);
    FileHelper.tmpPathClean("F:\\file\\userFile");
  }


}
