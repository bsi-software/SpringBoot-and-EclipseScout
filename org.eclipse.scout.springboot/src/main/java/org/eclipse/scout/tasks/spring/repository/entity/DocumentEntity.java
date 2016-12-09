package org.eclipse.scout.tasks.spring.repository.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.eclipse.scout.tasks.model.Document;
import org.eclipse.scout.tasks.spring.repository.entity.converter.UuidConverter;

@Entity
public class DocumentEntity extends Document {

  @Id
  @Convert(converter = UuidConverter.class)
  @Override
  public UUID getId() {
    return super.getId();
  }

  @Column(nullable = false)
  @Override
  public String getName() {
    return super.getName();
  }

  @Lob
  @Column(length = CONTENT_SIZE_MAX)
  @Override
  public byte[] getData() {
    return super.getData();
  }

}
