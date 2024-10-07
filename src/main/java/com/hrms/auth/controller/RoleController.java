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
    private RoleRepository roleRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody @Valid Role role, Authentication authentication) {
        // Verificar se o usuário autenticado tem ROLE_SUPERUSER ou ROLE_ADMIN


        //CONFIRMAR NO LOG O RECEBIMENTO DOS PARÂMETROS E DO TOKEN PELO AUTHORIZATION
        //System.out.println("Token: " + authentication.getCredentials());
        System.out.println("Principal: " + authentication.getPrincipal());

        //CONFIRMAR NO LOG O RECEBIMENTO DO OBJETO ROLE
        System.out.println("Role: " + role.getName());
        System.out.println("Role: " + role.getName());

        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            if (roleRepository.existsByName(role.getName())) {
                return ResponseEntity.badRequest().body("Role já existe");
            }

            roleRepository.save(role);
            return ResponseEntity.ok("Role criada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para criar roles");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listRoles(Principal principal) {
        //CONFIRMAR NO LOG O RECEBIMENTO DO PRINCIPAL
        System.out.println("Principal: " + principal.getName());

        return ResponseEntity.ok(roleRepository.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody @Valid Role role, Authentication authentication) {
        // Verificar se o usuário autenticado tem ROLE_SUPERUSER ou ROLE_ADMIN

        //CONFIRMAR NO LOG O RECEBIMENTO DOS PARÂMETROS E DO TOKEN PELO AUTHORIZATION
        //System.out.println("Token: " + authentication.getCredentials());
        System.out.println("Principal: " + authentication.getPrincipal());

        //CONFIRMAR NO LOG O RECEBIMENTO DO OBJETO ROLE
        System.out.println("Role: " + role.getName());
        System.out.println("Role: " + role.getName());

        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_SUPERUSER") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            if (roleRepository.existsById(id)) {
                Role roleToUpdate = roleRepository.getOne(id);
                roleToUpdate.setName(role.getName());
                roleRepository.save(roleToUpdate);
                return ResponseEntity.ok("Role atualizada com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Role não encontrada");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar roles");
        }
    }
}

