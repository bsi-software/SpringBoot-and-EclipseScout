package org.eclipse.scout.tasks.spring.repository;

import java.util.stream.Collectors;

import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.model.RoleEntity;
import org.eclipse.scout.tasks.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  default UserEntity convert(User user) {
    final UserEntity userEntity = new UserEntity();
    if (user != null) {
      userEntity.setId(user.getId());
      userEntity.setPassword(user.getPassword());
      userEntity.setFirstName(user.getFirstName());
      userEntity.setLastName(user.getLastName());
      userEntity.setPictureId(user.getPictureId());

      userEntity.setRoles(user.getRoles()
          .stream()
          .map(r -> new RoleEntity(r))
          .collect(Collectors.toSet()));
    }

    return userEntity;
  }

  default User convert(UserEntity userEntity) {
    final User user = new User();
    user.setId(userEntity.getId());
    user.setPassword(userEntity.getPassword());
    user.setFirstName(userEntity.getFirstName());
    user.setLastName(userEntity.getLastName());
    user.setPictureId(userEntity.getPictureId());

    user.setRoles(userEntity.getRoles()
        .stream()
        .map(r -> r.getId())
        .collect(Collectors.toSet()));

    return user;
  }
}
