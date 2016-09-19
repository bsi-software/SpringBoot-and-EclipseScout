package org.eclipse.scout.springboot.entity;

import java.util.UUID;

public class Entity {
  private String id;
  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Entity(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }

    name = name.trim();

    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be empty");
    }

    setId(UUID.randomUUID().toString());
    this.setName(name);
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Entity)) {
      return false;
    }

    return getId().equals(((Entity) obj).getId());
  }
}
