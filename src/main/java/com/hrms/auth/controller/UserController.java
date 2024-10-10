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
    private UserRepository userRepository;  // Repositório para interagir com os usuários

    // Método para atualizar um usuário com base no ID
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody @Valid User user, Authentication authentication) {
        // Verifica se o usuário autenticado tem as permissões adequadas (ROLE_SUPERUSER, ROLE_ADMIN, ou ROLE_USER)
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_USER"))) {

            // Verifica se o usuário existe
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }

            // Atualiza os dados do usuário encontrado
            User userToUpdate = userOptional.get();
            Role role = userToUpdate.getRoles().iterator().next();  // Obtém o primeiro role do usuário

            // Atualiza cada campo do usuário se não for nulo no request
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
                userToUpdate.setPassword(user.getPassword());  // Idealmente, deve ser criptografado
            }
            if (user.getRoles() != null) {
                userToUpdate.updateRole(role);  // Atualiza os papéis (roles) do usuário
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

            // Salva as alterações no banco de dados
            userRepository.save(userToUpdate);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        } else {
            // Caso o usuário não tenha permissão, retorna uma resposta de proibição
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar usuários");
        }
    }

    // Método para deletar um usuário com base no ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        // Verifica se o usuário autenticado tem as permissões adequadas (ROLE_SUPERUSER, ROLE_ADMIN, ou ROLE_USER)
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_USER"))) {

            // Verifica se o usuário existe
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }

            // Deleta o usuário
            userRepository.delete(userOptional.get());
            return ResponseEntity.ok("Usuário deletado com sucesso");
        } else {
            // Caso o usuário não tenha permissão, retorna uma resposta de proibição
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para deletar usuários");
        }
    }

    // Método para listar todos os usuários
    @GetMapping("/list")
    public ResponseEntity<?> listUsers(Authentication authentication) {
        // Verifica se o usuário autenticado tem as permissões adequadas (ROLE_SUPERUSER, ROLE_ADMIN, ou ROLE_USER)
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_USER"))) {

            // Retorna a lista de todos os usuários
            return ResponseEntity.ok(userRepository.findAll());
        } else {
            // Caso o usuário não tenha permissão, retorna uma resposta de proibição
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para listar usuários");
        }
    }
}
