package com.hrms.auth.repository;

import com.hrms.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interface RoleRepository estende JpaRepository, que fornece métodos padrão para interagir com o banco de dados
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método para buscar um papel (role) pelo nome
    // O uso de Optional evita problemas com valores nulos, retornando um Optional que pode ou não conter o valor
    Optional<Role> findByName(String name);

    // Método para verificar se um papel (role) existe no banco de dados com base no nome
    // Retorna um booleano indicando se o nome do role já está registrado
    boolean existsByName(String name);
}
