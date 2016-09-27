package org.eclipse.scout.springboot.demo.scout.ui.user;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.dnd.ResourceListTransferObject;
import org.eclipse.scout.rt.client.ui.dnd.TransferObject;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagefield.AbstractImageField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.springboot.demo.model.Role;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.scout.ui.user.AbstractUserBox.RoleTableField.Table;

public abstract class AbstractUserBox extends AbstractGroupBox {

  public static final int PICTURE_MAX_FILE_SIZE = 300 * 1024;

  protected abstract Collection<Role> execFindRoles();

  public PictureField getPictureField() {
    return getFieldByClass(PictureField.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  public LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  public UserNameField getUserNameField() {
    return getFieldByClass(UserNameField.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  public RoleTableField getRoleTableField() {
    return getFieldByClass(RoleTableField.class);
  }

  @Order(10)
  public class PictureField extends AbstractImageField {
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

          // if you want to work with buffered images
          // BufferedImage bi = ImageIO.read(new FileInputStream(fileName[0]));
          // setImage(bi);
          setImage(resource.getContent());
          setImageId(resource.getFilename());
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

  @Order(70)
  public class RoleTableField extends AbstractTableField<RoleTableField.Table> {

    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    protected int getConfiguredGridH() {
      return 4;
    }

    public class Table extends AbstractTable {

      public AssignedColumn getAssignedColumn() {
        return getColumnSet().getColumnByClass(AssignedColumn.class);
      }

      public NameColumn getNameColumn() {
        return getColumnSet().getColumnByClass(NameColumn.class);
      }

      public IdColumn getIdColumn() {
        return getColumnSet().getColumnByClass(IdColumn.class);
      }

      @Order(10)
      public class IdColumn extends AbstractStringColumn {
        @Override
        protected boolean getConfiguredDisplayable() {
          return false;
        }

        @Override
        protected boolean getConfiguredPrimaryKey() {
          return true;
        }
      }

      @Order(20)
      public class NameColumn extends AbstractStringColumn {
        @Override
        protected String getConfiguredHeaderText() {
          return TEXTS.get("Name");
        }

        @Override
        protected int getConfiguredWidth() {
          return 200;
        }
      }

      @Order(30)
      public class AssignedColumn extends AbstractBooleanColumn {
        @Override
        protected String getConfiguredHeaderText() {
          return TEXTS.get("Assigned");
        }

        @Override
        protected int getConfiguredWidth() {
          return 50;
        }

        @Override
        protected boolean getConfiguredEditable() {
          return true;
        }
      }
    }
  }

  public void importFormFieldData(User user) {
    getPictureField().setImage(user.getPicture());
    getFirstNameField().setValue(user.getFirstName());
    getLastNameField().setValue(user.getLastName());
    getUserNameField().parseAndSetValue(user.getName());
    getPasswordField().setValue(user.getPassword());

//    Set<Role> userRoles = user.getRoles();
    Set<Role> userRoles = Collections.emptySet(); // TODO mzi

    for (Role role : execFindRoles()) {
      String rId = role.getName();
      String rName = TEXTS.getWithFallback(rId, rId);

      Table table = getRoleTableField().getTable();
      ITableRow row = table.addRow();

      table.getIdColumn().setValue(row, rId);
      table.getNameColumn().setValue(row, rName);
      table.getAssignedColumn().setValue(row, userRoles.contains(rId));
    }
  }

  public void exportFormFieldData(User user) {
    user.setName(getUserNameField().getValue())
        .setFirstName(getFirstNameField().getValue())
        .setLastName(getLastNameField().getValue())
        .setPassword(getPasswordField().getValue())
        .setPicture(getPictureField().getByteArrayValue());

    // TODO mzi export user roles
  }
}
