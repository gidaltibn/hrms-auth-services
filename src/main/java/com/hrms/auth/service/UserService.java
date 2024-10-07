package com.hrms.auth.service;

import com.hrms.auth.model.Role;
import com.hrms.auth.model.User;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção via construtor para evitar referências circulares
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para carregar um usuário pelo nome de usuário (usado pelo Spring Security)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Usuário não encontrado com nome de usuário: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                getAuthorities(user.get())
        );
    }

    // Método para criar um novo usuário no sistema
    public User createUser(User user) {
        // Verificar se o usuário já existe
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Nome de usuário já existe: " + user.getUsername());
        }

        // Codificar a senha do usuário antes de salvar no banco de dados
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Verificar se a role do usuário existe
        Role role = roleRepository.findByName(user.getRoles().iterator().next().getName())
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + user.getRoles().iterator().next().getName()));

        // Adicionar a role ao usuário
        user.updateRole(role);

        // Salvar o usuário no banco de dados
        return userRepository.save(user);
    }


    // Método para retornar as permissões (roles) de um usuário
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))  // Aqui `role.getName()` deve funcionar
                .collect(Collectors.toList());
    }

}
