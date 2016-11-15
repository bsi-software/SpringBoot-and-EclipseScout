package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.tasks.data.User;

public interface UserService {

  List<User> getUsers();

  User getUser(UUID user);

  User getUser(String userName);

  void addUser(User user);

  void saveUser(User user);

  byte[] getUserPicture(UUID user);

  void setUserPicture(UUID user, byte[] picture);
}
