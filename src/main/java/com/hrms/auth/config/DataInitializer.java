package com.hrms.auth.config;

import com.hrms.auth.model.Role;
import com.hrms.auth.model.User;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;  // Injeção de dependência do repositório de usuários

    @Autowired
    private RoleRepository roleRepository;  // Injeção de dependência do repositório de roles

    @Autowired
    private PasswordEncoder passwordEncoder;  // Injeção de dependência para codificar senhas

    @Override
    public void run(String... args) throws Exception {
        // Criar role de SUPERUSER se ela ainda não existir no banco de dados
        Role superUserRole = roleRepository.findByName("ROLE_SUPERUSER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_SUPERUSER")));

        // Verificar se o superusuário já existe. Se não existir, criar um novo.
        if (!userRepository.existsByUsername("gidaltibn")) {
            User superUser = new User();  // Instanciar novo usuário
            superUser.setUsername("gidaltibn");  // Definir o nome de usuário
            superUser.setPassword(passwordEncoder.encode("gidalti123"));  // Codificar a senha do superusuário
            superUser.setEmail("gidaltibn@outlook.com");  // Definir o e-mail do superusuário

            // Atribuir a role de SUPERUSER ao novo usuário
            Set<Role> roles = new HashSet<>();
            roles.add(superUserRole);
            superUser.setRoles(roles);  // Associar a role ao superusuário

            // Salvar o superusuário no banco de dados
            userRepository.save(superUser);
            System.out.println("Superusuário criado com sucesso!");
        }
    }
}
