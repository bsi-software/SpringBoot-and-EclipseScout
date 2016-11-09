package org.eclipse.scout.tasks.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

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
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"firstName", "lastName"})
public class UserEntity implements Persistable<UUID> {
  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  @NonNull
  private UUID id = UUID.randomUUID();

  @NonNull
  private String name;

  @NonNull
  private String firstName;
  private String lastName;

  @NonNull
  private String password;

  @Lob
  private byte[] picture;

  private boolean active;

  @ManyToMany
  private Set<RoleEntity> roles = new HashSet<>();

  @Override
  public boolean isNew() {
    return getId() == null;
  }

}
