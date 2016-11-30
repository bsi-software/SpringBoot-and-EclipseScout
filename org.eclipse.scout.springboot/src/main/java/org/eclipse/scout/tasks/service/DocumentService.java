package org.eclipse.scout.tasks.service;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.tasks.model.Document;

public interface DocumentService extends ValidatorService<Document> {

  List<Document> getAll();

  boolean exists(UUID id);

  Document get(UUID id);

  void save(Document document);
}
