package org.eclipse.scout.tasks.scout.ui.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.scout.auth.AccessControlService;
import org.eclipse.scout.tasks.scout.auth.PermissionService;
import org.eclipse.scout.tasks.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.tasks.scout.ui.user.RoleForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.user.RoleForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.user.RoleForm.MainBox.RoleBox;
import org.eclipse.scout.tasks.scout.ui.user.RoleForm.MainBox.RoleBox.PermissionTableField;
import org.eclipse.scout.tasks.scout.ui.user.RoleForm.MainBox.RoleBox.PermissionTableField.Table;
import org.eclipse.scout.tasks.scout.ui.user.RoleForm.MainBox.RoleBox.RoleNameField;
import org.eclipse.scout.tasks.spring.service.RoleService;

@Bean
public class RoleForm extends AbstractForm {

  @Inject
  private RoleService roleService;

  @Inject
  private AccessControlService accessControlService;

  private UUID roleId;

  public UUID getRoleId() {
    return roleId;
  }

  public void setRoleId(UUID roleId) {
    this.roleId = roleId;
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return IForm.DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Role");
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

  public RoleNameField getRoleNameField() {
    return getFieldByClass(RoleNameField.class);
  }

  public PermissionTableField getPermissionTableField() {
    return getFieldByClass(PermissionTableField.class);
  }

  public RoleBox getRoleBox() {
    return getFieldByClass(RoleBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class RoleBox extends AbstractGroupBox {

      @Order(1000)
      public class RoleNameField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("RoleName");
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

      @Order(2000)
      public class PermissionTableField extends AbstractTableField<PermissionTableField.Table> {

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

          @Override
          protected void execInitTable() {
            Set<String> pKeys = BEANS.get(PermissionService.class).getPermissionKeys();
            Table table = getTable();

            if (isRoot()) {
              table.getAssignedColumn().setEditable(false);
            }

            for (String pKey : pKeys) {
              ITableRow row = table.addRow();

              table.getNameColumn().setValue(row, TEXTS.getWithFallback(pKey, pKey));
              table.getAssignedColumn().setValue(row, false);
            }
          }

          public AssignedColumn getAssignedColumn() {
            return getColumnSet().getColumnByClass(AssignedColumn.class);
          }

          public NameColumn getNameColumn() {
            return getColumnSet().getColumnByClass(NameColumn.class);
          }

          @Order(1000)
          public class NameColumn extends AbstractStringColumn {
            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Name");
            }

            @Override
            protected int getConfiguredWidth() {
              return 250;
            }

            @Override
            protected boolean getConfiguredPrimaryKey() {
              return true;
            }

            @Override
            protected boolean getConfiguredSortAscending() {
              return true;
            }
          }

          @Order(2000)
          public class AssignedColumn extends AbstractBooleanColumn {
            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Assigned");
            }

            @Override
            protected int getConfiguredWidth() {
              return 100;
            }

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }
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
      setEnabledPermission(new UpdateRolePermission());

      String name = getRoleNameField().getValue();
      Role role = roleService.getRole(name);
      importFormFieldData(role);

      setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      String roleName = getRoleNameField().getValue();
      Role role = roleService.getRole(roleName);
      exportFormFieldData(role);
      accessControlService.clearCacheOfCurrentUser();
      roleService.saveRole(role);
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
      setEnabledPermission(new CreateRolePermission());
    }

    @Override
    protected void execStore() {
      Role role = new Role();
      exportFormFieldData(role);
      roleService.saveRole(role);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private void importFormFieldData(Role role) {
    Set<String> rolePermissions = roleService.getRolePermissions(role.getName());
    Table table = getPermissionTableField().getTable();

    // special case for root role
    if (isRoot()) {
      getRoleNameField().setEnabled(false);
      table.getAssignedColumn().setEditable(false);
    }

    // fill assigned column in permission table field
    for (ITableRow row : table.getRows()) {
      String permission = table.getNameColumn().getValue(row);

      if (isRoot()) {
        table.getAssignedColumn().setValue(row, true);
      }
      else {
        table.getAssignedColumn().setValue(row, rolePermissions.contains(permission));
      }
    }
  }

  private void exportFormFieldData(Role role) {
    role.setName(getRoleNameField().getValue());

    Set<String> permissions = new HashSet<>();
    Table table = getPermissionTableField().getTable();
    for (ITableRow row : table.getRows()) {
      String permission = table.getNameColumn().getValue(row);
      boolean assigned = table.getAssignedColumn().getValue(row);

      if (assigned) {
        permissions.add(permission);
      }
      else {
        permissions.remove(permission);
      }
    }

    role.setPermissions(permissions);
  }

  private boolean isRoot() {
    String name = getRoleNameField().getValue();

    if (name == null) {
      return false;
    }
    else {
      return name.equals(RoleService.ROOT);
    }
  }

  private String calculateSubTitle() {
    return getRoleNameField().getValue();
  }
}
