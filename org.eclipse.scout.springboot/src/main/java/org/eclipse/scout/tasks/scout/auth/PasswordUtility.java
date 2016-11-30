package org.eclipse.scout.tasks.scout.auth;

import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;

public class PasswordUtility {

  public static final int PASSWORD_LENGTH_MIN = 3;
  public static final int PASSWORD_LENGTH_MAX = 64;
  public static final String PASSWORD_ERROR_LENGTH = "PasswordErrorLength";

  public static final int SALT_LENGTH = 64;
  private static final int HASH_ITERATIONS = 20000;

  /**
   * Checks if the provided password matches the implemented policy. The implemented policy does a null check and
   * verifies the password length.
   */
  public static boolean matchesPasswordPolicy(String password) {
    if (password == null) {
      return false;
    }

    if (password.length() < PASSWORD_LENGTH_MIN || password.length() > PASSWORD_LENGTH_MAX) {
      return false;
    }

    return true;
  }

  /**
   * Returns true if the provided plain text password matches with the provided password hash and salt value.
   */
  public static boolean passwordIsValid(String passwordPlainAttempt, String passwordHash, String passwordSalt) {
    if (passwordHash == null || passwordSalt == null || passwordPlainAttempt == null) {
      return false;
    }

    String passwordAttemptHash = calculatePasswordHash(passwordPlainAttempt, passwordSalt);
    return passwordHash.equals(passwordAttemptHash);
  }

  /**
   * Generates {@link SALT_LENGTH} random bytes encoded into a string.
   */
  public static String generatePasswordSalt() {
    byte[] bSalt = SecurityUtility.createRandomBytes(SALT_LENGTH);
    return Base64Utility.encode(bSalt);
  }

  /**
   * Provides a password hash for the provided salt value and plain text password.
   */
  public static String calculatePasswordHash(String passwordPlain, String passwordSalt) {
    char[] pass = passwordPlain.toCharArray();
    byte[] salt = Base64Utility.decode(passwordSalt);
    byte[] hash = SecurityUtility.hashPassword(pass, salt, HASH_ITERATIONS);

    return Base64Utility.encode(hash);
  }
}
