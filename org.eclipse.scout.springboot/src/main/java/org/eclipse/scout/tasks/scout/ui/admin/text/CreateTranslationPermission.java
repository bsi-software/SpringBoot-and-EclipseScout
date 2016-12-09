package org.eclipse.scout.tasks.scout.ui.admin.text;

import java.security.BasicPermission;

public class CreateTranslationPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public CreateTranslationPermission() {
    super(CreateTranslationPermission.class.getSimpleName());
  }
}
