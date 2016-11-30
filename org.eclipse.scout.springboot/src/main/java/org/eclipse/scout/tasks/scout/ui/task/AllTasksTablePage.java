package org.eclipse.scout.tasks.scout.ui.task;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.service.TaskService;

public class AllTasksTablePage extends AbstractTaskTablePage {

  @Inject
  private TaskService taskService;

  public AllTasksTablePage() {
    getTable().getReminderColumn().setVisible(false);
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("AllTasks");
  }

  @Override
  protected void execInitPage() {
    setVisiblePermission(new ViewAllTasksPermission());
  }

  @Override
  protected Collection<Task> getTasks() {
    return taskService.getAllTasks();
  }

  @Override
  protected void execPageActivated() {
    // NOOP
  }

}
