package org.eclipse.scout.tasks;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.commons.authentication.FormBasedAccessController;
import org.eclipse.scout.rt.server.commons.authentication.TrivialAccessController;
import org.eclipse.scout.rt.ui.html.UiHttpSessionListener;
import org.eclipse.scout.rt.ui.html.UiServlet;
import org.eclipse.scout.tasks.scout.auth.CredentialVerifier;
import org.eclipse.scout.tasks.scout.auth.UiServletFilter;
import org.eclipse.scout.tasks.scout.platform.ScoutSpringWebappListener;
import org.eclipse.scout.tasks.scout.platform.dev.ScoutSpringDevAccessController;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class ScoutServletConfig {

  @Bean
  public ServletListenerRegistrationBean<ServletContextListener> webappEventListener() {
    return new ServletListenerRegistrationBean<>(new ScoutSpringWebappListener());
  }

  @Bean
  public ServletListenerRegistrationBean<HttpSessionListener> uiHttpSessionListener() {
    return new ServletListenerRegistrationBean<>(new UiHttpSessionListener());
  }

  @Bean
  public ServletRegistrationBean dispatcherRegistration(WebApplicationContext webApplicationContext) {
    return new ServletRegistrationBean(new UiServlet(), WebMvcConfig.SCOUT_CONTEXT_PATH + "/*");
  }

  @Bean
  public UiServletFilter uiServletFilter(
      TrivialAccessController trivialAccessController,
      FormBasedAccessController formBasedAccessController,
      ScoutSpringDevAccessController scoutSpringDevAccessController,
      CredentialVerifier credentialVerifier) {
    return new UiServletFilter(trivialAccessController, formBasedAccessController, scoutSpringDevAccessController, credentialVerifier);
  }

  @Bean
  public CredentialVerifier credentialVerifier() {
    return BEANS.get(CredentialVerifier.class);
  }

  @Bean
  public FilterRegistrationBean authenticationFilter(UiServletFilter uiServletFilter) {
    final FilterRegistrationBean reg = new FilterRegistrationBean();
    reg.setFilter(uiServletFilter);
    reg.addUrlPatterns(WebMvcConfig.SCOUT_CONTEXT_PATH + "/*");
    reg.addInitParameter("filter-exclude", WebMvcConfig.SCOUT_CONTEXT_PATH + "/res/*,"
        + WebMvcConfig.SCOUT_CONTEXT_PATH + "/login.html,"
        + WebMvcConfig.SCOUT_CONTEXT_PATH + "/logout.html");
    reg.setName("authFilter");
    reg.setDispatcherTypes(DispatcherType.REQUEST); // apply this filter only for requests, but not for forwards or redirects.
    return reg;
  }

}
