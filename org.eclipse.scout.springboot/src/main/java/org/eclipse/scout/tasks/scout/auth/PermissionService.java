package org.eclipse.scout.tasks.scout.auth;

import java.security.BasicPermission;
import java.security.Permission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.inventory.ClassInventory;
import org.eclipse.scout.rt.platform.inventory.IClassInfo;
import org.eclipse.scout.rt.platform.inventory.IClassInventory;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * Class copied from scout.rt.server package.
 */
@Slf4j
@Order(4900)
public class PermissionService implements IPermissionService {

  private final Object m_permissionClassesLock = new Object();
  private Set<Class<? extends Permission>> m_permissionClasses;
  private Map<String, Permission> m_permissionMap = new HashMap<>();

  /**
   * Populates permission cache before it is accessed by the application.
   */
  @PostConstruct
  public void populatePermissionCache() {
    checkCache();
  }

  @Override
  public Set<Class<? extends Permission>> getAllPermissionClasses() {
    checkCache();
    return CollectionUtility.hashSet(m_permissionClasses);
  }

  public Set<String> getPermissionKeys() {
    checkCache();
    return m_permissionMap.keySet();
  }

  /**
   * Gets permission from cache via it's class name (i.e. permissionclass.getName()).
   *
   * @param key
   *          the fully classified class name of the permission
   * @return the permission class
   */
  public Permission getPermission(String key) {
    return m_permissionMap.get(key);
  }

  private void checkCache() {
    synchronized (m_permissionClassesLock) {
      // null-check with lock (valid check)
      if (m_permissionClasses == null) {
        Set<IClassInfo> allKnownPermissions = getPermissionsFromInventory();
        Set<Class<? extends Permission>> discoveredPermissions = new HashSet<>(allKnownPermissions.size());
        for (IClassInfo permInfo : allKnownPermissions) {
          if (acceptClass(permInfo)) {
            try {
              @SuppressWarnings("unchecked")
              Class<? extends Permission> permClass = (Class<? extends Permission>) permInfo.resolveClass();
              discoveredPermissions.add(permClass);

              String name = permClass.getName();
              Permission permission = (Permission) Class.forName(permClass.getName()).newInstance();
              m_permissionMap.put(name, permission);
            }
            catch (Exception e) {
              log.warn("Unable to load permission: " + e.getLocalizedMessage());
            }
          }
        }
        m_permissionClasses = CollectionUtility.hashSet(discoveredPermissions);
      }
    }
  }

  /**
   * @return Permission classes from class inventory. By default all direct subclasses of {@link Permission} and
   *         {@link BasicPermission} that are available in the {@link ClassInventory} are returned.
   */
  protected Set<IClassInfo> getPermissionsFromInventory() {
    IClassInventory inv = ClassInventory.get();
    //get BasicPermssion subclasses are not found directly, because jdk is not scanned by jandex.
    Set<IClassInfo> classes = inv.getAllKnownSubClasses(Permission.class);
    classes.addAll(inv.getAllKnownSubClasses(BasicPermission.class));
    return classes;
  }

  /**
   * Checks whether the given class is a Permission class that should be visible to this service. The default
   * implementation checks if the class meets the following conditions:
   * <ul>
   * <li>class is instanciable (public, not abstract, not interface, not inner member type)
   * <li>the name is accepted by {@link #acceptClassName(String)}
   * </ul>
   *
   * @param permInfo
   *          the class to be checked
   * @return Returns <code>true</code> if the class used by this service. <code>false</code> otherwise.
   */
  protected boolean acceptClass(IClassInfo permInfo) {
    return permInfo.isInstanciable() && acceptClassName(permInfo.name());
  }

  /**
   * Checks whether the given class name is a potential permission class and used by this service.
   *
   * @param className
   *          the class name to be checked
   * @return Returns <code>true</code> by default.
   */
  protected boolean acceptClassName(String className) {
    return true;
  }

}
