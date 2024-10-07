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
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Criar role de SUPERUSER se não existir
        Role superUserRole = roleRepository.findByName("ROLE_SUPERUSER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_SUPERUSER")));

        // Criar o superusuário se ele não existir
        if (!userRepository.existsByUsername("gidaltibn")) {
            User superUser = new User();
            superUser.setUsername("gidaltibn");
            superUser.setPassword(passwordEncoder.encode("gidalti123"));
            superUser.setEmail("gidaltibn@outlook.com");
            Set<Role> roles = new HashSet<>();
            roles.add(superUserRole);
            superUser.setRoles(roles);

            userRepository.save(superUser);
            System.out.println("Superusuário criado com sucesso!");
        }

    }
}
