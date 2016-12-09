package org.eclipse.scout.tasks.model;

import java.util.Locale;

import javax.validation.constraints.Size;

public class Text extends Model<String> {

  public static final int ID_LENGTH_MIN = 1;
  public static final int ID_LENGTH_MAX = 128;
  public static final String ID_ERROR_LENGTH = "TextIdErrorLength";

  public static final Locale LOCALE_UNDEFINED = Locale.ROOT;

  public static final int TEXT_LENGTH_MAX = 128;
  public static final String TEXT_ERROR_LENGTH = "TextErrorLength";

  private static final String ID_SEPARATOR = ":";

  /**
   * Represents the translation for the text key and locale defined in the id of this object.
   */
  @Size(max = TEXT_LENGTH_MAX, message = TEXT_ERROR_LENGTH)
  private String text;

  @Size(min = ID_LENGTH_MIN, max = ID_LENGTH_MAX, message = ID_ERROR_LENGTH)
  @Override
  public String getId() {
    return super.getId();
  }

  public static String toId(Locale locale, String key) {
    if (key == null) {
      key = "";
    }

    if (locale == null) {
      locale = LOCALE_UNDEFINED;
    }

    return String.format("%s%s%s", convert(locale), ID_SEPARATOR, key);
  }

  public static String toKey(String id) {
    if (!idIsValid(id)) {
      return null;
    }

    return id.substring(id.indexOf(ID_SEPARATOR) + 1);
  }

  public static Locale toLocale(String id) {
    if (!idIsValid(id)) {
      return null;
    }

    return convert(id.substring(0, id.indexOf(ID_SEPARATOR)));
  }

  public Text() {

  }

  public Text(String key, Locale locale, String translation) {
    setId(toId(locale, key));
    setText(translation);
  }

  /**
   * Gets the text key encoded in the id of this text object.
   */
  public String getKey() {
    return toKey(getId());
  }

  /**
   * Gets the locale encoded in the id of this text object.
   */
  public Locale getLocale() {
    return toLocale(getId());
  }

  /**
   * Gets the translation of this text object.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the translation for this text object.
   */
  public void setText(String text) {
    this.text = text;
  }

  private static boolean idIsValid(String id) {

    if (id == null) {
      return false;
    }

    int separatorIndex = id.indexOf(ID_SEPARATOR);

    if (separatorIndex <= 0) {
      return false;
    }

    if (separatorIndex + 1 >= id.length()) {
      return false;
    }

    return true;
  }

  private static String convert(Locale locale) {
    return locale.toLanguageTag();
  }

  private static Locale convert(String locale) {
    return Locale.forLanguageTag(locale);
  }

}
