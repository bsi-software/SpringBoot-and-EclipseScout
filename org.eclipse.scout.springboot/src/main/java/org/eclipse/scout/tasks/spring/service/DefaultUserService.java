package org.eclipse.scout.tasks.spring.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.data.Role;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.model.UserEntity;
import org.eclipse.scout.tasks.scout.auth.AccessControlService;
import org.eclipse.scout.tasks.scout.ui.ResourceBase;
import org.eclipse.scout.tasks.scout.ui.task.CreateTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.UpdateTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.ViewAllTasksPermission;
import org.eclipse.scout.tasks.scout.ui.user.UserPictureProviderService;
import org.eclipse.scout.tasks.spring.repository.DocumentRepository;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.eclipse.scout.tasks.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultUserService implements UserService {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

  protected static final String SUPER_USER = "SuperUser";
  protected static final String USER = "User";

  protected static final String USER_ROOT = "root";
  protected static final String USER_ALICE = "alice";
  protected static final String USER_BOB = "bob";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private UserPictureProviderService userPictureProviderService;

  @Autowired
  private AccessControlService accessControlService;

  protected Document getDefaultUserPicture(String userId, String image) {
    try {
      byte[] data = IOUtility.readFromUrl(ResourceBase.class.getResource("img/user/" + image));
      Document picture = new Document(image, data, Document.TYPE_PICTURE);

      userPictureProviderService.addUserPicture(userId, picture.getData());
      documentRepository.save(documentRepository.convert(picture));

      return picture;
    }
    catch (IOException e) {
      LOG.error("Error while loading " + image, e);
    }

    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getAll() {
    return userRepository.findAll()
        .stream()
        .map(u -> userRepository.convert(u))
        .collect(Collectors.toList());
  }

  @Override
  public boolean exists(String userId) {
    return userRepository.exists(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public User get(String userId) {
    return userRepository.getOne(userId) != null ? userRepository.convert(userRepository.getOne(userId)) : null;
  }

  @Override
  @Transactional
  public void save(User user) {
    if (user == null) {
      return;
    }

    validate(user);

    UserEntity userEntity = userRepository.save(userRepository.convert(user));
    userEntity.setRoles(user.getRoles()
        .stream()
        .map(r -> roleRepository.getOne(r))
        .collect(Collectors.toSet()));

    accessControlService.clearCache();
  }

  @Override
  @Transactional(readOnly = true)
  public Set<Role> getRoles(String userId) {
    final UserEntity userEntity = userRepository.getOne(userId);

    if (userEntity != null) {
      return userEntity.getRoles()
          .stream()
          .map(r -> roleRepository.convert(r))
          .collect(Collectors.toSet());
    }

    return new HashSet<Role>();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isRoot(String userId) {
    Set<Role> roles = getRoles(userId);
    return roles.contains(Role.ROOT);
  }

  @Override
  @Transactional(readOnly = true)
  public Document getPicture(String userId) {
    final UserEntity userEntity = userRepository.getOne(userId);
    if (userEntity != null) {
      UUID pictureId = userEntity.getPictureId();

      if (pictureId != null && documentRepository.exists(pictureId)) {
        return documentRepository.convert(documentRepository.getOne(pictureId));
      }
    }

    return null;
  }

  @Override
  @Transactional
  public void setPicture(String userId, Document picture) {
    final UserEntity userEntity = userRepository.getOne(userId);
    if (userEntity != null) {
      if (picture != null) {
        userEntity.setPictureId(picture.getId());
        documentRepository.save(documentRepository.convert(picture));
        userPictureProviderService.addUserPicture(userId, picture.getData());
        userRepository.save(userEntity);
      }
    }
  }

  /**
   * Add initial demo entities: roles and users.
   */
  @PostConstruct
  public void init() {
    initRoles();
    initUsers();
  }

  /**
   * Add roles: root, super user and user.
   */
  private void initRoles() {
    LOG.info("Check and initialise roles");

    if (!roleRepository.exists(Role.ROOT_ID)) {
      roleRepository.save(roleRepository.convert(Role.ROOT));
    }

    if (!roleRepository.exists(SUPER_USER)) {
      Role roleSuperUser = new Role(SUPER_USER);
      Set<String> permissions = new HashSet<>();

      permissions.add(ReadTaskPermission.class.getName());
      permissions.add(CreateTaskPermission.class.getName());
      permissions.add(UpdateTaskPermission.class.getName());
      permissions.add(ViewAllTasksPermission.class.getName());
      roleSuperUser.setPermissions(permissions);

      roleRepository.save(roleRepository.convert(roleSuperUser));
    }

    if (!roleRepository.exists(USER)) {
      Role roleUser = new Role(USER);
      Set<String> permissions = new HashSet<>();

      permissions.add(ReadTaskPermission.class.getName());
      permissions.add(CreateTaskPermission.class.getName());
      permissions.add(UpdateTaskPermission.class.getName());
      roleUser.setPermissions(permissions);

      roleRepository.save(roleRepository.convert(roleUser));
    }
  }

  /**
   * Add users: root, alice and bob.
   */
  private void initUsers() {
    LOG.info("Check and initialise users");

    if (!userRepository.exists(USER_ROOT)) {
      User root = new User(USER_ROOT, "Root", "cando");
      root.getRoles().add(Role.ROOT_ID);

      Document picture = getDefaultUserPicture(USER_ROOT, "eclipse.jpg");
      root.setPictureId(picture.getId());

      userRepository.save(userRepository.convert(root));
    }

    if (!userRepository.exists(USER_ALICE)) {
      User alice = new User(USER_ALICE, "Alice", "test");
      alice.getRoles().add(USER);
      alice.getRoles().add(SUPER_USER);

      Document picture = getDefaultUserPicture(USER_ALICE, "alice.jpg");
      alice.setPictureId(picture.getId());

      userRepository.save(userRepository.convert(alice));
    }

    if (!userRepository.exists(USER_BOB)) {
      User bob = new User(USER_BOB, "Bob", "test");
      bob.getRoles().add(USER);

      Document picture = getDefaultUserPicture(USER_BOB, "bob.jpg");
      bob.setPictureId(picture.getId());

      userRepository.save(userRepository.convert(bob));
    }
  }
}
