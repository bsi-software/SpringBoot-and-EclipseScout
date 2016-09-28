package org.eclipse.scout.tasks.scout.ui.task;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIconColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.form.FormEvent;
import org.eclipse.scout.rt.client.ui.form.FormListener;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.scout.ui.ClientSession;
import org.eclipse.scout.tasks.scout.ui.task.AbstractTaskTablePage.Table;
import org.eclipse.scout.tasks.scout.ui.user.UserLookupCall;
import org.eclipse.scout.tasks.scout.ui.user.UserPictureProviderService;
import org.eclipse.scout.tasks.spring.service.TaskService;

@Bean
public class AbstractTaskTablePage extends AbstractPageWithTable<Table> {

  private static final SimpleDateFormat WEEKDAY_FORMATTER = new SimpleDateFormat("EEEE");

  @Inject
  private TaskService taskService;

  @Inject
  private UserPictureProviderService userPictureService;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("TaskTablePage");
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

  @Override
  protected void execPageActivated() {
    reloadPage();
  }

  @Override
  protected void execLoadData(SearchFilter filter) {
    Collection<Task> tasks = getTasks();
    importTableRowData(tasks);
  }

  private void importTableRowData(Collection<Task> tasks) {
    Table table = getTable();

    table.deleteAllRows();

    if (tasks == null || tasks.size() == 0) {
      return;
    }

    for (Task task : tasks) {
      ITableRow row = table.createRow();

      table.getIdColumn().setValue(row, task.getId());
      // TODO doesn't work: html has <img> tag -> put image as static resource somewhere?
      // table.getIconColumn().setValue(row, task.getCreator().getName());

      table.getIconColumn().setValue(row, task.getCreator().getName());

      table.getDueInColumn().setValue(row, getDueInValue(task.getDueDate()));
      table.getDueDateColumn().setValue(row, task.getDueDate());
      table.getTitleColumn().setValue(row, task.getName());
      table.getCreatorColumn().setValue(row, task.getCreator());
      table.getResponsibleColumn().setValue(row, task.getResponsible());
      table.getReminderColumn().setValue(row, task.getReminder());
      table.getAcceptedColumn().setValue(row, task.isAccepted());
      table.getDoneColumn().setValue(row, task.isDone());

      table.addRow(row);
    }
  }

  protected User getUser() {
    return ClientSession.get().getUser();
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
      else if (days >= -7) {
        return "Last " + WEEKDAY_FORMATTER.format(date);
      }
      else {
        return "" + (-days) + " days ago";
      }
    }

    if (days > 0) {
      if (days == 1) {
        return "Tomorrow";
      }
      else if (days < 7) {
        return WEEKDAY_FORMATTER.format(date);
      }
      else if (days < 14) {
        return "Next " + WEEKDAY_FORMATTER.format(date);
      }
      else {
        return "In " + days + " days";
      }
    }

    return "Today";
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
        TaskForm form = BEANS.get(TaskForm.class);
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
        UUID taskId = getIdColumn().getSelectedValue();

        TaskForm form = BEANS.get(TaskForm.class);
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

        for (UUID taskId : getIdColumn().getSelectedValues()) {
          if (acceptTask(taskId)) {
            listHasChanged = true;
          }
        }

        if (listHasChanged) {
          reloadPage();
        }
      }

      private boolean acceptTask(UUID taskId) {
        Task task = taskService.getTask(taskId);

        if (task != null && task.getResponsible().equals(ClientSession.get().getUser())) {
          task.setAccepted(true);
          taskService.saveTask(task);

          return true;
        }

        return false;
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
    public class IdColumn extends AbstractColumn<UUID> {

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(1000)
    public class IconColumn extends AbstractIconColumn {

      @Override
      protected int getConfiguredWidth() {
        return 50;
      }
    }

    @Order(2000)
    public class CreatorColumn extends AbstractSmartColumn<User> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Creator");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected Class<? extends ILookupCall<User>> getConfiguredLookupCall() {
        return UserLookupCall.class;
      }
    }

    @Order(3000)
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

    @Order(4000)
    public class ResponsibleColumn extends AbstractSmartColumn<User> {
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
        return 150;
      }

      @Override
      protected Class<? extends ILookupCall<User>> getConfiguredLookupCall() {
        return UserLookupCall.class;
      }
    }

    @Order(5000)
    public class DueInColumn extends AbstractStringColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("DueIn");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(6000)
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

    @Order(7000)
    public class ReminderColumn extends AbstractDateTimeColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Reminder");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(8000)
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

    @Order(9000)
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
