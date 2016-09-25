package org.eclipse.scout.springboot.demo.spring.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Role;
import org.eclipse.scout.springboot.demo.model.Task;
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
   * Add initial demo entities: Roles, users and a task.
   */
  @PostConstruct
  public void init() {
    Role roleUser = roleRepository.save(RoleService.USER_ROLE);
    Role roleAdmin = roleRepository.save(RoleService.ROOT_ROLE);

    User alice = new User("alice", "Alice", "", "test");
    alice.getRoles().add(roleUser);
    userRepository.save(alice);

    User bob = new User("bob", "Bob", "", "test");
    bob.getRoles().add(roleUser);
    userRepository.save(bob);

    User eclipse = new User("eclipse", "Eclipse", "", "scout");
    eclipse.getRoles().add(roleUser);
    eclipse.getRoles().add(roleAdmin);
    userRepository.save(eclipse);

    Task task = new Task("Finish task application", alice, new Date());
    task.setResponsible(eclipse);
    // TODO: fix jpa annotations for task creator (and responsible too most likely)
    // Caused by: org.h2.jdbc.JdbcSQLException: Value too long for column "CREATOR BINARY(255)": "X'aced00057372002c6f72672e65636c697073652e73636f75742e737072696e67626f6f742e64656d6f2e6d6f64656c2e557365720000000000000001020006... (1077)"; SQL statement:
    //   insert into task (name, accepted, creator, description, done, due_date, reminder, responsible, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?) [22001-192]
    // taskRepository.save(task);
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
