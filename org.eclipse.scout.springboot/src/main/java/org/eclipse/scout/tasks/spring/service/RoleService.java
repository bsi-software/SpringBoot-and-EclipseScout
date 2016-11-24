package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.tasks.data.Role;

public interface RoleService extends ValidatorService {

  List<Role> getAll();

  boolean exists(String roleId);

  void save(Role role);

  Role get(String roleId);

  Set<String> getPermissions(String roleId);

}
