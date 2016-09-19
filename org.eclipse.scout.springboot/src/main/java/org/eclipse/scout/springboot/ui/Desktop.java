package org.eclipse.scout.springboot.ui;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.form.AbstractFormMenu;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.PlatformConfigProperties.ApplicationNameProperty;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.ui.user.OptionsForm;
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
    return Arrays.asList(HomeOutline.class, AdminOutline.class);
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

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F2;
    }
  }

  @Order(2000)
  public class AdminOutlineViewButton extends AbstractOutlineViewButton {

    public AdminOutlineViewButton() {
      this(AdminOutline.class);
    }

    protected AdminOutlineViewButton(Class<? extends AdminOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }

    @Override
    protected DisplayStyle getConfiguredDisplayStyle() {
      return DisplayStyle.TAB;
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F10;
    }

    // TODO is there a display bug? having this method results in hiding the defined icon for this outline
//    @Override
//    protected void execInitAction() {
//      User user = BEANS.get(ToDoListModel.class).loggedInUser();
//      setVisible(user.roles.contains(ToDoListModel.ROLE_ADMIN));
//    }
  }

//  @Order(10)
//  public class QuickAccessMenu extends AbstractMenu {
//
//    @Override
//    protected String getConfiguredText() {
//      return TEXTS.get("QuickAccess");
//    }
//
//    @Order(10)
//    public class LogoutMenu extends AbstractMenu {
//
//      @Override
//      protected String getConfiguredText() {
//        return TEXTS.get("Logout");
//      }
//
//      @Override
//      protected void execAction() {
//        final HttpSession session = IHttpServletRoundtrip.CURRENT_HTTP_SERVLET_REQUEST.get().getSession(false);
//        if (session != null) {
//          session.invalidate();
//        }
//      }
//    }
//  }

  @Order(1000)
  public class OptionsMenu extends AbstractFormMenu<OptionsForm> {

    @Override
    protected String getConfiguredIconId() {
      // get unicode http://fontawesome.io/icon/cog/
      return "font:awesomeIcons \uf013";
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("Options");
    }

    @Override
    protected Class<OptionsForm> getConfiguredForm() {
      return OptionsForm.class;
    }
  }

  @Order(2000)
  public class FileMenu extends AbstractMenu {

    @Override
    protected String getConfiguredIconId() {
      // get unicode http://fontawesome.io/icon/sign-out/
      return "font:awesomeIcons \uf08b";
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("Exit");
    }

    @Override
    protected void execAction() {
      ClientSessionProvider.currentSession(ClientSession.class).stop();
    }
  }

}
