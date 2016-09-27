package org.eclipse.scout.springboot.demo.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Role implements Persistable<UUID> {

  public static final String ROLE_ROOT = "root";
  public static final String ROLE_USER = "user";

  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  @NonNull
  private UUID id = UUID.randomUUID();

  @NonNull
  private String name;

  @ElementCollection
  private Set<String> permissions = new HashSet<>();

  public void addPermission(String permission) {
    permissions.add(permission);
  }

  public void removePermission(String permission) {
    permissions.remove(permission);
  }

  @Override
  public boolean isNew() {
    return getId() == null;
  }
}
