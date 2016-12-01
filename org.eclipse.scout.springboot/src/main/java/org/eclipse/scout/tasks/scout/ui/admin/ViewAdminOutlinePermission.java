package org.eclipse.scout.tasks.scout.ui.admin;

import java.security.BasicPermission;

public class ViewAdminOutlinePermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public ViewAdminOutlinePermission() {
    super(ViewAdminOutlinePermission.class.getSimpleName());
  }
}
