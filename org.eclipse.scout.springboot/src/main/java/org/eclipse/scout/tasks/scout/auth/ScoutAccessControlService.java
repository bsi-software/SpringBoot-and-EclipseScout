package org.eclipse.scout.tasks.scout.auth;

import java.security.AllPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ISession;
import org.eclipse.scout.rt.shared.services.common.security.AbstractAccessControlService;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;

/**
 * <h3>{@link ScoutAccessControlService}</h3> {@link IAccessControlService} service that uses
 * {@link ISession#getUserId()} as internal cache key required by {@link AbstractAccessControlService} implementation.
 * <p>
 * Replace this service at server side to load permission collection. It is <b>not</b> required to implement
 * {@link #execLoadPermissions(String)} at client side.
 *
 * @author mzi
 */
public class ScoutAccessControlService extends AbstractAccessControlService<String> {

  @Override
  protected String getCurrentUserCacheKey() {
    return getUserIdOfCurrentUser();
  }

  @Override
  protected PermissionCollection execLoadPermissions(String userId) {
    Permissions p = new Permissions();
    AllPermission all = new AllPermission();
    p.add(all);
    // TODO mzi add permissions depnding on roles
    // BEANS.get(IPermissionService.class).getAllPermissionClasses()
    System.out.println(">>> List of permission classes:");
    for (Class<? extends Permission> p1 : BEANS.get(IPermissionService.class).getAllPermissionClasses()) {
      System.out.println(p1.getSimpleName() + "," + p1.getName());
    }

    // more info
    // https://wiki.eclipse.org/Scout/Tutorial/3.8/Minicrm/Permissions
    return p;
  }
}
