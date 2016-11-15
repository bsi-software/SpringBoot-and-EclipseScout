package org.eclipse.scout.tasks.scout.auth;

import java.security.AllPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ISession;
import org.eclipse.scout.rt.shared.cache.ICacheBuilder;
import org.eclipse.scout.rt.shared.services.common.security.AbstractAccessControlService;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;
import org.eclipse.scout.tasks.data.Role;
import org.eclipse.scout.tasks.spring.service.RoleService;
import org.eclipse.scout.tasks.spring.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * <h3>{@link AccessControlService}</h3> {@link IAccessControlService} service that uses {@link ISession#getUserId()} as
 * internal cache key required by {@link AbstractAccessControlService} implementation.
 */
@Slf4j
//@Service
public class AccessControlService extends AbstractAccessControlService<String> {

  @Inject
  private UserService userService;

  @Inject
  private RoleService roleService;

  @Override
  protected String getCurrentUserCacheKey() {
    return getUserIdOfCurrentUser();
  }

  @Override
  protected ICacheBuilder<String, PermissionCollection> createCacheBuilder() {
    @SuppressWarnings("unchecked")
    ICacheBuilder<String, PermissionCollection> cacheBuilder = BEANS.get(ICacheBuilder.class);
    return cacheBuilder.withCacheId(ACCESS_CONTROL_SERVICE_CACHE_ID).withValueResolver(createCacheValueResolver())
        .withShared(false)
        .withClusterEnabled(false)
        .withTransactional(false)
        .withTransactionalFastForward(false)
        .withTimeToLive(1L, TimeUnit.HOURS, false);
  }

  @Override
  public void clearCache() {
    log.info("clearing cache");
    super.clearCache();
  }

  @Override
  protected PermissionCollection execLoadPermissions(String userId) {
    log.info("loading permissions for user '" + userId + "'");

    //TODO [msm] fix
    //Collection<Role> roles = userService.getUserRoles(userId);
    Collection<Role> roles = new HashSet<>();
    Permissions permissions = new Permissions();

    // check for root role
    //if (roles.contains(new Role(RoleService.ROOT))) {
    //TODO [msm] fix
    if (true) {
      permissions.add(new AllPermission());
      return permissions;
    }

    // collect all permissions from all roles
    //TODO [msm] fix
//    for (Role role : roles) {
//      Collection<String> permissionKeys = roleService.getRolePermissions(role.getName());
//
//      for (String permissionKey : permissionKeys) {
//        Permission permission = getPermission(permissionKey);
//
//        if (permission != null) {
//          permissions.add(permission);
//        }
//      }
//    }

    return permissions;
  }

  private Permission getPermission(String permissionKey) {
    return BEANS.get(PermissionService.class).getPermission(permissionKey);
  }
}
