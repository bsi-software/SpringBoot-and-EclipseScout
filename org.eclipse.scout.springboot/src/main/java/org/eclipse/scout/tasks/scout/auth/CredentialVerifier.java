package org.eclipse.scout.tasks.scout.auth;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.security.ICredentialVerifier;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.tasks.model.entity.User;
import org.eclipse.scout.tasks.model.service.UserService;

public class CredentialVerifier implements ICredentialVerifier {

  @Inject
  private UserService userService;

  @Override
  public int verify(String username, char[] passwordPlainText) throws IOException {
    if (StringUtility.isNullOrEmpty(username) || passwordPlainText == null || passwordPlainText.length == 0) {
      return AUTH_CREDENTIALS_REQUIRED;
    }

    final User user = userService.get(username);
    String passwordHash = user.getPasswordHash();
    String passwordSalt = user.getPasswordSalt();
    String passwordPlain = new String(passwordPlainText);

    if (user == null || !PasswordUtility.passwordIsValid(passwordPlain, passwordHash, passwordSalt)) {
      return AUTH_FORBIDDEN;
    }

    return AUTH_OK;
  }

}
