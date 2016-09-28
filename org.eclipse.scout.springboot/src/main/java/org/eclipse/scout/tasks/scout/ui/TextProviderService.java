package org.eclipse.scout.tasks.scout.ui;

import org.eclipse.scout.rt.shared.services.common.text.AbstractDynamicNlsTextProviderService;

public class TextProviderService extends AbstractDynamicNlsTextProviderService {

  @Override
  public String getDynamicNlsBaseName() {
    return "org.eclipse.scout.tasks.scout.ui.nls.Texts";
  }
}
