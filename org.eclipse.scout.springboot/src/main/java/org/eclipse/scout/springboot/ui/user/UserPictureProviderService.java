package org.eclipse.scout.springboot.ui.user;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.scout.rt.client.services.common.icon.AbstractIconProviderService;
import org.eclipse.scout.rt.client.services.common.icon.IconSpec;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// source: https://github.com/BSI-Business-Systems-Integration-AG/org.eclipsescout.demo/blob/4.0/widgets/org.eclipsescout.demo.widgets.client/src/org/eclipsescout/demo/widgets/client/services/FontIconProviderService.java
@Order(0)
@CreateImmediately // todo: check if necessary
public class UserPictureProviderService extends AbstractIconProviderService {
  private static final Logger LOG = LoggerFactory.getLogger(UserPictureProviderService.class);

  private Map<String, IconSpec> icons;

  public UserPictureProviderService() {
    icons = new ConcurrentHashMap<String, IconSpec>();
  }

  @Override
  public IconSpec findIconSpec(String iconName) {
    LOG.info("get icon spec for '" + iconName + "'");
    return icons.get(iconName);
  }

  @Override
  protected URL findResource(String relativePath) {
    LOG.info("!!! findResource returns null !!!");
    return null;
  }

  public void addUserPicture(String username, byte[] picture) {
    IconSpec usericon = new IconSpec();
    usericon.setContent(picture);
    usericon.setName(username);

    icons.put(username, usericon);

    LOG.info("user picture added for user '" + username + "'");
  }
}
