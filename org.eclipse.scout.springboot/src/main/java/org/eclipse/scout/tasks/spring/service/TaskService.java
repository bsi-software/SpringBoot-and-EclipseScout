package org.eclipse.scout.tasks.spring.service;

import java.util.Collection;
import java.util.UUID;

import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.User;

public interface TaskService {

  Collection<Task> getTasks();

  Task getTask(UUID taskId);

  Collection<Task> getInbox(User user);

  Collection<Task> getTodaysTasks(User user);

  Collection<Task> getOwnTasks(User user);

  void saveTask(Task task);
}
