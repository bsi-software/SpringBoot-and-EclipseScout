package org.eclipse.scout.tasks.spring.repository;

import org.eclipse.scout.tasks.spring.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
}
