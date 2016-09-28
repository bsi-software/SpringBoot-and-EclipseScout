package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRoleService implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void addRole(Role role) {
    save(role, true);
  }

  @Override
  public void saveRole(Role role) {
    save(role, false);
  }

  @Override
  public Role getRole(UUID id) {
    return roleRepository.getOne(id);
  }

  @Override
  public List<Role> getRoles() {
    return roleRepository.findAll();
  }

  private void save(Role role, boolean addRole) {
    if (role == null) {
      throw new VetoException(TEXTS.get("RoleToSaveIsNull"));
    }

    Role existingRole = roleRepository.findByName(role.getName());
    if (addRole && existingRole != null) {
      throw new VetoException(TEXTS.get("RoleToSaveExists"), role.getName());
    }
    else if (existingRole == null && !addRole) {
      throw new VetoException(TEXTS.get("RoleToSaveIsMissing"), role.getName());
    }

    roleRepository.save(role);
  }

}
