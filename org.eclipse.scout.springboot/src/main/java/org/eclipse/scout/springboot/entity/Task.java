package org.eclipse.scout.springboot.entity;

import java.util.Date;

public class Task extends Entity {

  private User creator;
  private User responsible;
  private Date reminder;
  private Date dueDate;
  private boolean accepted;
  private boolean done;
  private String description;

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public User getResponsible() {
    return responsible;
  }

  public void setResponsible(User responsible) {
    this.responsible = responsible;
  }

  public Date getReminder() {
    return reminder;
  }

  public void setReminder(Date reminder) {
    this.reminder = reminder;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Task(String name, User creator, Date dueDate) {
    super(name);

    if (creator == null) {
      throw new IllegalArgumentException("creator must not be null");
    }

    if (dueDate == null) {
      throw new IllegalArgumentException("due date must not be null");
    }

    this.setCreator(creator);
    this.setResponsible(creator);
    this.setDueDate(dueDate);
  }

  public void copyTaskAttributesFrom(Task taskNew) {
    if (taskNew == null) {
      throw new IllegalArgumentException("task to copy from must not be null");
    }

    setName(taskNew.getName());
    setResponsible(taskNew.getResponsible());
    setReminder(taskNew.getReminder());
    setDueDate(taskNew.getDueDate());
    setAccepted(taskNew.isAccepted());
    setDone(taskNew.isDone());
    setDescription(taskNew.getDescription());
  }
}
