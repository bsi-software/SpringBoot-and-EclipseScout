package org.eclipse.scout.tasks.scout.ui;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.platform.BEANS;

public class ClientSession extends AbstractClientSession {

  private String userId = "";

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
      userId = getSubject().getPrincipals().iterator().next().getName();
    }
  }

  @Override
  public String getUserId() {
    return userId;
  }
}
