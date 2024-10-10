package com.hrms.auth.repository;

import com.hrms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interface UserRepository estende JpaRepository, que fornece métodos padrão para interagir com o banco de dados
public interface UserRepository extends JpaRepository<User, Long> {

    // Método para buscar um usuário pelo nome de usuário (username)
    // Retorna um Optional<User> que pode conter o usuário encontrado ou estar vazio se o usuário não for encontrado
    Optional<User> findByUsername(String username);

    // Método para verificar se um usuário com determinado nome de usuário já existe
    // Retorna um booleano indicando se o nome de usuário já está registrado
    boolean existsByUsername(String username);
}
