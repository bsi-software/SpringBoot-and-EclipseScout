package org.eclipse.scout.tasks.spring.repository;

import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  UserEntity findByName(String name);

  default UserEntity convert(User user) {
    final UserEntity userEntity = new UserEntity();
    if (user != null) {
      if (user.getId() != null) {
        userEntity.setId(user.getId());
      }
      userEntity.setName(user.getName());
      userEntity.setFirstName(user.getFirstName());
      userEntity.setLastName(user.getLastName());
      userEntity.setPassword(user.getPassword());
      userEntity.setPicture(user.getPicture());
    }
    return userEntity;
  }

  default User convert(UserEntity userEntity) {
    final User user = new User();
    user.setId(userEntity.getId());
    user.setName(userEntity.getName());
    user.setFirstName(userEntity.getFirstName());
    user.setLastName(userEntity.getLastName());
    user.setPassword(userEntity.getPassword());
    user.setPicture(userEntity.getPicture());
    user.setActive(userEntity.isActive());
    user.setRoles(userEntity.getRoles().stream()
        .map(r -> r.getId())
        .collect(Collectors.toSet()));
    return user;
  }
}
