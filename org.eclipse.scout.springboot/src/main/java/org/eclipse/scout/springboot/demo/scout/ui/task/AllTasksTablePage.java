package org.eclipse.scout.springboot.demo.scout.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.spring.service.TaskService;

public class AllTasksTablePage extends AbstractTaskTablePage {

  public AllTasksTablePage() {
    getTable().getReminderColumn().setVisible(false);
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("AllTasks");
  }

  @Override
  protected Collection<Task> getTasks() {
    return BEANS.get(TaskService.class).getAllTasks();

  }
}
