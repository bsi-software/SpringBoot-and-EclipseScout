package org.eclipse.scout.springboot.entity;

import java.util.Date;

public class Task extends Entity {

  public User creator;
  public User responsible;
  public Date reminder;
  public Date dueDate;
  public boolean accepted;
  public boolean done;
  public String description;

  public Task(String name, User creator, Date dueDate) {
    super(name);

    if (creator == null) {
      throw new IllegalArgumentException("creator must not be null");
    }

    if (dueDate == null) {
      throw new IllegalArgumentException("due date must not be null");
    }

    this.creator = creator;
    this.responsible = creator;
    this.dueDate = dueDate;
  }

  public void copyTaskAttributesFrom(Task taskNew) {
    if (taskNew == null) {
      throw new IllegalArgumentException("task to copy from must not be null");
    }

    name = taskNew.name;
    responsible = taskNew.responsible;
    reminder = taskNew.reminder;
    dueDate = taskNew.dueDate;
    accepted = taskNew.accepted;
    done = taskNew.done;
    description = taskNew.description;
  }
}
