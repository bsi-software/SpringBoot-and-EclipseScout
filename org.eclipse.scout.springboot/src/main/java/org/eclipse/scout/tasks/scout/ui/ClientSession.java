package org.eclipse.scout.tasks.scout.ui;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.spring.service.UserService;
import org.springframework.beans.factory.BeanFactory;

public class ClientSession extends AbstractClientSession {

  @Inject
  private UserService userService;

  @Inject
  private BeanFactory beanFactory;

  private User user;

  public User getUser() {
    return user;
  }

  public ClientSession() {
    super(true);
  }

  /**
   * @return The {@link IClientSession} which is associated with the current thread, or <code>null</code> if not found.
   */
  public static ClientSession get() {
    return ClientSessionProvider.currentSession(ClientSession.class);
  }

  @Override
  protected void execLoadSession() {
    initCurrentUser();
    setDesktop(BEANS.get(Desktop.class)); // lookup via BeanManager to support auto-wiring.
  }

  private void initCurrentUser() {
    if (getSubject() != null && !getSubject().getPrincipals().isEmpty()) {
      String username = getSubject().getPrincipals().iterator().next().getName();
      user = userService.getUser(username);
    }
  }
}
