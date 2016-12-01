package org.eclipse.scout.tasks.spring.controller;

import java.util.Collection;
import java.util.UUID;

import org.eclipse.scout.tasks.WebMvcConfig;
import org.eclipse.scout.tasks.model.entity.Task;
import org.eclipse.scout.tasks.model.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebMvcConfig.API_SERVICES_PATH + "/tasks")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @RequestMapping(path = {"", "/"})
  public Collection<Task> showTasks() {
    return taskService.getAll();
  }

  @RequestMapping("/{id}")
  public Task showTaskById(@PathVariable String id) {
    return taskService.get(UUID.fromString(id));
  }

}
