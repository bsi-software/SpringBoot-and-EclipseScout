package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.scout.tasks.model.entity.Document;
import org.eclipse.scout.tasks.model.service.DocumentService;
import org.eclipse.scout.tasks.spring.repository.DocumentRepository;
import org.eclipse.scout.tasks.spring.repository.entity.DocumentEntity;
import org.modelmapper.ModelMapper;
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
        .map(document -> convert(document))
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
    return documentRepository.exists(id) ? convert(documentRepository.findOne(id)) : null;
  }

  @Override
  @Transactional
  public void save(Document document) {
    validate(document);
    documentRepository.save(convert(document));
  }

  private static ModelMapper mapper = new ModelMapper();

  public static Document convert(DocumentEntity document) {
    return mapper.map(document, Document.class);
  }

  public static DocumentEntity convert(Document document) {
    return mapper.map(document, DocumentEntity.class);
  }

}
