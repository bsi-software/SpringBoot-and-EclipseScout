package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.tasks.model.Role;

public interface RoleService {

  public static final String ROOT = "RootRole";
  public static final String SUPER_USER = "SuperUserRole";
  public static final String USER = "UserRole";

  List<Role> getRoles();

  Role getRole(String name);

  Set<String> getRolePermissions(String roleName);

  void saveRole(Role role);
}
