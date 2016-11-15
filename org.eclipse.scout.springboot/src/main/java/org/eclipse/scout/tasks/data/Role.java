package org.eclipse.scout.tasks.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class Role implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String ROLE_ROOT = "root";

  public Role() {
  }

  public Role(String rootId) {
    this.name = rootId;
  }

  private UUID id = UUID.randomUUID();

  private String name;

  private Set<String> permissions = new HashSet<>();

}
