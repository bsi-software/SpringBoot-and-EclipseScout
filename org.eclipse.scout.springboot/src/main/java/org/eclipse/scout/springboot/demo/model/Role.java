package org.eclipse.scout.springboot.demo.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Role extends BaseEntity {

  public static final String ROLE_ROOT = "root";
  public static final String ROLE_USER = "user";

  private static final long serialVersionUID = 1L;

  @ElementCollection
  private Set<String> permissions = new HashSet<>();

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
