package org.eclipse.scout.springboot.demo.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"firstName", "lastName"})
@Accessors(chain = true)
public class User implements Persistable<UUID> {

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
  private Set<Role> roles = new HashSet<>();

  @Override
  public boolean isNew() {
    return getId() == null;
  }

  public String toDisplayText() {
    return StringUtility.join(" ", firstName, lastName);
  }
}
