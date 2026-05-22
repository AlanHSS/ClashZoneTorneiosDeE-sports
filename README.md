# ClashZone

ClashZone é uma aplicação para gerenciamento de torneios de e-sports. O projeto possui uma API REST em Java com Spring Boot, frontend em Angular e banco de dados PostgreSQL, com execução local facilitada via Docker Compose.

## Visão Geral

A aplicação permite cadastrar usuários, criar equipes, gerenciar membros, criar torneios, realizar inscrições e controlar o fluxo de aprovação das equipes inscritas.

Principais funcionalidades:

- Autenticação com JWT.
- Cadastro e login de usuários.
- Gerenciamento de torneios.
- Filtros e paginação para torneios.
- Gerenciamento de equipes.
- Gerenciamento de membros da equipe.
- Inscrição de equipes em torneios.
- Aprovação e recusa de inscrições.
- Perfis de usuário.
- Atualização automática de status de torneios.

## Stack

### Backend

- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT
- Lombok
- Maven

### Frontend

- Angular 21
- TypeScript
- Angular Material
- SCSS

### Infraestrutura

- Docker
- Docker Compose
- PostgreSQL 16

## Estrutura do Projeto

```text
ClashZone/
├── backend/
│   ├── src/main/java/com/alanhss/ClashZone/
│   │   ├── core/
│   │   │   ├── domain/
│   │   │   ├── enums/
│   │   │   ├── exceptions/
│   │   │   ├── gateway/
│   │   │   └── usecases/
│   │   └── infra/
│   │       ├── beans/
│   │       ├── dtos/
│   │       ├── exceptionHandlers/
│   │       ├── gateway/
│   │       ├── mappers/
│   │       ├── persistence/
│   │       ├── presentation/
│   │       └── security/
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/app/
│   │   ├── core/
│   │   ├── features/
│   │   ├── layout/
│   │   ├── pages/
│   │   └── shared/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── docs/
│   └── postman/
│       └── ClashZone.postman_collection.json
│
├── docker-compose.yml
├── docker-compose.dev.yml
└── README.md
```

## Arquitetura do Backend

O backend segue uma separação inspirada em Clean Architecture:

- `core/domain`: modelos de domínio da aplicação.
- `core/usecases`: regras de negócio e casos de uso.
- `core/gateway`: contratos que o domínio usa para acessar dados.
- `infra/persistence`: entidades JPA e repositórios.
- `infra/gateway`: implementação dos gateways usando persistência.
- `infra/presentation`: controllers REST.
- `infra/dtos`: objetos de entrada e saída da API.
- `infra/mappers`: conversão entre DTOs, entidades e domínio.
- `infra/security`: autenticação, autorização e JWT.
- `infra/exceptionHandlers`: tratamento global de exceções.

Essa estrutura ajuda a manter as regras de negócio menos dependentes de detalhes externos como banco de dados, HTTP e frameworks.

## Como Rodar com Docker

Suba os containers:

```bash
docker compose up --build
```

Serviços disponíveis:

- Frontend: `http://localhost:4200`
- Backend: `http://localhost:8058`
- PostgreSQL: `localhost:5432`

## Como Rodar o Backend Localmente

Entre na pasta do backend:

```bash
cd backend
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

O backend roda por padrão na porta `8058`.

## Como Rodar o Frontend Localmente

Entre na pasta do frontend:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Execute o servidor de desenvolvimento:

```bash
npm start
```

O frontend roda por padrão em `http://localhost:4200`.

## Autenticação

A API usa autenticação JWT. Após login, o token retornado deve ser enviado nas rotas protegidas usando o header:

```http
Authorization: Bearer <token>
```

## Coleção Postman

O repositório inclui uma coleção do Postman com endpoints da API:

```text
docs/postman/ClashZone.postman_collection.json
```

Para usar, importe esse arquivo no Postman e ajuste o ambiente/base URL conforme sua execução local.

## Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/clashzone/auth/register` | Cadastra um novo usuário |
| POST | `/clashzone/auth/login` | Autentica o usuário e retorna o token JWT |

### Usuários

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | `/clashzone/usuarios/listartodosusuarios` | Lista todos os usuários |
| GET | `/clashzone/usuarios/userprofile/{id}` | Busca o perfil completo de um usuário |
| GET | `/clashzone/usuarios/public/{id}` | Busca dados públicos de um usuário |
| PATCH | `/clashzone/usuarios/atualizarusuario/{id}` | Atualiza dados do usuário |
| DELETE | `/clashzone/usuarios/deletarusuario/{id}` | Remove um usuário |

### Torneios

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/clashzone/torneios/criartorneio` | Cria um torneio |
| GET | `/clashzone/torneios/listartorneios` | Lista torneios |
| GET | `/clashzone/torneios/listartorneios/paginado` | Lista torneios com paginação |
| POST | `/clashzone/torneios/torneiosfiltrados` | Lista torneios filtrados |
| POST | `/clashzone/torneios/torneiosfiltrados/paginado` | Lista torneios filtrados com paginação |
| GET | `/clashzone/torneios/paginadotorneio/{id}` | Busca torneio por ID |
| GET | `/clashzone/torneios/meustorneios` | Lista torneios criados pelo usuário autenticado |
| PATCH | `/clashzone/torneios/atualizartorneio/{id}` | Atualiza um torneio |

### Equipes

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/clashzone/equipes/criarequipe` | Cria uma equipe |
| GET | `/clashzone/equipes/listartodasequipes` | Lista todas as equipes |
| GET | `/clashzone/equipes/listartodasequipes/paginado` | Lista equipes com paginação |
| GET | `/clashzone/equipes/informacoesdaequipe/{id}` | Busca detalhes da equipe |
| GET | `/clashzone/equipes/minhasequipes` | Lista equipes do usuário autenticado |
| GET | `/clashzone/equipes/minhasequipes/paginado` | Lista equipes do usuário com paginação |
| PATCH | `/clashzone/equipes/atualizarequipe/{id}` | Atualiza uma equipe |
| DELETE | `/clashzone/equipes/deletarequipe/{id}` | Remove uma equipe |

### Membros de Equipe

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/clashzone/equipes/{equipeId}/membros/adicionar` | Adiciona membros à equipe |
| GET | `/clashzone/equipes/{equipeId}/membros/listarmembros` | Lista membros da equipe |
| PATCH | `/clashzone/equipes/{equipeId}/membros/atualizar` | Atualiza membros da equipe |
| DELETE | `/clashzone/equipes/{equipeId}/membros/deletar/{membroId}` | Remove um membro da equipe |

### Inscrições em Torneios

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/clashzone/inscricao/criar` | Cria uma inscrição de equipe em torneio |
| GET | `/clashzone/inscricao/torneio/{torneioId}` | Lista inscrições de um torneio |
| GET | `/clashzone/inscricao/equipe/{equipeId}` | Lista inscrições de uma equipe |
| GET | `/clashzone/inscricao/minhasinscricoes` | Lista inscrições das equipes do usuário autenticado |
| PATCH | `/clashzone/inscricao/atualizar/{inscricaoId}` | Atualiza status/dados de uma inscrição |
| GET | `/clashzone/inscricao/torneio/{torneioId}/paginado` | Lista inscrições de um torneio com paginação |
| GET | `/clashzone/inscricao/equipe/{equipeId}/paginado` | Lista inscrições de uma equipe com paginação |
| GET | `/clashzone/inscricao/minhasinscricoes/paginado` | Lista inscrições do usuário com paginação |
| GET | `/clashzone/inscricao/torneio/{torneioId}/equipe/{equipeId}/membros` | Lista membros de uma equipe inscrita |

## Paginação e Ordenação

Alguns endpoints aceitam os parâmetros:

```http
?page=0&size=10&sort=dataCriacao,desc
```

Exemplo:

```http
GET /clashzone/torneios/listartorneios/paginado?page=0&size=10&sort=dataCriacao,desc
```

## Banco de Dados e Migrations

As migrations ficam em:

```text
backend/src/main/resources/db/migration/
```

O projeto usa Flyway para versionar a estrutura do banco. Ao iniciar a aplicação, as migrations são executadas automaticamente.

## Status de Torneios

O domínio possui controle de status de torneios, como:

- `AGENDADO`
- `EM_ANDAMENTO`
- `FINALIZADO`

Também existe um scheduler no backend para finalizar torneios antigos automaticamente.

## Próxima Evolução: Microserviço de Notificações

A próxima etapa planejada é extrair o envio de notificações e emails para um serviço separado.

Estrutura prevista:

```text
ClashZone/
├── backend/
│   └── API principal
├── notification-service/
│   └── serviço de notificações e emails
├── frontend/
└── docker-compose.yml
```

## Observações de Desenvolvimento

- O frontend possui um `ToastService` para mensagens rápidas de interface, como sucesso e erro.
- O futuro `notification-service` será responsável por notificações de domínio, emails e alertas relacionados a torneios e inscrições.
- Essa separação evita confusão entre feedback visual do frontend e notificações reais do sistema.
