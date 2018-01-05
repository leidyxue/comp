package com.baifendian.comp.api.service;

import static java.util.stream.Collectors.*;

import com.baifendian.comp.api.dto.dashboard.DashInfo;
import com.baifendian.comp.api.dto.dashboard.DashProjectTreeDTO;
import com.baifendian.comp.api.dto.error.NotFoundException;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.DashboardUtil;
import com.baifendian.comp.api.service.util.FolderHelper;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.common.structs.dash.Project;
import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.postgresql.mapper.ProjectMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashNewMapper;
import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

  @Autowired
  private ProjectMapper projectMapper;
  @Autowired
  private DashNewMapper dashMapper;
  @Autowired
  private DashboardUtil dashboardUtil;

  /**
   * 获取控制面板和项目树
   */
  public DashProjectTreeDTO getProjectDashTree(String userId) {
    List<Project> projectList = dashboardUtil.createProject(userId);
    List<Dashboard> dashboardList = dashMapper.findByOwner(userId);

    DashProjectTreeDTO dashProjectTreeDTO = new DashProjectTreeDTO();
    if (CollectionUtils.isNotEmpty(projectList)) {
      dashProjectTreeDTO.setProjects(projectList.stream()
          .sorted(Comparator.comparing(Project::getCreateTime))
          .collect(toList()));
    }
    if (CollectionUtils.isNotEmpty(dashboardList)) {
      dashProjectTreeDTO.setDashboads(
          dashboardList.stream().map(DashInfo::new)
              .sorted(Comparator.comparing(DashInfo::getCreateTime))
              .collect(toList()));
    }

    return dashProjectTreeDTO;
  }

  /**
   * 创建一个project
   */
  public Project createProject(Project project) {

    String parentId = project.getParentId();
    String owner = project.getOwner();

    //检测根节点是否存在
    if (StringUtils.isNotEmpty(parentId)) {
      dashboardUtil.checkProject(owner, parentId);
    }

    project.setId(GenIdUtil.genProjectId());
    project.setCreateTime(new Date());
    project.setModifyTime(new Date());
    try {
      projectMapper.insert(project);
    } catch (DuplicateKeyException e) {
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate",
          I18nUtil.projectName());
    }
    AuditUtil.pushInsert(AuditPageName.DIR, AuditUtil.getAuditData(project));
    return project;
  }

  /**
   * 删除一个项目
   */
  public void deleteProject(String userId, String projectId) {

    Project project = dashboardUtil.createProject(userId, projectId);

    //检查project 子类是否存在
    List<Project> projectList = projectMapper.findChildById(projectId);
    List<Dashboard> dashboardList = dashMapper.findByProjectId(projectId);

    if (!CollectionUtils.isEmpty(projectList) || !CollectionUtils.isEmpty(dashboardList)) {
      throw new PreFailedException("com.baifendian.bi.api.project.delete.notEmpty");
    }

    projectMapper.deleteById(projectId);
    AuditUtil.pushDel(AuditPageName.DIR, AuditUtil.getAuditData(project));
  }

  /**
   * 更新一个项目名称
   */
  public Project updateProject(String userId, String projectId, String projectName) {
    Project project = dashboardUtil.createProject(userId, projectId);

    String oldData = AuditUtil.getAuditData(project);

    project.setModifyTime(new Date());
    project.setName(projectName);

    try {
      projectMapper.update(project);
    } catch (DuplicateKeyException e) {
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate",
          I18nUtil.projectName());
    }

    AuditUtil.pushUpdate(AuditPageName.DIR, oldData, AuditUtil.getAuditData(project));

    return project;
  }

  /**
   * 移动一个项目
   */
  public void moveProject(String userId, String projectId, String parentId) {
    List<Project> projectList = dashboardUtil.createProject(userId);

    Optional<Project> project = projectList.stream()
        .filter(p -> StringUtils.equals(p.getId(), projectId)).findFirst();
    if (!project.isPresent()) {
      throw new NotFoundException("com.baifendian.bi.api.common.notFound", I18nUtil.projectName(),
          projectId);
    }

    Optional<Project> parent = projectList.stream()
        .filter(p -> StringUtils.equals(p.getId(), projectId)).findFirst();
    if (!parent.isPresent()) {
      throw new NotFoundException("com.baifendian.bi.api.common.notFound", I18nUtil.projectName(),
          parentId);
    }

    Project srcProject = project.get();
    String oldData = AuditUtil.getAuditData(srcProject);

    srcProject.setParentId(parentId);
    srcProject.setModifyTime(new Date());

    FolderHelper.checkFolderCycle(
        projectList.stream().map(p -> Pair.of(p.getParentId(), p.getId())).collect(toList()),
        Pair.of(srcProject.getParentId(), srcProject.getId()),
        "com.baifendian.bi.common.name.project");

    projectMapper.update(srcProject);
    AuditUtil.pushUpdate(AuditPageName.DIR, oldData, AuditUtil.getAuditData(srcProject));
  }

  /**
   *
   * @param userId
   * @param dashId
   */
  public void moveDash(String userId, String dashId, String projectId) {

    Dashboard dashboard = dashboardUtil.createDashboard(userId, dashId);

    dashboardUtil.checkProject(userId, projectId);

    dashboard.setProjectId(projectId);
    dashboard.setModifyTime(new Date());

    dashMapper.update(dashboard);
  }

}
