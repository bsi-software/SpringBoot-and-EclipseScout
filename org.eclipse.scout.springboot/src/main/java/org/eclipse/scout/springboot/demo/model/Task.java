package org.eclipse.scout.springboot.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @OneToOne
  private User creator;
  @OneToOne
  private User responsible;
  private Date reminder;
  private Date dueDate;
  private boolean accepted;
  private boolean done;
  private String description;

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
