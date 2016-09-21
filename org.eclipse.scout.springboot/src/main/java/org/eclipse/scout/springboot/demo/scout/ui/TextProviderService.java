package org.eclipse.scout.springboot.demo.scout.ui;

import org.eclipse.scout.rt.shared.services.common.text.AbstractDynamicNlsTextProviderService;

public class TextProviderService extends AbstractDynamicNlsTextProviderService {

  @Override
  public String getDynamicNlsBaseName() {
    return "org.eclipse.scout.springboot.ui.nls.Texts";
  }
}
