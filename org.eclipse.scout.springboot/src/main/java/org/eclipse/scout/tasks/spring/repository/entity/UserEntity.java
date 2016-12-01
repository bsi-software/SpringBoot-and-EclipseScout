package org.eclipse.scout.tasks.spring.repository.entity;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.scout.tasks.model.entity.User;
import org.eclipse.scout.tasks.spring.repository.entity.converter.LocaleConverter;
import org.eclipse.scout.tasks.spring.repository.entity.converter.UuidConverter;

@Entity
public class UserEntity extends User {

  @Id
  @Column(length = ID_LENGTH_MAX)
  @Override
  public String getId() {
    return super.getId();
  }

  @Column(nullable = false, length = NAME_LENGTH_MAX)
  @Override
  public String getFirstName() {
    return super.getFirstName();
  }

  @Column(length = NAME_LENGTH_MAX)
  @Override
  public String getLastName() {
    return super.getLastName();
  }

  @Column(nullable = false)
  @Override
  public String getPasswordHash() {
    return super.getPasswordHash();
  }

  @Column(nullable = false)
  @Override
  public String getPasswordSalt() {
    return super.getPasswordSalt();
  }

  @Convert(converter = LocaleConverter.class)
  @Column(nullable = false)
  @Override
  public Locale getLocale() {
    return super.getLocale();
  }

  @Convert(converter = UuidConverter.class)
  @Override
  public UUID getPictureId() {
    return super.getPictureId();
  }

  @ElementCollection
  @Override
  public Set<String> getRoles() {
    return super.getRoles();
  }

}
