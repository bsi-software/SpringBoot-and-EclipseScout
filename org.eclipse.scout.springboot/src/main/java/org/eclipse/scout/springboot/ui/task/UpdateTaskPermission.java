package org.eclipse.scout.springboot.ui.task;

import java.security.BasicPermission;

public class UpdateTaskPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public UpdateTaskPermission() {
    super(UpdateTaskPermission.class.getSimpleName());
  }
}
