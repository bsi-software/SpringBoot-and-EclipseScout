package org.eclipse.scout.springboot.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.entity.Task;
import org.eclipse.scout.springboot.entity.ToDoListModel;

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
    Collection<Task> tasks = BEANS.get(ToDoListModel.class).getAllTasks();

    return tasks;
  }
}
