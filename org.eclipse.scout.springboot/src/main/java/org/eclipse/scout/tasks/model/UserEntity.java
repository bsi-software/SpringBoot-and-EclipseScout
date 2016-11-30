package org.eclipse.scout.tasks.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

@Entity
public class UserEntity implements Persistable<String> {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @NotNull
  private String firstName;
  private String lastName;

  @NotNull
  private String passwordHash;
  @NotNull
  private String passwordSalt;
  private String locale;

  @Type(type = "uuid-char")
  private UUID pictureId;

  @ManyToMany
  private Set<RoleEntity> roles = new HashSet<>();

  @Override
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

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
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

  @Override
  public boolean isNew() {
    return getId() == null;
  }

}
