package org.eclipse.scout.tasks.scout.ui;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.text.ITextProviderService;
import org.eclipse.scout.tasks.model.Text;
import org.eclipse.scout.tasks.model.service.TextService;

/**
 * Manages translated texts from the database. The Ordering of this service is lower than {@link TextProviderService}
 * which provides the default translations. Therefore, the application first tries to obtain a text from the database
 * text service and only afterward switches to the default.
 */
@Order(1000)
public class TextDbProviderService implements ITextProviderService {

  public static final Locale LOCALE_DEFAULT = Locale.ROOT;

  private Map<String, String> translationCache;
  private boolean cacheIsValid = false;

  @Inject
  TextService textService;

  @Override
  public String getText(Locale locale, String key, String... messageArguments) {
    checkCache();

    if (locale == null && ClientSession.get() != null) {
      locale = ClientSession.get().getLocale();
    }

    // try to get exact translation
    String text = translationCache.get(Text.toId(locale, key));
    if (StringUtility.hasText(text)) {
      return text;
    }

    // try to find the right language only
    if (locale != null) {
      String[] part = locale.toLanguageTag().split("[-_]");
      locale = Locale.forLanguageTag(part[0]);
      text = translationCache.get(Text.toId(locale, key));

      if (StringUtility.hasText(text)) {
        return text;
      }
    }

    // return default translation
    return translationCache.get(Text.toId(null, key));
  }

  @Override
  public Map<String, String> getTextMap(Locale locale) {
    checkCache();

    return null;
  }

  public void addText(String key, Locale locale, String translation) {
    checkCache();

    Text text = new Text(key, locale, translation);

    translationCache.put(text.getId(), text.getText());
    textService.save(text);
  }

  public void deleteText(String key, Locale locale) {
    String textId = Text.toId(locale, key);

    translationCache.remove(textId);
    textService.delete(textId);
  }

  public Map<Locale, String> getTexts(String key) {
    checkCache();

    Map<Locale, String> texts = new HashMap<Locale, String>();

    translationCache.keySet()
        .stream()
        .forEach(textId -> {
          if (key.equals(Text.toKey(textId))) {
            Locale locale = Text.toLocale(textId);
            String text = translationCache.get(textId);
            texts.put(locale, text);
          }
        });

    return texts;
  }

  /**
   * Loads text from repository if it is not valid.
   */
  private void checkCache() {
    if (cacheIsValid) {
      return;
    }

    translationCache = new HashMap<>();

    textService.getAll()
        .stream()
        .forEach(text -> translationCache.put(text.getId(), text.getText()));

    cacheIsValid = true;
  }
}
