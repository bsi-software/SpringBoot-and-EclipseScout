package org.eclipse.scout.tasks.scout.ui.task;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.model.entity.Task;
import org.eclipse.scout.tasks.model.service.TaskService;

public class InboxTablePage extends AbstractTaskTablePage {

  @Inject
  private TaskService taskService;

  public InboxTablePage() {
    getTable().getResponsibleColumn().setDisplayable(false);
    getTable().getAcceptedColumn().setDisplayable(false);
    getTable().getDoneColumn().setDisplayable(false);
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("InboxTablePage");
  }

  @Override
  protected Collection<Task> getTasks() {
    return taskService.getInbox(getUserId());
  }
}
