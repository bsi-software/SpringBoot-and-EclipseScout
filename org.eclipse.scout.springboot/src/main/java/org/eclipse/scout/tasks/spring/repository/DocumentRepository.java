package org.eclipse.scout.tasks.spring.repository;

import java.util.UUID;

import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

  default DocumentEntity convert(Document document) {
    final DocumentEntity documentEntity = new DocumentEntity();

    documentEntity.setId(document.getId());
    documentEntity.setName(document.getName());
    documentEntity.setData(document.getData());
    documentEntity.setType(document.getType());

    return documentEntity;
  }

  default Document convert(DocumentEntity documentEntity) {
    final Document document = new Document();

    document.setId(documentEntity.getId());
    document.setName(documentEntity.getName());
    document.setData(documentEntity.getData());
    document.setType(documentEntity.getType());

    return document;
  }
}
