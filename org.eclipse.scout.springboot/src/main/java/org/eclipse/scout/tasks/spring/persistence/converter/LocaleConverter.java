package org.eclipse.scout.tasks.spring.persistence.converter;

import java.util.Locale;

import javax.persistence.AttributeConverter;

public class LocaleConverter implements AttributeConverter<Locale, String> {

  @Override
  public String convertToDatabaseColumn(Locale locale) {
    if (locale == null) {
      return null;
    }

    return locale.toLanguageTag();
  }

  @Override
  public Locale convertToEntityAttribute(String locale) {
    if (locale == null) {
      return null;
    }

    return Locale.forLanguageTag(locale);
  }

}
