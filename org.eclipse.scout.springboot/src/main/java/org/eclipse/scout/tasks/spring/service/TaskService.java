package org.eclipse.scout.tasks.spring.service;

import java.util.Collection;
import java.util.UUID;

import org.eclipse.scout.tasks.data.Task;
import org.eclipse.scout.tasks.data.User;

public interface TaskService {

  Collection<Task> getAllTasks();

  Collection<Task> getTodaysTasks(User user);

  Collection<Task> getOwnTasks(User user);

  Collection<Task> getInbox(User user);

  Task getTask(UUID taskId);

  void saveTask(Task task);

  void addTask(Task task);
}
