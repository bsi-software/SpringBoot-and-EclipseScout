package org.eclipse.scout.tasks.scout.ui.admin.role;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.tasks.scout.ui.admin.role.PermissionTablePage.PermissionTable;

@Bean
public class PermissionTablePage extends AbstractPageWithTable<PermissionTable> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("PermissionTablePage");
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  public class PermissionTable extends AbstractPermissionTable {

    @Override
    protected void execInitTable() {
      getAssignedColumn().setDisplayable(false);
    }

    @Override
    protected void execReloadPage() {
      reloadPage();
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    getTable().loadData(filter);
  }
}
