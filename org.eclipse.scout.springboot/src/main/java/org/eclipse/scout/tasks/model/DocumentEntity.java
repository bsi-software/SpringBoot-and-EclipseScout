package org.eclipse.scout.tasks.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

@Entity
public class DocumentEntity implements Persistable<UUID> {

  private static final long serialVersionUID = 1L;

  @Id
  @Type(type = "uuid-char")
  private UUID id = UUID.randomUUID();

  @NotNull
  private String name;

  @Lob
  private byte[] data;

  private int type;

  public DocumentEntity() {
  }

  /**
   * @param name
   * @param data
   */
  public DocumentEntity(String name, byte[] data) {
    super();
    setName(name);
    setData(data);
  }

  @Override
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

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  /**
   * Returns true if the two objects share the sames id, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof DocumentEntity)) {
      return false;
    }

    DocumentEntity user = (DocumentEntity) obj;

    if (user.getId() == null) {
      return false;
    }

    return user.getId().equals(id);
  }

  /**
   * returns the hash code of the id of this object.
   */
  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }

  @Override
  public boolean isNew() {
    return getId() == null;
  }
}
