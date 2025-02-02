package com.tutorial.authorization_server.repository;

import com.tutorial.authorization_server.entity.Role;
import com.tutorial.authorization_server.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRole(RoleName roleName);
}
