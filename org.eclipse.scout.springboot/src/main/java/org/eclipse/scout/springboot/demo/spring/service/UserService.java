package org.eclipse.scout.springboot.demo.spring.service;

import java.util.List;

import org.eclipse.scout.springboot.demo.model.Role;
import org.eclipse.scout.springboot.demo.model.User;

/**
 * <h3>{@link UserService}</h3>
 *
 * @author patbaumgartner
 */
public interface UserService {

  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

  List<User> getUsers();

  User getUser(String userName);

  void addUser(User user);

  Role getRole(String roleName);
}
