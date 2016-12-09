package org.eclipse.scout.tasks.scout.ui.admin.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

@Bean
public class LocaleLookupCall extends LocalLookupCall<Locale> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<Locale>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<Locale>> rows = new ArrayList<LookupRow<Locale>>();

    getAvailableLocales()
        .stream()
        .forEach(locale -> {
          rows.add(new LookupRow<Locale>(locale, locale.getDisplayName()));
        });

    return rows;
  }

  public List<Locale> getAvailableLocales() {
    return Arrays.asList(sort(Locale.getAvailableLocales()));
  }

  private Locale[] sort(Locale[] locales) {
    Arrays.sort(locales, new Comparator<Locale>() {
      @Override
      public int compare(Locale locale1, Locale locale2) {
        return locale1.toString().compareTo(locale2.toString());
      }
    });

    return locales;
  }

}
