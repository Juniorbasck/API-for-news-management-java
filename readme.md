# Documentação da API

## Instale as Dependências
```bash
mvn clean install
```

## Execute o Servidor
```bash
mvn spring-boot:run
```

## 📚 Endpoints

### 1. Autenticação de Usuário
- **Rota:** `/users/login`
- **Método:** `POST`
- **Descrição:** Autentica um usuário com base no e-mail e senha.

#### Exemplo de Requisição:
```json
{
  "email": "admin@example.com",
  "senha": "123"
}
```

#### Exemplo de Resposta:
```json
{
  "status": 200,
  "message": "Usuário autenticado com sucesso",
  "data": {
    "id": 1,
    "nome": "Administrador",
    "email": "admin@example.com",
    "isadmin": true
  }
}
```

### 2. Salvar Notícias
- **Rota:** `/news/save`
- **Método:** `POST`
- **Descrição:** Busca notícias de uma API externa e salva no banco de dados.

#### Exemplo de Resposta:
```json
{
  "status": 200,
  "message": "Notícias salvas com sucesso!"
}
```

### 3. Listar Usuários
- **Rota:** `/users`
- **Método:** `GET`
- **Descrição:** Retorna todos os usuários cadastrados no banco de dados.

#### Exemplo de Resposta:
```json
{
  "status": 200,
  "data": [
    {
      "id": 1,
      "nome": "Administrador",
      "email": "admin@example.com",
      "isadmin": true
    },
    {
      "id": 2,
      "nome": "Usuário Normal",
      "email": "user@example.com",
      "isadmin": false
    }
  ]
}
```

### 4. Curtir uma Notícia
- **Rota:** `/news/like`
- **Método:** `PUT`
- **Descrição:** Incrementa o número de curtidas de uma notícia.

#### Parâmetros na Query String:
- `id`: ID da notícia a ser curtida.

#### Exemplo de Resposta:
```json
{
  "status": 200,
  "message": "Notícia curtida com sucesso!"
}
```

### 5. Editar uma Notícia
- **Rota:** `/news/edit`
- **Método:** `PUT`
- **Descrição:** Permite editar os dados de uma notícia, mas somente usuários administradores podem realizar essa ação.

#### Parâmetros na Query String:
- `userId`: ID do usuário que está tentando editar a notícia.
- `newsId`: ID da notícia a ser editada.

#### Exemplo de Requisição (JSON no corpo):
```json
{
  "title": "Título Atualizado",
  "abstract": "Resumo atualizado da notícia."
}
```

#### Exemplo de Resposta:
```json
{
  "status": 200,
  "message": "Notícia atualizada com sucesso!"
}
```

### 6. Excluir uma Notícia
- **Rota:** `/news/delete`
- **Método:** `DELETE`
- **Descrição:** Exclui uma notícia do banco de dados. Somente administradores podem realizar essa ação.

#### Parâmetros na Query String:
- `userId`: ID do usuário que está tentando excluir a notícia.
- `newsId`: ID da notícia a ser excluída.

#### Exemplo de Requisição:
```bash
http://localhost:8080/news/delete?userId=1&newsId=10
```

#### Exemplo de Resposta:
```json
{
  "status": 200,
  "message": "Notícia excluída com sucesso."
}
```

## 🗊 Estrutura do Banco de Dados

### Tabela `usuarios`
| Campo   | Tipo     | Descrição               |
|---------|----------|-------------------------|
| id      | SERIAL   | Identificador único     |
| nome    | TEXT     | Nome do usuário         |
| email   | TEXT     | E-mail único do usuário |
| senha   | TEXT     | Senha do usuário        |
| isadmin | BOOLEAN  | Se o usuário é admin    |

### Tabela `noticias`
| Campo           | Tipo       | Descrição                   |
|------------------|------------|-----------------------------|
| id              | SERIAL     | Identificador único         |
| title           | TEXT       | Título da notícia           |
| abstract        | TEXT       | Resumo da notícia           |
| url             | TEXT       | URL única da notícia        |
| published_date  | TIMESTAMP  | Data de publicação          |
| source          | TEXT       | Fonte da notícia            |
| likes           | INTEGER    | Número de curtidas          |
| created_at      | TIMESTAMP  | Data de criação do registro |

## 🛠️ Desenvolvimento

### Principais Arquivos
- **`NewsApiApplication.java`**: Classe principal que inicializa o servidor e carrega o contexto.
- **`UserController.java`**: Define os endpoints relacionados a usuários.
- **`NewsController.java`**: Define os endpoints relacionados a notícias.
- **`UserService.java`**: Contém a lógica de negócios para os usuários.
- **`NewsService.java`**: Contém a lógica de negócios para as notícias.

### Testar Localmente
Use ferramentas como Postman ou cURL para testar os endpoints:

```bash
curl -X POST http://localhost:8080/users/login -H "Content-Type: application/json" -d '{"email": "admin@example.com", "senha": "123"}'
```

## 🖋️ Licença
Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.
