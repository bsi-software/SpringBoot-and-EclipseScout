package org.eclipse.scout.springboot.demo.spring.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Role;
import org.eclipse.scout.springboot.demo.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRoleService implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role getRole(UUID id) {
    return roleRepository.getOne(id);
  }

  @Override
  public List<Role> getRoles() {
    return roleRepository.findAll();
  }

}
