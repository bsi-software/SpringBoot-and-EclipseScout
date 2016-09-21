package org.eclipse.scout.springboot.demo.spring.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Role;

/**
 * <h3>{@link RoleService}</h3>
 *
 * @author patbaumgartner
 */
public interface RoleService {

  Role getRole(UUID id);

  List<Role> getRoles();

}
