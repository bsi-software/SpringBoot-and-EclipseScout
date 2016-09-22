package org.eclipse.scout.springboot.demo.spring.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Role;

public interface RoleService {

  public static final String ROOT_ID = "root";
  public static final Role ROOT_ROLE = new Role(ROOT_ID);
  public static final String USER_ID = "user";
  public static final Role USER_ROLE = new Role(USER_ID);

  void addRole(Role role);

  void saveRole(Role role);

  Role getRole(UUID id);

  List<Role> getRoles();

}
