package org.eclipse.scout.tasks.scout.ui.admin.db;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.scout.ui.FontAwesomeIcons;

@Bean
public class DatabaseAdministrationConsolePage extends AbstractPageWithNodes {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("DatabaseAdministrationConsole");
  }

  @Override
  protected void execInitPage() {
    setVisiblePermission(new ReadDatabaseAdministrationConsolePermission());
  }

  @Override
  protected Class<? extends IForm> getConfiguredDetailForm() {
    return DatabaseAdministrationConsoleForm.class;
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  @Order(10)
  public class AssignMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return "Show JDBC URL";
    }

    @Override
    protected String getConfiguredIconId() {
      return FontAwesomeIcons.fa_info;
    }

    @Override
    protected void execAction() {
      if (getDetailForm() instanceof DatabaseAdministrationConsoleForm) {
        DatabaseAdministrationConsoleForm form = (DatabaseAdministrationConsoleForm) getDetailForm();
        form.execDisplayJdbcUrl();
      }
    }

  }

}
