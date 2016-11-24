package org.eclipse.scout.tasks.scout.ui.user;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.scout.ui.ClientSession;
import org.eclipse.scout.tasks.scout.ui.user.OptionsForm.MainBox.UserBox;
import org.eclipse.scout.tasks.spring.service.DocumentService;
import org.eclipse.scout.tasks.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Bean
public class OptionsForm extends AbstractForm {
  private static final Logger LOG = LoggerFactory.getLogger(OptionsForm.class);

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

  @Override
  public void start() {
    startInternal(new ViewHandler());
  }

  public void reload() {
    LOG.info("reloading user data");
    LOG.info("client session locale: " + ClientSession.get().getLocale().toString());

    final User user = userService.get(getUserId());
    final Document picture = userService.getPicture(getUserId());

    importFormFieldData(user);
    importUserPicture(picture);

    getUserBox().getUserIdField().setEnabled(false);

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

  public class ViewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      reload();
    }

    @Override
    protected void execStore() {
      final User user = userService.get(getUserId());
      exportFormFieldData(user);
      userService.save(user);

      final Document picture = exportUserPicture();
      if (picture != null) {
        userService.setPicture(getUserId(), picture);
      }
    }
  }

  protected void importFormFieldData(User user) {
    getUserBox().importFormFieldData(user);
  }

  protected void exportFormFieldData(User user) {
    getUserBox().exportFormFieldData(user);
  }

  protected void importUserPicture(Document picture) {
    getUserBox().importUserPicture(picture);
  }

  protected Document exportUserPicture() {
    return getUserBox().exportUserPicture();
  }
}
