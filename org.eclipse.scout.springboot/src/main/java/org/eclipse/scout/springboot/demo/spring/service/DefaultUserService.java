package org.eclipse.scout.springboot.demo.spring.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Role;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.spring.repository.RoleRepository;
import org.eclipse.scout.springboot.demo.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  /**
   * Add initial demo entities: Roles and users.
   */
  @PostConstruct
  public void init() {
    Role roleAdmin = roleRepository.save(RoleService.ROOT_ROLE);
    Role roleUser = roleRepository.save(new Role("user"));

    User alice = new User("alice", "Alice", "test");
    alice.getRoles().add(roleUser);
    userRepository.save(alice);

    User bob = new User("bob", "Bob", "test");
    bob.getRoles().add(roleUser);
    userRepository.save(bob);

    User eclipse = new User("eclipse", "Eclipse", "scout");
    eclipse.getRoles().add(roleUser);
    eclipse.getRoles().add(roleAdmin);
    userRepository.save(eclipse);
  }

  @Override
  public void addUser(User user) {
    save(user, true);
  }

  @Override
  public void saveUser(User user) {
    save(user, false);
  }

  @Override
  public User getUser(String userName) {
    return userRepository.findByName(userName);
  }

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  private void save(User user, boolean addUser) {
    if (user == null) {
      throw new VetoException(TEXTS.get("UserToSaveIsNull"));
    }

    User existingUser = userRepository.findByName(user.getName());
    if (addUser && existingUser != null) {
      throw new VetoException(TEXTS.get("UserToSaveExists"), user.getName());
    }
    else if (existingUser == null && !addUser) {
      throw new VetoException(TEXTS.get("UserToSaveIsMissing"), user.getName());
    }

    userRepository.save(user);
  }

}
