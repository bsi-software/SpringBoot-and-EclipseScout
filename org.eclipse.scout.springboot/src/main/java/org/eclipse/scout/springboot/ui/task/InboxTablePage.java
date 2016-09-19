package org.eclipse.scout.springboot.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.entity.Task;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;

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
    User user = BEANS.get(ToDoListModel.class).loggedInUser();
    Collection<Task> tasks = BEANS.get(ToDoListModel.class).getInbox(user);

    return tasks;
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }
}
