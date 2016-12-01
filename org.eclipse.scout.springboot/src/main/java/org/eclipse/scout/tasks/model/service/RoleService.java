package org.eclipse.scout.tasks.model.service;

import java.util.List;

import org.eclipse.scout.tasks.model.entity.Role;

public interface RoleService extends ValidatorService<Role> {

  List<Role> getAll();

  boolean exists(String roleId);

  void save(Role role);

  Role get(String roleId);

}
