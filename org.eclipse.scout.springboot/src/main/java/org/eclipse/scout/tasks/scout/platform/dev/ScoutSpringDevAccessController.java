package org.eclipse.scout.tasks.scout.platform.dev;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.server.commons.authentication.AnonymousAccessController;
import org.eclipse.scout.rt.server.commons.authentication.AnonymousAccessController.AnonymousAuthConfig;
import org.eclipse.scout.rt.server.commons.authentication.IAccessController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Bean
public class ScoutSpringDevAccessController implements IAccessController {

  private static final String USERNAME = "bob";

  private final AnonymousAccessController m_anonymousAccessController = BEANS.get(AnonymousAccessController.class);
  private final AnonymousAuthConfig m_config = new AnonymousAuthConfig();

  private final AtomicBoolean m_warningLogged = new AtomicBoolean(false);

  public ScoutSpringDevAccessController init() {
    m_anonymousAccessController.init(m_config
        .withEnabled(Platform.get().inDevelopmentMode())
        .withUsername(USERNAME));
    return this;
  }

  @Override
  public boolean handle(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
    if (m_config.isEnabled() && m_warningLogged.compareAndSet(false, true)) {
      log.warn("+++ Development access control with user {}", m_config.getUsername());
    }
    return m_anonymousAccessController.handle(request, response, chain);
  }

  @Override
  public void destroy() {
    m_anonymousAccessController.destroy();
  }
}
