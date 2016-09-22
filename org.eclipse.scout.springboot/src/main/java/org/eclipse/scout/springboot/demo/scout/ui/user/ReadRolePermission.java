package org.eclipse.scout.springboot.demo.scout.ui.user;

import java.security.BasicPermission;

public class ReadRolePermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public ReadRolePermission() {
    super(ReadRolePermission.class.getSimpleName());
  }

}
