package org.eclipse.scout.tasks.data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class Task implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id = UUID.randomUUID();

  private String name;
  private String description;

  private UUID creator;
  private UUID responsible;

  private Date dueDate;
  private Date reminder;

  private boolean accepted;
  private boolean done;

}
