package org.eclipse.scout.springboot.demo.spring.repository;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, UUID> {

  List<Task> findByResponsible(User user);

}
