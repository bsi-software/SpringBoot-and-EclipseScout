package org.eclipse.scout.springboot.demo.scout.ui.user;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.scout.ui.ApplicationContexts;
import org.eclipse.scout.springboot.demo.scout.ui.ClientSession;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.AdminField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.FirstNameField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.LastNameField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.PasswordField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.PictureField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.UserNameField;
import org.eclipse.scout.springboot.demo.scout.ui.user.OptionsForm.MainBox.UserBox;
import org.eclipse.scout.springboot.demo.spring.service.UserService;
import org.springframework.context.ApplicationContext;

public class OptionsForm extends AbstractForm {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Options");
  }

  @Override
  protected void execInitForm() {
    User user = getUser();

    getUsernameField().setValue(user.getName());
    getUsernameField().setEnabled(false);

    getPictureField().setImage(user.getPicture());
    getFirstNameField().setValue(user.getFirstName());
    getLastNameField().setValue(user.getLastName());
    getPasswordField().setValue(user.getPassword());

    getAdminField().setVisible(false);
  }

  protected void storeOptions() {
    User user = getUser();

    user.setPicture(getPictureField().getByteArrayValue());
    user.setFirstName(getFirstNameField().getValue());
    user.setLastName(getLastNameField().getValue());
    user.setPassword(getPasswordField().getValue());

    getUserService().saveUser(user);
  }

  public UserBox getUserBox() {
    return getFieldByClass(UserBox.class);
  }

  PictureField getPictureField() {
    return getFieldByClass(PictureField.class);
  }

  FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  UserNameField getUsernameField() {
    return getFieldByClass(UserNameField.class);
  }

  PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  AdminField getAdminField() {
    return getFieldByClass(AdminField.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class UserBox extends AbstractUserBox {

    }

    @Order(1000)
    public class ApplyButton extends AbstractOkButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ApplyChanges");
      }

      @Override
      protected void execClickAction() {
        storeOptions();
      }
    }
  }

  private User getUser() {
    UserService userService = getUserService();
    String username = ClientSession.get().getUser().getName();
    return userService.getUser(username);
  }

  private UserService getUserService() {
    final ApplicationContext applicationContext = ApplicationContexts.current();
    return applicationContext.getBean(UserService.class);
  }
}
