package org.eclipse.scout.tasks.spring.repository;

import java.util.stream.Collectors;

import org.eclipse.scout.tasks.data.Role;
import org.eclipse.scout.tasks.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

  default RoleEntity convert(Role role) {
    final RoleEntity roleEntity = new RoleEntity();
    if (role != null) {
      roleEntity.setId(role.getId());
      roleEntity.setPermissions(role.getPermissions());
    }
    return roleEntity;
  }

  default Role convert(RoleEntity roleEntity) {
    final Role role = new Role();
    role.setId(roleEntity.getId());
    //TODO [msm] Remove this unproxy Hack
    role.setPermissions(
        roleEntity.getPermissions().stream()
            .map(p -> new String(p.getBytes()))
            .collect(Collectors.toSet()));
    return role;
  }
}
