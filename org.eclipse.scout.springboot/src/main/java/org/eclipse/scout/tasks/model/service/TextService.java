package org.eclipse.scout.tasks.model.service;

import java.util.List;
import java.util.Locale;

import org.eclipse.scout.tasks.model.Text;

public interface TextService extends ValidatorService<Text> {

  /**
   * Returns all available translated texts.
   */
  List<Text> getAll();

  /**
   * Returns all available translated texts for the provided text key.
   */
  List<Text> getAll(String key);

  /**
   * Returns all available translated texts for the provided locale.
   */
  List<Text> getAll(Locale locale);

  /**
   * Returns true if a translated text with the provided id exists. Returns false otherwise.
   */
  boolean exists(String textId);

  /**
   * Returns the translated text specified by the provided id. If no such text exists, null is returned.
   */
  Text get(String textId);

  /**
   * Persists the provided translated text.
   */
  void save(Text text);

  /**
   * Delete the text entry with the provided text id.
   */
  void delete(String textId);

}
