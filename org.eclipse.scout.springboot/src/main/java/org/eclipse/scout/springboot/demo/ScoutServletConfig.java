package org.eclipse.scout.springboot.demo;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.scout.rt.ui.html.UiHttpSessionListener;
import org.eclipse.scout.rt.ui.html.UiServlet;
import org.eclipse.scout.springboot.demo.scout.auth.UiServletFilter;
import org.eclipse.scout.springboot.demo.scout.platform.ScoutSpringWebappListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class ScoutServletConfig {

  private static final String CONTEXT_PATH = "/*";

  @Bean
  public ServletListenerRegistrationBean<ServletContextListener> webappEventListener() {
    return new ServletListenerRegistrationBean<>(new ScoutSpringWebappListener());
  }

  @Bean
  public ServletListenerRegistrationBean<HttpSessionListener> uiHttpSessionListener() {
    return new ServletListenerRegistrationBean<>(new UiHttpSessionListener());
  }

  @Bean
  public ServletRegistrationBean uiServlet() {
    final ServletRegistrationBean reg = new ServletRegistrationBean(new UiServlet(), CONTEXT_PATH);
    reg.setName("uiServlet");
    return reg;
  }

  @Bean
  public FilterRegistrationBean authenticationFilter(UiServletFilter uiServletFilter) {
    final FilterRegistrationBean reg = new FilterRegistrationBean();
    reg.setFilter(uiServletFilter);
    reg.addUrlPatterns(CONTEXT_PATH);
    reg.addInitParameter("filter-exclude", "/res/*");
    reg.setName("authFilter");
    reg.setDispatcherTypes(DispatcherType.REQUEST); // apply this filter only for requests, but not for forwards or redirects.
    return reg;
  }

  @Bean
  public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
    ServletRegistrationBean registration = new ServletRegistrationBean(
        dispatcherServlet);
    registration.addUrlMappings("/services/*");
    return registration;
  }

}
