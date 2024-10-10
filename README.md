```markdown
# Auth Service

Este é o serviço de autenticação para o sistema de recursos humanos (HRMS). Ele fornece endpoints para autenticação de usuários, gerenciamento de papéis (roles) e controle de acesso.

## Requisitos

- Java 17 ou superior
- Maven 3.6+
- Banco de dados MySQL 8.0+
- Spring Boot 6.1.13

## Configurações

### Banco de Dados

Este projeto usa MySQL como banco de dados principal. Certifique-se de configurar corretamente as credenciais de banco de dados no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/authdb
spring.datasource.username=root
spring.datasource.password=rootpassword
spring.jpa.hibernate.ddl-auto=update
```

### Dependências Principais

- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Spring Security**: Para gerenciamento de autenticação e autorização.
- **JWT (Json Web Token)**: Para autenticação baseada em token.
- **Hibernate**: Para interação com o banco de dados MySQL.
- **HikariCP**: Para gerenciamento de conexões ao banco de dados.

### Rodando o Projeto

1. Clone o repositório:

```bash
git clone https://github.com/your-username/auth-service.git
```

2. Compile o projeto com Maven:

```bash
mvn clean install
```

3. Execute o serviço:

```bash
mvn spring-boot:run
```

## Endpoints

### **AuthController**

- **`POST /auth/signup`**  
  **Descrição:** Cria um novo usuário no sistema.  
  **Exemplo de corpo da requisição:**
  ```json
  {
      "name": "John Doe",
      "username": "johndoe",
      "password": "password123",
      "email": "johndoe@example.com",
      "cpf": "12345678901",
      "phone": "5511999999999",
      "dataNascimento": "1990-01-01",
      "role": ["ROLE_USER"]
  }
  ```
  **Resposta de sucesso:**  
  `200 OK`  
  `"Usuário criado com sucesso!"`

  **Resposta de erro:**  
  `400 Bad Request`  
  `"O nome de usuário já está em uso."`

- **`POST /auth/login`**  
  **Descrição:** Realiza login de um usuário, autenticando-o e gerando um token JWT.  
  **Exemplo de corpo da requisição:**
  ```json
  {
      "username": "johndoe",
      "password": "password123"
  }
  ```
  **Resposta de sucesso:**  
  `200 OK`
  ```json
  {
      "token": "eyJhbGciOiJIUzUxMiJ9..."
  }
  ```

  **Resposta de erro:**  
  `401 Unauthorized`  
  `"Credenciais inválidas"`

- **`POST /auth/logout`**  
  **Descrição:** Efetua o logout de um usuário.  
  **Resposta de sucesso:**  
  `200 OK`  
  `"Logout realizado com sucesso."`

### **RoleController**

- **`POST /roles/create`**  
  **Descrição:** Cria uma nova role no sistema.  
  **Exemplo de corpo da requisição:**
  ```json
  {
      "name": "ROLE_ADMIN"
  }
  ```
  **Resposta de sucesso:**  
  `200 OK`  
  `"Role criada com sucesso"`

  **Resposta de erro:**  
  `400 Bad Request`  
  `"Role já existe"`

  **Resposta de erro:**  
  `403 Forbidden`  
  `"Você não tem permissão para criar roles"`

- **`GET /roles/list`**  
  **Descrição:** Retorna a lista de roles cadastradas no sistema.  
  **Resposta de sucesso:**  
  `200 OK`
  ```json
  [
    {
      "id": 1,
      "name": "ROLE_USER"
    },
    {
      "id": 2,
      "name": "ROLE_ADMIN"
    }
  ]
  ```

- **`PUT /roles/update/{id}`**  
  **Descrição:** Atualiza uma role existente com o ID fornecido.  
  **Exemplo de corpo da requisição:**
  ```json
  {
      "name": "ROLE_SUPERUSER"
  }
  ```
  **Resposta de sucesso:**  
  `200 OK`  
  `"Role atualizada com sucesso"`

  **Resposta de erro:**  
  `400 Bad Request`  
  `"Role não encontrada"`

  **Resposta de erro:**  
  `403 Forbidden`  
  `"Você não tem permissão para atualizar roles"`

### **UserController**

- **`PUT /users/update/{id}`**  
  **Descrição:** Atualiza as informações de um usuário existente.  
  **Exemplo de corpo da requisição:**
  ```json
  {
      "name": "John Doe",
      "email": "johndoe@example.com",
      "phone": "5511999999999"
  }
  ```
  **Resposta de sucesso:**  
  `200 OK`  
  `"Usuário atualizado com sucesso"`

  **Resposta de erro:**  
  `400 Bad Request`  
  `"Usuário não encontrado"`

  **Resposta de erro:**  
  `403 Forbidden`  
  `"Você não tem permissão para atualizar usuários"`

- **`DELETE /users/delete/{id}`**  
  **Descrição:** Deleta um usuário com o ID fornecido.  
  **Resposta de sucesso:**  
  `200 OK`  
  `"Usuário deletado com sucesso"`

  **Resposta de erro:**  
  `400 Bad Request`  
  `"Usuário não encontrado"`

  **Resposta de erro:**  
  `403 Forbidden`  
  `"Você não tem permissão para deletar usuários"`

- **`GET /users/list`**  
  **Descrição:** Retorna uma lista de todos os usuários cadastrados.  
  **Resposta de sucesso:**  
  `200 OK`
  ```json
  [
    {
      "id": 1,
      "name": "John Doe",
      "username": "johndoe",
      "email": "johndoe@example.com"
    }
  ]
  ```

  **Resposta de erro:**  
  `403 Forbidden`  
  `"Você não tem permissão para listar usuários"`

## Segurança

Este projeto utiliza o Spring Security com autenticação JWT. As credenciais de login são verificadas e, se válidas, um token JWT é retornado. O token deve ser incluído no header `Authorization` das requisições subsequentes no seguinte formato:

```http
Authorization: Bearer <TOKEN>
```

## Considerações Finais

Este serviço de autenticação é parte fundamental do sistema de gerenciamento de usuários. Ele oferece uma base segura e escalável para autenticação e autorização, usando as melhores práticas de segurança e frameworks de mercado.
```

Você pode usar este `README.md` como base para documentar seu projeto. Se houver mais endpoints ou alterações no serviço, você pode atualizar esta documentação conforme necessário.