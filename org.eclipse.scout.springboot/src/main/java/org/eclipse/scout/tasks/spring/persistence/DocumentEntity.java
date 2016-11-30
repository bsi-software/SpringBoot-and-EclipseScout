package org.eclipse.scout.tasks.spring.persistence;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.eclipse.scout.tasks.spring.persistence.converter.UuidConverter;

@Entity
public class DocumentEntity {

  @Id
  @Convert(converter = UuidConverter.class)
  private UUID id = UUID.randomUUID();

  @Column(nullable = false)
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
}
