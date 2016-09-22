package org.eclipse.scout.springboot.demo.spring.repository;

import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {

  Role findByName(String name);

}
