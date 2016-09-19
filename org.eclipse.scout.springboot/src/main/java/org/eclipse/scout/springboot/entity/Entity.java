package org.eclipse.scout.springboot.entity;

import java.util.UUID;

public class Entity {
  public String id;
  public String name;

  public Entity(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }

    name = name.trim();

    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be empty");
    }

    id = UUID.randomUUID().toString();
    this.name = name;
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

    if (!(obj instanceof Entity)) {
      return false;
    }

    return id.equals(((Entity) obj).id);
  }
}
