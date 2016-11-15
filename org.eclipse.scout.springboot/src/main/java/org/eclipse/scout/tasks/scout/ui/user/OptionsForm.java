package org.eclipse.scout.tasks.scout.ui.user;

import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.scout.ui.ClientSession;
import org.eclipse.scout.tasks.scout.ui.user.OptionsForm.MainBox.UserBox;
import org.eclipse.scout.tasks.spring.service.UserService;

@Bean
public class OptionsForm extends AbstractForm {

  @Inject
  protected UserService userService;

  @Inject
  protected UserPictureProviderService userPictureService;

  public OptionsForm() {
    super();
    setUserId(ClientSession.get().getUser().getId());
  }

  public UUID getUserId() {
    return getUserBox().getUserId();
  }

  public void setUserId(UUID userId) {
    getUserBox().setUserId(userId);
  }

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

  @Order(10)
  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class UserBox extends AbstractUserBox {

    }

    @Order(20)
    public class ApplyButton extends AbstractOkButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("ApplyChanges");
      }
    }
  }

  public class DefaultHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      final User user = userService.getUser(getUserId());
      importFormFieldData(user);

      final BinaryResource picture = userPictureService.getUserPicture(user.getId());
      if (picture != null) {
        importUserPicture(picture.getContent());
      }

      getUserBox().getUserNameField().setEnabled(false);
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
  }

  protected void importFormFieldData(User user) {
    getUserBox().importFormFieldData(user);
  }

  protected void importUserPicture(byte[] picture) {
    getUserBox().importUserPicture(picture);
  }

  protected void exportFormFieldData(User user) {
    getUserBox().exportFormFieldData(user);
  }

  protected byte[] exportUserPicture() {
    return getUserBox().exportUserPicture();
  }
}
