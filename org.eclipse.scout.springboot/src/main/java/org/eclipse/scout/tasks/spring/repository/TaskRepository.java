package org.eclipse.scout.tasks.spring.repository;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.tasks.spring.repository.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

  List<TaskEntity> findByResponsible(String userId);

}
