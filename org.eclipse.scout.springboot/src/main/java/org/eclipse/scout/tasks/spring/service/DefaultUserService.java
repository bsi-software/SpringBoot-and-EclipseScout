package org.eclipse.scout.tasks.spring.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.tasks.model.entity.Document;
import org.eclipse.scout.tasks.model.entity.Role;
import org.eclipse.scout.tasks.model.entity.User;
import org.eclipse.scout.tasks.model.service.DocumentService;
import org.eclipse.scout.tasks.model.service.RoleService;
import org.eclipse.scout.tasks.model.service.UserService;
import org.eclipse.scout.tasks.scout.auth.AccessControlService;
import org.eclipse.scout.tasks.scout.ui.ResourceBase;
import org.eclipse.scout.tasks.scout.ui.admin.user.UserPictureProviderService;
import org.eclipse.scout.tasks.scout.ui.task.CreateTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.ReadTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.UpdateTaskPermission;
import org.eclipse.scout.tasks.scout.ui.task.ViewAllTasksPermission;
import org.eclipse.scout.tasks.spring.repository.UserRepository;
import org.eclipse.scout.tasks.spring.repository.entity.UserEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
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
  private RoleService roleService;

  @Autowired
  private DocumentService documentService;

  @Autowired
  private UserPictureProviderService userPictureProviderService;

  @Autowired
  private AccessControlService accessControlService;

  protected Document getDefaultUserPicture(String userId, String image) {
    try {
      byte[] data = IOUtility.readFromUrl(ResourceBase.class.getResource("img/user/" + image));
      Document picture = new Document(image, data, Document.TYPE_PICTURE);

      userPictureProviderService.addUserPicture(userId, picture.getData());
      documentService.save(picture);

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
        .map(user -> convert(user))
        .collect(Collectors.toList());
  }

  @Override
  public boolean exists(String userId) {
    return userRepository.exists(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public User get(String userId) {
    UserEntity user = userRepository.getOne(userId);
    return user != null ? convert(user) : null;
  }

  @Override
  @Transactional
  public void save(User user) {
    if (user == null) {
      return;
    }

    validate(user);

    userRepository.save(convert(user));
    accessControlService.clearCache();
  }

  @Override
  @Transactional(readOnly = true)
  public Set<Role> getRoles(String userId) {
    final UserEntity userEntity = userRepository.getOne(userId);

    if (userEntity != null) {
      return userEntity.getRoles()
          .stream()
          .map(roleId -> roleService.get(roleId))
          .collect(Collectors.toSet());
    }

    return new HashSet<Role>();
  }

  @Override
  @Transactional(readOnly = true)
  public Document getPicture(String userId) {
    final UserEntity userEntity = userRepository.getOne(userId);
    if (userEntity != null) {
      UUID pictureId = userEntity.getPictureId();

      if (pictureId != null && documentService.exists(pictureId)) {
        return documentService.get(pictureId);
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
        documentService.save(picture);
        userPictureProviderService.addUserPicture(userId, picture.getData());
        userRepository.save(userEntity);
      }
    }
  }

  private static ModelMapper mapper = getMapper();

  private static ModelMapper getMapper() {
    ModelMapper mapper = new ModelMapper();

    mapper.createTypeMap(UserEntity.class, User.class).setPostConverter(new Converter<UserEntity, User>() {
      @Override
      public User convert(MappingContext<UserEntity, User> context) {
        context.getDestination().setRoles(
            context.getSource().getRoles()
                .stream()
                .map(r -> new String(r))
                .collect(Collectors.toSet()));

        return context.getDestination();
      }
    });

    mapper.createTypeMap(User.class, UserEntity.class).setPostConverter(new Converter<User, UserEntity>() {
      @Override
      public UserEntity convert(MappingContext<User, UserEntity> context) {
        context.getDestination().setRoles(
            context.getSource().getRoles()
                .stream()
                .map(r -> new String(r))
                .collect(Collectors.toSet()));

        return context.getDestination();
      }
    });

    return mapper;
  }

  public static User convert(UserEntity role) {
    return mapper.map(role, User.class);
  }

  public static UserEntity convert(User role) {
    return mapper.map(role, UserEntity.class);
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

    if (!roleService.exists(Role.ROOT_ID)) {
      roleService.save(Role.ROOT);
    }

    if (!roleService.exists(SUPER_USER)) {
      Role roleSuperUser = new Role(SUPER_USER);
      Set<String> permissions = new HashSet<>();

      permissions.add(ReadTaskPermission.class.getName());
      permissions.add(CreateTaskPermission.class.getName());
      permissions.add(UpdateTaskPermission.class.getName());
      permissions.add(ViewAllTasksPermission.class.getName());
      roleSuperUser.setPermissions(permissions);

      roleService.save(roleSuperUser);
    }

    if (!roleService.exists(USER)) {
      Role roleUser = new Role(USER);
      Set<String> permissions = new HashSet<>();

      permissions.add(ReadTaskPermission.class.getName());
      permissions.add(CreateTaskPermission.class.getName());
      permissions.add(UpdateTaskPermission.class.getName());
      roleUser.setPermissions(permissions);

      roleService.save(roleUser);
    }
  }

  /**
   * Add users: root, alice and bob.
   */
  private void initUsers() {
    LOG.info("Check and initialise users");

    addUser(USER_ROOT, "Root", "eclipse", "eclipse.jpg", Role.ROOT_ID, null);
    addUser(USER_ALICE, "Alice", "test", "alice.jpg", USER, SUPER_USER);
    addUser(USER_BOB, "Bob", "test", "bob.jpg", USER, null);
  }

  private void addUser(String login, String firstName, String password, String pictureFile, String role1, String role2) {
    if (exists(login)) {
      return;
    }

    User user = new User(login, firstName, password);
    user.getRoles().add(role1);

    if (role2 != null) {
      user.getRoles().add(role2);
    }

    if (pictureFile != null) {
      Document picture = getDefaultUserPicture(login, pictureFile);
      user.setPictureId(picture.getId());
    }

    save(user);
  }
}
