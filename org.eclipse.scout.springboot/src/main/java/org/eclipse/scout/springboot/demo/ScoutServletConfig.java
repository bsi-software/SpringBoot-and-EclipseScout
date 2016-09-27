package org.eclipse.scout.springboot.demo;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.scout.rt.server.commons.authentication.FormBasedAccessController;
import org.eclipse.scout.rt.server.commons.authentication.TrivialAccessController;
import org.eclipse.scout.rt.ui.html.UiHttpSessionListener;
import org.eclipse.scout.rt.ui.html.UiServlet;
import org.eclipse.scout.springboot.demo.scout.auth.CredentialVerifier;
import org.eclipse.scout.springboot.demo.scout.auth.UiServletFilter;
import org.eclipse.scout.springboot.demo.scout.platform.ScoutSpringWebappListener;
import org.eclipse.scout.springboot.demo.spring.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class ScoutServletConfig {

  private static final String CONTEXT_PATH = "/*";
  public static final String SERVICES_PATH = "/services";

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
    return new ServletRegistrationBean(new DispatcherServletEx(webApplicationContext), CONTEXT_PATH);
  }

  @Bean
  public UiServletFilter uiServletFilter(TrivialAccessController trivialAccessController, FormBasedAccessController formBasedAccessController, CredentialVerifier credentialVerifier) {
    return new UiServletFilter(trivialAccessController, formBasedAccessController, credentialVerifier);
  }

  @Bean
  public CredentialVerifier credentialVerifier(UserService userService) {
    return new CredentialVerifier(userService);
  }

  @Bean
  public FilterRegistrationBean authenticationFilter(UiServletFilter uiServletFilter) {
    final FilterRegistrationBean reg = new FilterRegistrationBean();
    reg.setFilter(uiServletFilter);
    reg.addUrlPatterns(CONTEXT_PATH);
    reg.addInitParameter("filter-exclude", "/res/*, " + SERVICES_PATH + "/*");
    reg.setName("authFilter");
    reg.setDispatcherTypes(DispatcherType.REQUEST); // apply this filter only for requests, but not for forwards or redirects.
    return reg;
  }

  //TODO [Patrick Baumgartner] Remove this dispatcher servlet.
  //                           Implement via Spring request handler (adapter) and not by extending the Spring DispatcherServlet
  public static class DispatcherServletEx extends DispatcherServlet {

    private static final long serialVersionUID = 1L;

    private final UiServlet uiServlet = new UiServlet();

    public DispatcherServletEx(final WebApplicationContext webApplicationContext) {
      super(webApplicationContext);
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
      super.init(config);
      uiServlet.init(config);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
      if (request.getRequestURI().startsWith(SERVICES_PATH)) {
        super.service(request, response);
      }
      else {
        uiServlet.service(request, response);
      }
    }
  }
}
