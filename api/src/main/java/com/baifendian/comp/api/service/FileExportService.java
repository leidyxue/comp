package com.baifendian.comp.api.service;

import com.baifendian.comp.common.config.BizConfig;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by xuelei on 2017/11/18 0018.
 */
@Service
public class FileExportService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public void exportFile(String url, String type, String fileName, HttpServletResponse response) {
    String path = BizConfig.getExportPath();
//    String path = "E:\\tmp\\";
    String cmd = "";
    String nodeParam = url;
    String localFile = path;
    if (type.equals("PNG")) {
      localFile += "png.png";
      cmd = "node " + path + "screenshot-fullpage.js " + nodeParam + " " + localFile;
      fileName += ".png";
    } else if (type.equals("PDF")) {
      localFile += "pdf.pdf";
      cmd = "node " + path + "pdf.js " + nodeParam + " " + localFile;
      fileName += ".pdf";
    }

    logger.info(cmd);

    //执行node命令生成文件
    Process process = null;

    try {
      process = Runtime.getRuntime().exec(cmd);
      process.waitFor();
    } catch (Exception e) {
      logger.error("生成文件失败");
      e.printStackTrace();
    }
    logger.info("success create file");

    //下载文件
    File file = new File(localFile);
    if (file.exists()) {
      response.setContentType("application/force-download");// 设置强制下载不打开
      response.addHeader("Content-Disposition",
          "attachment;fileName=" + fileName);// 设置文件名
      byte[] buffer = new byte[1024];
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      try {
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        OutputStream os = response.getOutputStream();
        int i = bis.read(buffer);
        while (i != -1) {
          os.write(buffer, 0, i);
          i = bis.read(buffer);
        }
        System.out.println("success");
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (bis != null) {
          try {
            bis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (fis != null) {
          try {
            fis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

    }else {
      logger.error("生成文件不存在");
    }
  }
}
