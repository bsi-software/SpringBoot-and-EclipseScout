package org.eclipse.scout.tasks.model;

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
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name"})
@ToString
public class Role implements Persistable<UUID> {

  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  @NonNull
  private UUID id = UUID.randomUUID();

  @NonNull
  private String name;

  @ElementCollection
  private Set<String> permissions;

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
