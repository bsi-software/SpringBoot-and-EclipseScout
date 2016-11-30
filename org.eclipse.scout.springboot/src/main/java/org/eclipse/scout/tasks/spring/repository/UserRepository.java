package org.eclipse.scout.tasks.spring.repository;

import java.util.Locale;
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
      userEntity.setPasswordHash(user.getPasswordHash());
      userEntity.setPasswordSalt(user.getPasswordSalt());
      userEntity.setFirstName(user.getFirstName());
      userEntity.setLastName(user.getLastName());
      userEntity.setPictureId(user.getPictureId());

      if (user.getLocale() != null) {
        userEntity.setLocale(user.getLocale().toLanguageTag());
      }

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

    if (userEntity.getLocale() != null) {
      user.setLocale(Locale.forLanguageTag(userEntity.getLocale()));
    }

    user.setRoles(userEntity.getRoles()
        .stream()
        .map(r -> r.getId())
        .collect(Collectors.toSet()));

    return user;
  }
}
