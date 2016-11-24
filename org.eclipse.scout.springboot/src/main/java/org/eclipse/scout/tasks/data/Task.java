package org.eclipse.scout.tasks.data;

import java.util.Date;
import java.util.UUID;

public class Task {
  private UUID id = UUID.randomUUID();

  private String name;
  private String description;

  private String assignedBy;
  private String responsible;

  private Date dueDate;
  private Date assignedAt;
  private Date reminder;

  private boolean accepted;
  private boolean done;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAssignedBy() {
    return assignedBy;
  }

  public void setAssignedBy(String assignedBy) {
    this.assignedBy = assignedBy;
  }

  public String getResponsible() {
    return responsible;
  }

  public void setResponsible(String responsible) {
    this.responsible = responsible;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public Date getAssignedAt() {
    return assignedAt;
  }

  public void setAssignedAt(Date assignedAt) {
    this.assignedAt = assignedAt;
  }

  public Date getReminder() {
    return reminder;
  }

  public void setReminder(Date reminder) {
    this.reminder = reminder;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  /**
   * Returns true if the two objects share the sames id, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Task)) {
      return false;
    }

    Task task = (Task) obj;

    if (task.getId() == null) {
      return false;
    }

    return task.getId().equals(id);
  }

  /**
   * returns the hash code of the id of this object.
   */
  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }

}
