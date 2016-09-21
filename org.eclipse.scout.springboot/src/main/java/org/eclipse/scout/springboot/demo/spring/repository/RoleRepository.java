package org.eclipse.scout.springboot.demo.spring.repository;

import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <h3>{@link RoleRepository}</h3>
 *
 * @author patbaumgartner
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {

  Role findByName(String name);

}
