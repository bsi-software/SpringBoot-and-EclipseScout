package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.scout.tasks.data.Role;

public interface RoleService {

  static final String ROOT_ID = "root";
  static final Role ROOT_ROLE = new Role(ROOT_ID);

  void addRole(Role role);

  void saveRole(Role role);

  Role getRole(UUID id);

  List<Role> getRoles();

  Map<UUID, String> getAllRoleNames();

}
