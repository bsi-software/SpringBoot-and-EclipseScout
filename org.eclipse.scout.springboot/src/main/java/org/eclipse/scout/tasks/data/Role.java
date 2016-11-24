package org.eclipse.scout.tasks.data;

import java.security.Permission;
import java.util.HashSet;
import java.util.Set;

public class Role {

  public static final String ROOT_ID = "Root";
  public static final Role ROOT = new Role(ROOT_ID);

  public Role() {
  }

  public Role(String roleId) {
    id = roleId;
  }

  private String id;

  private Set<String> permissions = new HashSet<>();

  public String getId() {
    return id;
  }

  public void setId(String roleId) {
    id = roleId;
  }

  /**
   * Returns the set of permissions associated with this role.
   *
   * @return The set of permissions represented by their fully qualified {@link Permission} class names
   */
  public Set<String> getPermissions() {
    return permissions;
  }

  /**
   * Sets the set of permissions for this role.
   *
   * @param permissions
   *          The set of permissions represented by their fully qualified {@link Permission} class names
   */
  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }

  /**
   * Returns true if the two objects share the same role id, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Role)) {
      return false;
    }

    Role role = (Role) obj;

    if (role.getId() == null) {
      return false;
    }

    return role.getId().equals(id);
  }

  /**
   * returns the hash code of the role id of this object.
   */
  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }
}
