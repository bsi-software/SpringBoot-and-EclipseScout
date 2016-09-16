package org.eclipse.scout.springboot.ui;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.shared.TEXTS;

public class HomeOutline extends AbstractOutline {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Home");
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.Category;
  }
}
