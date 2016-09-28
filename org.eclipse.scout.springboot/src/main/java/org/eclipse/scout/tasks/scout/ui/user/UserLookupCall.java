package org.eclipse.scout.tasks.scout.ui.user;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.eclipse.scout.tasks.model.User;
import org.eclipse.scout.tasks.spring.service.UserService;

public class UserLookupCall extends LocalLookupCall<User> {

  @Inject
  UserService userService;

  private static final long serialVersionUID = 1L;

  @Override
  protected List<? extends ILookupRow<User>> execCreateLookupRows() {
    List<ILookupRow<User>> list = new ArrayList<>();

    for (User user : userService.getUsers()) {
      list.add(new LookupRow<>(user, user.toDisplayText()));
    }

    return list;
  }
}
