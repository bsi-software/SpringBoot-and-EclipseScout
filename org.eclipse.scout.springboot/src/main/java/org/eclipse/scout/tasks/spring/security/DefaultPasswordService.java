package org.eclipse.scout.tasks.spring.security;

import javax.inject.Inject;

import org.eclipse.scout.tasks.scout.auth.PasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultPasswordService implements PasswordService {

  @Inject
  private PasswordEncoder passwordEncoder;

  public static final int PASSWORD_LENGTH_MIN = 4;
  public static final int PASSWORD_LENGTH_MAX = 64;

  @Override
  public boolean matchesPasswordPolicy(String password) {
    if (password == null) {
      return false;
    }

    if (password.length() < PASSWORD_LENGTH_MIN || password.length() > PASSWORD_LENGTH_MAX) {
      return false;
    }

    return true;
  }

  @Override
  public boolean passwordIsValid(String passwordPlainAttempt, String passwordHash) {
    if (passwordHash == null || passwordPlainAttempt == null) {
      return false;
    }

    return passwordEncoder.matches(passwordPlainAttempt, passwordHash);
  }

  @Override
  public String calculatePasswordHash(String passwordNew) {
    return passwordEncoder.encode(passwordNew);
  }
}
