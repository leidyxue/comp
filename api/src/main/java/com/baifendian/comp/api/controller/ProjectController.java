package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.dashboard.DashProjectTreeDTO;
import com.baifendian.comp.api.dto.dashboard.PostProjectReqDTO;
import com.baifendian.comp.api.dto.dashboard.PutDashMoveReqDTO;
import com.baifendian.comp.api.dto.dashboard.PutProjectMoveReqDTO;
import com.baifendian.comp.api.dto.dashboard.PutProjectReqDTO;
import com.baifendian.comp.api.service.ProjectService;
import com.baifendian.comp.common.structs.dash.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @GetMapping("/projects")
  public DashProjectTreeDTO getProject(@RequestAttribute("userId") String userId) {

    return projectService.getProjectDashTree(userId);
  }

  @PostMapping("/projects")
  public Project postProject(
      @RequestAttribute(value = "userId") String userId,
      @RequestBody PostProjectReqDTO postProjectReqDTO) {

    String owner = userId;

    Project project = new Project();
    project.setName(postProjectReqDTO.getName());
    project.setDesc(postProjectReqDTO.getDesc());
    project.setOwner(owner);
    project.setParentId(postProjectReqDTO.getParentId());

    return projectService.createProject(project);
  }

  @DeleteMapping("/projects/{projectId}")
  public void deleteProject(
      @RequestAttribute(value = "userId") String userId,
      @PathVariable("projectId") String projectId) {

    projectService.deleteProject(userId, projectId);
  }

  @PutMapping("/projects/{projectId}")
  public Project putProject(@RequestAttribute(value = "userId") String userId,
      @PathVariable("projectId") String projectId,
      @RequestBody PutProjectReqDTO putProjectReqDTO) {

    return projectService.updateProject(userId, projectId, putProjectReqDTO.getName());
  }

  @PutMapping("/projects-move/{projectId}")
  public void putProjectsMove(@RequestAttribute(value = "userId") String userId,
      @PathVariable("projectId") String projectId,
      @RequestBody(required = false) PutProjectMoveReqDTO putProjectMoveReqDTO) {


    projectService.moveProject(userId, projectId,
        putProjectMoveReqDTO != null ? putProjectMoveReqDTO.getParentId() : null);
  }

  @PutMapping("/projects/dashboards-move/{dashId}")
  public void putProjectsDashMove(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId,
      @RequestBody(required = false) PutDashMoveReqDTO putDashMoveReqDTO) {


    projectService.moveDash(userId, dashId,
        putDashMoveReqDTO != null ? putDashMoveReqDTO.getProjectId() : null);
  }
}
