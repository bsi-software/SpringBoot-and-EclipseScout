package org.eclipse.scout.tasks.scout.ui.admin.text;

import java.security.BasicPermission;

public class UpdateTranslationPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public UpdateTranslationPermission() {
    super(UpdateTranslationPermission.class.getSimpleName());
  }
}
