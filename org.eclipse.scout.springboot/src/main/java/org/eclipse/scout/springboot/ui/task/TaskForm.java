package org.eclipse.scout.springboot.ui.task;

import java.util.Date;

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
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.springboot.entity.Task;
import org.eclipse.scout.springboot.entity.ToDoListModel;
import org.eclipse.scout.springboot.entity.User;
import org.eclipse.scout.springboot.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.CancelButton;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.OkButton;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox.AcceptedField;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.AcceptedAndDoneBox.DoneField;
import org.eclipse.scout.springboot.ui.user.UserLookupCall;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.CreatorField;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.DescriptionField;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.DueDateField;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.ReminderField;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.ResponsibleField;
import org.eclipse.scout.springboot.ui.task.TaskForm.MainBox.TopBox.TitleField;

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
    startInternalExclusive(new ModifyHandler());
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

      getTitleField().setValue(task.name);
      getResponsibleField().setValue(task.responsible);
      getReminderField().setValue(task.reminder);
      getDueDateField().setValue(task.dueDate);
      getAcceptedField().setValue(task.accepted);
      getDoneField().setValue(task.done);
      getDescriptionField().setValue(task.description);

      getForm().setSubTitle(calculateSubTitle());

      setEnabledPermission(new UpdateTaskPermission());
    }

    @Override
    protected void execStore() {
      String title = getTitleField().getValue();
      User user = getResponsibleField().getValue();
      Date date = getDueDateField().getValue();

      Task taskNew = new Task(title, user, date);
      taskNew.reminder = getReminderField().getValue();
      taskNew.accepted = getAcceptedField().getValue();
      taskNew.done = getDoneField().getValue();
      taskNew.description = getDescriptionField().getValue();

      BEANS.get(ToDoListModel.class).saveTask(getTask(), taskNew);
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
      Task task = BEANS.get(ToDoListModel.class).getTask(getTaskId());

      if (task == null) {
        throw new IllegalArgumentException("invalid taskId '" + getTaskId() + "': no record found");
      }

      return task;
    }
  }

  public class NewHandler extends AbstractDirtyFormHandler {

    @Override
    protected void execLoad() {
      User user = BEANS.get(ToDoListModel.class).loggedInUser();

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
      task.reminder = getReminderField().getValue();
      task.accepted = getAcceptedField().getValue();
      task.done = getDoneField().getValue();
      task.description = getDescriptionField().getValue();

      BEANS.get(ToDoListModel.class).addTask(task);
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
