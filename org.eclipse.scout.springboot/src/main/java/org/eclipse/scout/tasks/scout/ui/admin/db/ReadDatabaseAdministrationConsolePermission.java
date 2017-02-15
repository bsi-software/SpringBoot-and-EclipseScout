package org.eclipse.scout.tasks.scout.ui.admin.db;

import java.security.BasicPermission;

public class ReadDatabaseAdministrationConsolePermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public ReadDatabaseAdministrationConsolePermission() {
    super(ReadDatabaseAdministrationConsolePermission.class.getSimpleName());
  }
}
