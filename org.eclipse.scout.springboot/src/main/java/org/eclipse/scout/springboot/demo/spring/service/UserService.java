package org.eclipse.scout.springboot.demo.spring.service;

import java.util.List;

import org.eclipse.scout.springboot.demo.model.User;

public interface UserService {

  List<User> getUsers();

  User getUser(String userName);

  void addUser(User user);

  void saveUser(User user);
}
