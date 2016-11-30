package org.eclipse.scout.tasks.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.eclipse.scout.tasks.scout.auth.PasswordUtility;

public class User {

  public static final Locale LOCALE_DEFAULT = Locale.forLanguageTag("en_US");

  public static final int ID_LENGTH_MIN = 3;
  public static final int ID_LENGTH_MAX = 32;
  public static final String ID_PATTERN = "^[a-zA-Z0-9\\.]+$";
  public static final String ID_ERROR_LENGTH = "UserIdErrorLength";
  public static final String ID_ERROR_PATTERN = "UserIdErrorPattern";

  public static final int FIRST_NAME_LENGTH_MIN = 1;
  public static final int FIRST_NAME_LENGTH_MAX = 64;
  public static final String FIRST_NAME_ERROR_LENGTH = "FirstNameErrorLength";

  @NotNull
  @Size(min = ID_LENGTH_MIN, max = ID_LENGTH_MAX, message = ID_ERROR_LENGTH)
  @Pattern(regexp = ID_PATTERN, message = ID_ERROR_PATTERN)
  private String id;

  @NotNull
  private String passwordHash;

  @NotNull
  @Size(min = PasswordUtility.SALT_LENGTH)
  private String passwordSalt;

  @NotNull
  private Locale locale;

  @NotNull
  @Size(min = FIRST_NAME_LENGTH_MIN, max = FIRST_NAME_LENGTH_MAX, message = FIRST_NAME_ERROR_LENGTH)
  private String firstName;

  private String lastName;
  private UUID pictureId;
  private Set<String> roles = new HashSet<>();

  public User() {
  }

  public User(String userId, String firstName, String passwordPlain) {
    this.id = userId;
    this.firstName = firstName;
    this.passwordSalt = PasswordUtility.generatePasswordSalt();
    this.passwordHash = PasswordUtility.calculatePasswordHash(passwordPlain, passwordSalt);
    this.locale = LOCALE_DEFAULT;
  }

  public String getId() {
    return id;
  }

  public void setId(String userId) {
    id = userId;
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
