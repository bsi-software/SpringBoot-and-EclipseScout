package org.eclipse.scout.springboot.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.commons.authentication.FormBasedAccessController;
import org.eclipse.scout.rt.server.commons.authentication.FormBasedAccessController.FormBasedAuthConfig;
import org.eclipse.scout.rt.server.commons.authentication.ServletFilterHelper;
import org.eclipse.scout.rt.server.commons.authentication.TrivialAccessController;
import org.eclipse.scout.rt.server.commons.authentication.TrivialAccessController.TrivialAuthConfig;
import org.springframework.stereotype.Component;

@Component
public class UiServletFilter implements Filter {

  @Inject
  private TrivialAccessController trivialAccessController;

  @Inject
  private FormBasedAccessController formBasedAccessController;

  @Inject
  private CredentialVerifier credentialVerifier;

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    trivialAccessController.init(new TrivialAuthConfig()
        .withExclusionFilter(filterConfig.getInitParameter("filter-exclude"))
        .withLoginPageInstalled(true));
    formBasedAccessController.init(new FormBasedAuthConfig()
        .withCredentialVerifier(credentialVerifier));
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest req = (HttpServletRequest) request;
    final HttpServletResponse resp = (HttpServletResponse) response;

    if (trivialAccessController.handle(req, resp, chain)) {
      return;
    }

    if (formBasedAccessController.handle(req, resp, chain)) {
      return;
    }

    BEANS.get(ServletFilterHelper.class).forwardToLoginForm(req, resp);
  }

  @Override
  public void destroy() {
    formBasedAccessController.destroy();
    trivialAccessController.destroy();
  }
}
