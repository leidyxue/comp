package com.baifendian.comp.api.service;

import com.baifendian.comp.api.dto.error.NotFoundException;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.dto.folder.FolderData;
import com.baifendian.comp.api.dto.folder.FolderTableData;
import com.baifendian.comp.api.dto.folder.FolderTree;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.FolderHelper;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.common.ds.DsConfUtil;
import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.structs.folder.Folder;
import com.baifendian.comp.common.utils.NameUtil;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.postgresql.mapper.DsDirRefMapper;
import com.baifendian.comp.dao.postgresql.mapper.FolderMapper;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import com.baifendian.comp.dao.postgresql.model.ds.DsDirRef;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FolderService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private FolderMapper folderMapper;

  @Autowired
  private TableMapperNew tableMapperNew;

  @Autowired
  private TableUtilNew tableUtil;

  @Autowired
  private DsDirRefMapper dsDirRefMapper;

  private List<Folder> createFolder(String user) {
    List<Folder> folder = folderMapper.selectByUser(user);

    if (folder == null) {
      return new ArrayList<>();
    }
    return folder;
  }

  private Folder createFolder(String user, String folderId) {
    if (folderId == null) {
      return Folder.getRoot(user);
    }
    Folder folder = folderMapper.selectById(folderId);

    if (folder == null) {
      throw new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.dirName());
    }
    if (!StringUtils.equals(user, folder.getOwner())) {
      throw new ParamException("com.baifendian.bi.api.common.notOwner", I18nUtil.tableName(),
          folderId);
    }
    return folder;
  }

  public FolderTree getAllFolders(String usrId) {
    List<Folder> folderList = folderMapper.selectByUser(usrId);
    List<Table> tableList = tableUtil.createSimpleTable(usrId);

    FolderTree.FolderTreeBuilder builder = FolderTree.builder();

    if (CollectionUtils.isNotEmpty(folderList)) {

      builder.folders(folderList.stream().map(folder -> FolderData.builder()
          .name(folder.getName())
          .modifyTime(folder.getModifyTime())
          .createTime(folder.getCreateTime())
          .id(folder.getId())
          .parentId(folder.getParentId())
          .build()).sorted(Comparator.comparing(FolderData::getModifyTime))
          .collect(Collectors.toList()));
    }
    if (CollectionUtils.isNotEmpty(tableList)) {
      builder.tables(tableList.stream()
          .filter(Table::nonDeleted)
          .map(table -> FolderTableData.builder()
              .id(table.getId())
              .originName(table.getOriginName())
              .desc(table.getDesc())
              .tableType(table.getType())
              .createTime(table.getCreateTime())
              .dsId(table.getDsId())
              .dsName(table.getDsName())
              .parentId(table.getParentId())
              .dsType(table.getDsType())
              .modifyTime(table.getModifyTime())
              .name(table.getName())
              .build())
          .sorted(Comparator.comparing(FolderTableData::getModifyTime))
          .collect(Collectors.toList()));
    }
    return builder.build();
  }

  @Transactional
  public String getShareFolderId(String userId) {
    return getPubFolderId(userId, "SHARE_INNER");
  }

  @Transactional
  public String getPubFolderId(String userId, String dsId) {
    List<DsDirRef> dsDirRefList = dsDirRefMapper.selectByUser(userId);
    if (CollectionUtils.isNotEmpty(dsDirRefList)) {
      Optional<DsDirRef> dsDirRefOptional = dsDirRefList.stream()
          .filter(dd -> StringUtils.equals(dsId, dd.getDsId()))
          .findFirst();
      if (dsDirRefOptional.isPresent()) {
        return dsDirRefOptional.get().getDirId();
      }
    }

    Folder existFolder = folderMapper.selectByName(userId, DsConfUtil.getDsName(dsId));
    if (existFolder != null) {
      // 如果存在同名目录，则将数据插入到同名目录下,不再新建目录
      dsDirRefMapper.insert(DsDirRef.builder()
          .dirId(existFolder.getId())
          .dsId(dsId)
          .owner(userId).build());
      return existFolder.getId();
    }

    String dirId = NameUtil.folderName();
    Folder folder = Folder.builder()
        .id(dirId)
        .name(DsConfUtil.getDsName(dsId))
        .parentId(null)
        .owner(userId)
        .build();

    folderMapper.insert(folder);

    dsDirRefMapper.insert(DsDirRef.builder()
        .dirId(dirId)
        .dsId(dsId)
        .owner(userId).build());
    return dirId;
  }

  public String getFolderId(String userId, String name) {
    Folder folder = folderMapper.selectByName(userId, name);
    if (folder != null) {
      return folder.getId();
    }

    try {
      return createFolder(userId, null, name).getId();
    } catch (Exception e) {
      return folderMapper.selectByName(userId, name).getId();
    }
  }

  public FolderData createFolder(String userId, String parentId, String name) {
    Folder folder = Folder.builder()
        .id(NameUtil.folderName())
        .name(name)
        .parentId(parentId)
        .owner(userId)
        .build();

    try {
      folderMapper.insert(folder);
    } catch (DuplicateKeyException e) {
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate",
          new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.dir"));
    } catch (DataIntegrityViolationException e) {
      throw new PreFailedException("com.baifendian.bi.api.dir.parent.error");
    }
    AuditUtil.pushInsert(AuditPageName.DIR, AuditUtil.getAuditData(folder));

    return FolderData.builder().id(folder.getId())
        .createTime(folder.getCreateTime())
        .modifyTime(folder.getModifyTime())
        .parentId(parentId)
        .name(name)
        .build();
  }

  public FolderData modifyFolder(String userId, String folderId, String newName) {
    Folder folder = createFolder(userId, folderId);
    String oldData = AuditUtil.getAuditData(folder);
    folder.setName(newName);
    try {
      folderMapper.updateById(folder);
    } catch (DuplicateKeyException e) {
      logger.error("Insert error.", e);
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate"
          , I18nUtil.dirName());
    }

    AuditUtil.pushUpdate(AuditPageName.DIR, oldData, AuditUtil.getAuditData(folder));
    return FolderData.builder()
        .id(folder.getId())
        .createTime(folder.getCreateTime())
        .modifyTime(folder.getModifyTime())
        .name(newName)
        .build();
  }

  public void moveFolder(String userId, String folderId, String newParentId) {
    // TODO same of project
    List<Folder> folderList = createFolder(userId);

    Optional<Folder> folderOpt = folderList.stream()
        .filter(p -> StringUtils.equals(p.getId(), folderId)).findFirst();
    if (!folderOpt.isPresent()) {
      throw new NotFoundException("com.baifendian.bi.api.common.notFound", I18nUtil.projectName(),
          folderId);
    }

    folderList.stream().filter(f -> StringUtils.equals(f.getParentId(), folderId))
        .findFirst().ifPresent(f ->{
      throw new PreFailedException("com.baifendian.bi.api.dir.notEmpty");
    });

    if (tableMapperNew.countByFolder(folderId) > 0) {
      throw new PreFailedException("com.baifendian.bi.api.dir.notEmpty");
    }

    // check parent
    if (newParentId != null) {
      Optional<Folder> parentFolder = folderList.stream()
          .filter(p -> StringUtils.equals(p.getId(), newParentId)).findFirst();
      if (!parentFolder.isPresent()) {
        throw new PreFailedException("com.baifendian.bi.api.dir.parent.error");
      }
    }

    Folder folder = folderOpt.get();
    String oldData = AuditUtil.getAuditData(folder);

    folder.setParentId(newParentId);
    FolderHelper
        .checkFolderCycle(folderList.stream().map(f -> Pair.of(f.getParentId(), f.getId())).collect(
            Collectors.toList()), Pair.of(folder.getParentId(), folder.getId()),
            "com.baifendian.bi.common.name.dir");

    folderMapper.updateById(folder);

    AuditUtil.pushUpdate(AuditPageName.DIR, oldData, AuditUtil.getAuditData(folder));
  }

  public void moveTable(String userId, String tableId, String newParentId) {
    createFolder(userId, newParentId);
    Table table = tableUtil.createSimpleTable(userId, tableId);
    table.setModifyTime(new Date());
    table.setParentId(newParentId);

    try {
      tableMapperNew.updateById(table);
    } catch (DataIntegrityViolationException e) {
      throw new PreFailedException("com.baifendian.bi.api.dir.parent.error");
    }
    // TODO
    //AuditUtil.pushUpdate(AuditPageName.DIR, oldData, AuditUtil.getAuditData(folder));
  }

  public void deleteFolder(String userId, String folderId) {
    List<Folder> folderList = createFolder(userId);
    Optional<Folder> folderOpt = folderList.stream()
        .filter(p -> StringUtils.equals(p.getId(), folderId)).findFirst();
    if (!folderOpt.isPresent()) {
      throw new NotFoundException("com.baifendian.bi.api.common.notFound", I18nUtil.projectName(),
          folderId);
    }

    folderList.stream().filter(f -> StringUtils.equals(f.getParentId(), folderId))
        .findFirst().ifPresent(f ->{
      throw new PreFailedException("com.baifendian.bi.api.dir.notEmpty");
    });

    if (tableMapperNew.countByFolder(folderId) > 0) {
      throw new PreFailedException("com.baifendian.bi.api.dir.notEmpty");
    }

    folderMapper.deleteById(folderId);
    AuditUtil.pushDel(AuditPageName.DIR, AuditUtil.getAuditData(folderOpt.get()));
  }

}
