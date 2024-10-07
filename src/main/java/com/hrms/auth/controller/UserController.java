package com.hrms.auth.controller;

import com.hrms.auth.model.Role;
import com.hrms.auth.model.User;
import com.hrms.auth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Métodos de CRUD
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody @Valid User user, Authentication authentication) {
        // Verificar se o usuário autenticado tem ROLE_SUPERUSER ou ROLE_ADMIN
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                    grantedAuthority.getAuthority().equals("ROLE_USER"))) {
            // Verificar se o usuário existe
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            User userToUpdate = userOptional.get();
            Role role = userToUpdate.getRoles().iterator().next();

            // Atualizar os campos do usuário
            if (user.getName() != null) {
                userToUpdate.setName(user.getName());
            }
            if (user.getUsername() != null) {
                userToUpdate.setUsername(user.getUsername());
            }
            if (user.getEmail() != null) {
                userToUpdate.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                userToUpdate.setPassword(user.getPassword());
            }
            if (user.getRoles() != null) {
                userToUpdate.updateRole(role);
            }
            if (user.getConselho() != null) {
                userToUpdate.setConselho(user.getConselho());
            }
            if (user.getCpf() != null) {
                userToUpdate.setCpf(user.getCpf());
            }
            if (user.getPhone() != null) {
                userToUpdate.setPhone(user.getPhone());
            }
            if (user.getDataNascimento() != null) {
                userToUpdate.setDataNascimento(user.getDataNascimento());
            }


            userRepository.save(userToUpdate);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar usuários");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        // Verificar se o usuário autenticado tem ROLE_SUPERUSER ou ROLE_ADMIN
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                    grantedAuthority.getAuthority().equals("ROLE_USER"))) {
            // Verificar se o usuário existe
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            userRepository.delete(userOptional.get());
            return ResponseEntity.ok("Usuário deletado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para deletar usuários");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listUsers(Authentication authentication) {
        // Verificar se o usuário autenticado tem ROLE_SUPERUSER ou ROLE_ADMIN
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                    grantedAuthority.getAuthority().equals("ROLE_USER"))) {
            return ResponseEntity.ok(userRepository.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para listar usuários");
        }
    }
}
