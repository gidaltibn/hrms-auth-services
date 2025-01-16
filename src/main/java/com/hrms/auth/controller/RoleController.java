package com.hrms.auth.controller;

import com.hrms.auth.model.Role;
import com.hrms.auth.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;  // Repositório para interagir com as roles (perfis de usuário)

    // Endpoint para criar uma nova role
    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody @Valid Role role, Authentication authentication) {
        // Verifica se o usuário autenticado tem o papel de ROLE_SUPERUSER ou ROLE_ADMIN

        // Exibe o principal (usuário logado) no log para verificação
        System.out.println("Principal: " + authentication.getPrincipal());

        // Confirma no log o recebimento do objeto role
        System.out.println("Role: " + role.getName());

        // Verifica se o usuário autenticado tem permissão (ROLE_SUPERUSER ou ROLE_ADMIN)
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            // Se a role já existir, retorna um erro
            if (roleRepository.existsByName(role.getName())) {
                return ResponseEntity.badRequest().body("Role já existe");
            }

            // Salva a nova role no banco de dados
            roleRepository.save(role);
            return ResponseEntity.ok("Role criada com sucesso");
        } else {
            // Se o usuário não tiver permissão, retorna um status de proibição
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para criar roles");
        }
    }

    // Endpoint para listar todas as roles
    @GetMapping("/list")
    public ResponseEntity<?> listRoles() {

        // Retorna a lista de todas as roles no sistema
        return ResponseEntity.ok(roleRepository.findAll());
    }

    // Endpoint para atualizar uma role existente
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody @Valid Role role, Authentication authentication) {
        // Verifica se o usuário autenticado tem o papel de ROLE_SUPERUSER ou ROLE_ADMIN

        // Exibe o principal (usuário logado) no log para verificação
        System.out.println("Principal: " + authentication.getPrincipal());

        // Confirma no log o recebimento do objeto role
        System.out.println("Role: " + role.getName());

        // Verifica se o usuário autenticado tem permissão (ROLE_SUPERUSER ou ROLE_ADMIN)
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            // Se a role com o ID fornecido existir, ela é atualizada
            if (roleRepository.existsById(id)) {
                Role roleToUpdate = roleRepository.getOne(id);
                roleToUpdate.setName(role.getName());
                roleRepository.save(roleToUpdate);
                return ResponseEntity.ok("Role atualizada com sucesso");
            } else {
                // Se a role não for encontrada, retorna um erro
                return ResponseEntity.badRequest().body("Role não encontrada");
            }
        } else {
            // Se o usuário não tiver permissão, retorna um status de proibição
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar roles");
        }
    }
}
