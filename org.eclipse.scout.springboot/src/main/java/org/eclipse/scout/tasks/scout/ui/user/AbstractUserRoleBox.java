package org.eclipse.scout.tasks.scout.ui.user;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.scout.ui.user.AbstractUserRoleBox.RoleTableField.Table;

public class AbstractUserRoleBox extends AbstractUserBox {

  public RoleTableField getRoleTableField() {
    return getFieldByClass(RoleTableField.class);
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
      public class IdColumn extends AbstractColumn<UUID> {

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

  public void importFormFieldData(User user, Map<UUID, String> roles) {
    importFormFieldData(user);

    final Table table = getRoleTableField().getTable();
    roles.entrySet().stream().forEach(e -> {
      final ITableRow row = table.createRow();
      table.getIdColumn().setValue(row, e.getKey());
      table.getNameColumn().setValue(row, e.getValue());
      if (user.getRoles().contains(e.getKey())) {
        table.getAssignedColumn().setValue(row, true);
      }
      table.addRow(row);
    });
  }

  @Override
  public void exportFormFieldData(User user) {
    super.exportFormFieldData(user);

    final Table table = getRoleTableField().getTable();
    user.getRoles().addAll(
        table.getRows().stream()
            .filter(r -> table.getAssignedColumn().getValue(r))
            .map(r -> table.getIdColumn().getValue(r))
            .collect(Collectors.toSet()));
  }
}
