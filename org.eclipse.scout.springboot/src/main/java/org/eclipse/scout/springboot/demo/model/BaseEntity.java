package org.eclipse.scout.springboot.demo.model;

import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@MappedSuperclass
public abstract class BaseEntity implements Persistable<UUID> {

  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  private UUID id;
  private String name;

  public BaseEntity(String name) {

    setId(UUID.randomUUID());

    setName(name);
  }

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }

    name = name.trim();

    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be empty");
    }

    this.name = name;
  }

  @Override
  public boolean isNew() {
    return getId() == null;
  }
}
