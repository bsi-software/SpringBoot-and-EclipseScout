package org.eclipse.scout.tasks.scout.ui.user;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.scout.rt.client.services.common.icon.AbstractIconProviderService;
import org.eclipse.scout.rt.client.services.common.icon.IconSpec;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.resource.BinaryResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1000)
@CreateImmediately // todo: check if necessary
public class UserPictureProviderService extends AbstractIconProviderService {

  private Map<String, BinaryResource> icons;

  public UserPictureProviderService() {
    icons = new ConcurrentHashMap<>();
  }

  @Override
  public IconSpec findIconSpec(String name) {
    BinaryResource resource = icons.get(name);

    if (resource == null) {
      return null;
    }

    return new IconSpec(name, resource.getContent());
  }

  public BinaryResource getBinaryResource(String name) {
    return icons.get(name);
  }

  @Override
  protected URL findResource(String relativePath) {
    log.warn("!!! returns null (not implemented) !!!");
    return null;
  }

  public void addUserPicture(String name, byte[] picture) {
    BinaryResource usericon = new BinaryResource(name, picture);
    icons.put(name, usericon);
  }
}
