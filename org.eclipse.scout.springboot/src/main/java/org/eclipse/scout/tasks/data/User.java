package org.eclipse.scout.tasks.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.eclipse.scout.rt.platform.util.StringUtility;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id = UUID.randomUUID();

  private String name;

  private String firstName;
  private String lastName;

  private String password;

  private boolean active;

  private Set<UUID> roles = new HashSet<>();

  public String toDisplayText() {
    return StringUtility.join(" ", firstName, lastName);
  }
}
