package org.eclipse.scout.springboot.demo.scout.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.scout.ui.ClientSession;
import org.eclipse.scout.springboot.demo.spring.service.TaskService;

public class InboxTablePage extends AbstractTaskTablePage {

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
    return BEANS.get(TaskService.class).getInbox(ClientSession.get().getUser());
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }
}
