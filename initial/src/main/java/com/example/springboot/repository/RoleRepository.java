package com.example.springboot.repository;

import com.example.springboot.model.Role;
import com.example.springboot.model.RoleName;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// RoleRepository interface for managing Role entities.
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}
