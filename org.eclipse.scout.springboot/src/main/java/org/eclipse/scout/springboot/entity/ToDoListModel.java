package org.eclipse.scout.springboot.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

@ApplicationScoped
@CreateImmediately
public class ToDoListModel {
  public static final Role ROLE_USER = new Role("user");
  public static final Role ROLE_ADMIN = new Role("admin");

  private Map<String, Task> taskList;
  private Map<User, Queue<Task>> userTasks;

  private Map<String, Role> roleList;
  private Map<String, User> userList;

  public ToDoListModel() {
    userTasks = new ConcurrentHashMap<>();
    taskList = new ConcurrentHashMap<>();

    roleList = new ConcurrentHashMap<>();
    userList = new ConcurrentHashMap<>();

    User alice = new User("alice", "Alice", "", "test");
    alice.roles.add(ROLE_USER);
    alice.roles.add(ROLE_ADMIN);

    User bob = new User("bob", "Bob", "", "test");
    bob.roles.add(ROLE_USER);

    roleList.put(ROLE_USER.name, ROLE_USER);
    roleList.put(ROLE_ADMIN.name, ROLE_ADMIN);
    userList.put(alice.name, alice);
    userList.put(bob.name, bob);
  }

  public void addUser(User user) {
    if (user == null) {
      return;
    }

    // make sure user is not already in list
    if (userList.containsKey(user.name)) {
      return;
    }

    userList.put(user.name, user);
  }

  public User getUser(String userName) {
    return userList.get(userName);
  }

  public Collection<User> getUsers() {
    return userList.values();
  }

  public User loggedInUser() {
    return getUser("alice");
  }

  public void addTask(Task task) {
    if (task == null) {
      return;
    }

    // make sure task is not already in list
    if (taskList.containsKey(task.id)) {
      return;
    }

    User responsible = task.responsible;

    if (!userTasks.containsKey(responsible)) {
      userTasks.put(responsible, new ConcurrentLinkedQueue<Task>());
    }

    userTasks.get(responsible).add(task);
    taskList.put(task.id, task);
  }

  public void saveTask(Task taskOld, Task taskNew) {
    User userOld = taskOld.responsible;
    User userNew = taskNew.responsible;

    if (!userNew.equals(userOld)) {
      changeUser(taskOld, userNew);
    }

    taskOld.copyTaskAttributesFrom(taskNew);
  }

  private void changeUser(Task task, User userNew) {
    getUserTasks(task.responsible).remove(task);
    getUserTasks(userNew).add(task);
  }

  private Collection<Task> getUserTasks(User user) {
    if (!userTasks.containsKey(user)) {
      userTasks.put(user, new ConcurrentLinkedQueue<Task>());
    }

    return userTasks.get(user);
  }

  public Task getTask(String taskId) {
    return taskList.get(taskId);
  }

  public Collection<Task> getInbox(User user) {
    List<Task> inboxList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (!task.accepted) {
        inboxList.add(task);
      }
    }

    return inboxList;
  }

  public Collection<Task> getOwnTasks(User user) {
    List<Task> ownList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (task.accepted) {
        ownList.add(task);
      }
    }

    return ownList;
  }

  public Collection<Task> getTodaysTasks(User user) {
    List<Task> todaysList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (!task.accepted || task.done) {
        continue;
      }

      if (isToday(task.dueDate) || isToday(task.reminder)) {
        todaysList.add(task);
      }
    }

    return todaysList;
  }

  private boolean isToday(Date date) {
    if (date == null) {
      return false;
    }

    return DateUtility.isSameDay(new Date(), date);
  }

  public Collection<Task> getAllTasks() {
    return taskList.values();
  }
}
