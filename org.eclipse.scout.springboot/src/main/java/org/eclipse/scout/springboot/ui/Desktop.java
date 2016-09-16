package org.eclipse.scout.springboot.ui;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.PlatformConfigProperties.ApplicationNameProperty;
import org.eclipse.scout.rt.server.commons.servlet.IHttpServletRoundtrip;
import org.eclipse.scout.rt.shared.TEXTS;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This Spring managed bean represents the web application.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Desktop extends AbstractDesktop {

  private final ApplicationContext applicationContext;

  private final ApplicationNameProperty applicationNameConfig;

  @Inject
  public Desktop(final ApplicationContext applicationContext, final ApplicationNameProperty applicationNameConfig) {
    super(false);
    this.applicationContext = applicationContext;
    this.applicationNameConfig = applicationNameConfig;
    callInitializer();
  }

  /**
   * Returns the Spring application context.
   */
  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  protected String getConfiguredTitle() {
    return applicationNameConfig.getValue();
  }

  @Override
  protected String getConfiguredLogoId() {
    return Icons.ECLIPSE_SCOUT;
  }

  @Override
  protected List<Class<? extends IOutline>> getConfiguredOutlines() {
    return Arrays.asList(HomeOutline.class);
  }

  @Override
  protected void execDefaultView() {
    setOutline(HomeOutline.class);
  }

  @Order(10)
  public class HomeOutlineViewButton extends AbstractOutlineViewButton {

    public HomeOutlineViewButton() {
      this(HomeOutline.class);
    }

    protected HomeOutlineViewButton(final Class<? extends HomeOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }
  }

  @Order(10)
  public class QuickAccessMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("QuickAccess");
    }

    @Order(10)
    public class LogoutMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Logout");
      }

      @Override
      protected void execAction() {
        final HttpSession session = IHttpServletRoundtrip.CURRENT_HTTP_SERVLET_REQUEST.get().getSession(false);
        if (session != null) {
          session.invalidate();
        }
      }
    }
  }
}
