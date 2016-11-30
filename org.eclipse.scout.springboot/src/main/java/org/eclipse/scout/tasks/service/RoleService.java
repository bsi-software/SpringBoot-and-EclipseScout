package org.eclipse.scout.tasks.service;

import java.util.List;

import org.eclipse.scout.tasks.model.Role;

public interface RoleService extends ValidatorService {

  List<Role> getAll();

  boolean exists(String roleId);

  void save(Role role);

  Role get(String roleId);

}
