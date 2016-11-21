package org.eclipse.scout.tasks.scout.auth;

import java.io.IOException;

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
import org.eclipse.scout.tasks.scout.platform.dev.ScoutSpringDevAccessController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UiServletFilter implements Filter {

  private TrivialAccessController trivialAccessController;
  private FormBasedAccessController formBasedAccessController;
  private ScoutSpringDevAccessController m_scoutSpringDevAccessController;

  private CredentialVerifier credentialVerifier;

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    trivialAccessController.init(new TrivialAuthConfig()
        .withExclusionFilter(filterConfig.getInitParameter("filter-exclude"))
        .withLoginPageInstalled(true));
    formBasedAccessController.init(new FormBasedAuthConfig()
        .withCredentialVerifier(credentialVerifier));
    m_scoutSpringDevAccessController = BEANS.get(ScoutSpringDevAccessController.class).init();
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

    if (m_scoutSpringDevAccessController.handle(req, resp, chain)) {
      return;
    }

    BEANS.get(ServletFilterHelper.class).forwardToLoginForm(req, resp);
  }

  @Override
  public void destroy() {
    m_scoutSpringDevAccessController.destroy();
    formBasedAccessController.destroy();
    trivialAccessController.destroy();
  }
}
