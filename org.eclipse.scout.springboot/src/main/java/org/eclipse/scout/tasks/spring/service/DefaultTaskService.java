package org.eclipse.scout.tasks.spring.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.tasks.model.Task;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.spring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTaskService implements TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Override
  public Collection<Task> getTasks() {
    return taskRepository.findAll();
  }

  @Override
  public Task getTask(UUID taskId) {
    return taskRepository.findOne(taskId);
  }

  @Override
  public Collection<Task> getInbox(User user) {
    List<Task> inboxList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (!task.isAccepted()) {
        inboxList.add(task);
      }
    }

    return inboxList;
  }

  @Override
  public Collection<Task> getTodaysTasks(User user) {
    List<Task> todaysList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (!task.isAccepted() || task.isDone()) {
        continue;
      }

      if (isToday(task.getDueDate()) || isToday(task.getReminder())) {
        todaysList.add(task);
      }
    }

    return todaysList;
  }

  @Override
  public Collection<Task> getOwnTasks(User user) {
    List<Task> ownList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (task.isAccepted()) {
        ownList.add(task);
      }
    }

    return ownList;
  }

  @Override
  public void saveTask(Task task) {
    taskRepository.save(task);
  }

  private List<Task> getUserTasks(User user) {
    return taskRepository.findByResponsible(user);
  }

  private boolean isToday(Date date) {
    if (date == null) {
      return false;
    }

    return DateUtility.isSameDay(new Date(), date);
  }
}
