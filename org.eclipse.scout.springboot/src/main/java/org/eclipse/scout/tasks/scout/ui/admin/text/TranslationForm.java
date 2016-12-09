package org.eclipse.scout.tasks.scout.ui.admin.text;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.tasks.scout.ui.TextDbProviderService;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.CancelButton;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.OkButton;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.TopBox;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.TopBox.HasTextFilterField;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.TopBox.KeyField;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.TopBox.LocaleFilterField;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.TopBox.TranslationTableField;
import org.eclipse.scout.tasks.scout.ui.admin.text.TranslationForm.MainBox.TopBox.TranslationTableField.Table;
import org.eclipse.scout.tasks.scout.ui.admin.user.LocaleLookupCall;

@Bean
public class TranslationForm extends AbstractForm {

  @Inject
  TextDbProviderService textService;

  @Inject
  LocaleLookupCall localeLookup;

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Translation");
  }

  public void setKey(String key) {
    getKeyField().setValue(key);
  }

  public String getKey() {
    return getKeyField().getValue();
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public TopBox getTopBox() {
    return getFieldByClass(TopBox.class);
  }

  public KeyField getKeyField() {
    return getFieldByClass(KeyField.class);
  }

  public TranslationTableField getTranslationTableField() {
    return getFieldByClass(TranslationTableField.class);
  }

  public LocaleFilterField getLocaleFilterField() {
    return getFieldByClass(LocaleFilterField.class);
  }

  public HasTextFilterField getHasTextFilterField() {
    return getFieldByClass(HasTextFilterField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class TopBox extends AbstractGroupBox {

      @Order(10)
      public class KeyField extends AbstractStringField {

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("TextKey");
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 128;
        }
      }

      @Order(20)
      public class LocaleFilterField extends AbstractSmartField<Locale> {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("LocaleFilter");
        }

        @Override
        protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
          return LocaleLookupCall.class;
        }

        @Override
        protected void execChangedValue() {
          reloadTranslations();
        }
      }

      @Order(30)
      public class HasTextFilterField extends AbstractBooleanField {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("HasTextFilter");
        }

        @Override
        protected void execChangedValue() {
          reloadTranslations();
        }
      }

      @Order(40)
      public class TranslationTableField extends AbstractTableField<TranslationTableField.Table> {
        public class Table extends AbstractTable {

          public HasTextColumn getHasTextColumn() {
            return getColumnSet().getColumnByClass(HasTextColumn.class);
          }

          public TranslationColumn getTranslationColumn() {
            return getColumnSet().getColumnByClass(TranslationColumn.class);
          }

          public LocaleColumn getLocaleColumn() {
            return getColumnSet().getColumnByClass(LocaleColumn.class);
          }

          @Order(10)
          public class LocaleColumn extends AbstractSmartColumn<Locale> {
            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Language");
            }

            @Override
            protected int getConfiguredWidth() {
              return 200;
            }

            @Override
            protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
              return LocaleLookupCall.class;
            }
          }

          @Order(20)
          public class TranslationColumn extends AbstractStringColumn {
            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Translation");
            }

            @Override
            protected int getConfiguredWidth() {
              return 300;
            }

            @Override
            protected boolean getConfiguredEditable() {
              return true;
            }

            @Override
            protected void execCompleteEdit(ITableRow row, IFormField editingField) {
              String value = ((AbstractStringField) editingField).getValue();

              row.getCellForUpdate(getTranslationColumn()).setValue(value);
              row.getCellForUpdate(getHasTextColumn()).setValue(StringUtility.hasText(value));
            }
          }

          @Order(3000)
          public class HasTextColumn extends AbstractBooleanColumn {
            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("HasText");
            }

            @Override
            protected int getConfiguredWidth() {
              return 100;
            }
          }

        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected int getConfiguredGridH() {
          return 6;
        }
      }

    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      setEnabledPermission(new UpdateTranslationPermission());
      reloadTranslations();
    }

    @Override
    protected void execStore() {
      saveTranslations();
    }
  }

  private void reloadTranslations() {
    String key = getKey();
    Table table = getTranslationTableField().getTable();
    Locale localeFilter = getLocaleFilterField().getValue();
    boolean textFilter = getHasTextFilterField().getValue();
    Map<Locale, String> map = textService.getTexts(key);

    table.deleteAllRows();

    localeLookup.getAvailableLocales()
        .stream()
        .forEach(locale -> {
          boolean addLocale = true;
          boolean hasText = map.containsKey(locale);

          if (localeFilter != null && !localeFilter.toLanguageTag().contentEquals(Locale.ROOT.toLanguageTag())) {
            if (!locale.toLanguageTag().startsWith(localeFilter.toLanguageTag())) {
              addLocale = false;
            }
          }

          if (textFilter && !hasText) {
            addLocale = false;
          }

          if (addLocale) {
            ITableRow row = table.createRow();
            table.getLocaleColumn().setValue(row, locale);
            table.getTranslationColumn().setValue(row, map.get(locale));
            table.getHasTextColumn().setValue(row, hasText);
            table.addRow(row);
          }
        });
  }

  private void saveTranslations() {
    String key = getKey();
    Table table = getTranslationTableField().getTable();
//    TextDbProviderService service = BEANS.get(TextDbProviderService.class);

    table.getRows()
        .stream()
        .forEach(row -> {
          Locale locale = (Locale) row.getKeyValues().get(0);
          String text = (String) row.getCell(table.getTranslationColumn()).getValue();

          if (StringUtility.hasText(text)) {
            textService.addText(key, locale, text);
          }
          else {
            textService.deleteText(key, locale);
          }
        });
  }
}
