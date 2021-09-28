package com.circle.video.repository;

import com.circle.video.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Short> {
  List<Role> findAllByOrderByRoleNameDesc();
}
