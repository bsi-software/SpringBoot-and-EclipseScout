package org.eclipse.scout.springboot.demo.spring.repository;

import java.util.UUID;

import org.eclipse.scout.springboot.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  User findByName(String name);

}
