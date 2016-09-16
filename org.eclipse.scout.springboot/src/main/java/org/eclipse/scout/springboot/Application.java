package org.eclipse.scout.springboot;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.scout.rt.ui.html.UiHttpSessionListener;
import org.eclipse.scout.rt.ui.html.UiServlet;
import org.eclipse.scout.springboot.platform.ScoutSpringWebappListener;
import org.eclipse.scout.springboot.ui.UiServletFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot start class.
 */
@SpringBootApplication
public class Application {

  private static final String CONTEXT_PATH = "/*";

  @Inject
  private ApplicationContext applicationContext;

  public static void main(final String[] args) {
    start(args);
  }

  public static ConfigurableApplicationContext start(final String[] args) {
    return SpringApplication.run(Application.class, args);
  }

  // ==== Registration of Servlet 3.0 artifacts ==== //

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
  public FilterRegistrationBean authenticationFilter() {
    final FilterRegistrationBean reg = new FilterRegistrationBean();
    reg.setFilter(applicationContext.getBean(UiServletFilter.class));
    reg.addUrlPatterns(CONTEXT_PATH);
    reg.addInitParameter("filter-exclude", "/res/*");
    reg.setName("authFilter");
    reg.setDispatcherTypes(DispatcherType.REQUEST); // apply this filter only for requests, but not for forwards or redirects.
    return reg;
  }
}
