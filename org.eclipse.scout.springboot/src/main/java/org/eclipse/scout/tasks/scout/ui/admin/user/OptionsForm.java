package org.eclipse.scout.tasks.scout.ui.admin.user;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.AbstractFormField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.model.Document;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.model.service.DocumentService;
import org.eclipse.scout.tasks.model.service.UserService;
import org.eclipse.scout.tasks.scout.auth.PasswordUtility;
import org.eclipse.scout.tasks.scout.ui.ClientSession;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ApplyButton;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.UserBox;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox.CancelPasswordChangeLink;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox.ChangePasswordLink;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox.ConfirmPasswordField;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox.NewPasswordField;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox.OldPasswordField;
import org.eclipse.scout.tasks.scout.ui.admin.user.OptionsForm.MainBox.ChangePasswordBox.UpdateLinkButton;

@Bean
public class OptionsForm extends AbstractForm {

  @Inject
  protected UserService userService;

  @Inject
  protected DocumentService documentService;

  public OptionsForm() {
    super();
    setUserId(ClientSession.get().getUserId());
  }

  public String getUserId() {
    return getUserBox().getUserIdField().getValue();
  }

  public void setUserId(String userId) {
    getUserBox().getUserIdField().setValue(userId);
  }

  public UserBox getUserBox() {
    return getFieldByClass(UserBox.class);
  }

  public OldPasswordField getOldPasswordField() {
    return getFieldByClass(OldPasswordField.class);
  }

  public CancelPasswordChangeLink getCancelPasswordChangeField() {
    return getFieldByClass(CancelPasswordChangeLink.class);
  }

  public UpdateLinkButton getVerifyLinkButton() {
    return getFieldByClass(UpdateLinkButton.class);
  }

  public NewPasswordField getNewPasswordField() {
    return getFieldByClass(NewPasswordField.class);
  }

  public ConfirmPasswordField getConfirmPasswordField() {
    return getFieldByClass(ConfirmPasswordField.class);
  }

  public ChangePasswordLink getChangePasswordLink() {
    return getFieldByClass(ChangePasswordLink.class);
  }

  public ChangePasswordBox getChangePasswordBox() {
    return getFieldByClass(ChangePasswordBox.class);
  }

  public ApplyButton getApplyButton() {
    return getFieldByClass(ApplyButton.class);
  }

  @Override
  public void start() {
    startInternal(new ViewHandler());
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Options");
  }

  @Order(10)
  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class UserBox extends AbstractUserBox {

    }

    @Order(20)
    public class ChangePasswordBox extends AbstractSequenceBox {

      boolean changeIt = false;

      public boolean getValue() {
        return changeIt;
      }

      public void setValue(boolean changeIt) {
        this.changeIt = changeIt;

        getApplyButton().setEnabled(!changeIt);
        getChangePasswordLink().setVisible(!changeIt);
        getOldPasswordField().setVisible(changeIt);
        getNewPasswordField().setVisible(changeIt);
        getConfirmPasswordField().setVisible(changeIt);
        getVerifyLinkButton().setVisible(changeIt);
        getCancelPasswordChangeField().setVisible(changeIt);

        resetPasswordFieldValues();
        clearPasswordErrorStates();
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Password");
      }

      @Override
      protected boolean getConfiguredAutoCheckFromTo() {
        return false;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected void execInitField() {
        setValue(false);
      }

      @Order(0)
      public class ChangePasswordLink extends AbstractLinkButton {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ChangePassword");
        }

        @Override
        protected void execClickAction() {
          getApplyButton().setEnabled(false);
          setValue(true);
        }
      }

      @Order(10)
      public class OldPasswordField extends AbstractPasswordField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("OldPassword");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return AbstractFormField.LABEL_POSITION_ON_FIELD;
        }
      }

      @Order(20)
      public class NewPasswordField extends AbstractPasswordField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("NewPassword");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return AbstractFormField.LABEL_POSITION_ON_FIELD;
        }
      }

      @Order(30)
      public class ConfirmPasswordField extends AbstractPasswordField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ConfirmPassword");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return AbstractFormField.LABEL_POSITION_ON_FIELD;
        }
      }

      @Order(40)
      public class UpdateLinkButton extends AbstractLinkButton {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Update");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ClickToEnableApplyButton");
        }

        @Override
        protected void execClickAction() {
          if (validatePasswordChange()) {
            getApplyButton().setEnabled(true);
            getOldPasswordField().setEnabled(false);
            getNewPasswordField().setEnabled(false);
            getConfirmPasswordField().setEnabled(false);
          }
        }
      }

      @Order(50)
      public class CancelPasswordChangeLink extends AbstractLinkButton {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("CancelPasswordChange");
        }

        @Override
        protected void execClickAction() {
          setValue(false);
          getApplyButton().setEnabled(true);
          getOldPasswordField().setEnabled(true);
          getNewPasswordField().setEnabled(true);
          getConfirmPasswordField().setEnabled(true);
        }
      }
    }

    public class DummyField extends AbstractPlaceholderField {
//      @Override
//      protected String getConfiguredCssClass() {
//        return "options-form-bottom-placeholder";
//      }

      @Override
      protected int getConfiguredHeightInPixel() {
        return 6;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }
    }

    @Order(40)
    public class ApplyButton extends AbstractOkButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ApplyChanges");
      }

      @Override
      protected void execClickAction() {
        if (getChangePasswordBox().getValue()) {
          if (!validatePasswordChange()) {
            return;
          }
        }

        super.execClickAction();
      }
    }

    private boolean validatePasswordChange() {
      clearPasswordErrorStates();

      if (!getChangePasswordBox().getValue()) {
        return true;
      }

      boolean ok = true;
      ok &= validateOldPassword();
      ok &= validateNewPassword();
      ok &= validateConfirmPassword();

      return ok;
    }

    private void resetPasswordFieldValues() {
      getOldPasswordField().setValue(null);
      getNewPasswordField().setValue(null);
      getConfirmPasswordField().setValue(null);
    }

    private void clearPasswordErrorStates() {
      getOldPasswordField().clearErrorStatus();
      getNewPasswordField().clearErrorStatus();
      getConfirmPasswordField().clearErrorStatus();
    }

    private boolean validateOldPassword() {
      User user = userService.get(getUserId());
      String password = getOldPasswordField().getValue();

      if (!PasswordUtility.passwordIsValid(password, user.getPasswordHash(), user.getPasswordSalt())) {
        getOldPasswordField().setError(TEXTS.get("PasswordInvalid"));
        return false;
      }

      return true;
    }

    private boolean validateNewPassword() {
      return getNewPasswordField().validateField();
    }

    private boolean validateConfirmPassword() {
      if (!getConfirmPasswordField().validateField()) {
        return false;
      }

      String passwordConfirm = getConfirmPasswordField().getValue();
      String passwordNew = getNewPasswordField().getValue();

      if (!passwordConfirm.equals(passwordNew)) {
        getConfirmPasswordField().setError(TEXTS.get("PasswordMismatchError"));
        return false;
      }

      return true;
    }
  }

  public class ViewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      reload();
    }

    @Override
    protected void execStore() {
      String userId = getUserId();
      User user = userService.get(userId);
      UserBox box = getUserBox();

      // handle basic fields
      box.exportFormFieldData(user);

      // handle user picture
      Document picture = box.exportUserPicture();
      if (picture != null) {
        user.setPictureId(picture.getId());
        userService.setPicture(userId, picture);
      }

      //  handle user password
      if (getChangePasswordBox().getValue()) {
        String passwordNew = getNewPasswordField().getValue();
        String passwordSalt = user.getPasswordSalt();
        String passwordHash = PasswordUtility.calculatePasswordHash(passwordNew, passwordSalt);

        user.setPasswordHash(passwordHash);
      }

      userService.save(user);
    }
  }

  /**
   * Reload needs to be public as it is triggered from outside while the form is still open (but invisible).
   */
  public void reload() {
    String userId = getUserId();
    User user = userService.get(userId);
    Document picture = userService.getPicture(userId);

    UserBox box = getUserBox();
    box.importFormFieldData(user);
    box.importUserPicture(picture);
    box.getUserIdField().setEnabled(false);
  }
}
