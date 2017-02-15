package org.eclipse.scout.tasks.spring.controller;

import java.security.BasicPermission;

public class ReadApiPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public ReadApiPermission() {
    super(ReadApiPermission.class.getSimpleName());
  }
}
