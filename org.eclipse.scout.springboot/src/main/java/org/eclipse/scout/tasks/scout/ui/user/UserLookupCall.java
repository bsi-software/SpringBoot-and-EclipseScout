package org.eclipse.scout.tasks.scout.ui.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.eclipse.scout.tasks.data.User;
import org.eclipse.scout.tasks.spring.service.UserService;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserLookupCall extends LocalLookupCall<UUID> {

  @Inject
  UserService userService;

  private static final long serialVersionUID = 1L;

  @Override
  protected List<? extends ILookupRow<UUID>> execCreateLookupRows() {
    List<ILookupRow<UUID>> list = new ArrayList<>();

    for (User user : userService.getUsers()) {
      list.add(new LookupRow<>(user.getId(), user.toDisplayText()));
    }

    return list;
  }
}
