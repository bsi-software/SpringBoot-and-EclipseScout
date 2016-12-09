
package org.eclipse.scout.tasks.scout.auth;

import java.security.AllPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ISession;
import org.eclipse.scout.rt.shared.cache.ICacheBuilder;
import org.eclipse.scout.rt.shared.services.common.security.AbstractAccessControlService;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link AccessControlService}</h3> {@link IAccessControlService} service that uses {@link ISession#getUserId()} as
 * internal cache key required by {@link AbstractAccessControlService} implementation.
 */
public class AccessControlService extends AbstractAccessControlService<String> {
  private static final Logger LOG = LoggerFactory.getLogger(AccessControlService.class);

  @Inject
  private UserService userService;

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
    LOG.info("clearing cache");
    super.clearCache();
  }

  @Override
  protected PermissionCollection execLoadPermissions(String userId) {
    LOG.info("loading permissions for user '" + userId + "'");

    Collection<Role> roles = userService.getRoles(userId);
    Permissions permissions = new Permissions();

    // check for root role
    if (roles.contains(Role.ROOT)) {
      permissions.add(new AllPermission());
    }
    // collect all permissions from all non-root roles
    else {
      for (Role role : roles) {
        for (String permissionId : role.getPermissions()) {
          Permission permission = getPermission(permissionId);

          if (permission != null) {
            permissions.add(permission);
          }
        }
      }
    }

    return permissions;
  }

  private Permission getPermission(String permissionKey) {
    return BEANS.get(PermissionService.class).getPermission(permissionKey);
  }
}
