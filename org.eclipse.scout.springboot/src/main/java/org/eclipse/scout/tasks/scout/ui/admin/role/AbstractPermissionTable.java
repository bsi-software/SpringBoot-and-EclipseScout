package org.eclipse.scout.tasks.scout.ui.admin.role;

import java.security.Permission;
import java.util.Collection;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;
import org.eclipse.scout.tasks.scout.ui.admin.text.AbstractTranslateMenu;

public abstract class AbstractPermissionTable extends AbstractTable {

  protected abstract void execReloadPage();

  public IdColumn getIdColumn() {
    return getColumnSet().getColumnByClass(IdColumn.class);
  }

  public GroupColumn getGroupColumn() {
    return getColumnSet().getColumnByClass(GroupColumn.class);
  }

  public TextColumn getTextColumn() {
    return getColumnSet().getColumnByClass(TextColumn.class);
  }

  public AssignedColumn getAssignedColumn() {
    return getColumnSet().getColumnByClass(AssignedColumn.class);
  }

  public String getGroupId(String permissionId) {
    return permissionId.substring(0, permissionId.lastIndexOf("."));
  }

  @Order(10)
  public class TranslateMenu extends AbstractTranslateMenu {

    @Override
    protected String getTextKey() {
      return getIdColumn().getSelectedValue();
    }

    @Override
    protected void reload() {
      execReloadPage();
    }
  }

  @Order(20)
  public class TranslateGroupMenu extends AbstractTranslateMenu {

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("TranslateGroup");
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return "alt-g";
    }

    @Override
    protected String getTextKey() {
      return getGroupId(getIdColumn().getSelectedValue());
    }

    @Override
    protected void reload() {
      execReloadPage();
    }
  }

  @Order(1000)
  public class IdColumn extends AbstractStringColumn {

    @Override
    protected boolean getConfiguredPrimaryKey() {
      return true;
    }

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(2000)
  public class GroupColumn extends AbstractStringColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("PermissionGroup");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(3000)
  public class TextColumn extends AbstractStringColumn {
    @Override
    protected String getConfiguredHeaderText() {
      return TEXTS.get("Permission");
    }

    @Override
    protected int getConfiguredWidth() {
      return 100;
    }
  }

  @Order(4000)
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

  protected Collection<Class<? extends Permission>> getPermissions() {
    return BEANS.get(IPermissionService.class).getAllPermissionClasses();
  }

  protected void loadData(SearchFilter filter) {
    deleteAllRows();
    getPermissions()
        .stream()
        .forEach(permission -> {
          ITableRow row = createRow();
          String id = permission.getName();
          String groupId = getGroupId(id);
          String group = TEXTS.getWithFallback(groupId, groupId);
          String name = TEXTS.getWithFallback(id, id);

          getIdColumn().setValue(row, id);
          getGroupColumn().setValue(row, group);
          getTextColumn().setValue(row, name);

          addRow(row);
        });
  }
}
