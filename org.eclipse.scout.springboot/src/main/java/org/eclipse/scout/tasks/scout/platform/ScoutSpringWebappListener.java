package org.eclipse.scout.tasks.scout.platform;

import java.net.CookieHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.server.commons.WebappEventListener;
import org.springframework.web.context.ContextCleanupListener;

/**
 * Same as {@link WebappEventListener}, but without starting the platform.
 */
public class ScoutSpringWebappListener extends ContextCleanupListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent event) {
    BeanMetaData servletContextBean = new BeanMetaData(ServletContext.class, event.getServletContext()).withApplicationScoped(true);
    BEANS.getBeanManager().registerBean(servletContextBean);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    CookieHandler.setDefault(null);
  }
}
