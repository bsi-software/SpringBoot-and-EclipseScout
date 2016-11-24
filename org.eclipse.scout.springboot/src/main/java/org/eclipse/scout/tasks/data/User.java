package org.eclipse.scout.tasks.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

  private String id;
  private String password;
  private String firstName;
  private String lastName;
  private UUID pictureId;
  private Set<String> roles = new HashSet<>();

  public User() {
  }

  public User(String userId, String firstName, String password) {
    this.id = userId;
    this.firstName = firstName;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(String userId) {
    id = userId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UUID getPictureId() {
    return pictureId;
  }

  public void setPictureId(UUID pictureId) {
    this.pictureId = pictureId;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  /**
   * Returns true if the two objects share the same user id, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof User)) {
      return false;
    }

    User user = (User) obj;

    if (user.getId() == null) {
      return false;
    }

    return user.getId().equals(id);
  }

  /**
   * returns the hash code of the user id of this object.
   */
  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }

  @Override
  public String toString() {
    return id + "(" + toDisplayText() + ")";
  }

  public String toDisplayText() {
    if (lastName == null) {
      return firstName;
    }

    return String.join(" ", firstName, lastName);
  }
}
