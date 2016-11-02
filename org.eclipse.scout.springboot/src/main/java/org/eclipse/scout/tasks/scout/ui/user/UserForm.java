package org.eclipse.scout.tasks.scout.ui.user;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.scout.auth.AccessControlService;
import org.eclipse.scout.tasks.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.UserBox;
import org.eclipse.scout.tasks.spring.service.RoleService;
import org.eclipse.scout.tasks.spring.service.UserService;

@Bean
public class UserForm extends AbstractForm {

  @Inject
  private UserService userService;

  @Inject
  private RoleService roleService;

  @Inject
  private AccessControlService accessControlService;

  @Inject
  private UserPictureProviderService userPictureService;

  @Override
  public Object computeExclusiveKey() {
    return getUserBox().getUserNameField().getValue();
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

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class UserBox extends AbstractUserBox {

      @Override
      protected Collection<Role> execFindAllRoles() {
        return roleService.getRoles();
      }

      @Override
      protected Role execFindRole(String name) {
        return roleService.getRole(name);
      }

      @Override
      protected Collection<Role> execFindUserRoles() {
        return userService.getUserRoles(getUserNameField().getValue());
      }
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
      String userName = getUserBox().getUserNameField().getValue();
      User user = userService.getUser(userName);
      importFormFieldData(user);

      setEnabledPermission(new UpdateUserPermission());
      getUserBox().getUserNameField().setEnabled(false);
      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      User user = userService.getUser(getUserBox().getUserNameField().getValue());
      exportFormFieldData(user);

      // TODO should not clear all cache and not cache of logged in user but cache of changed user
      accessControlService.clearCache();
      userPictureService.addUserPicture(user.getName(), user.getPicture());
      userService.saveUser(user);
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
      String userName = getUserBox().getUserNameField().getValue();
      User user = userService.getUser(userName);
      if (user != null) {
        throw new VetoException(TEXTS.get("AccountAlreadyExists", user.getName()));
      }

      user = new User();
      exportFormFieldData(user);
      userService.saveUser(user);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private void importFormFieldData(User user) {
    getUserBox().importFormFieldData(user);
  }

  private void exportFormFieldData(User user) {
    getUserBox().exportFormFieldData(user);
  }

  private String calculateSubTitle() {
    return getUserBox().getUserNameField().getValue();
  }
}
