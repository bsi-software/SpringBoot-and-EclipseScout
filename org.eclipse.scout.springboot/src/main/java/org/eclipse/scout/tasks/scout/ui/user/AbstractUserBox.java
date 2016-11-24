package org.eclipse.scout.tasks.scout.ui.user;

import java.util.List;

import org.eclipse.scout.rt.client.ui.dnd.ResourceListTransferObject;
import org.eclipse.scout.rt.client.ui.dnd.TransferObject;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagefield.AbstractImageField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.data.User;

public abstract class AbstractUserBox extends AbstractGroupBox {

  public static final int PICTURE_MAX_FILE_SIZE = 300 * 1024;

  public PictureField getPictureField() {
    return getFieldByClass(PictureField.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  public LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  public UserIdField getUserIdField() {
    return getFieldByClass(UserIdField.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  @Order(10)
  public class PictureField extends AbstractImageField {

    private Document picture;

    public Document getPicture() {
      return picture;
    }

    public void setPicture(Document picture) {
      this.picture = picture;

      if (picture != null) {
        setImage(picture.getData());
        setImageId(picture.getName());
      }
      else {
        setImage(new byte[]{});
        setImageId("");
      }

      touch();
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("DropImageFile");
    }

    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Override
    protected int getConfiguredGridH() {
      return 4;
    }

    @Override
    protected boolean getConfiguredAutoFit() {
      return true;
    }

    @Override
    protected int getConfiguredDropType() {
      return TYPE_FILE_TRANSFER;
    }

    @Override
    protected void execDropRequest(TransferObject transferObject) {
      clearErrorStatus();

      if (transferObject instanceof ResourceListTransferObject) {
        List<BinaryResource> resources = ((ResourceListTransferObject) transferObject).getResources();

        if (resources.size() > 0) {
          BinaryResource resource = CollectionUtility.firstElement(resources);
          int resource_size = resource.getContentLength();

          if (resource_size > PICTURE_MAX_FILE_SIZE) {
            throw new VetoException(TEXTS.get("ImageFileTooLarge", "" + (PICTURE_MAX_FILE_SIZE / 1024)));
          }

          setPicture(new Document(resource.getFilename(), resource.getContent(), Document.TYPE_PICTURE));
        }
      }
    }
  }

  @Order(20)
  public class FirstNameField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("FirstName");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 128;
    }
  }

  @Order(30)
  public class LastNameField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("LastName");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 128;
    }
  }

  @Order(40)
  public class UserIdField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("UserName");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 128;
    }
  }

  @Order(50)
  public class PasswordField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Password");
    }

    @Override
    protected boolean getConfiguredMandatory() {
      return true;
    }

    @Override
    protected boolean getConfiguredInputMasked() {
      return true;
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 128;
    }
  }

  public void importFormFieldData(User user) {
    if (user == null) {
      return;
    }

    getUserIdField().parseAndSetValue(user.getId());
    getFirstNameField().parseAndSetValue(user.getFirstName());
    getLastNameField().parseAndSetValue(user.getLastName());
    getPasswordField().parseAndSetValue(user.getPassword());
  }

  public void exportFormFieldData(User user) {
    user.setId(getUserIdField().getValue());
    user.setFirstName(getFirstNameField().getValue());
    user.setLastName(getLastNameField().getValue());
    user.setPassword(getPasswordField().getValue());
  }

  public void importUserPicture(Document picture) {
    getPictureField().setPicture(picture);
  }

  public Document exportUserPicture() {
    if (getPictureField().isSaveNeeded()) {
      return getPictureField().getPicture();
    }

    return null;
  }
}
