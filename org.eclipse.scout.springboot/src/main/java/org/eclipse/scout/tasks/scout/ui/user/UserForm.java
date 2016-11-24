package org.eclipse.scout.tasks.scout.ui.user;

import java.util.List;
import java.util.stream.Collectors;

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
import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.UserRoleBox;
import org.eclipse.scout.tasks.spring.service.DocumentService;
import org.eclipse.scout.tasks.spring.service.RoleService;
import org.eclipse.scout.tasks.spring.service.UserService;

@Bean
public class UserForm extends AbstractForm {

  @Inject
  protected UserService userService;

  @Inject
  protected RoleService roleService;

  @Inject
  protected DocumentService documentService;

  public String getUserId() {
    return getUserRoleBox().getUserIdField().getValue();
  }

  public void setUserId(String userId) {
    getUserRoleBox().getUserIdField().setValue(userId);
  }

  @Override
  public Object computeExclusiveKey() {
    return getUserId();
  }

  protected String calculateSubTitle() {
    return getUserId();
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
      setEnabledPermission(new UpdateUserPermission());

      final User user = userService.get(getUserId());
      final List<String> roles = roleService.getAll().stream()
          .map(r -> r.getId())
          .collect(Collectors.toList());
      final Document picture = userService.getPicture(getUserId());

      importFormFieldData(user, roles);
      importUserPicture(picture);

      getUserRoleBox().getUserIdField().setEnabled(false);
      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      store(userService.get(getUserId()));
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

      final List<String> roles = roleService.getAll().stream()
          .map(r -> r.getId())
          .collect(Collectors.toList());

      importFormFieldData(null, roles);
    }

    @Override
    protected void execStore() {
      if (userService.exists(getUserId())) {
        throw new VetoException(TEXTS.get("AccountAlreadyExists", getUserId()));
      }

      store(new User());
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  protected void store(User user) {
    exportFormFieldData(user);
    userService.save(user);

    final Document picture = exportUserPicture();
    if (picture != null) {
      userService.setPicture(user.getId(), picture);
    }
  }

  protected void importFormFieldData(User user, List<String> roles) {
    getUserRoleBox().importFormFieldData(user, roles);
  }

  protected void exportFormFieldData(User user) {
    getUserRoleBox().exportFormFieldData(user);
  }

  protected void importUserPicture(Document picture) {
    getUserRoleBox().importUserPicture(picture);
  }

  protected Document exportUserPicture() {
    return getUserRoleBox().exportUserPicture();
  }

}
