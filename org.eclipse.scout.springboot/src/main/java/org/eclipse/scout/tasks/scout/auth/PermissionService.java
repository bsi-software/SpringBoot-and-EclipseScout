package org.eclipse.scout.tasks.scout.auth;

import java.security.BasicPermission;
import java.security.Permission;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.inventory.ClassInventory;
import org.eclipse.scout.rt.platform.inventory.IClassInfo;
import org.eclipse.scout.rt.platform.inventory.IClassInventory;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;
import org.eclipse.scout.tasks.scout.ui.TextDbProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class copied from scout.rt.server package.
 */
@Order(4900)
public class PermissionService implements IPermissionService {

  public static final String EXCLUDE_PROPERTY = "scout.auth.permission.exclude";

  private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);

  private final Object m_permissionClassesLock = new Object();
  private Set<Class<? extends Permission>> m_permissionClasses;
  private Map<String, Permission> m_permissionMap = new HashMap<>();

  @Inject
  TextDbProviderService textDbService;

  /**
   * Populates permission cache before it is accessed by the application. Also fills in default translations if these
   * should be missing.
   */
  @PostConstruct
  public void populatePermissionCache() {
    checkCache();

    getAllPermissionClasses()
        .stream()
        .forEach(permission -> {
          checkTranslations(permission.getName());
        });
  }

  private void checkTranslations(String id) {
    String prefix = id.substring(0, id.lastIndexOf("."));
    String group = prefix.substring(prefix.lastIndexOf(".") + 1);
    String key = id.substring(id.lastIndexOf(".") + 1);

    checkTranslation(prefix, group);
    checkTranslation(id, key);
  }

  private void checkTranslation(String key, String text) {
    if (StringUtility.hasText(key)) {
      Map<Locale, String> translations = textDbService.getTexts(key);
      if (translations == null || translations.size() == 0) {
        String t1 = StringUtility.splitCamelCase(text);
        String t2 = t1.substring(0, 1).toUpperCase() + t1.substring(1);

        if (t2.endsWith(" Permission")) {
          t2 = t2.substring(0, t2.indexOf(" Permission"));
        }

        textDbService.addText(key, Locale.ROOT, t2);
      }
    }
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
        Set<String> excludePermissions = getPermissionsToExclude();
        Set<Class<? extends Permission>> discoveredPermissions;

        discoveredPermissions = processPermission(allKnownPermissions, excludePermissions);
        m_permissionClasses = CollectionUtility.hashSet(discoveredPermissions);
      }
    }
  }

  private Set<Class<? extends Permission>> processPermission(Set<IClassInfo> allPermissions, Set<String> excludePermissions) {
    Set<Class<? extends Permission>> discoveredPermissions = new HashSet<>(allPermissions.size());

    for (IClassInfo permInfo : allPermissions) {
      if (acceptClass(permInfo)) {
        try {
          @SuppressWarnings("unchecked")
          Class<? extends Permission> permClass = (Class<? extends Permission>) permInfo.resolveClass();

          String name = permInfo.name();
          if (!excludePermissions.contains(name)) {
            discoveredPermissions.add(permClass);

//            String name = permClass.getName();
            Permission permission = (Permission) Class.forName(permClass.getName()).newInstance();
            m_permissionMap.put(name, permission);
          }
        }
        catch (Exception e) {
          LOG.warn("Unable to load permission: " + e.getLocalizedMessage());
        }
      }
    }

    return discoveredPermissions;
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

  public static class ExcludePermissionsProperty extends AbstractStringConfigProperty {

    @Override
    public String getKey() {
      return EXCLUDE_PROPERTY;
    }

    @Override
    protected String getDefaultValue() {
      return "";
    }

  }

  private Set<String> getPermissionsToExclude() {
    String excludePermission = CONFIG.getPropertyValue(ExcludePermissionsProperty.class);

    if (StringUtility.isNullOrEmpty(excludePermission)) {
      return new HashSet<String>();
    }

    Set<String> excludeList = new HashSet<String>();
    Arrays.asList(excludePermission.split(","))
        .stream()
        .forEach(permission -> {
          if (StringUtility.hasText(permission)) {
            excludeList.add(permission);
          }
        });

    return excludeList;
  }
}
