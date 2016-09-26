package org.eclipse.scout.springboot.demo.scout.ui.task;

import java.util.Date;
import java.util.UUID;

import org.eclipse.scout.rt.client.dto.FormData;
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
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.model.User;
import org.eclipse.scout.springboot.demo.scout.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.springboot.demo.scout.ui.ApplicationContexts;
import org.eclipse.scout.springboot.demo.scout.ui.ClientSession;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox.AcceptedField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox.DoneField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.CreatorField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.DescriptionField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.DueDateField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.ReminderField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.ResponsibleField;
import org.eclipse.scout.springboot.demo.scout.ui.task.TaskForm.MainBox.TopBox.TitleField;
import org.eclipse.scout.springboot.demo.scout.ui.user.UserLookupCall;
import org.eclipse.scout.springboot.demo.spring.service.TaskService;
import org.springframework.context.ApplicationContext;

public class TaskForm extends AbstractForm {

  private String taskId;

  @FormData
  public String getTaskId() {
    return taskId;
  }

  @FormData
  public void setTaskId(String taskId) {
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
//    final ApplicationContext applicationContext = ApplicationContexts.getApplicationContext();
//    startInternalExclusive(applicationContext.getBean(ModifyHandler.class));
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

  public CreatorField getCreatorField() {
    return getFieldByClass(CreatorField.class);
  }

  public DueDateField getDueDateField() {
    return getFieldByClass(DueDateField.class);
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

      @Order(2000)
      public class ResponsibleField extends AbstractSmartField<User> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Responsible");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected Class<? extends ILookupCall<User>> getConfiguredLookupCall() {
          return UserLookupCall.class;
        }

        @Override
        protected void execChangedValue() {
          getAcceptedField().setValue(false);
        }
      }

      @Order(2000)
      public class CreatorField extends AbstractSmartField<User> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Creator");
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected Class<? extends ILookupCall<User>> getConfiguredLookupCall() {
          return UserLookupCall.class;
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
      Task task = getTask();

      getTitleField().setValue(task.getName());
      getResponsibleField().setValue(task.getResponsible());
      getReminderField().setValue(task.getReminder());
      getDueDateField().setValue(task.getDueDate());
      getAcceptedField().setValue(task.isAccepted());
      getDoneField().setValue(task.isDone());
      getDescriptionField().setValue(task.getDescription());

      getForm().setSubTitle(calculateSubTitle());

      setEnabledPermission(new UpdateTaskPermission());
    }

    @Override
    protected void execStore() {
      Task task = getTask();

      task.setName(getTitleField().getValue());
      task.setResponsible(getResponsibleField().getValue());
      task.setDueDate(getDueDateField().getValue());
      task.setReminder(getReminderField().getValue());
      task.setAccepted(getAcceptedField().getValue());
      task.setDone(getDoneField().getValue());
      task.setDescription(getDescriptionField().getValue());

      getTaskService().saveTask(task);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected boolean getConfiguredOpenExclusive() {
      return true;
    }

    private Task getTask() {
      UUID id = UUID.fromString(getTaskId());
      Task task = getTaskService().getTask(id);

      if (task == null) {
        throw new IllegalArgumentException("invalid taskId '" + id + "': no record found");
      }

      return task;
    }

    private TaskService getTaskService() {
      final ApplicationContext applicationContext = ApplicationContexts.getApplicationContext();
      return applicationContext.getBean(TaskService.class);
    }
  }

  public class NewHandler extends AbstractDirtyFormHandler {

    @Override
    protected void execLoad() {
      User user = ClientSession.get().getUser();

      // add default values
      getCreatorField().setValue(user);
      getResponsibleField().setValue(user);
      getAcceptedField().setValue(true);
      getDueDateField().setValue(new Date());
      setEnabledPermission(new CreateTaskPermission());
    }

    @Override
    protected void execStore() {
      String title = getTitleField().getValue();
      User user = getResponsibleField().getValue();
      Date date = getDueDateField().getValue();

      Task task = new Task(title, user, date);
      task.setReminder(getReminderField().getValue());
      task.setAccepted(getAcceptedField().getValue());
      task.setDone(getDoneField().getValue());
      task.setDescription(getDescriptionField().getValue());

      final ApplicationContext applicationContext = ApplicationContexts.getApplicationContext();
      applicationContext.getBean(TaskService.class).addTask(task);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private String calculateSubTitle() {
    return getTitleField().getValue();
  }
}
