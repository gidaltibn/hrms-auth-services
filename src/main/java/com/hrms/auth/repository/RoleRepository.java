package com.hrms.auth.repository;

import com.hrms.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método para buscar um papel (role) pelo nome
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
