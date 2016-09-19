package org.eclipse.scout.springboot.ui.task;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIconColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.springboot.entity.Task;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.eclipse.scout.springboot.ui.task.AbstractTaskTablePage.Table;

public class AbstractTaskTablePage extends AbstractPageWithTable<Table> {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaskTablePage");
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    Collection<Task> tasks = getTasks();
    Table table = getTable();

    table.deleteAllRows();

    if (tasks == null || tasks.size() == 0) {
      return;
    }

    for (Task task : tasks) {
      ITableRow row = table.createRow();

      table.getIdColumn().setValue(row, task.id);
      // TODO doesn't work: html has <img> tag -> put image as static resource somewhere?
//			table.getIconColumn().setValue(row, task.creator.name);
      table.getIconColumn().setValue(row, AbstractIcons.Star);
      table.getDueInColumn().setValue(row, getDueInValue(task.dueDate));
      table.getDueDateColumn().setValue(row, task.dueDate);
      table.getTitleColumn().setValue(row, task.name);
      table.getCreatorColumn().setValue(row, task.creator.toString());
      table.getResponsibleColumn().setValue(row, task.responsible.toString());
      table.getReminderColumn().setValue(row, task.reminder);
      table.getAcceptedColumn().setValue(row, task.accepted);
      table.getDoneColumn().setValue(row, task.done);

      table.addRow(row);
    }
  }

  private String getDueInValue(Date date) {
    Date today = new Date();
    int days = DateUtility.getDaysBetween(new Date(), date);

    if (today.after(date)) {
      days = -days;
    }

    if (days < 0) {
      if (days == -1) {
        return "Yesterday";
      }
      else if (days > -7) {
        return "Last" + weekdayToString(DateUtility.getWeekday(date));
      }
      else {
        return "" + (-days) + " days ago";
      }
    }

    if (days > 0) {
      if (days == 1) {
        return "Tomorrow";
      }
      else {
        return "In " + days + " days";
      }
    }

    return "Today";
  }

  private String weekdayToString(int day) {
    switch (day) {
      case 1:
        return TEXTS.get("Sunday");
    }

    return "Anderer Tag";
  }

  /**
   * callback method to specify a collection of tasks to be displayed by this table page.
   *
   * @return
   */
  protected Collection<Task> getTasks() {
    return null;
  }

  public class Table extends AbstractTable {

    @Override
    protected void execRowAction(ITableRow row) {
      getMenuByClass(EditMenu.class).execAction();
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
        TaskForm form = new TaskForm();
        form.addFormListener(new TaskFormListener());
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
        String taskId = (String) getSelectedRow().getKeyValues().get(0);

        TaskForm form = new TaskForm();
        form.addFormListener(new TaskFormListener());
        form.setTaskId(taskId);
        form.startModify();
      }
    }

    @Order(3000)
    public class AcceptMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Accept");
      }

      @Override
      protected String getConfiguredIconId() {
        // get unicode from http://fontawesome.io/icon/check/
        return "font:awesomeIcons \uf00c";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
      }

      @Override
      protected void execAction() {
        boolean listHasChanged = false;

        for (ITableRow row : getSelectedRows()) {
          String taskId = (String) row.getKeyValues().get(0);
          User user = BEANS.get(ToDoListModel.class).loggedInUser();
          Task task = BEANS.get(ToDoListModel.class).getTask(taskId);

          if (task != null && task.responsible.equals(user)) {
            task.accepted = true;
            listHasChanged = true;
          }
        }

        if (listHasChanged) {
          reloadPage();
        }
      }
    }

    private class TaskFormListener implements FormListener {

      @Override
      public void formChanged(FormEvent e) {
        // reload page to reflect new/changed data after saving any changes
        if (FormEvent.TYPE_CLOSED == e.getType() && e.getForm().isFormStored()) {
          reloadPage();
        }
      }
    }

    public DueDateColumn getDueDateColumn() {
      return getColumnSet().getColumnByClass(DueDateColumn.class);
    }

    public DoneColumn getDoneColumn() {
      return getColumnSet().getColumnByClass(DoneColumn.class);
    }

    public AcceptedColumn getAcceptedColumn() {
      return getColumnSet().getColumnByClass(AcceptedColumn.class);
    }

    public CreatorColumn getCreatorColumn() {
      return getColumnSet().getColumnByClass(CreatorColumn.class);
    }

    public IconColumn getIconColumn() {
      return getColumnSet().getColumnByClass(IconColumn.class);
    }

    public DueInColumn getDueInColumn() {
      return getColumnSet().getColumnByClass(DueInColumn.class);
    }

    public ReminderColumn getReminderColumn() {
      return getColumnSet().getColumnByClass(ReminderColumn.class);
    }

    public ResponsibleColumn getResponsibleColumn() {
      return getColumnSet().getColumnByClass(ResponsibleColumn.class);
    }

    public TitleColumn getTitleColumn() {
      return getColumnSet().getColumnByClass(TitleColumn.class);
    }

    public IdColumn getIdColumn() {
      return getColumnSet().getColumnByClass(IdColumn.class);
    }

    @Order(0)
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

    @Order(500)
    public class IconColumn extends AbstractIconColumn {

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(1000)
    public class CreatorColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Creator");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(2000)
    public class TitleColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Title");
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 300;
      }
    }

    @Order(3000)
    public class ResponsibleColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Responsible");
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(3500)
    public class DueInColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("DueIn");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(4000)
    public class DueDateColumn extends AbstractDateColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("DueDate");
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(5000)
    public class ReminderColumn extends AbstractDateTimeColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Reminder");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(6000)
    public class AcceptedColumn extends AbstractBooleanColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Accepted");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(7000)
    public class DoneColumn extends AbstractBooleanColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Done");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }
  }
}
