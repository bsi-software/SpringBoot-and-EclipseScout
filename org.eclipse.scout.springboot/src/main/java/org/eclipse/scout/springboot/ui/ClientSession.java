package org.eclipse.scout.springboot.ui;

import javax.servlet.ServletContext;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.platform.BEANS;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ClientSession extends AbstractClientSession {

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
    final ServletContext servletContext = BEANS.get(ServletContext.class);
    final WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    setDesktop(applicationContext.getBean(Desktop.class)); // lookup via BeanManager to support auto-wiring.
  }
}
