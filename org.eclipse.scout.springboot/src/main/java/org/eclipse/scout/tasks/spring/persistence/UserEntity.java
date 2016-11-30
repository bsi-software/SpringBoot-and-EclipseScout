package org.eclipse.scout.tasks.spring.persistence;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.eclipse.scout.tasks.spring.persistence.converter.LocaleConverter;
import org.eclipse.scout.tasks.spring.persistence.converter.UuidConverter;

// TODO [mzi] follow pattern used for role, extend Role class here
@Entity
public class UserEntity {

  @Id
  private String id;

  @Column(nullable = false)
  private String firstName;
  private String lastName;

  @Column(nullable = false)
  private String passwordHash;
  @Column(nullable = false)
  private String passwordSalt;

  @Convert(converter = LocaleConverter.class)
  private Locale locale;

  @Convert(converter = UuidConverter.class)
  private UUID pictureId;

  @ManyToMany
  private Set<RoleEntity> roles = new HashSet<>();

  public String getId() {
    return id;
  }

  public void setId(String userId) {
    id = userId;
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

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String password) {
    this.passwordHash = password;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public UUID getPictureId() {
    return pictureId;
  }

  public void setPictureId(UUID pictureId) {
    this.pictureId = pictureId;
  }

  public Set<RoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleEntity> roles) {
    this.roles = roles;
  }

  /**
   * Returns true if the two objects share the same user id, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof UserEntity)) {
      return false;
    }

    UserEntity user = (UserEntity) obj;

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

}
