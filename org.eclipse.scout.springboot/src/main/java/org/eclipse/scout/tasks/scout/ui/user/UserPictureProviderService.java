package org.eclipse.scout.tasks.scout.ui.user;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.tasks.spring.service.UserService;

public class UserPictureProviderService implements IService {

  @Inject
  private UserService userService;

  protected Map<UUID, BinaryResource> userPictureCache = new ConcurrentHashMap<>();

  public BinaryResource getUserPicture(UUID user) {
    // try to resolve from cache
    if (userPictureCache.containsKey(user)) {
      return userPictureCache.get(user);
    }
    else {
      // try to resolve from data store
      byte[] picture = userService.getUserPicture(user);
      if (picture != null) {
        final BinaryResource binaryResource = new BinaryResource(user.toString(), picture);
        userPictureCache.put(user, binaryResource);
        return binaryResource;
      }
      return null;
    }
  }

  public void setUserPicture(UUID user, byte[] picture) {
    // store picture in data store
    userService.setUserPicture(user, picture);
    // store picture in cache
    BinaryResource binaryResource = new BinaryResource(user.toString(), picture);
    userPictureCache.put(user, binaryResource);
  }
}
