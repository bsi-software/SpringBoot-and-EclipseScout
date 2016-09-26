package org.eclipse.scout.springboot.demo.spring.service;

import java.util.Collection;
import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.Task;
import org.eclipse.scout.springboot.demo.model.User;

public interface TaskService {

  Collection<Task> getAllTasks();

  Collection<Task> getTodaysTasks(User user);

  Collection<Task> getOwnTasks(User user);

  Collection<Task> getInbox(User user);

  Task getTask(UUID taskId);

  void saveTask(Task task);

  void addTask(Task task);
}
