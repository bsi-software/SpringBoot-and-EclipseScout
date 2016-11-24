package org.eclipse.scout.tasks.spring.service;

import java.util.Collection;
import java.util.UUID;

import org.eclipse.scout.tasks.data.Task;

public interface TaskService extends ValidatorService {

  Collection<Task> getAllTasks();

  Collection<Task> getTodaysTasks(String userId);

  Collection<Task> getOwnTasks(String userId);

  Collection<Task> getInbox(String userId);

  Task getTask(UUID taskId);

  void saveTask(Task task);

  void addTask(Task task);
}
