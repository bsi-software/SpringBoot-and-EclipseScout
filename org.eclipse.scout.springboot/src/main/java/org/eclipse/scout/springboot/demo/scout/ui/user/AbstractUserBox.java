package org.eclipse.scout.springboot.demo.scout.ui.user;

import java.util.List;

import org.eclipse.scout.rt.client.ui.dnd.ResourceListTransferObject;
import org.eclipse.scout.rt.client.ui.dnd.TransferObject;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagefield.AbstractImageField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;

public class AbstractUserBox extends AbstractGroupBox {

  public static final int PICTURE_MAX_FILE_SIZE = 300 * 1024;

  public AdminField getAdminField() {
    return getFieldByClass(AdminField.class);
  }

  @Order(1000)
  public class PictureField extends AbstractImageField {
    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Override
    protected int getConfiguredGridH() {
      return 5;
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

          // if you want to work with buffered images
          // BufferedImage bi = ImageIO.read(new FileInputStream(fileName[0]));
          // setImage(bi);
          setImage(resource.getContent());
          setImageId(resource.getFilename());
        }
      }
    }
  }

  @Order(2000)
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

  @Order(3000)
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

  @Order(4000)
  public class UserNameField extends AbstractStringField {
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

  @Order(5000)
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

  @Order(6000)
  public class AdminField extends AbstractBooleanField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Admin");
    }
  }

}
