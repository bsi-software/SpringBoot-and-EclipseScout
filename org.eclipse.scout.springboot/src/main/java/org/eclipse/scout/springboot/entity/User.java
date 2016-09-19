package org.eclipse.scout.springboot.entity;

import java.util.Collection;
import java.util.HashSet;

public class User extends Entity {

  public String firstName;
  public String lastName;
  public String password;
  public byte[] picture;
  public boolean active;
  public Collection<Role> roles;

  public User(String name, String firstName, String lastName, String password) {
    super(name);

    if (firstName == null) {
      throw new IllegalArgumentException("first name must not be null");
    }

    if (password == null) {
      throw new IllegalArgumentException("password must not be null");
    }

    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;

    roles = new HashSet<Role>();
  }

  @Override
  public String toString() {
    if (lastName != null && lastName.trim().length() > 0) {
      return firstName + " " + lastName;
    }
    else {
      return firstName;
    }
  }
}
