package org.eclipse.scout.springboot.entity;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

public class Role extends Entity {

  private final Collection<String> permissions = new ConcurrentSkipListSet<>();

  public Role(String name) {
    super(name);
  }

  public Collection<String> getPermissions() {
    return permissions;
  }

  public void addPermission(String permission) {
    if (permission == null) {
      return;
    }

    permissions.add(permission);
  }

  public void removePermission(String permission) {
    if (permission == null) {
      return;
    }

    permissions.remove(permission);
  }
}
