package org.eclipse.scout.tasks.model;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

public class Task extends Model<UUID> {

  @NotNull
  private String name;
  private String description;

  @NotNull
  private String responsible;

  @NotNull
  private String assignedBy;

  @NotNull
  private Date dueDate;

  @NotNull
  private Date assignedAt;
  private Date reminder;

  private boolean accepted;
  private boolean done;

  public Task() {
    setId(UUID.randomUUID());
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
}
