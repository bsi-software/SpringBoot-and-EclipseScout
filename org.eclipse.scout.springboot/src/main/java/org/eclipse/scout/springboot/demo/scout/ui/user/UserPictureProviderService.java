package org.eclipse.scout.springboot.demo.scout.ui.user;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.scout.rt.client.services.common.icon.AbstractIconProviderService;
import org.eclipse.scout.rt.client.services.common.icon.IconSpec;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1000)
@CreateImmediately // todo: check if necessary
public class UserPictureProviderService extends AbstractIconProviderService {

  private Map<String, IconSpec> icons;

  public UserPictureProviderService() {
    icons = new ConcurrentHashMap<>();
  }

  @Override
  public IconSpec findIconSpec(String iconName) {
    log.info("get icon spec for '" + iconName + "'");
    return icons.get(iconName);
  }

  @Override
  protected URL findResource(String relativePath) {
    log.info("!!! findResource returns null !!!");
    return null;
  }

  public void addUserPicture(String username, byte[] picture) {
    IconSpec usericon = new IconSpec();
    usericon.setContent(picture);
    usericon.setName(username);

    icons.put(username, usericon);

    log.info("user picture added for user '" + username + "'");
  }
}
