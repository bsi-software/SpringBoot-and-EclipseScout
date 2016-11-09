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

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class TaskEntity implements Persistable<UUID> {
  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  @NonNull
  private UUID id = UUID.randomUUID();

  @NonNull
  private String name;
  private String description;

  @OneToOne
  @NonNull
  private UserEntity creator;

  @OneToOne
  @NonNull
  private UserEntity responsible;

  @NonNull
  private Date dueDate;
  private Date reminder;

  private boolean accepted;
  private boolean done;

  @Override
  public boolean isNew() {
    return getId() == null;
  }

}
