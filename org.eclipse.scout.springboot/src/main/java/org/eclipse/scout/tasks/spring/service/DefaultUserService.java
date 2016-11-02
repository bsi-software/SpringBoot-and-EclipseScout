package org.eclipse.scout.tasks.spring.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.scout.ui.task.CreateTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.UpdateTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.ViewAllTasksPermission;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.eclipse.scout.tasks.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultUserService implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUser(String userName) {
    return userRepository.findByName(userName);
  }

  @Transactional
  @Override
  public Set<Role> getUserRoles(String userName) {
    User user = userRepository.findByName(userName);

    if (user != null) {
      user.getRoles().size();
      return user.getRoles();
    }

    return new HashSet<Role>();
  }

  @Override
  public void saveUser(User user) {
    userRepository.save(user);
  }

  /**
   * Add initial demo entities: roles and users.
   */
  @PostConstruct
  public void init() {
    log.debug("Check and initialise roles and users");

    initRoles();
    initUsers();
  }

  /**
   * Add roles: root, super user and user.
   */
  private void initRoles() {
    Role roleAdmin = roleRepository.findByName(RoleService.ROOT);
    if (roleAdmin == null) {
      roleAdmin = new Role(RoleService.ROOT);
      roleRepository.save(roleAdmin);
    }

    Role roleSuperUser = roleRepository.findByName(RoleService.SUPER_USER);
    if (roleSuperUser == null) {
      roleSuperUser = new Role(RoleService.SUPER_USER);
      roleSuperUser.setPermissions(new HashSet<String>());
      roleSuperUser.addPermission(ReadTaskPermission.class.getSimpleName());
      roleSuperUser.addPermission(CreateTaskPermission.class.getSimpleName());
      roleSuperUser.addPermission(UpdateTaskPermission.class.getSimpleName());
      roleSuperUser.addPermission(ViewAllTasksPermission.class.getSimpleName());
      roleRepository.save(roleSuperUser);
    }

    Role roleUser = roleRepository.findByName(RoleService.USER);
    if (roleUser == null) {
      roleUser = new Role(RoleService.USER);
      roleUser.setPermissions(new HashSet<String>());
      roleUser.addPermission(ReadTaskPermission.class.getSimpleName());
      roleUser.addPermission(CreateTaskPermission.class.getSimpleName());
      roleUser.addPermission(UpdateTaskPermission.class.getSimpleName());
      roleRepository.save(roleUser);
    }
  }

  /**
   * Add users: root, alice and bob.
   */
  private void initUsers() {
    if (userRepository.findByName("root") == null) {
      User root = new User("root", "Root", "cando");
      root.setRoles(new HashSet<Role>());
      root.getRoles().add(roleRepository.findByName(RoleService.ROOT));
      userRepository.save(root);
    }

    if (userRepository.findByName("alice") == null) {
      User alice = new User("alice", "Alice", "test");
      alice.setRoles(new HashSet<Role>());
      alice.getRoles().add(roleRepository.findByName(RoleService.SUPER_USER));
      userRepository.save(alice);
    }

    if (userRepository.findByName("bob") == null) {
      User bob = new User("bob", "Bob", "test");
      bob.setRoles(new HashSet<Role>());
      bob.getRoles().add(roleRepository.findByName(RoleService.USER));
      userRepository.save(bob);
    }
  }
}
