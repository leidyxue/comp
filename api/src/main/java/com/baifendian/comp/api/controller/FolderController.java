package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.folder.FolderData;
import com.baifendian.comp.api.dto.folder.FolderTree;
import com.baifendian.comp.api.service.FolderService;
import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.annotation.ParamRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FolderController {

  @Autowired
  private FolderService folderService;

  @GetMapping("/folders")
  public FolderTree getFolders(@RequestAttribute("userId")String userId) {
    return folderService.getAllFolders(userId);
  }

  @PostMapping("/folders")
  public FolderData createFolder(@RequestAttribute("userId") String userId,
      @RequestBody CreateFolderObj createFolderParam) {
    return folderService
        .createFolder(userId, createFolderParam.getParentId(), createFolderParam.getName());
  }

  @PutMapping("/folders/{folderId}")
  public FolderData modifyFolder(@RequestAttribute(value = "userId") String userId, @PathVariable("folderId") String folderId,
      @RequestBody NameParam nameParam) {
    return folderService.modifyFolder(userId, folderId, nameParam.getName());
  }

  @PutMapping("/folders-move/{folderId}")
  public void moveFolder(@RequestAttribute(value = "userId") String userId, @PathVariable("folderId") String folderId,
      @RequestBody ParentIdParam parentIdParam) {
    folderService.moveFolder(userId, folderId, parentIdParam.getParentId());
  }

  @DeleteMapping("/folders/{folderId}")
  public void deleteFolder(@RequestAttribute("userId")String userId, @PathVariable("folderId") String dirId) {
    folderService.deleteFolder(userId, dirId);
  }

  @PutMapping("/tables-move/{tableId}")
  public void moveTable(@RequestAttribute("userId")String userId, @PathVariable("tableId") String tableId, @RequestBody ParentIdParam parentIdParam) {
    folderService.moveTable(userId, tableId, parentIdParam.getParentId());
  }
}

@Setter
@Getter
@ParamRequest
class CreateFolderObj {

  private String parentId;

  @ParamProperty
  private String name;
}

@Setter
@Getter
@ParamRequest
class ParentIdParam{

  private String parentId;
}

@Setter
@Getter
@ParamRequest
class NameParam{

  @ParamProperty
  private String name;
}