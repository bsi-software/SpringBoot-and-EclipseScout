package org.eclipse.scout.tasks.scout.ui.admin.role;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.service.RoleService;
import org.eclipse.scout.tasks.scout.ui.FontAwesomeIcons;
import org.eclipse.scout.tasks.scout.ui.admin.role.RoleTablePage.Table;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm;

@Bean
public class RoleTablePage extends AbstractPageWithTable<Table> {

  @Inject
  private RoleService roleService;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("RoleTablePage");
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    Collection<Role> roles = roleService.getAll();
    importTableRowData(roles);
  }

  private void importTableRowData(Collection<Role> roles) {
    Table table = getTable();

    table.deleteAllRows();

    if (roles == null || roles.size() == 0) {
      return;
    }

    for (Role role : roles) {
      ITableRow row = table.createRow();
      String roleId = role.getId();
      table.getIdColumn().setValue(row, roleId);
      table.getNameColumn().setValue(row, TEXTS.getWithFallback(roleId, roleId));
      table.addRow(row);
    }
  }

  public class Table extends AbstractTable {

    @Override
    protected void execRowAction(ITableRow row) {
      getMenuByClass(EditMenu.class).execAction();
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public IdColumn getIdColumn() {
      return getColumnSet().getColumnByClass(IdColumn.class);
    }

    @Order(10)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("New");
      }

      @Override
      protected String getConfiguredIconId() {
        return FontAwesomeIcons.fa_magic;
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "alt-n";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace, TableMenuType.SingleSelection, TableMenuType.MultiSelection);
      }

      @Override
      protected void execAction() {
        RoleForm form = BEANS.get(RoleForm.class);
        form.addFormListener(new RoleFormListener());
        form.startNew();
      }
    }

    @Order(20)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Edit");
      }

      @Override
      protected String getConfiguredIconId() {
        return FontAwesomeIcons.fa_pencil;
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "alt-e";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        String roleId = getIdColumn().getSelectedValue();

        RoleForm form = BEANS.get(RoleForm.class);
        form.addFormListener(new RoleFormListener());
        form.setRoleId(roleId);
        form.startModify();
      }
    }

    @Order(30)
    public class TranslateMenu extends AbstractMenu {
      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Translate");
      }

      @Override
      protected String getConfiguredIconId() {
        return FontAwesomeIcons.fa_language;
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "alt-t";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        String roleId = getIdColumn().getSelectedValue();

        TranslationForm form = BEANS.get(TranslationForm.class);
        form.setKey(roleId);
        form.startModify();
        form.waitFor();

        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    private class RoleFormListener implements FormListener {

      @Override
      public void formChanged(FormEvent e) {
        // reload page to reflect new/changed data after saving any changes
        if (FormEvent.TYPE_CLOSED == e.getType() && e.getForm().isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(10)
    public class IdColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("RoleName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(20)
    public class NameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("RoleText");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

  }
}
