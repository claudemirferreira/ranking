# Ranking Salinha — Backend

API Spring Boot para controlar pontuações dos alunos da salinha (Classe dos Juvenis) da Igreja Adventista.

## Stack

- Java 25 / Spring Boot 4.0
- Clean Architecture + SOLID
- Spring Data JPA + MySQL 8.4
- Docker Compose
- Swagger (springdoc-openapi 3.x)

> Requer **JDK 25** (`JAVA_HOME` apontando para o JDK 25).

## Arquitetura

```text
domain        → modelos e RankingCalculator (sem Spring)
application   → ports (in/out) + use cases
infrastructure→ JPA adapters, config, seed
adapters.web  → controllers REST + DTOs HTTP
```

Controllers dependem apenas de **use cases** (`port.in`). Persistência JPA fica isolada em adapters que implementam `port.out`.

## Subir o MySQL

```bash
docker compose up -d
```

Aguarde o healthcheck (`ranking-mysql` healthy). Credenciais locais padrão:

| Variável | Default |
|----------|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/ranking?...` |
| `SPRING_DATASOURCE_USERNAME` | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `root` |

## Pré-requisito: JDK 25

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
java -version
```

## Rodar a API

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Profile padrão: `dev` (ddl-auto=update + seed da turma Juvenis).

O wrapper usa `.mvn/settings.xml` (Maven Central), para não depender do Nexus do `~/.m2/settings.xml`.

## Swagger

- UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Fluxo rápido

1. `POST /api/turmas` — criar turma (ou usar seed Juvenis)
2. `POST /api/turmas/{id}/alunos` — cadastrar alunos
3. `POST /api/turmas/{id}/trimestres` — criar trimestre
4. `POST /api/trimestres/{id}/encontros` — `{ "quantidade": 13 }`
5. `POST /api/trimestres/{id}/criterios` — critérios A/B/C
6. `PUT /api/notas` ou `PUT /api/notas/lote` — lançar pontos
7. `GET /api/trimestres/{id}/ranking` — ranking (só alunos ativos)

## Testes

```bash
./mvnw test
```

## Segurança

TODO futuro: Spring Security / roles para professores e dirigentes. Neste momento a API está aberta para facilitar o uso local e o Swagger.
