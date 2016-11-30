package org.eclipse.scout.tasks.scout.ui.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.scout.auth.PasswordUtility;
import org.eclipse.scout.tasks.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.PasswordField;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.RoleTableField;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.RoleTableField.Table;
import org.eclipse.scout.tasks.scout.ui.user.UserForm.MainBox.UserBox;
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
    return getUserBox().getUserIdField().getValue();
  }

  public void setUserId(String userId) {
    getUserBox().getUserIdField().setValue(userId);
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

  public UserBox getUserBox() {
    return getFieldByClass(UserBox.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  private RoleTableField getRoleTableField() {
    return getFieldByClass(RoleTableField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10)
  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class UserBox extends AbstractUserBox {
    }

    @Order(20)
    public class PasswordField extends AbstractPasswordField {

      @Override
      protected void execChangedValue() {
        validateField();
      }
    }

    @Order(30)
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

        public IdColumn getIdColumn() {
          return getColumnSet().getColumnByClass(IdColumn.class);
        }

        @Order(10)
        public class IdColumn extends AbstractStringColumn {

          @Override
          protected boolean getConfiguredPrimaryKey() {
            return true;
          }

          @Override
          protected String getConfiguredHeaderText() {
            return TEXTS.get("Name");
          }

          @Override
          protected int getConfiguredWidth() {
            return 200;
          }
        }

        @Order(20)
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

      User user = userService.get(getUserId());
      Document picture = userService.getPicture(getUserId());

      UserBox box = getUserBox();
      box.getUserIdField().setEnabled(false);
      box.importFormFieldData(user);
      box.importUserPicture(picture);
      importUserRoles(user);

      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      User user = userService.get(getUserId());
      store(user);
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

      getPasswordField().setMandatory(true);
      importUserRoles(null);
    }

    @Override
    protected void execStore() {
      String userId = getUserId();
      if (userService.exists(userId)) {
        throw new VetoException(TEXTS.get("AccountAlreadyExists", userId));
      }

      store(new User());
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private void store(User user) {
    boolean newUser = (user.getId() == null);
    String userId = getUserId();
    UserBox box = getUserBox();
    box.exportFormFieldData(user);

    // handle password
    String password = getPasswordField().getValue();
    String salt, hash;

    if (newUser) {
      salt = PasswordUtility.generatePasswordSalt();
      hash = PasswordUtility.calculatePasswordHash(password, salt);
      user.setPasswordSalt(salt);
      user.setPasswordHash(hash);
    }
    else if (password != null) {
      salt = user.getPasswordSalt();
      hash = PasswordUtility.calculatePasswordHash(password, salt);
      user.setPasswordHash(hash);
    }

    // handle user roles
    exportUserRoles(user);

    userService.save(user);

    // handle user picture
    Document picture = box.exportUserPicture();
    if (picture != null) {
      userService.setPicture(userId, picture);
    }
  }

  private void importUserRoles(User user) {
    Table table = getRoleTableField().getTable();
    List<String> roles = roleService.getAll().stream()
        .map(r -> r.getId())
        .collect(Collectors.toList());

    roles.stream()
        .forEach(e -> {
          ITableRow row = table.createRow();
          table.getIdColumn().setValue(row, e);
          if (user != null) {
            if (user.getRoles().contains(e)) {
              table.getAssignedColumn().setValue(row, true);
            }
          }
          table.addRow(row);
        });
  }

  private void exportUserRoles(User user) {
    Table table = getRoleTableField().getTable();
    Set<String> roles = new HashSet<>();

    table.getRows().stream()
        .forEach(r -> {
          if (table.getAssignedColumn().getValue(r)) {
            roles.add(table.getIdColumn().getValue(r));
          }
        });

    user.setRoles(roles);
  }
}
