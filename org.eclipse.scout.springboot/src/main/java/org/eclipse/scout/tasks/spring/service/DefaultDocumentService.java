package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.scout.tasks.data.Document;
import org.eclipse.scout.tasks.spring.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultDocumentService implements DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Document> getAll() {
    return documentRepository.findAll()
        .stream()
        .map(d -> documentRepository.convert(d))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exists(UUID id) {
    return documentRepository.exists(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Document get(UUID id) {
    return documentRepository.exists(id) ? documentRepository.convert(documentRepository.findOne(id)) : null;
  }

  @Override
  @Transactional
  public void save(Document document) {
    validate(document);

    documentRepository.save(documentRepository.convert(document));
  }
}
