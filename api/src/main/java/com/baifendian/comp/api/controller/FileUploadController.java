package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.fileImport.FieldTypeInfo;
import com.baifendian.comp.api.dto.fileImport.FileSplitData;
import com.baifendian.comp.api.dto.fileImport.FileUploadData;
import com.baifendian.comp.api.service.FileImportService;
import com.baifendian.comp.common.annotation.ParamProperty;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文件上传
 *
 * <p>
 *
 * @author : shuanghu
 */
@RestController
@RequestMapping("")
public class FileUploadController {
  @Autowired
  FileImportService fileImportService;

  @PostMapping(value = "/files")
  public FileUploadData fileUpload(MultipartHttpServletRequest request, @RequestAttribute("userId") String userId) throws IOException {

    MultipartFile file = request.getFile("file");
    return fileImportService.fileUpload(userId, file);
  }

  @DeleteMapping("/files/{fileId}")
  public void deleteFile(@RequestAttribute("userId") String userId, @PathVariable("fileId") String fileId){
    fileImportService.delete(userId, fileId);
  }

  @GetMapping("/files")
  public FileSplitData getSplitData(@RequestAttribute("userId") String userId,
      @RequestParam("delimiter") String delimiter,
      @RequestParam("fileName") String fileName,
      @RequestParam(value="decode",required = false) String decode){
    //统一登陆
    return fileImportService.splitData(delimiter, fileName, decode);
  }

  @PostMapping("/files/{fileId}/tables")
  public void fileImport(HttpServletRequest request, @RequestAttribute("userId") String userId, @PathVariable("fileId") String fileId,
      @RequestBody FileImportParam fileImportParam) throws Exception{
    fileImportService.fileImport(userId, fileId, fileImportParam.getSchema(),
        fileImportParam.getParentId(), fileImportParam.getName(), fileImportParam.getDelimiter(), fileImportParam.getDecode());
  }
}


@Setter
@Getter
class FileImportParam {
  @ParamProperty
  private String parentId;

  @ParamProperty
  private String name;

  @ParamProperty
  private String delimiter;

  @ParamProperty
  private List<FieldTypeInfo> schema;

  @ParamProperty
  private String decode = "UTF-8";
}