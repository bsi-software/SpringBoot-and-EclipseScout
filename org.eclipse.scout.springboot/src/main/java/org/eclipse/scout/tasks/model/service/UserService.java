package org.eclipse.scout.tasks.model.service;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.tasks.model.Document;
import org.eclipse.scout.tasks.model.Role;
import org.eclipse.scout.tasks.model.User;

public interface UserService extends ValidatorService<User> {

  /**
   * Returns all available Users.
   */
  List<User> getAll();

  /**
   * Checks if the user specified by the provided user id exists.
   *
   * @param userId
   * @return True if the specified user exits, false otherwise.
   */
  boolean exists(String userId);

  /**
   * Returns the user object for the user specified by the provided user id.
   *
   * @param userId
   * @return
   */
  User get(String userId);

  /**
   * Returns the set of roles for the user specified by the provided user id.
   *
   * @param userId
   */
  Set<Role> getRoles(String userId);

  /**
   * Returns the picture associated with the user specified by the provided user id. If the specified user does not
   * exist or does not have a picture associated null is returned.
   *
   * @param userId
   * @return The picture in the form of the byte array (corresponds to the respective image file content)
   */
  Document getPicture(String userId);

  /**
   * Persists the provided user, including associated roles. To save the user picture use method {@link setPicture}.
   *
   * @param user
   */
  void save(User user);

  /**
   * Persists the picture for the user specified by the provided user id.
   *
   * @param userId
   * @param picture
   *          The picture in the form of a document
   */
  void setPicture(String userId, Document picture);
}
