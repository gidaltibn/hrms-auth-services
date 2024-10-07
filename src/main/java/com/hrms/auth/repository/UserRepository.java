package com.hrms.auth.repository;

import com.hrms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Método para buscar um usuário pelo nome de usuário (username)
    Optional<User> findByUsername(String username);

    // Método para verificar se um usuário com determinado nome de usuário já existe
    boolean existsByUsername(String username);
}
