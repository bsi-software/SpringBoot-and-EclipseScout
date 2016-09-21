package org.eclipse.scout.springboot.demo.scout.ui.task;

import java.util.Collection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.scout.ui.ClientSession;
import org.eclipse.scout.springboot.demo.scout.ui.task.AbstractTaskTablePage.Table.AcceptMenu;
import org.eclipse.scout.springboot.demo.spring.service.TaskService;

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
    Collection<Task> tasks = BEANS.get(TaskService.class).getTodaysTasks(user);
    return tasks;
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }
}
