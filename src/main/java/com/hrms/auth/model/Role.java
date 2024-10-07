package com.hrms.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Role(String roleSuperuser) {
        this.name = roleSuperuser;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Role role = (Role) obj;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean isUser() {
        return name.equals("ROLE_USER");
    }

    public boolean isAdmin() {
        return name.equals("ROLE_ADMIN");
    }

    public boolean is(String roleName) {
        return name.equals(roleName);
    }

    public String getName() {
        return name;
    }
}
