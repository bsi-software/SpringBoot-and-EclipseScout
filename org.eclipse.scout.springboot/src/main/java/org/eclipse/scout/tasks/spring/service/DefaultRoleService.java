package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.tasks.model.entity.Role;
import org.eclipse.scout.tasks.model.service.RoleService;
import org.eclipse.scout.tasks.scout.auth.AccessControlService;
import org.eclipse.scout.tasks.spring.repository.RoleRepository;
import org.eclipse.scout.tasks.spring.repository.entity.RoleEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
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
        .map(r -> convert(r))
        .collect(Collectors.toList());
  }

  @Override
  public boolean exists(String roleId) {
    return roleRepository.exists(roleId);
  }

  @Override
  @Transactional(readOnly = true)
  public Role get(String roleId) {
    return convert(roleRepository.getOne(roleId));
  }

  @Override
  @Transactional
  public void save(Role role) {
    validate(role);
    roleRepository.save(convert(role));
    accessControlService.clearCache();
  }

  private static ModelMapper mapper = getMapper();

  private static ModelMapper getMapper() {
    ModelMapper mapper = new ModelMapper();

    mapper.createTypeMap(RoleEntity.class, Role.class).setPostConverter(new Converter<RoleEntity, Role>() {
      @Override
      public Role convert(MappingContext<RoleEntity, Role> context) {
        context.getDestination().setPermissions(
            context.getSource().getPermissions()
                .stream()
                .map(permission -> new String(permission))
                .collect(Collectors.toSet()));

        return context.getDestination();
      }
    });

    mapper.createTypeMap(Role.class, RoleEntity.class).setPostConverter(new Converter<Role, RoleEntity>() {
      @Override
      public RoleEntity convert(MappingContext<Role, RoleEntity> context) {
        context.getDestination().setPermissions(
            context.getSource().getPermissions()
                .stream()
                .map(permission -> new String(permission))
                .collect(Collectors.toSet()));

        return context.getDestination();
      }
    });

    return mapper;
  }

  public static Role convert(RoleEntity role) {
    return mapper.map(role, Role.class);
  }

  public static RoleEntity convert(Role role) {
    return mapper.map(role, RoleEntity.class);
  }

}
