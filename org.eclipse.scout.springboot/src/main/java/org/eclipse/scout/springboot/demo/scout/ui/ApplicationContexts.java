package org.eclipse.scout.springboot.demo.scout.ui;

import javax.servlet.ServletContext;

import org.eclipse.scout.rt.platform.BEANS;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Helper class to access the current {@link ApplicationContext}.
 */
public final class ApplicationContexts {

  private ApplicationContexts() {
  }

  public static ApplicationContext current() {
    final ServletContext servletContext = BEANS.get(ServletContext.class);
    return WebApplicationContextUtils.getWebApplicationContext(servletContext);
  }
}
