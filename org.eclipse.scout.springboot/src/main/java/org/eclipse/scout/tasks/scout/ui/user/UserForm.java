package org.eclipse.scout.tasks.scout.ui.user;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.UserRoleBox;
import org.eclipse.scout.tasks.spring.service.RoleService;
import org.eclipse.scout.tasks.spring.service.UserService;

@Bean
public class UserForm extends AbstractForm {

  @Inject
  protected UserService userService;

  @Inject
  protected RoleService roleService;

  @Inject
  protected UserPictureProviderService userPictureService;

  public UUID getUserId() {
    return getUserRoleBox().getUserId();
  }

  public void setUserId(UUID userId) {
    getUserRoleBox().setUserId(userId);
  }

  @Override
  public Object computeExclusiveKey() {
    return getUserId();
  }

  protected String calculateSubTitle() {
    return getUserRoleBox().getUserNameField().getValue();
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

  public UserRoleBox getUserRoleBox() {
    return getFieldByClass(UserRoleBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class UserRoleBox extends AbstractUserRoleBox {
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
      final User user = userService.getUser(getUserId());
      final Map<UUID, String> roles = roleService.getAllRoleNames();
      importFormFieldData(user, roles);

      final BinaryResource picture = userPictureService.getUserPicture(user.getId());
      if (picture != null) {
        importUserPicture(picture.getContent());
      }

      setEnabledPermission(new UpdateUserPermission());
      getUserRoleBox().getUserNameField().setEnabled(false);
      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      final User user = userService.getUser(getUserId());
      exportFormFieldData(user);
      userService.saveUser(user);

      final byte[] picture = exportUserPicture();
      if (picture != null) {
        userPictureService.setUserPicture(user.getId(), picture);
      }
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
      User user = userService.getUser(getUserId());
      if (user != null) {
        throw new VetoException(TEXTS.get("AccountAlreadyExists", user.getName()));
      }

      user = new User();
      exportFormFieldData(user);
      userService.addUser(user);

      final byte[] picture = exportUserPicture();
      if (picture != null) {
        userPictureService.setUserPicture(user.getId(), null);
      }
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  protected void importFormFieldData(User user, Map<UUID, String> roles) {
    getUserRoleBox().importFormFieldData(user, roles);
  }

  protected void importUserPicture(byte[] picture) {
    getUserRoleBox().importUserPicture(picture);
  }

  protected void exportFormFieldData(User user) {
    getUserRoleBox().exportFormFieldData(user);
  }

  protected byte[] exportUserPicture() {
    return getUserRoleBox().exportUserPicture();
  }

}
