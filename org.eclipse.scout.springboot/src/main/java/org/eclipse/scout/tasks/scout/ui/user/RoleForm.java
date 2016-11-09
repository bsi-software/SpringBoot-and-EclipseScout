package org.eclipse.scout.tasks.scout.ui.user;

import java.security.Permission;
import java.util.Collection;
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
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;
import org.eclipse.scout.tasks.data.Role;
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

  private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

          public AssignedColumn getAssignedColumn() {
            return getColumnSet().getColumnByClass(AssignedColumn.class);
          }

          public NameColumn getNameColumn() {
            return getColumnSet().getColumnByClass(NameColumn.class);
          }

          public IdColumn getIdColumn() {
            return getColumnSet().getColumnByClass(IdColumn.class);
          }

          @Order(1000)
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

          @Order(2000)
          public class NameColumn extends AbstractStringColumn {
            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Name");
            }

            @Override
            protected int getConfiguredWidth() {
              return 250;
            }
          }

          @Order(3000)
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
      setEnabledPermission(new UpdateUserPermission());

      Role role = roleService.getRole(getId());
      // TODO comment in once lazy loading is fixed
      importFormFieldData(role);

      setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      Role role = roleService.getRole(getId());
      exportFormFieldData(role);

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
      setEnabledPermission(new CreateUserPermission());
    }

    @Override
    protected void execStore() {
      Role role = new Role("");
      exportFormFieldData(role);

      roleService.addRole(role);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private void importFormFieldData(Role role) {
    getRoleNameField().setValue(role.getName());

    final Table table = getPermissionTableField().getTable();
    getPermissions().stream().forEach(permission -> {
      final ITableRow row = table.createRow();

      String pId = permission.getName();
      String pName = TEXTS.getWithFallback(pId, pId);

      table.getIdColumn().setValue(row, pId);
      table.getNameColumn().setValue(row, pName);

      if (role.getPermissions().contains(permission.getName())) {
        table.getAssignedColumn().setValue(row, true);
      }
      table.addRow(row);
    });
  }

  protected Collection<Class<? extends Permission>> getPermissions() {
    return BEANS.get(IPermissionService.class).getAllPermissionClasses();
  }

  private void exportFormFieldData(Role role) {
    role.setName(getRoleNameField().getValue());

    Table table = getPermissionTableField().getTable();
    for (ITableRow row : table.getRows()) {
      String permission = table.getIdColumn().getValue(row);
      boolean assigned = table.getAssignedColumn().getValue(row);

      if (assigned) {
        role.getPermissions().add(permission);
      }
      else {
        role.getPermissions().remove(permission);
      }
    }
  }

  private String calculateSubTitle() {
    return getRoleNameField().getValue();
  }
}
