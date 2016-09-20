package org.eclipse.scout.springboot.ui;

import javax.servlet.ServletContext;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ClientSession extends AbstractClientSession {

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
    final ServletContext servletContext = BEANS.get(ServletContext.class);
    final WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    setDesktop(applicationContext.getBean(Desktop.class)); // lookup via BeanManager to support auto-wiring.
  }

  private void initCurrentUser() {
    if (getSubject() != null && !getSubject().getPrincipals().isEmpty()) {
      String username = getSubject().getPrincipals().iterator().next().getName();
      user = BEANS.get(ToDoListModel.class).getUser(username);
    }
  }
}
