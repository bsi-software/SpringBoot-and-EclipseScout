package org.eclipse.scout.tasks.scout.ui.admin.user;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.model.service.UserService;
import org.eclipse.scout.tasks.scout.ui.admin.user.UserTablePage.Table;

@Bean
public class UserTablePage extends AbstractPageWithTable<Table> {

  @Inject
  private UserService userService;

  @Inject
  private UserPictureProviderService userPictureService;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("UserTablePage");
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    Collection<User> users = userService.getAll();
    importTableRowData(users);
  }

  private void importTableRowData(Collection<User> users) {
    Table table = getTable();

    table.deleteAllRows();

    if (users == null || users.size() == 0) {
      return;
    }

    for (User user : users) {
      ITableRow row = table.createRow();
      table.getUserIdColumn().setValue(row, user.getId());
      table.getFirstNameColumn().setValue(row, user.getFirstName());
      table.getLastNameColumn().setValue(row, user.getLastName());
      table.isRootColumn().setValue(row, user.isRoot());
      table.getIsLockedColumn().setValue(row, !user.isEnabled());
      table.addRow(row);
    }
  }

  public class Table extends AbstractTable {

    @Override
    protected void execRowAction(ITableRow row) {
      getMenuByClass(EditMenu.class).execAction();
    }

    public IsLockedColumn getIsLockedColumn() {
      return getColumnSet().getColumnByClass(IsLockedColumn.class);
    }

    public UserIdColumn getUserIdColumn() {
      return getColumnSet().getColumnByClass(UserIdColumn.class);
    }

    public FirstNameColumn getFirstNameColumn() {
      return getColumnSet().getColumnByClass(FirstNameColumn.class);
    }

    public IsRootColumn isRootColumn() {
      return getColumnSet().getColumnByClass(IsRootColumn.class);
    }

    public LastNameColumn getLastNameColumn() {
      return getColumnSet().getColumnByClass(LastNameColumn.class);
    }

    @Order(0)
    public class UserPictureColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredHtmlEnabled() {
        return true;
      }

      @Override
      protected void execDecorateCell(Cell cell, ITableRow row) {
        final String resourceName = getUserIdColumn().getValue(row);
        if (resourceName != null) {
          final BinaryResource value = userPictureService.getBinaryResource(resourceName);

          if (value != null) {
            addAttachment(value);
            cell.setText(
                HTML
                    .imgByBinaryResource(value.getFilename())
                    .cssClass("usericon-html")
                    .toHtml());
          }
        }
      }

      @Override
      protected int getConfiguredWidth() {
        return 50;
      }
    }

    @Order(1000)
    public class UserIdColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("UserName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(2000)
    public class FirstNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("FirstName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(3000)
    public class LastNameColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("LastName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(4000)
    public class IsRootColumn extends AbstractBooleanColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("RootColumn");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(5000)
    public class IsLockedColumn extends AbstractBooleanColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("IsLocked");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(1000)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("New");
      }

      @Override
      protected String getConfiguredIconId() {
        // get unicode from http://fontawesome.io/icon/magic/
        return "font:awesomeIcons \uf0d0";

      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace, TableMenuType.SingleSelection, TableMenuType.MultiSelection);
      }

      @Override
      protected void execAction() {
        UserForm form = BEANS.get(UserForm.class);
        form.addFormListener(new UserFormListener());
        form.startNew();
      }
    }

    @Order(2000)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Edit");
      }

      @Override
      protected String getConfiguredIconId() {
        // get unicode from http://fontawesome.io/icon/pencil/
        return "font:awesomeIcons \uf040";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() {
        UserForm form = BEANS.get(UserForm.class);
        form.addFormListener(new UserFormListener());
        form.setUserId(getTable().getUserIdColumn().getSelectedValue());
        form.startModify();
      }
    }

    protected class UserFormListener implements FormListener {

      @Override
      public void formChanged(FormEvent e) {
        // reload page to reflect new/changed data after saving any changes
        if (FormEvent.TYPE_CLOSED == e.getType() && e.getForm().isFormStored()) {
          reloadPage();
        }
      }
    }
  }
}
