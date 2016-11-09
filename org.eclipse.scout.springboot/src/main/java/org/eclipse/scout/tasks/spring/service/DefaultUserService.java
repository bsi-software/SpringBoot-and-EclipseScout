package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.model.RoleEntity;
import org.eclipse.scout.tasks.model.UserEntity;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.eclipse.scout.tasks.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultUserService implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  /**
   * Add initial demo entities: roles and users.
   */
  @PostConstruct
  public void init() {
    log.debug("Check and initialise roles and users");
    RoleEntity roleAdmin = roleRepository.findByName("root");
    if (roleAdmin == null) {
      roleAdmin = roleRepository.save(roleRepository.convert(RoleService.ROOT_ROLE));
    }
    RoleEntity roleUser = roleRepository.findByName("user");
    if (roleUser == null) {
      roleUser = roleRepository.save(new RoleEntity("user"));
    }

    if (userRepository.findByName("alice") == null) {
      UserEntity alice = new UserEntity("alice", "Alice", "test");
      alice.getRoles().add(roleUser);
      userRepository.save(alice);
    }

    if (userRepository.findByName("bob") == null) {
      UserEntity bob = new UserEntity("bob", "Bob", "test");
      bob.getRoles().add(roleUser);
      userRepository.save(bob);
    }

    if (userRepository.findByName("eclipse") == null) {
      UserEntity eclipse = new UserEntity("eclipse", "Eclipse", "scout");
      eclipse.getRoles().add(roleUser);
      eclipse.getRoles().add(roleAdmin);
      userRepository.save(eclipse);
    }
  }

  @Override
  @Transactional
  public void addUser(User user) {
    save(user, true);
  }

  @Override
  @Transactional
  public void saveUser(User user) {
    save(user, false);
  }

  @Override
  @Transactional(readOnly = true)
  public User getUser(String userName) {
    return userRepository.findByName(userName) != null ? userRepository.convert(userRepository.findByName(userName)) : null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getUsers() {
    return userRepository.findAll().stream().map(u -> userRepository.convert(u)).collect(Collectors.toList());
  }

  protected void save(User user, boolean addUser) {
    if (user == null) {
      throw new VetoException(TEXTS.get("UserToSaveIsNull"));
    }

    UserEntity existingUser = userRepository.findByName(user.getName());
    if (addUser && existingUser != null) {
      throw new VetoException(TEXTS.get("UserToSaveExists"), user.getName());
    }
    else if (existingUser == null && !addUser) {
      throw new VetoException(TEXTS.get("UserToSaveIsMissing"), user.getName());
    }

    UserEntity userEntity = userRepository.save(userRepository.convert(user));
    userEntity.setRoles(user.getRoles().stream()
        .map(r -> roleRepository.getOne(r))
        .collect(Collectors.toSet()));
  }

}
