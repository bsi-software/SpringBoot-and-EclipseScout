package org.eclipse.scout.tasks.spring.repository;

import java.util.List;
import java.util.UUID;

import org.eclipse.scout.tasks.data.Task;
import org.eclipse.scout.tasks.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

  List<TaskEntity> findByResponsible(String userId);

  default TaskEntity convert(Task task) {
    final TaskEntity taskEntity = new TaskEntity();

    taskEntity.setId(task.getId());
    taskEntity.setName(task.getName());
    taskEntity.setCreator(task.getCreator());
    taskEntity.setResponsible(task.getResponsible());
    taskEntity.setReminder(task.getReminder());
    taskEntity.setDueDate(task.getDueDate());
    taskEntity.setReminder(task.getReminder());
    taskEntity.setAccepted(task.isAccepted());
    taskEntity.setDone(task.isDone());
    taskEntity.setDescription(task.getDescription());

    return taskEntity;
  }

  default Task convert(TaskEntity taskEntity) {
    final Task task = new Task();

    task.setId(taskEntity.getId());
    task.setName(taskEntity.getName());
    task.setCreator(taskEntity.getCreator());
    task.setReminder(taskEntity.getReminder());
    task.setResponsible(taskEntity.getResponsible());
    task.setDueDate(taskEntity.getDueDate());
    task.setReminder(taskEntity.getReminder());
    task.setAccepted(taskEntity.isAccepted());
    task.setDone(taskEntity.isDone());
    task.setDescription(taskEntity.getDescription());

    return task;
  }

}
