package org.eclipse.scout.springboot.demo.scout.auth;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.security.ICredentialVerifier;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CredentialVerifier implements ICredentialVerifier {

  @Autowired // TODO [mzi] ask Patrick why this annotation is required. Here we have construction injection. Is it just to make clear that this field is injected?
  private UserService userService;

  @Inject
  public CredentialVerifier(UserService userService) {
    this.userService = userService;
  }

  @Override
  public int verify(String username, char[] passwordPlainText) throws IOException {
    if (StringUtility.isNullOrEmpty(username) || passwordPlainText == null || passwordPlainText.length == 0) {
      return AUTH_CREDENTIALS_REQUIRED;
    }

    User user = userService.getUser(username);
    if (user == null || !user.getPassword().equals(String.valueOf(passwordPlainText))) {
      return AUTH_FORBIDDEN;
    }

    return AUTH_OK;
  }

}
