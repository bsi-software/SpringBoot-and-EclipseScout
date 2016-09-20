package org.eclipse.scout.springboot.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.entity.Task;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.eclipse.scout.springboot.ui.ClientSession;
import org.eclipse.scout.springboot.ui.task.AbstractTaskTablePage.Table.AcceptMenu;

public class TodaysTaskTablePage extends AbstractTaskTablePage {

  public TodaysTaskTablePage() {
    getTable().getResponsibleColumn().setDisplayable(false);
    getTable().getAcceptedColumn().setDisplayable(false);
    getTable().getDoneColumn().setDisplayable(false);
    getTable().getMenuByClass(AcceptMenu.class).setVisible(false);
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TodaysTasks");
  }

  @Override
  protected Collection<Task> getTasks() {
    User user = ClientSession.get().getUser();
    return BEANS.get(ToDoListModel.class).getTodaysTasks(user);
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }
}
