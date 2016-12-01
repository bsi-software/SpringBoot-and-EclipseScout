package org.eclipse.scout.tasks.spring.repository;

import org.eclipse.scout.tasks.spring.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
