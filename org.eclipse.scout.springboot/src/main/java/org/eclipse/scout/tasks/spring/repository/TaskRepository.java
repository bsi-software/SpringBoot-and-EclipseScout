package org.eclipse.scout.tasks.spring.repository;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, UUID> {

  List<Task> findByResponsible(User user);

}
