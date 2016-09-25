package org.eclipse.scout.springboot.demo.model;

import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Persistable<UUID> {

  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  private UUID id;
  private String name;

  public BaseEntity(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }

    name = name.trim();

    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be empty");
    }

    setId(UUID.randomUUID());

    this.name = name;
  }

  @Override
  public boolean isNew() {
    return getId() == null;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof BaseEntity)) {
      return false;
    }

    return id.equals(((BaseEntity) obj).id);
  }
}
