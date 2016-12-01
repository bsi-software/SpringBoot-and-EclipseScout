package org.eclipse.scout.tasks.scout.ui.admin.db;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.Bean;

@Bean
public class H2ConsolePage extends AbstractPageWithNodes {

  //TODO [msm] add permissions to h2console

  @Override
  protected String getConfiguredTitle() {
    return "H2 console";
  }

  @Override
  protected Class<? extends IForm> getConfiguredDetailForm() {
    return H2ConsoleForm.class;
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }

}
