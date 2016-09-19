package org.eclipse.scout.springboot.ui.user;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.AdminField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.FirstNameField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.LastNameField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.PasswordField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.PictureField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.UserNameField;
import org.eclipse.scout.springboot.ui.user.OptionsForm.MainBox.UserBox;

public class OptionsForm extends AbstractForm {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Options");
  }

  @Override
  protected void execInitForm() {
    User user = BEANS.get(ToDoListModel.class).loggedInUser();

    getUsernameField().setValue(user.name);
    getUsernameField().setEnabled(false);

    getPictureField().setImage(user.picture);
    getFirstNameField().setValue(user.firstName);
    getLastNameField().setValue(user.lastName);
    getPasswordField().setValue(user.password);

    getAdminField().setVisible(false);
  }

  protected void storeOptions() {
    User user = BEANS.get(ToDoListModel.class).loggedInUser();

    user.picture = getPictureField().getByteArrayValue();
    user.firstName = getFirstNameField().getValue();
    user.lastName = getLastNameField().getValue();
    user.password = getPasswordField().getValue();
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
}
