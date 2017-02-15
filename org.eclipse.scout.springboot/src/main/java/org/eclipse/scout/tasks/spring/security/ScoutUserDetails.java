package org.eclipse.scout.tasks.spring.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.tasks.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

class ScoutUserDetails implements UserDetails {
  private static final long serialVersionUID = -62907629237667118L;

  private User user;
  private Set<SimpleGrantedAuthority> authorities;

  public ScoutUserDetails(User user, Set<String> set) {
    this.user = user;
    this.authorities = set.stream()
        .map(p -> new SimpleGrantedAuthority(p))
        .collect(Collectors.toSet());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPasswordHash();
  }

  @Override
  public String getUsername() {
    return user.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

}
