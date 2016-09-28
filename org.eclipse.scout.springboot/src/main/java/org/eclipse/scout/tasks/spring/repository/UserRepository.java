package org.eclipse.scout.tasks.spring.repository;

import java.util.UUID;

import org.eclipse.scout.tasks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  User findByName(String name);

}
