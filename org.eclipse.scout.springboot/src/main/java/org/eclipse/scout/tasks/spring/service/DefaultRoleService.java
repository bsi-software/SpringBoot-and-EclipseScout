package org.eclipse.scout.tasks.spring.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRoleService implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public List<Role> getRoles() {
    return roleRepository.findAll();
  }

  @Override
  public Role getRole(String name) {
    return roleRepository.findByName(name);
  }

  @Override
  @Transactional
  public Set<String> getRolePermissions(String roleName) {
    Role role = roleRepository.findByName(roleName);

    if (role != null) {
      role.getPermissions().size();
      return role.getPermissions();
    }

    return new HashSet<String>();
  }

  @Override
  public void saveRole(Role role) {
    roleRepository.save(role);
  }
}
