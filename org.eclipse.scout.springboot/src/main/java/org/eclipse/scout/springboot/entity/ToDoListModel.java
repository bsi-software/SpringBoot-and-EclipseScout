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

  private final Map<String, Task> taskList = new ConcurrentHashMap<>();
  private final Map<User, Queue<Task>> userTasks = new ConcurrentHashMap<>();

  private final Map<String, Role> roleList = new ConcurrentHashMap<>();
  private final Map<String, User> userList = new ConcurrentHashMap<>();

  public ToDoListModel() {
    createDummyUsers();
  }

  private void createDummyUsers() {
    User alice = new User("alice", "Alice", "", "test");
    alice.getRoles().add(ROLE_USER);
    alice.getRoles().add(ROLE_ADMIN);
    addDummyUser(alice);

    User bob = new User("bob", "Bob", "", "test");
    bob.getRoles().add(ROLE_USER);
    addDummyUser(bob);

    User eclipse = new User("eclipse", "Eclipse", "", "scout");
    eclipse.getRoles().add(ROLE_USER);
    addDummyUser(eclipse);
  }

  private void addDummyUser(User user) {
    userList.put(user.getName(), user);
    user.getRoles().forEach((role) -> {
      roleList.put(role.getName(), role);
    });
  }

  public void addUser(User user) {
    if (user == null) {
      return;
    }

    // make sure user is not already in list
    if (userList.containsKey(user.getName())) {
      return;
    }

    userList.put(user.getName(), user);
  }

  public User getUser(String userName) {
    return userList.get(userName);
  }

  public Collection<User> getUsers() {
    return userList.values();
  }

  public void addTask(Task task) {
    if (task == null) {
      return;
    }

    // make sure task is not already in list
    if (taskList.containsKey(task.getId())) {
      return;
    }

    User responsible = task.getResponsible();

    if (!userTasks.containsKey(responsible)) {
      userTasks.put(responsible, new ConcurrentLinkedQueue<Task>());
    }

    userTasks.get(responsible).add(task);
    taskList.put(task.getId(), task);
  }

  public void saveTask(Task taskOld, Task taskNew) {
    User userOld = taskOld.getResponsible();
    User userNew = taskNew.getResponsible();

    if (!userNew.equals(userOld)) {
      changeUser(taskOld, userNew);
    }

    taskOld.copyTaskAttributesFrom(taskNew);
  }

  private void changeUser(Task task, User userNew) {
    getUserTasks(task.getResponsible()).remove(task);
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
      if (!task.isAccepted()) {
        inboxList.add(task);
      }
    }

    return inboxList;
  }

  public Collection<Task> getOwnTasks(User user) {
    List<Task> ownList = new ArrayList<>();

    for (Task task : getUserTasks(user)) {
      if (task.isAccepted()) {
        ownList.add(task);
      }
    }

    return ownList;
  }

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
