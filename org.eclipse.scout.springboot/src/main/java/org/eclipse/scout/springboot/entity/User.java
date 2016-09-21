package org.eclipse.scout.springboot.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public class User extends Entity {

  private String firstName;
  private String lastName;
  private String password;
  private Locale locale;
  private byte[] picture;
  private boolean active;
  private Collection<Role> roles;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public Collection<Role> getRoles() {
    return roles;
  }

  public void setRoles(Collection<Role> roles) {
    this.roles = roles;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
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
