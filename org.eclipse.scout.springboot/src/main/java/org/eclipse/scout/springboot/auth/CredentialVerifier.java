package org.eclipse.scout.springboot.auth;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.security.ICredentialVerifier;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CredentialVerifier implements ICredentialVerifier {

  private final ToDoListModel m_repo;

  @Inject
  public CredentialVerifier(ToDoListModel repo) {
    m_repo = repo;
  }

  @Override
  public int verify(String username, char[] passwordPlainText) throws IOException {
    if (StringUtility.isNullOrEmpty(username) || passwordPlainText == null || passwordPlainText.length == 0) {
      return AUTH_CREDENTIALS_REQUIRED;
    }

    User user = m_repo.getUser(username);
    if (user == null || !user.getPassword().equals(String.valueOf(passwordPlainText))) {
      return AUTH_FORBIDDEN;
    }

    return AUTH_OK;
  }

}
