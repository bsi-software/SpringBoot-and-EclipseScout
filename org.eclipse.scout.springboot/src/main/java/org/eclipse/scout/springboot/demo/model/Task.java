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

    setCreator(creator);
    setResponsible(creator);
    setDueDate(dueDate);
  }

  public void setCreator(User creator) {
    if (creator == null) {
      throw new IllegalArgumentException("creator must not be null");
    }

    this.creator = creator;
  }

  public void setResponsible(User responsible) {
    if (responsible == null) {
      throw new IllegalArgumentException("responsible must not be null");
    }

    this.responsible = responsible;
  }

  public void setDueDate(Date dueDate) {
    if (dueDate == null) {
      throw new IllegalArgumentException("due date must not be null");
    }

    this.dueDate = dueDate;
  }
}
