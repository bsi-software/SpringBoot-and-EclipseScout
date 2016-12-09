package org.eclipse.scout.tasks.spring.repository;

import org.eclipse.scout.tasks.spring.repository.entity.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<TextEntity, String> {

}
