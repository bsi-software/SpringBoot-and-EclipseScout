package org.eclipse.scout.tasks.spring.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.service.TaskService;
import org.eclipse.scout.tasks.spring.WebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebMvcConfiguration.API_CONTEXT_PATH + "/tasks")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @RequestMapping(path = {"", "/", "/info"})
  public Map<String, String> info() {
    final Map<String, String> info = new HashMap<>();
    info.put("api", "Task API");
    info.put("version", "Spring " + SpringVersion.getVersion());
    return info;
  }

  @RequestMapping("/{id}")
  @PreAuthorize("hasAuthority('org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission')")
  public Task showTaskById(@PathVariable String id) {
    return taskService.get(UUID.fromString(id));
  }

  @RequestMapping("/inbox")
  @PreAuthorize("hasAuthority('org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission')")
  public Collection<Task> inbox(Principal principal) {
    return taskService.getInbox(principal.getName());
  }

  @RequestMapping("/today")
  @PreAuthorize("hasAuthority('org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission')")
  public Collection<Task> today(Principal principal) {
    return taskService.getToday(principal.getName());
  }

  @RequestMapping("/own")
  @PreAuthorize("hasAuthority('org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission')")
  public Collection<Task> own(Principal principal) {
    return taskService.getOwn(principal.getName());
  }

  @RequestMapping("/all")
  @PreAuthorize("hasAuthority('org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission') "
      + "and hasAuthority('org.eclipse.scout.tasks.scout.ui.task.ViewAllTasksPermission')")
  public Collection<Task> all() {
    return taskService.getAll();
  }

}
