package org.eclipse.scout.tasks.spring.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.assertj.core.util.Strings;
import org.eclipse.scout.tasks.model.Text;
import org.eclipse.scout.tasks.model.service.TextService;
import org.eclipse.scout.tasks.spring.repository.TextRepository;
import org.eclipse.scout.tasks.spring.repository.entity.TextEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultTextService implements TextService, MapperService<Text, TextEntity> {

  @Autowired
  private TextRepository textRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Text> getAll() {
    return textRepository.findAll()
        .stream()
        .map(text -> convertToModel(text, Text.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Text> getAll(String key) {

    if (Strings.isNullOrEmpty(key)) {
      return getAll();
    }

    return getAll()
        .stream()
        .filter(text -> text.getKey().equals(key))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Text> getAll(Locale locale) {

    if (locale == null || Text.LOCALE_UNDEFINED.equals(locale)) {
      return getAll();
    }

    String localeId = locale.toLanguageTag();

    return getAll()
        .stream()
        .filter(text -> text.getLocale().toLanguageTag().startsWith(localeId))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exists(String textId) {
    return textRepository.exists(textId);
  }

  @Override
  @Transactional(readOnly = true)
  public Text get(String textId) {
    return textRepository.exists(textId) ? convertToModel(textRepository.findOne(textId), Text.class) : null;
  }

  @Override
  @Transactional
  public void save(Text text) {
    validate(text);
    textRepository.save(convertToEntity(text, TextEntity.class));
  }

  @Override
  @Transactional
  public void delete(String textId) {
    if (Strings.isNullOrEmpty(textId)) {
      return;
    }

    if (textRepository.exists(textId)) {
      textRepository.delete(textId);
    }
  }

}
