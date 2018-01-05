package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.comparison.TaskResultData;
import com.baifendian.comp.api.service.ComparisonService;
import com.baifendian.comp.api.service.FileExportService;
import com.baifendian.comp.api.service.util.FileExportUtil;
import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.dao.postgresql.model.comparison.TitleInfo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuelei on 2017/11/20 0020.
 */
@RestController
public class FileExportController {

  @Autowired
  FileExportService fileExportService;

  @Autowired
  private FileExportUtil fileExportUtil;

  @Autowired
  private ComparisonService comparisonService;

  @GetMapping("/download")
  public void downloadFile(@RequestParam(value = "url") String url,
      @RequestParam(value = "type", required = false) String type,
      @RequestParam(value = "fileName", required = false) String fileName,
      HttpServletRequest request, HttpServletResponse response){
    fileExportService.exportFile(url, type, fileName, response);
  }

  @ResponseBody
  @GetMapping("/download/result/{resultId}")
  public ResponseEntity<Resource> downloadDataChart(@RequestAttribute("userId") String userId,
      @PathVariable("resultId") String resultId,
      @RequestParam(value = "fileType", required = false) String fileType){
    TaskResultData taskResultData = comparisonService.getResultData(resultId, 20000);
    List<String> titles = new ArrayList<>();
    for (TitleInfo titleInfo : taskResultData.getTitles()){
      titles.add(titleInfo.getTitle());
    }
    switch (fileType) {
      case "CSV": {
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+ taskResultData.getTaskId() + "_" + taskResultData.getResultId() + ".csv")
            .body(new ByteArrayResource(
                fileExportUtil.createCsv(titles, taskResultData.getData()).getBytes()));
      }

      case "EXCEL": {
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+ taskResultData.getTaskId() + "_" + taskResultData.getResultId() + ".xlsx")
            .body(new ByteArrayResource(
                fileExportUtil.createExcel(titles, taskResultData.getData())));
      }
    }
    throw new BiException("暂未支持的文件类型");
  }

}
