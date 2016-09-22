package org.eclipse.scout.springboot.demo.scout.ui.user;

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
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.springboot.demo.scout.ui.ApplicationContexts;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.AdminField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.FirstNameField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.LastNameField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.PasswordField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.PictureField;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.UserNameField;
import org.eclipse.scout.springboot.demo.spring.service.RoleService;
import org.eclipse.scout.springboot.demo.spring.service.UserService;
import org.springframework.context.ApplicationContext;

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
      User user = getUserService().getUser(getUsernameField().getValue());

      getUsernameField().setEnabled(false);
      getPictureField().setImage(user.getPicture());
      getFirstNameField().setValue(user.getFirstName());
      getLastNameField().setValue(user.getLastName());
      getPasswordField().setValue(user.getPassword());
      // TODO fix line below. lazy loading -> failed to lazily initialize a collection of role
      // getAdminField().setValue(user.getRoles().contains(RoleService.ROOT_ROLE));
      getForm().setSubTitle(calculateSubTitle());

      setEnabledPermission(new UpdateUserPermission());
    }

    @Override
    protected void execStore() {
      User user = getUser();

      user.setPicture(getPictureField().getByteArrayValue());
      user.setFirstName(getFirstNameField().getValue());
      user.setLastName(getLastNameField().getValue());
      user.setPassword(getPasswordField().getValue());

      // TODO fix code below (same lazy loading problem as with UserTablePage)
//      if (getAdminField().getValue()) {
//        user.getRoles().add(RoleService.ROOT_ROLE);
//      }
//      else {
//        user.getRoles().remove(RoleService.ROOT_ROLE);
//      }

      BEANS.get(UserPictureProviderService.class).addUserPicture(user.getName(), user.getPicture());

      getUserService().saveUser(user);
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
      User user = getUser();

      if (user != null) {
        getUsernameField().setValue(null);
        throw new VetoException(TEXTS.get("AccountAlreadyExists", user.getName()));
      }

      String userName = getUsernameField().getValue();
      String firstName = getFirstNameField().getValue();
      String lastName = getLastNameField().getValue();
      String password = getPasswordField().getValue();

      user = new User(userName, firstName, lastName, password);
      user.setPicture(getPictureField().getByteArrayValue());

      if (getAdminField().getValue()) {
        user.getRoles().add(RoleService.ROOT_ROLE);
      }
      else {
        user.getRoles().remove(RoleService.ROOT_ROLE);
      }

      getUserService().addUser(user);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private String calculateSubTitle() {
    return getUsernameField().getValue();
  }

  private User getUser() {
    UserService userService = getUserService();
    String username = getUsernameField().getValue();
    return userService.getUser(username);
  }

  private UserService getUserService() {
    final ApplicationContext applicationContext = ApplicationContexts.current();
    return applicationContext.getBean(UserService.class);
  }

}
