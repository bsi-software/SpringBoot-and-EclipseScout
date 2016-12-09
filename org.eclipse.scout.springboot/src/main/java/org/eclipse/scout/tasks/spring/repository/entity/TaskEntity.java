package org.eclipse.scout.tasks.spring.repository.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.User;

@Entity
public class TaskEntity extends Task {

  @Id
  @Override
  public UUID getId() {
    return super.getId();
  }

  @Override
  @Column(nullable = false)
  public String getName() {
    return super.getName();
  }

  @Column(nullable = false, length = User.ID_LENGTH_MAX)
  @Override
  public String getResponsible() {
    return super.getResponsible();
  }

  /**
   * Method is only needed to convince JPA to persist this attribute (isAccepted won't do).
   */
  public boolean getAccepted() {
    return isAccepted();
  }

  /**
   * Method is only needed to convince JPA to persist this attribute (isDone won't do).
   */
  public boolean getDone() {
    return isDone();
  }

  @Column(nullable = false)
  @Override
  public Date getDueDate() {
    return super.getDueDate();
  }

  @Column(nullable = false, length = User.ID_LENGTH_MAX)
  @Override
  public String getAssignedBy() {
    return super.getAssignedBy();
  }

  @Column(nullable = false)
  @Override
  public Date getAssignedAt() {
    return super.getAssignedAt();
  }

}
