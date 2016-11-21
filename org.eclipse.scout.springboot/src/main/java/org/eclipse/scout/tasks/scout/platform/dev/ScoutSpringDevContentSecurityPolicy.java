package org.eclipse.scout.tasks.scout.platform.dev;

import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.server.commons.servlet.ContentSecurityPolicy;

@Replace
public class ScoutSpringDevContentSecurityPolicy extends ContentSecurityPolicy {

  @Override
  protected void initDirectives() {
    super.initDirectives();
    // allow Websockets for LiveReload mechanism
    if (Platform.get().inDevelopmentMode()) {
      withConnectSrc("*");
    }
  }

}
