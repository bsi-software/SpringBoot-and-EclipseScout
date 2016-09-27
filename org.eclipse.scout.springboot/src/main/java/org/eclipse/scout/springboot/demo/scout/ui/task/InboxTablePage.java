package org.eclipse.scout.springboot.demo.scout.ui.task;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.spring.service.TaskService;

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
    return taskService.getInbox(getUser());
  }
}
