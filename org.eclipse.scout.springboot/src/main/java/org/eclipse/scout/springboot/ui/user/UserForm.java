package org.eclipse.scout.springboot.ui.user;

import org.eclipse.scout.rt.client.ui.desktop.bookmark.internal.ManageBookmarksForm.MainBox.UserBox;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.eclipse.scout.springboot.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.AdminField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.FirstNameField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.LastNameField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.PasswordField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.PictureField;
import org.eclipse.scout.springboot.ui.user.AbstractUserBox.UserNameField;

public class UserForm extends AbstractForm {

  @Override
  public Object computeExclusiveKey() {
    return getUsernameField().getValue();
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return IForm.DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("User");
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
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

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class User extends AbstractUserBox {
    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractDirtyFormHandler {

    @Override
    protected void execLoad() {
      User user = BEANS.get(ToDoListModel.class).getUser(getUsernameField().getValue());

      getUsernameField().setEnabled(false);
      getPictureField().setImage(user.picture);
      getFirstNameField().setValue(user.firstName);
      getLastNameField().setValue(user.lastName);
      getPasswordField().setValue(user.password);
      getAdminField().setValue(user.roles.contains(ToDoListModel.ROLE_ADMIN));

      getForm().setSubTitle(calculateSubTitle());

      setEnabledPermission(new UpdateUserPermission());
    }

    @Override
    protected void execStore() {
      User user = BEANS.get(ToDoListModel.class).getUser(getUsernameField().getValue());

      user.picture = getPictureField().getByteArrayValue();
      user.firstName = getFirstNameField().getValue();
      user.lastName = getLastNameField().getValue();
      user.password = getPasswordField().getValue();

      if (getAdminField().getValue()) {
        user.roles.add(ToDoListModel.ROLE_ADMIN);
      }
      else {
        user.roles.remove(ToDoListModel.ROLE_ADMIN);
      }

      BEANS.get(UserPictureProviderService.class).addUserPicture(user.name, user.picture);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected boolean getConfiguredOpenExclusive() {
      return true;
    }
  }

  public class NewHandler extends AbstractDirtyFormHandler {

    @Override
    protected void execLoad() {
      setEnabledPermission(new CreateUserPermission());
    }

    @Override
    protected void execStore() {
      User user = BEANS.get(ToDoListModel.class).getUser(getUsernameField().getValue());

      if (user != null) {
        getUsernameField().setValue(null);
        throw new VetoException(TEXTS.get("AccountAlreadyExists", user.name));
      }

      String userName = getUsernameField().getValue();
      String firstName = getFirstNameField().getValue();
      String lastName = getLastNameField().getValue();
      String password = getPasswordField().getValue();

      user = new User(userName, firstName, lastName, password);
      user.picture = getPictureField().getByteArrayValue();

      if (getAdminField().getValue()) {
        user.roles.add(ToDoListModel.ROLE_ADMIN);
      }
      else {
        user.roles.remove(ToDoListModel.ROLE_ADMIN);
      }

      BEANS.get(ToDoListModel.class).addUser(user);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private String calculateSubTitle() {
    return getUsernameField().getValue();
  }
}
