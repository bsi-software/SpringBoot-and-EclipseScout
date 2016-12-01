package org.eclipse.scout.tasks.scout.ui.admin.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class LocaleLookupCall extends LocalLookupCall<Locale> {

  private static final long serialVersionUID = 1L;

  private Locale[] sort(Locale[] locales) {
    Arrays.sort(locales, new Comparator<Locale>() {
      @Override
      public int compare(Locale locale1, Locale locale2) {
        return locale1.toString().compareTo(locale2.toString());
      }
    });

    return locales;
  }

  @Override
  protected List<LookupRow<Locale>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<Locale>> rows = new ArrayList<LookupRow<Locale>>();

    for (Locale locale : sort(Locale.getAvailableLocales())) {
      rows.add(new LookupRow<Locale>(locale, locale.getDisplayName()));
    }

    return rows;
  }
}
