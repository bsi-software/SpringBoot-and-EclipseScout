package org.eclipse.scout.tasks.spring.persistence;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.scout.tasks.model.Role;

@Entity
public class RoleEntity extends Role {

  public RoleEntity() {
    super();
  }

  public RoleEntity(String roleId) {
    super(roleId);
  }

  @Id
  @Override
  public String getId() {
    return super.getId();
  }

  @ElementCollection
  @Override
  public Set<String> getPermissions() {
    return super.getPermissions();
  }
}
