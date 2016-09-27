package org.eclipse.scout.springboot.demo.scout.ui.user;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Role;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.scout.ui.ClientSession;
import org.eclipse.scout.springboot.demo.scout.ui.user.OptionsForm.MainBox.UserBox;
import org.eclipse.scout.springboot.demo.spring.service.RoleService;
import org.eclipse.scout.springboot.demo.spring.service.UserService;

@Bean
public class OptionsForm extends AbstractForm {

  @Inject
  private UserService userService;

  @Inject
  private RoleService roleService;

  public UserBox getUserBox() {
    return getFieldByClass(UserBox.class);
  }

  public void startDefault() {
    startInternal(new DefaultHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Options");
  }

  @Override
  protected void execInitForm() {
    getUserBox().getRoleTableField().setVisible(false);
  }

  @Order(10)
  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class UserBox extends AbstractUserBox {

      @Override
      protected Collection<Role> execFindRoles() {
        return roleService.getRoles();
      }
    }

    @Order(20)
    public class ApplyButton extends AbstractOkButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ApplyChanges");
      }

      @Override
      protected void execClickAction() {
        String username = ClientSession.get().getUser().getName();
        User user = userService.getUser(username);

        getUserBox().exportFormFieldData(user);
        userService.saveUser(user);
      }
    }
  }

  public class DefaultHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      String username = ClientSession.get().getUser().getName();
      User user = userService.getUser(username);

      getUserBox().importFormFieldData(user);
      getUserBox().getUserNameField().setEnabled(false);
    }
  }
}
