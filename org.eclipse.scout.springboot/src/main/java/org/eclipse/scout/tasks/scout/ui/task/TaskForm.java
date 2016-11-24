package org.eclipse.scout.tasks.scout.ui.task;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateTimeField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.tasks.data.Task;
import org.eclipse.scout.tasks.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.tasks.scout.ui.ClientSession;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox.AcceptedField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox.DoneField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.AssignedAtField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.AssignedByField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.DescriptionField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.DueDateField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.ReminderField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.ResponsibleField;
import org.eclipse.scout.tasks.scout.ui.task.TaskForm.MainBox.TopBox.TitleField;
import org.eclipse.scout.tasks.scout.ui.user.UserLookupCall;
import org.eclipse.scout.tasks.spring.service.TaskService;

@Bean
public class TaskForm extends AbstractForm {

  private UUID taskId;

  @Inject
  private TaskService taskService;

  public UUID getTaskId() {
    return taskId;
  }

  public void setTaskId(UUID taskId) {
    this.taskId = taskId;
  }

  @Override
  public Object computeExclusiveKey() {
    return getTaskId();
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return IForm.DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Task");
  }

  public void startModify() {
    startInternal(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public TopBox getTopBox() {
    return getFieldByClass(TopBox.class);
  }

  public TitleField getTitleField() {
    return getFieldByClass(TitleField.class);
  }

  public ResponsibleField getResponsibleField() {
    return getFieldByClass(ResponsibleField.class);
  }

  public AssignedByField getAssignedByField() {
    return getFieldByClass(AssignedByField.class);
  }

  public DueDateField getDueDateField() {
    return getFieldByClass(DueDateField.class);
  }

  public AssignedAtField getAssignedAtField() {
    return getFieldByClass(AssignedAtField.class);
  }

  public DoneField getDoneField() {
    return getFieldByClass(DoneField.class);
  }

  public ReminderField getReminderField() {
    return getFieldByClass(ReminderField.class);
  }

  public DescriptionField getDescriptionField() {
    return getFieldByClass(DescriptionField.class);
  }

  public AcceptedField getAcceptedField() {
    return getFieldByClass(AcceptedField.class);
  }

  public AcceptedAndDoneBox getMySequenceBox() {
    return getFieldByClass(AcceptedAndDoneBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class TopBox extends AbstractGroupBox {

      @Order(1000)
      public class TitleField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Title");
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

      @Order(1500)
      public class ResponsibleField extends AbstractSmartField<String> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Responsible");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return UserLookupCall.class;
        }

        @Override
        protected void execChangedValue() {
          if (!getForm().isFormLoading()) {
            getAcceptedField().setValue(false);
            getAssignedByField().setValue(ClientSession.get().getUserId());
            getAssignedAtField().setValue(new Date());
          }
        }
      }

      @Order(1800)
      public class AssignedByField extends AbstractSmartField<String> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("AssignedBy");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return UserLookupCall.class;
        }
      }

      @Order(2500)
      public class AssignedAtField extends AbstractDateTimeField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("AssignedAt");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }
      }

      @Order(3000)
      public class DueDateField extends AbstractDateField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("DueDate");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }
      }

      @Order(4000)
      public class ReminderField extends AbstractDateTimeField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Reminder");
        }
      }

      @Order(5000)
      public class AcceptedAndDoneBox extends AbstractSequenceBox {

        @Override
        protected boolean getConfiguredAutoCheckFromTo() {
          return false;
        }

        @Order(1000)
        public class AcceptedField extends AbstractBooleanField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Accepted");
          }
        }

        @Order(2000)
        public class DoneField extends AbstractBooleanField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Done");
          }
        }
      }

      @Order(6000)
      public class DescriptionField extends AbstractStringField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Description");
        }

        @Override
        protected boolean getConfiguredMultilineText() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 2000;
        }

        @Override
        protected int getConfiguredGridH() {
          return 4;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
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
      setEnabledPermission(new UpdateTaskPermission());

      Task task = taskService.getTask(getTaskId());
      importFormFieldData(task);

      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      Task task = taskService.getTask(getTaskId());
      exportFormFieldData(task);

      taskService.saveTask(task);
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
      setEnabledPermission(new CreateTaskPermission());

      String userId = ClientSession.get().getUserId();
      setDefaultFieldValues(userId);
    }

    @Override
    protected void execStore() {
      Task task = new Task();
      exportFormFieldData(task);

      taskService.addTask(task);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private void setDefaultFieldValues(String userId) {
    getAssignedByField().setValue(userId);
    getResponsibleField().setValue(userId);
    getAcceptedField().setValue(true);
    getDueDateField().setValue(new Date());
    getAssignedAtField().setValue(new Date());
  }

  private void importFormFieldData(Task task) {
    getTitleField().setValue(task.getName());
    getAssignedByField().setValue(task.getAssignedBy());
    getResponsibleField().setValue(task.getResponsible());
    getDueDateField().setValue(task.getDueDate());
    getAssignedAtField().setValue(task.getAssignedAt());

    getReminderField().setValue(task.getReminder());
    getAcceptedField().setValue(task.isAccepted());
    getDoneField().setValue(task.isDone());
    getDescriptionField().setValue(task.getDescription());
  }

  private void exportFormFieldData(Task task) {
    task.setName(getTitleField().getValue());
    task.setAssignedBy(getAssignedByField().getValue());
    task.setResponsible(getResponsibleField().getValue());
    task.setDueDate(getDueDateField().getValue());
    task.setAssignedAt(getAssignedAtField().getValue());

    task.setReminder(getReminderField().getValue());
    task.setAccepted(getAcceptedField().getValue());
    task.setDone(getDoneField().getValue());
    task.setDescription(getDescriptionField().getValue());
  }

  private String calculateSubTitle() {
    return getTitleField().getValue();
  }
}
