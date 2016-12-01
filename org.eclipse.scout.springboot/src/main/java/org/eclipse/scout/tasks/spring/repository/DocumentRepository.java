package org.eclipse.scout.tasks.spring.repository;

import java.util.UUID;

import org.eclipse.scout.tasks.spring.repository.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
}
