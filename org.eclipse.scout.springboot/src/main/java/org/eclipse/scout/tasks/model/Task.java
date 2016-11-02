package org.eclipse.scout.tasks.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// this is a jpa entity
@Entity
// lombok annotations
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class Task implements Persistable<UUID> {

  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  @NonNull
  private UUID id = UUID.randomUUID();

  @NonNull
  private String name;

  @OneToOne
  @NonNull
  private User creator;

  @OneToOne
  @NonNull
  private User responsible;

  @NonNull
  private Date dueDate;

  private Date reminder;
  private boolean accepted;
  private boolean done;
  private String description;

  @Override
  public boolean isNew() {
    return getId() == null;
  }
}
