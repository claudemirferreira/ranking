# Prompt: Sistema de Pontuação — Salinha Adventista (Classe dos Juvenis)

## Contexto
Backend Spring Boot para controlar as notas/pontuações dos alunos que frequentam a salinha da Igreja Adventista todos os sábados.

Referência de domínio: planilha "Tabela de Pontuação – Classe dos Juvenis", com alunos em linhas e sábados (1–13) em colunas, pontos por sábado e total/ranking.

Como funciona cada trimestre da Escola Sabatina (13 sábados, lição, 13º sábado e cartão de pontos): `doc/PROMPT_ESCOLA_SABATINA.md`.

O tipo no código é trimestre (antes chamado de período). Tarefas dessa troca: `doc/PROMPT_REFACTOR_TRIMESTRE.md`.

O sábado no código é `Sabado` (antes chamado de encontro). Tarefas dessa troca: `doc/PROMPT_REFACTOR_SABADO.md`.

Projeto atual:
- Path: `c:\work\setebit\ranking\backend`
- Stack: **Spring Boot 4.0.x**, **Java 25**, Spring Web MVC (`spring-boot-starter-webmvc`), Spring Data JPA, MySQL 8.4
- Swagger: **springdoc-openapi 3.x** (compatível com Boot 4)
- Pacote base: `br.com.setebit.ranking`
- Estado: **API implementada** (Clean Architecture, endpoints REST, seed dev, testes mínimos)
- Há pasta irmã `frontend/` — contratos REST prontos para o frontend
- **Obrigatório mantido:** Swagger (OpenAPI), Docker Compose, MySQL, **Clean Architecture** e princípios **SOLID**

### Pré-requisito local
- JDK 25 (`JAVA_HOME` apontando para o JDK 25; ex.: `C:\Program Files\Java\jdk-25`)
- Docker Desktop (para MySQL via Compose)
- Maven Wrapper (`mvnw` / `mvnw.cmd`) — usa `.mvn/settings.xml` (Maven Central)

## Objetivo do produto
Permitir que professores/dirigentes:
1. Cadastrem turmas e alunos
2. Cadastrem sábados de um trimestre
3. Definam critérios de pontuação (ex.: presença, lição, comportamento — a planilha tem 3 blocos de sábados)
4. Lancem notas por aluno/sábado/critério de forma rápida
5. Consultem ranking e total por aluno automaticamente
6. Consultem histórico de um aluno

## Regras de negócio
- Uma **Turma** (ex.: Juvenis) possui vários **Alunos**.
- Um **Trimestre** (ex.: "Lição 4" de 2026) pertence a uma turma, guarda o **ano** (o mesmo nome se repete todo ano) e contém vários **Sábados** (Sábado 1…N) com data opcional. Na mesma turma, nome + ano não se repetem.
- Cada trimestre tem um ou mais **Critérios** de pontuação (nome + ordem + pontuação máxima padrão, ex.: 10).
- Uma **Nota** é única por combinação (aluno + sábado + critério).
- Valor da nota: numérico decimal; na prática costuma ser 0 ou 10, mas aceitar 0 até o máximo do critério.
- **Total do aluno no trimestre** = soma de todas as notas do aluno naquele trimestre.
- **Ranking** = ordenação dos alunos ativos da turma/trimestre por total decrescente; em empate, ordenar por nome.
- Aluno pode ser inativado (não some do histórico, mas **não aparece** no ranking).
- Soft-delete em turmas (`excluidoEm`); alunos usam flag `ativo`.

## Modelo de dados
- `Turma`: id, nome, descricao, ativo, excluidoEm, createdAt, updatedAt
- `Aluno`: id, turmaId, numero (opcional), nome, ativo, createdAt, updatedAt
- `Trimestre`: id, turmaId, nome (ex.: "Lição 3"), dataInicio, dataFim, ativo
- `Sabado`: id, trimestreId, numero (1..N), data (opcional), titulo (ex.: "Sábado 5")
- `Criterio`: id, trimestreId, nome, ordem, pontuacaoMaxima (default 10)
- `Nota`: id, alunoId, sabadoId, criterioId, valor, observacao (opcional), lançadoEm, atualizadoEm  
  Constraint unique: (aluno_id, sabado_id, criterio_id)

Seeds iniciais (profile `dev`, `ranking.seed.enabled=true`):
- Turma: Juvenis
- Alunos da planilha: Luísa, Júlia, Sofhia, Adrielly, Lorena, Valentina, Esther, João, Isaque, João Levi, Vitória, Manuela, João Pedro, João Lucas, João Arthur
- Trimestre com 13 sábados (Sábado 1–13)
- 3 critérios genéricos (Critério A, B e C)

## API REST
Base: `/api`  
Swagger UI: `http://localhost:8080/swagger-ui.html`  
OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Turmas
- `GET /api/turmas`
- `POST /api/turmas`
- `GET /api/turmas/{id}`
- `PUT /api/turmas/{id}`
- `DELETE /api/turmas/{id}` (soft)

### Alunos
- `GET /api/alunos` — listar todos os alunos cadastrados (todas as turmas)
- `GET /api/turmas/{turmaId}/alunos`
- `POST /api/turmas/{turmaId}/alunos`
- `GET /api/alunos/{id}`
- `PUT /api/alunos/{id}`
- `PATCH /api/alunos/{id}/ativo` (ativar/inativar)

### Trimestres / Sábados / Critérios
- `GET|POST /api/turmas/{turmaId}/trimestres`
- `GET|PUT /api/trimestres/{id}`
- `POST /api/trimestres/{id}/sabados` (criar em lote: quantidade N)
- `GET /api/trimestres/{id}/sabados`
- `GET|POST /api/trimestres/{id}/criterios`
- `PUT /api/criterios/{id}`

### Notas (lançamento)
- `PUT /api/notas` — upsert de uma nota `{ alunoId, sabadoId, criterioId, valor, observacao? }`
- `PUT /api/notas/lote` — upsert em lote (tela de lançamento do sábado)
- `GET /api/trimestres/{trimestreId}/notas?sabadoId=&criterioId=` — grade para a tela
- `GET /api/alunos/{alunoId}/historico?trimestreId=` — notas + totais do aluno

### Ranking
- `GET /api/trimestres/{trimestreId}/ranking`  
  Resposta: lista `{ posicao, alunoId, numero, nome, total, porCriterio?: { criterioId, total } }`

## Infraestrutura: MySQL + Docker Compose + Swagger

### MySQL
- Banco padrão: `ranking` (utf8mb4)
- Conexão via Spring DataSource apontando para o MySQL do Compose
- Configurar em `application.yaml` / `application-dev.yaml` com variáveis de ambiente:
  - `SPRING_DATASOURCE_URL` (default: `jdbc:mysql://localhost:3306/ranking?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo`)
  - `SPRING_DATASOURCE_USERNAME` (default: `root`)
  - `SPRING_DATASOURCE_PASSWORD` (default: `root`)
- `spring.jpa.hibernate.ddl-auto=update` em dev
- `open-in-view=false`
- Não commitar senhas reais; defaults apenas para ambiente local

### Docker Compose
Arquivo `docker-compose.yml` na raiz do backend (serviço MySQL):

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: ranking-mysql
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: ranking
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    volumes:
      - ranking_mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
      interval: 5s
      timeout: 5s
      retries: 20

volumes:
  ranking_mysql_data:
```

Fluxo local:
1. `docker compose up -d` → aguardar healthy
2. Garantir JDK 25 no PATH / `JAVA_HOME`
3. `mvnw.cmd spring-boot:run` (Windows) ou `./mvnw spring-boot:run`

### Swagger / OpenAPI
- Usar **springdoc-openapi 3.x** (obrigatório com Spring Boot 4; a linha 2.x é só para Boot 3)
- Dependência: `org.springdoc:springdoc-openapi-starter-webmvc-ui`
- UI: `http://localhost:8080/swagger-ui.html`
- JSON OpenAPI: `/v3/api-docs`
- Bean `OpenAPI` com:
  - título: `Ranking Salinha API`
  - descrição: API de pontuação da classe dos Juvenis
  - versão: `0.0.1`
- Swagger lista `/api/**` e permite Try it out sem autenticação

## Arquitetura: Clean Architecture + SOLID

### Clean Architecture (obrigatório)
Dependências **sempre apontando para dentro** (domínio no centro). Frameworks (Spring, JPA, Swagger) nas bordas.

```text
br.com.setebit.ranking
├── domain                      # Entidades e regras puras (sem Spring/JPA)
│   ├── model                   # Turma, Aluno, Trimestre, Sabado, Criterio, Nota...
│   ├── exception               # DomainException, ResourceNotFoundException, BusinessRuleException
│   └── service                 # RankingCalculator (puro)
├── application                 # Casos de uso (orquestração)
│   ├── port
│   │   ├── in                  # ManageTurmaUseCase, ManageAlunoUseCase, ManageTrimestreUseCase, ManageNotaUseCase, GetRankingUseCase
│   │   └── out                 # Repositórios (TurmaRepository, AlunoRepository, ...)
│   ├── usecase                 # Implementações @Service
│   ├── dto                     # Responses dos use cases
│   └── mapper                  # ApplicationMapper
├── infrastructure
│   ├── persistence             # JPA entities, Spring Data repos, adapters, JpaMapper
│   ├── config                  # OpenAPI, CORS
│   └── seed                    # DevDataLoader (profile dev)
└── adapters
    └── web                     # Controllers, HTTP DTOs, GlobalExceptionHandler
```

Regras de dependência:
1. `domain` **não** depende de Spring, JPA, Web, Swagger ou MySQL.
2. `application` depende só de `domain` + portas (interfaces).
3. `infrastructure` e `adapters.web` implementam/adaptam as portas.
4. Controllers **não** acessam JPA/repositories — só use cases (`port.in`).
5. Persistência JPA: entity JPA ↔ model de domínio via adapter.
6. Cálculo de ranking isolado em `RankingCalculator` (domínio puro, testável).

### SOLID (obrigatório)

| Princípio | Como aplicar neste projeto |
|-----------|----------------------------|
| **S** — Single Responsibility | Controller só HTTP; use case só orquestra um fluxo; repository só persistência; ranking em serviço próprio |
| **O** — Open/Closed | Novos critérios/relatórios via novos use cases/adapters |
| **L** — Liskov | Implementações de `port.out` respeitam o contrato |
| **I** — Interface Segregation | Portas pequenas (`TurmaRepository`, `NotaRepository`, …) |
| **D** — Dependency Inversion | Use cases dependem de `port.out`; Spring injeta adapters JPA |

## Requisitos técnicos
1. **Clean Architecture + SOLID** (não usar `controller → repository` direto).
2. Stack: **Java 25** + **Spring Boot 4.0.x** + starters modulares (`webmvc`, `webmvc-test`, `data-jpa`, `data-jpa-test`, `validation`).
3. Validação com Bean Validation nos adapters web.
4. Erros 404/409/400 via `@RestControllerAdvice` com body `{ timestamp, status, error, message, path }`.
5. MySQL obrigatório em dev; H2 **somente** em testes.
6. Docker Compose obrigatório para MySQL local.
7. springdoc-openapi **3.x** obrigatório.
8. Variáveis de ambiente (`SPRING_DATASOURCE_*`); sem senhas reais no repo.
9. CORS liberado para `localhost` / `127.0.0.1`.
10. Testes mínimos: `RankingCalculatorTest` + `@WebMvcTest` (usar `@MockitoBean`, não `@MockBean` — removido no Boot 4) + context load.
11. Código Java em inglês; mensagens de API podem ser PT-BR.
12. Sem autenticação completa; TODO futuro Spring Security. Swagger aberto em dev.
13. README com: arquitetura, Compose, JDK 25, como rodar, Swagger, endpoints.

## Critérios de aceite
- [x] CRUD de turma e aluno funcionando
- [x] `GET /api/alunos` lista todos os alunos cadastrados
- [x] Criar trimestre com 13 sábados e 3 critérios
- [x] Lançar notas em lote para um sábado
- [x] Ranking reflete somas corretamente após updates
- [x] Aluno inativo não aparece no ranking
- [x] Unique de nota impede duplicidade
- [x] MySQL via `docker compose up -d` e API conecta nele
- [x] Swagger UI acessível e documentando os endpoints
- [x] Pacotes seguem Clean Architecture
- [x] Controllers dependem de use cases (`port.in`), não de JPA
- [x] README com Compose + Swagger + arquitetura + fluxo de uso
- [x] Projeto em Java 25 / Spring Boot 4

## Fora de escopo (não implementar agora)
- Frontend React/Angular
- Importação automática do Excel (pode deixar stub/TODO)
- Auth/roles completos
- App mobile
- Gráficos
- Flyway/Liquibase (hoje: `ddl-auto=update` em dev)
- Serviço `app` no Docker Compose (hoje só MySQL)

## Como validar
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
docker compose up -d
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Fluxo manual sugerido (Swagger ou HTTP):
1. `GET /api/turmas` — usar seed Juvenis ou criar turma
2. `GET /api/alunos` — listar todos os alunos (ou `GET /api/turmas/{id}/alunos` por turma)
3. `GET /api/turmas/{id}/trimestres` — obter trimestre
4. `PUT /api/notas` ou `PUT /api/notas/lote` — lançar pontos
5. `GET /api/trimestres/{id}/ranking` — conferir ordenação/totais
6. `GET /api/alunos/{id}/historico?trimestreId=` — histórico

## Evoluções futuras (opcional)
1. Spring Security + roles (professor / dirigente)
2. Migrations (Flyway ou Liquibase) e FKs no banco
3. Dockerfile + serviço `app` no Compose
4. Ampliar cobertura de testes (use cases de nota/trimestre, integração)
5. Importação da planilha Excel
