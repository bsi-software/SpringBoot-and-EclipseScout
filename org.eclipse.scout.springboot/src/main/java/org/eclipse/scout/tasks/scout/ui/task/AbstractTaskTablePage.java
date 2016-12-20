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
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
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
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.service.TaskService;
import org.eclipse.scout.tasks.scout.ui.ClientSession;
import org.eclipse.scout.tasks.scout.ui.admin.user.UserLookupCall;
import org.eclipse.scout.tasks.scout.ui.admin.user.UserPictureProviderService;
import org.eclipse.scout.tasks.scout.ui.task.AbstractTaskTablePage.Table;
import org.eclipse.scout.tasks.spring.service.DefaultUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Bean
public class AbstractTaskTablePage extends AbstractPageWithTable<Table> {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

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
    LOG.info("Loading data from persistence layer");
    Collection<Task> tasks = getTasks();
    importTableRowData(tasks);
  }

  private void importTableRowData(Collection<Task> tasks) {
    if (tasks == null) {
      return;
    }

    Table table = getTable();
    table.deleteAllRows();

    if (tasks.size() == 0) {
      return;
    }

    SimpleDateFormat formatter = getFormatter();
    for (Task task : tasks) {
      ITableRow row = table.createRow();

      table.getIdColumn().setValue(row, task.getId());
      table.getDueInColumn().setValue(row, getDueInValue(task.getDueDate(), formatter));
      table.getDueDateColumn().setValue(row, task.getDueDate());
      table.getTitleColumn().setValue(row, task.getName());
      table.getAssignedByColumn().setValue(row, task.getAssignedBy());
      table.getAssignedAtColumn().setValue(row, task.getAssignedAt());
      table.getResponsibleColumn().setValue(row, task.getResponsible());
      table.getReminderColumn().setValue(row, task.getReminder());
      table.getAcceptedColumn().setValue(row, task.isAccepted());
      table.getDoneColumn().setValue(row, task.isDone());

      table.addRow(row);
    }
  }

  protected String getUserId() {
    return ClientSession.get().getUserId();
  }

  private String getDueInValue(Date date, SimpleDateFormat formatter) {
    Date today = new Date();
    int days = DateUtility.getDaysBetween(new Date(), date);

    if (today.after(date)) {
      days = -days;
    }

    if (days < 0) {
      if (days == -1) {
        return TEXTS.get("Yesterday");
      }
      else if (days >= -7) {
        return TEXTS.get("Last", formatter.format(date));
      }
      else {
        return TEXTS.get("DaysAgo", Integer.toString(-days));
      }
    }

    if (days > 0) {
      if (days == 1) {
        return TEXTS.get("Tomorrow");
      }
      else if (days < 7) {
        return formatter.format(date);
      }
      else if (days < 14) {
        return TEXTS.get("Next", formatter.format(date));
      }
      else {
        return TEXTS.get("InDays", Integer.toString(days));
      }
    }

    return TEXTS.get("Today");
  }

  private SimpleDateFormat getFormatter() {
    return new SimpleDateFormat("EEEE", ClientSession.get().getLocale());
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
      protected String getConfiguredKeyStroke() {
        return "alt-n";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.EmptySpace, TableMenuType.SingleSelection, TableMenuType.MultiSelection);
      }

      @Override
      protected boolean getConfiguredVisible() {
        return accessAllowed();
      }

      @Override
      protected void execAction() {
        if (!accessAllowed()) {
          return;
        }

        TaskForm form = BEANS.get(TaskForm.class);
        form.addFormListener(new TaskFormListener());
        form.startNew();
      }

      private boolean accessAllowed() {
        return BEANS.get(IAccessControlService.class).checkPermission(new CreateTaskPermission());
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
      protected String getConfiguredKeyStroke() {
        return "alt-e";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection);
      }

      @Override
      protected boolean getConfiguredVisible() {
        return accessAllowed();
      }

      @Override
      protected void execAction() {
        if (!accessAllowed()) {
          return;
        }

        UUID taskId = getIdColumn().getSelectedValue();

        TaskForm form = BEANS.get(TaskForm.class);
        form.addFormListener(new TaskFormListener());
        form.setTaskId(taskId);
        form.startModify();

        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }

      private boolean accessAllowed() {
        return BEANS.get(IAccessControlService.class).checkPermission(new ReadTaskPermission());
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
      protected String getConfiguredKeyStroke() {
        return "alt-a";
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.hashSet(TableMenuType.SingleSelection, TableMenuType.MultiSelection);
      }

      @Override
      protected boolean getConfiguredVisible() {
        return accessAllowed();
      }

      @Override
      protected void execAction() {
        if (!accessAllowed()) {
          return;
        }

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
        Task task = taskService.get(taskId);

        if (task != null && task.getResponsible().equals(ClientSession.get().getUserId())) {
          task.setAccepted(true);
          taskService.save(task);

          return true;
        }

        return false;
      }

      private boolean accessAllowed() {
        return BEANS.get(IAccessControlService.class).checkPermission(new UpdateTaskPermission());
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

    public AssignedByColumn getAssignedByColumn() {
      return getColumnSet().getColumnByClass(AssignedByColumn.class);
    }

    public AssignedAtColumn getAssignedAtColumn() {
      return getColumnSet().getColumnByClass(AssignedAtColumn.class);
    }

    public DueInColumn getDueInColumn() {
      return getColumnSet().getColumnByClass(DueInColumn.class);
    }

    public AssignedByIconColumn getCreatorPictureColumn() {
      return getColumnSet().getColumnByClass(AssignedByIconColumn.class);
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

    @Order(1500)
    public class AssignedByIconColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredHtmlEnabled() {
        return true;
      }

      @Override
      protected void execDecorateCell(Cell cell, ITableRow row) {
        final String resourceName = getAssignedByColumn().getValue(row);
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

    @Order(2000)
    public class AssignedByColumn extends AbstractSmartColumn<String> {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("AssignedBy");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
        return UserLookupCall.class;
      }
    }

    @Order(2500)
    public class AssignedAtColumn extends AbstractDateTimeColumn {
      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("AssignedAt");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
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
    public class ResponsibleColumn extends AbstractSmartColumn<String> {
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
      protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
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
