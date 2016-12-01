package org.eclipse.scout.tasks.scout.ui.admin.role;

import java.security.BasicPermission;

public class UpdateRolePermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public UpdateRolePermission() {
    super(UpdateRolePermission.class.getSimpleName());
  }
}
