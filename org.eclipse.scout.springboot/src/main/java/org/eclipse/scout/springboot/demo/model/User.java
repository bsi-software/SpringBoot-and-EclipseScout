package org.eclipse.scout.springboot.demo.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

  private static final long serialVersionUID = 1L;

  private String firstName;
  private String lastName;
  private String password;
  private byte[] picture;
  private boolean active;

  @ManyToMany
  private Set<Role> roles;

  public User(String name, String firstName, String lastName, String password) {
    super(name);

    if (firstName == null) {
      throw new IllegalArgumentException("first name must not be null");
    }

    if (password == null) {
      throw new IllegalArgumentException("password must not be null");
    }

    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setPassword(password);

    setRoles(new HashSet<Role>());
  }

  @Override
  public String toString() {
    if (getLastName() != null && getLastName().trim().length() > 0) {
      return getFirstName() + " " + getLastName();
    }
    else {
      return getFirstName();
    }
  }
}
