package org.eclipse.scout.tasks.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.scout.tasks.data.Role;
import org.springframework.data.domain.Persistable;

@Entity
public class RoleEntity implements Persistable<String> {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @ElementCollection
  private Set<String> permissions = new HashSet<>();

  public RoleEntity() {
  }

  public RoleEntity(String roleId) {
    id = roleId;
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Set<String> getPermissions() {
    return permissions;
  }

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

  @Override
  public boolean isNew() {
    return getId() == null;
  }

}
