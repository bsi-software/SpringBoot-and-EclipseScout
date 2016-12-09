package org.eclipse.scout.tasks.scout.ui.admin.text;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;

public abstract class AbstractTranslateMenu extends AbstractMenu {

  protected abstract String getTextKey();

  protected abstract void reload();

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("Translate");
  }

  @Override
  protected String getConfiguredIconId() {
    // get unicode from http://fontawesome.io/icon/language/
    return "font:awesomeIcons \uf1ab";
  }

  @Override
  protected String getConfiguredKeyStroke() {
    return "alt-t";
  }

  @Override
  protected Set<? extends IMenuType> getConfiguredMenuTypes() {
    return CollectionUtility.hashSet(TableMenuType.SingleSelection);
  }

  @Override
  protected void execAction() {
    String textId = getTextKey();

    TranslationForm form = BEANS.get(TranslationForm.class);
    form.setKey(textId);
    form.startModify();
    form.waitFor();

    if (form.isFormStored()) {
      reload();
    }
  }
}
