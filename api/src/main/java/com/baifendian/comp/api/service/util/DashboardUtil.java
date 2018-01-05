package com.baifendian.comp.api.service.util;

import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.common.structs.dash.Project;
import com.baifendian.comp.dao.postgresql.mapper.DashShareMapper;
import com.baifendian.comp.dao.postgresql.mapper.ProjectMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashElementMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashFilterMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashLinkMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashNewMapper;
import com.baifendian.comp.dao.postgresql.model.DashAllData;
import com.baifendian.comp.dao.postgresql.model.dash.DashElement;
import com.baifendian.comp.dao.postgresql.model.dash.DashFilter;
import com.baifendian.comp.dao.postgresql.model.dash.DashLink;
import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
import com.baifendian.comp.dao.postgresql.model.share.ShareDash;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DashboardUtil {

  @Autowired
  private DashNewMapper dashMapper;
  @Autowired
  private ProjectMapper projectMapper;
  @Autowired
  private DashLinkMapper dashLinkMapper;

  @Autowired
  private DashFilterMapper dashFilterMapper;

  @Autowired
  private DashElementMapper dashElementMapper;
  @Autowired
  private DashShareMapper dashShareMapper;

  public ShareDash createShareDash(String shareId) {
    ShareDash shareDash = dashShareMapper.queryById(shareId);
    if (shareDash == null) {
      throw new PreFailedException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.shareName());
    }

    return shareDash;
  }

  public List<Project> createProject(String owner) {
    List<Project> projects = projectMapper.findByOwner(owner);
    if (projects == null){
      return new ArrayList<>();
    }

    return projects;
  }

  public Project createProject(String owner, String projectId) {
    Project project = projectMapper.findById(projectId);

    if (project == null) {
      throw new PreFailedException("com.baifendian.bi.api.common.notFound", I18nUtil.projectName(),
          projectId);
    }

    if (!StringUtils.equals(owner, project.getOwner())) {
      throw new PreFailedException("com.baifendian.bi.api.common.notOwner", I18nUtil.projectName(),
          projectId);
    }

    return project;
  }

  public void checkProject(String owner, String projectId) {
    if (StringUtils.isEmpty(projectId)) {
      return;
    }
    createProject(owner, projectId);
  }

  public void checkDashboard(String owner, String dashId) {
    createDashboard(owner, dashId);
  }

  public Dashboard createDashboard(String dashId) {
    Dashboard dashboard = dashMapper.findById(dashId);
    if (dashboard == null) {
      throw new PreFailedException("com.baifendian.bi.api.common.notFound", I18nUtil.dashName(),
          dashId);
    }
    return dashboard;
  }

  public Dashboard createDashboard(String owner, String dashId) {
    Dashboard dashboard = dashMapper.findById(dashId);
    if (dashboard == null) {
      throw new PreFailedException("com.baifendian.bi.api.common.notFound", I18nUtil.dashName(),
          dashId);
    }

    if (!StringUtils.equals(owner, dashboard.getOwner())) {
      throw new PreFailedException("com.baifendian.bi.api.common.notOwner", I18nUtil.dashName(),
          dashId);
    }

    return dashboard;
  }

  public DashAllData createDashAll(String owner, String dashId) {
    Dashboard dashboard = createDashboard(owner, dashId);

    List<DashLink> links = dashLinkMapper.queryByDashId(dashId);
    List<DashFilter> tmp = dashFilterMapper.queryByDashId(dashId);

    return DashAllData.builder()
        .dash(dashboard)
        .elements(createDashElements(dashId))
        .links(links == null ? new ArrayList<>() : links)
        .filters(tmp == null ? new ArrayList<>() : tmp)
        .build();
  }

  public DashAllData createDashAll(String dashId) {
    Dashboard dashboard = createDashboard(dashId);

    List<DashLink> links = dashLinkMapper.queryByDashId(dashId);
    List<DashFilter> tmp = dashFilterMapper.queryByDashId(dashId);

    return DashAllData.builder()
        .dash(dashboard)
        .elements(createDashElements(dashId))
        .links(links == null ? new ArrayList<>() : links)
        .filters(tmp == null ? new ArrayList<>() : tmp)
        .build();
  }

  public List<DashElement> createDashElements(String dashId) {
    return dashElementMapper.queryByDashId(dashId);
  }

  @Transactional
  public void insertDashElements(List<DashElement> dashElementList) {
    dashElementList.forEach(de -> dashElementMapper.insert(de));
  }
}
