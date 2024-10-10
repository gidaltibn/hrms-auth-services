package com.hrms.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// A anotação @Entity indica que esta classe é uma entidade JPA (Java Persistence API), ou seja, será mapeada para uma tabela no banco de dados
@Entity
@Getter // Lombok - Gera automaticamente os métodos getter para todos os campos
@Setter // Lombok - Gera automaticamente os métodos setter para todos os campos
@NoArgsConstructor // Lombok - Gera um construtor sem argumentos
public class Role {

    // Anotação @Id define o campo 'id' como chave primária da tabela
    // Anotação @GeneratedValue define a estratégia de geração automática do valor da chave primária (IDENTITY significa que o banco de dados gerará um valor único)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotação @Column define as propriedades da coluna 'name' no banco de dados
    // unique = true -> valores devem ser únicos
    // nullable = false -> não permite valores nulos
    @Column(unique = true, nullable = false)
    private String name;

    // Construtor que permite a criação de um objeto 'Role' com um nome de role específico
    public Role(String roleSuperuser) {
        this.name = roleSuperuser;
    }

    // Sobrescrita do método toString para retornar o nome da role quando o objeto for convertido em string
    @Override
    public String toString() {
        return name;
    }

    // Sobrescrita do método equals para verificar a igualdade entre dois objetos 'Role' com base no nome da role
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Se for o mesmo objeto, retorna true
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Se for nulo ou de outra classe, retorna false
        }
        Role role = (Role) obj;
        return name.equals(role.name); // Compara os nomes das roles
    }

    // Sobrescrita do método hashCode, garantindo que dois objetos iguais tenham o mesmo hash
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    // Método auxiliar para verificar se o nome da role é "ROLE_USER"
    public boolean isUser() {
        return name.equals("ROLE_USER");
    }

    // Método auxiliar para verificar se o nome da role é "ROLE_ADMIN"
    public boolean isAdmin() {
        return name.equals("ROLE_ADMIN");
    }

    // Método genérico que permite verificar se o nome da role é igual a um valor fornecido
    public boolean is(String roleName) {
        return name.equals(roleName);
    }

    // Sobrescrevendo o getter para a propriedade 'name'
    public String getName() {
        return name;
    }
}
