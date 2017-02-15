package org.eclipse.scout.tasks.spring.security;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.commons.authentication.ServletFilterHelper;
import org.springframework.security.web.jaasapi.JaasApiIntegrationFilter;

public final class ScoutJaasApiIntegrationFilter extends JaasApiIntegrationFilter {

  @Override
  protected Subject obtainSubject(ServletRequest request) {
    Subject subject = super.obtainSubject(request);

    if (subject == null) {
      final HttpServletRequest req = (HttpServletRequest) request;
      final Principal principal = req.getUserPrincipal();
      if (principal != null) {
        subject = BEANS.get(ServletFilterHelper.class).createSubject(principal);
      }
    }

    return subject;
  }
}
