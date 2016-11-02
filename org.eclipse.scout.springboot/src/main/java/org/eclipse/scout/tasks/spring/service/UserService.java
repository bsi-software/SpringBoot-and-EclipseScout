package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.User;

public interface UserService {

  List<User> getUsers();

  User getUser(String userName);

  Set<Role> getUserRoles(String userName);

  void saveUser(User user);
}
