package org.eclipse.scout.springboot.demo.scout.ui;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.scout.ui.task.AllTasksTablePage;
import org.eclipse.scout.springboot.demo.scout.ui.task.InboxTablePage;
import org.eclipse.scout.springboot.demo.scout.ui.task.MyTaskTablePage;
import org.eclipse.scout.springboot.demo.scout.ui.task.TodaysTaskTablePage;
import org.springframework.context.ApplicationContext;

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

    final ApplicationContext applicationContext = ApplicationContexts.current();
    pageList.add(applicationContext.getBean(InboxTablePage.class));
    pageList.add(applicationContext.getBean(TodaysTaskTablePage.class));
    pageList.add(applicationContext.getBean(MyTaskTablePage.class));
    pageList.add(applicationContext.getBean(AllTasksTablePage.class));
  }
}
