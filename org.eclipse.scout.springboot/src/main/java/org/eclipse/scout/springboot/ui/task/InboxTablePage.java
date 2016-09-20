package org.eclipse.scout.springboot.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.entity.Task;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.ui.ClientSession;

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
    return BEANS.get(ToDoListModel.class).getInbox(ClientSession.get().getUser());
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }
}
