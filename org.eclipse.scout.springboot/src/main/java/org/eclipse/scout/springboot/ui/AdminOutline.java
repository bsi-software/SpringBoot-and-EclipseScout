package org.eclipse.scout.springboot.ui;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.ui.user.RoleTablePage;
import org.eclipse.scout.springboot.ui.user.UserTablePage;

/**
 * <h3>{@link AdminOutline}</h3>
 *
 * @author mzi
 */
@Order(3000)
public class AdminOutline extends AbstractOutline {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Administration");
  }

  @Override
  protected String getConfiguredIconId() {
    // get unicode http://fontawesome.io/icon/users/
    return "font:awesomeIcons \uf0c0";
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    super.execCreateChildPages(pageList);
    pageList.add(new UserTablePage());
    pageList.add(new RoleTablePage());
  }
}
