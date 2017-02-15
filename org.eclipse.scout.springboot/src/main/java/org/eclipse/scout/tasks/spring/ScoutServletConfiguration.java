package org.eclipse.scout.tasks.spring;

import javax.servlet.ServletContextListener;

import org.eclipse.scout.rt.server.commons.HttpSessionMutex;
import org.eclipse.scout.rt.ui.html.UiServlet;
import org.eclipse.scout.tasks.scout.platform.ScoutSpringWebappListener;
import org.eclipse.scout.tasks.spring.security.ScoutJaasApiIntegrationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class ScoutServletConfiguration {

  @Bean
  public ServletListenerRegistrationBean<ServletContextListener> scoutSpringWebappListener() {
    return new ServletListenerRegistrationBean<>(new ScoutSpringWebappListener());
  }

  @Bean
  public ServletListenerRegistrationBean<HttpSessionMutex> httpSessionMutex() {
    return new ServletListenerRegistrationBean<>(new HttpSessionMutex());
  }

  @Bean
  public ServletRegistrationBean dispatcherRegistration(WebApplicationContext webApplicationContext) {
    return new ServletRegistrationBean(new UiServlet(), WebMvcConfiguration.SCOUT_CONTEXT_PATH + "/*");
  }

  @Bean
  public FilterRegistrationBean userInsertingMdcFilterRegistrationBean() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new ScoutJaasApiIntegrationFilter());
    registrationBean.setOrder(Integer.MAX_VALUE);
    return registrationBean;
  }

}
