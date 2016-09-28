package org.eclipse.scout.tasks.scout.ui;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktopExtension;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.form.AbstractFormMenu;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.PlatformConfigProperties.ApplicationNameProperty;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.scout.ui.user.OptionsForm;

/**
 * This Spring managed bean represents the web application.
 */
@Bean
public class Desktop extends AbstractDesktop {

  private final ApplicationNameProperty applicationNameConfig;

  @Inject
  public Desktop(final ApplicationNameProperty applicationNameConfig) {
    super(false);
    this.applicationNameConfig = applicationNameConfig;
    callInitializer();
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
    protected void startForm() {
      getForm().startDefault();
    }

    @Override
    protected OptionsForm createForm() {
      return BEANS.get(OptionsForm.class);
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

  public static class DesktopExtension extends AbstractDesktopExtension {

    @Override
    public void contributeOutlines(OrderedCollection<IOutline> outlines) {
      outlines.addAllLast(BEANS.all(IOutline.class));
    }
  }
}
