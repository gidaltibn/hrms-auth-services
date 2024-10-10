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

@Service  // Indica que esta classe é um serviço que contém lógica de negócio e será gerenciada pelo Spring.
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção via construtor para evitar dependências circulares e garantir que as dependências sejam injetadas corretamente.
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Sobrescreve o método `loadUserByUsername` do `UserDetailsService` para carregar o usuário pelo nome de usuário.
    // Usado pelo Spring Security para autenticar o usuário.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);  // Busca o usuário pelo nome de usuário.

        if (!user.isPresent()) {
            // Lança uma exceção caso o usuário não seja encontrado.
            throw new UsernameNotFoundException("Usuário não encontrado com nome de usuário: " + username);
        }

        // Retorna um objeto `UserDetails` que contém o nome de usuário, senha e permissões (roles).
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                getAuthorities(user.get())  // Busca as permissões (roles) do usuário.
        );
    }

    // Método para criar um novo usuário no sistema.
    public User createUser(User user) {
        // Verifica se o nome de usuário já existe no banco de dados.
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Nome de usuário já existe: " + user.getUsername());
        }

        // Codifica a senha do usuário antes de salvar no banco de dados.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Verifica se a role associada ao usuário existe no banco de dados.
        Role role = roleRepository.findByName(user.getRoles().iterator().next().getName())
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + user.getRoles().iterator().next().getName()));

        // Atualiza a role do usuário.
        user.updateRole(role);

        // Salva o usuário no banco de dados e retorna o objeto salvo.
        return userRepository.save(user);
    }

    // Método privado para obter as permissões (roles) de um usuário.
    // O Spring Security usa essas permissões para aplicar regras de segurança no sistema.
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))  // Converte cada role em uma autoridade reconhecida pelo Spring Security.
                .collect(Collectors.toList());  // Converte a lista de roles em uma lista de autoridades.
    }
}
