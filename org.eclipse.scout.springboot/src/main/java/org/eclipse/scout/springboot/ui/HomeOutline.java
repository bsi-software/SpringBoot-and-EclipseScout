package org.eclipse.scout.springboot.ui;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.ui.task.AllTasksTablePage;
import org.eclipse.scout.springboot.ui.task.InboxTablePage;
import org.eclipse.scout.springboot.ui.task.MyTaskTablePage;
import org.eclipse.scout.springboot.ui.task.TodaysTaskTablePage;

public class HomeOutline extends AbstractOutline {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Tasks");
  }

  @Override
  protected String getConfiguredIconId() {
    // get unicode http://fontawesome.io/icon/calendar-check-o/
    return "font:awesomeIcons \uf274";
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    super.execCreateChildPages(pageList);
    pageList.add(new InboxTablePage());
    pageList.add(new TodaysTaskTablePage());
    pageList.add(new MyTaskTablePage());
    pageList.add(new AllTasksTablePage());
  }
}
