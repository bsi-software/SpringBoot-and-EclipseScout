package org.eclipse.scout.tasks.spring.persistence.repository;

import java.util.stream.Collectors;

import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.spring.persistence.RoleEntity;
import org.eclipse.scout.tasks.spring.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  default UserEntity convert(User user) {
    final UserEntity userEntity = new UserEntity();
    if (user != null) {
      userEntity.setId(user.getId());
      userEntity.setPasswordHash(user.getPasswordHash());
      userEntity.setPasswordSalt(user.getPasswordSalt());
      userEntity.setFirstName(user.getFirstName());
      userEntity.setLastName(user.getLastName());
      userEntity.setPictureId(user.getPictureId());
      userEntity.setLocale(user.getLocale());

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
    user.setPasswordHash(userEntity.getPasswordHash());
    user.setPasswordSalt(userEntity.getPasswordSalt());
    user.setFirstName(userEntity.getFirstName());
    user.setLastName(userEntity.getLastName());
    user.setPictureId(userEntity.getPictureId());
    user.setLocale(userEntity.getLocale());

    user.setRoles(userEntity.getRoles()
        .stream()
        .map(r -> r.getId())
        .collect(Collectors.toSet()));

    return user;
  }
}
