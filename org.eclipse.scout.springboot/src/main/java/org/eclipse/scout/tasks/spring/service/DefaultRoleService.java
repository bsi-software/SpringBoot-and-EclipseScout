package org.eclipse.scout.tasks.spring.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.tasks.data.Role;
import org.eclipse.scout.tasks.model.RoleEntity;
import org.eclipse.scout.tasks.scout.auth.AccessControlService;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultRoleService implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AccessControlService accessControlService;

  @Override
  @Transactional(readOnly = true)
  public List<Role> getAll() {
    return roleRepository.findAll()
        .stream()
        .map(r -> roleRepository.convert(r))
        .collect(Collectors.toList());
  }

  @Override
  public boolean exists(String roleId) {
    return roleRepository.exists(roleId);
  }

  @Override
  @Transactional(readOnly = true)
  public Role get(String roleId) {
    return roleRepository.convert(roleRepository.getOne(roleId));
  }

  @Override
  @Transactional
  public void save(Role role) {
    roleRepository.save(roleRepository.convert(role));
    accessControlService.clearCache();
  }

  @Override
  @Transactional(readOnly = true)
  public Set<String> getPermissions(String roleId) {
    RoleEntity role = roleRepository.getOne(roleId);

    if (role != null) {
      role.getPermissions().size();
      return role.getPermissions();
    }

    return new HashSet<String>();
  }

}
