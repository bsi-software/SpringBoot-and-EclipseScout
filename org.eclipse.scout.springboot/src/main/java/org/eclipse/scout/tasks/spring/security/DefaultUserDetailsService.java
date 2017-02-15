package org.eclipse.scout.tasks.spring.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.model.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

  @Inject
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User userInJpa = userService.get(username);
      ScoutUserDetails userInSpringSecurity = new ScoutUserDetails(userInJpa, execLoadPermissions(username));
      return userInSpringSecurity;
    }
    catch (Exception e) {
      throw new UsernameNotFoundException(e.getMessage());
    }
  }

  protected Set<String> execLoadPermissions(String userId) {
    Collection<Role> roles = userService.getRoles(userId);
    Set<String> permissions = new HashSet<String>();

    for (Role role : roles) {
      permissions.add("ROLE_" + role.getId().toUpperCase());

      if (Role.ROOT.equals(role)) {
        permissions.addAll(BEANS.get(IPermissionService.class).getAllPermissionClasses().stream()
            .map(p -> p.getName())
            .collect(Collectors.toSet()));
      }
      else {
        for (String permissionId : role.getPermissions()) {
          permissions.add(permissionId);
        }
      }
    }

    return permissions;
  }

}
