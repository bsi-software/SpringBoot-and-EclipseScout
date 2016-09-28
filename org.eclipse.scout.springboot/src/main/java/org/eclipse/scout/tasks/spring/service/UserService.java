package org.eclipse.scout.tasks.spring.service;

import java.util.List;

import org.eclipse.scout.tasks.model.User;

public interface UserService {

  List<User> getUsers();

  User getUser(String userName);

  void addUser(User user);

  void saveUser(User user);
}
