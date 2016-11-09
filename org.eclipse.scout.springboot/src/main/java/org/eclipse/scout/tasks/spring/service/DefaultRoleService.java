package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.Role;
import org.eclipse.scout.tasks.model.RoleEntity;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultRoleService implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  @Transactional
  public void addRole(Role role) {
    save(role, true);
  }

  @Override
  @Transactional
  public void saveRole(Role role) {
    save(role, false);
  }

  @Override
  @Transactional(readOnly = true)
  public Role getRole(UUID id) {
    return roleRepository.convert(roleRepository.getOne(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Role> getRoles() {
    return roleRepository.findAll().stream()
        .map(r -> roleRepository.convert(r))
        .collect(Collectors.toList());
  }

  protected void save(Role role, boolean addRole) {
    if (role == null) {
      throw new VetoException(TEXTS.get("RoleToSaveIsNull"));
    }

    RoleEntity existingRole = roleRepository.findByName(role.getName());
    if (addRole && existingRole != null) {
      throw new VetoException(TEXTS.get("RoleToSaveExists"), role.getName());
    }
    else if (existingRole == null && !addRole) {
      throw new VetoException(TEXTS.get("RoleToSaveIsMissing"), role.getName());
    }

    roleRepository.save(roleRepository.convert(role));
  }

  @Override
  @Transactional(readOnly = true)
  public Map<UUID, String> getAllRoleNames() {
    return roleRepository.findAll().stream()
        .collect(Collectors.toMap(
            r -> r.getId(),
            r -> r.getName()));
  }

}
