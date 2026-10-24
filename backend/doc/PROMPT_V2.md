# Prompt: Ranking Salinha 2.0

Implemente a versão 2.0 do sistema de pontuação da salinha da Igreja Adventista (Classe dos Juvenis). Este texto é a especificação completa. Não reintroduza nomes antigos: no código não existe `Periodo` nem `Encontro`. O ciclo se chama **trimestre** e o encontro semanal se chama **sábado**.

O domínio da Escola Sabatina está resumido abaixo. Não invente regra de pontuação nem amarre o código a um tema bíblico: o cartão de pontos é costume local da classe; o tema da lição muda todo trimestre e fica só no nome.

## O que o produto faz

Professores e dirigentes da classe:

1. Cadastram turmas e alunos.
2. Abrem um trimestre de um ano (o mesmo nome se repete todo ano).
3. Geram os sábados desse trimestre (padrão 13) e definem os critérios do cartão.
4. Lançam as notas de um sábado numa grade (alunos nas linhas, critérios nas colunas) e salvam em lote.
5. Veem o ranking do trimestre, a evolução da pontuação e onde a turma mais perde pontos.
6. Consultam o histórico de um aluno naquele trimestre.
7. Exportam o ranking e a grade de lançamento em PDF.

## Domínio

A Escola Sabatina é o estudo bíblico semanal da Igreja Adventista do Sétimo Dia, no sábado de manhã, antes do culto. A manhã tem um programa geral e depois as classes por idade. A classe deste sistema é a dos **Juvenis**.

O ano tem quatro trimestres (janeiro–março, abril–junho, julho–setembro, outubro–dezembro). Cada um tem **13 sábados** e **13 lições**. O estudo em casa começa no sábado à tarde e fecha na sexta, em geral com uma revisão: a “tarefa da lição de sexta”. Cada semana tem um verso para decorar. O sábado 13 é o fechamento e a oferta do 13º sábado. Ao gerar sábados, o padrão é 13. O sábado 1 abre o tema; os sábados 2 a 12 são a rotina em que se lançam os pontos; o sábado 13 fecha o trimestre.

Cartão usual da classe, cada item com máximo 10. A igreja pode cadastrar outros nomes, outra ordem e outro máximo:

| Ordem | Critério | Significado |
|---|---|---|
| 1 | Presença | Veio à classe no sábado |
| 2 | Oração | Fez ou participou da oração |
| 3 | Tarefa da lição de sexta | Estudou e trouxe a revisão da semana |
| 4 | Decorar o verso | Decorou o versículo da lição |
| 5 | Passar lição ou informativo | Contou a lição ou entregou um informativo a alguém |
| 6 | Comportamento | Participou com ordem durante a classe |
| 7 | Oferta | Trouxe a oferta da Escola Sabatina |

## Vocabulário obrigatório

| Conceito | Código (sem acento) | Texto para a pessoa (com acento) |
|---|---|---|
| Classe por idade | `Turma` | Turma |
| Ciclo de 13 semanas | `Trimestre`, `trimestreId` | Trimestre |
| Um sábado do ciclo | `Sabado`, `sabadoId` | Sábado, Sábados |
| Item do cartão | `Criterio` | Critério |
| Pontos lançados | `Nota` | Nota |

- Título gerado de cada sábado: `"Sábado " + numero` (ex.: `Sábado 1`).
- Rótulo de trimestre na interface, em listas, breadcrumb, PDF e selects: `nome + " · " + ano` (ex.: `Lição 4 · 2026`).
- Não usar `periodo`, `encontro`, `periodoId` nem `encontroId` em código, URL, coluna, parâmetro ou texto.

## Regras de negócio

- Uma turma tem vários alunos. Exemplo: Juvenis.
- Um trimestre pertence a uma turma. Campos: nome livre (ex.: `Lição 4`), **ano** obrigatório (2000–2100), início e fim opcionais, ativo. Na mesma turma, nome + ano não se repetem (comparação sem diferenciar maiúsculas). Mensagem: `Já existe o trimestre "<nome>" em <ano> nesta turma`.
- Um sábado pertence a um trimestre. `numero` é único naquele trimestre. Data opcional. Gerar em lote acrescenta N sábados depois dos que já existem, com título `Sábado N`. Quantidade mínima 1.
- Um critério pertence a um trimestre: nome, ordem e pontuação máxima (padrão 10).
- Uma nota é única por (aluno, sábado, critério). Lançar de novo a mesma combinação atualiza o valor (upsert). Valor decimal, no mínimo 0 e no máximo a pontuação do critério. Sábado e critério precisam ser do mesmo trimestre. Observação opcional.
- Total do aluno no trimestre = soma das notas dele naquele trimestre.
- Ranking = alunos **ativos** da turma daquele trimestre, ordenados por total decrescente; empate desempata por nome, sem diferenciar maiúsculas. A posição começa em 1. Cada item traz o total por critério, na ordem do critério. Aluno inativo não entra no ranking, na evolução nem na perda de pontos, e o histórico dele continua consultável.
- Evolução: para cada aluno ativo, a soma das notas em cada sábado (sábados ordenados por `numero`) e o acumulado até aquele sábado.
- Perda de pontos: por critério, compara o máximo possível (`pontuacaoMaxima × alunos ativos × sábados`) com o que foi lançado. Ordenar da maior perda para a menor. Sem alunos, sábados ou critérios, a lista vem vazia.
- Turma excluída some das listagens ativas (`excluidoEm` preenchido e `ativo = false`). O registro permanece.
- Excluir aluno apaga as notas dele e o próprio aluno.
- Data de nascimento é obrigatória na tela de aluno. Número da chamada é opcional.

## Modelo

Entidades de domínio e entidades JPA usam Lombok `@Data`. Não escrever getter nem setter. Métodos de negócio (ex.: `Turma.softDelete()`) e callbacks JPA (`@PrePersist`, `@PreUpdate`) continuam na classe. DTOs de entrada e saída são `record`.

- **Turma:** id, nome, descricao, ativo (default true), excluidoEm, createdAt, updatedAt. `softDelete()` marca inativa, grava `excluidoEm` e atualiza `updatedAt`.
- **Aluno:** id, turmaId, numero (opcional), nome, dataNascimento, ativo (default true), createdAt, updatedAt.
- **Trimestre:** id, turmaId, nome (até 120), ano (int, obrigatório), dataInicio, dataFim, ativo (default true), createdAt, updatedAt. Único `(turma_id, ano, nome)`.
- **Sabado:** id, trimestreId, numero, data, titulo. Único `(trimestre_id, numero)`. Coluna física do trimestre: `trimestre_id`.
- **Criterio:** id, trimestreId (`trimestre_id`), nome, ordem, pontuacaoMaxima (decimal 10,2, default 10).
- **Nota:** id, alunoId, sabadoId (`sabado_id`), criterioId, valor (decimal 10,2), observacao, lancadoEm, atualizadoEm. Único `(aluno_id, sabado_id, criterio_id)`.

Listar trimestres de uma turma por ano decrescente e, no mesmo ano, por nome.

## API

Base `/api`. Swagger em `http://localhost:8080/swagger-ui.html`. OpenAPI em `/v3/api-docs`. Título `Ranking Salinha API`, versão `2.0`. Sem autenticação. Erros 400, 404 e 409 com `{ timestamp, status, error, message, path }`. Mensagens de validação e de regra em português.

### Turmas

- `GET /api/turmas` — só as ativas (não excluídas)
- `POST /api/turmas` — `{ nome, descricao? }`
- `GET /api/turmas/{id}`
- `PUT /api/turmas/{id}`
- `DELETE /api/turmas/{id}` — soft delete

### Alunos

- `GET /api/alunos` — todos
- `GET /api/turmas/{turmaId}/alunos`
- `POST /api/turmas/{turmaId}/alunos` — `{ numero?, nome, dataNascimento }`
- `GET /api/alunos/{id}`
- `PUT /api/alunos/{id}`
- `DELETE /api/alunos/{id}` — apaga notas e o aluno
- `PATCH /api/alunos/{id}/ativo` — `{ ativo }`
- `GET /api/alunos/{alunoId}/historico?trimestreId=` — notas do aluno naquele trimestre e o total

### Trimestres, sábados e critérios

- `GET /api/turmas/{turmaId}/trimestres`
- `POST /api/turmas/{turmaId}/trimestres` — `{ nome, ano, dataInicio?, dataFim?, ativo? }`. Cria ativo.
- `GET /api/trimestres/{id}`
- `PUT /api/trimestres/{id}` — mesmos campos; pode inativar
- `POST /api/trimestres/{id}/sabados` — `{ quantidade }` (default útil: 13)
- `GET /api/trimestres/{id}/sabados`
- `POST /api/trimestres/{id}/criterios` — `{ nome, ordem?, pontuacaoMaxima? }`
- `GET /api/trimestres/{id}/criterios`
- `PUT /api/criterios/{id}`

A resposta de trimestre inclui `ano`. O use case expõe `rotulo()` = `nome · ano` para PDF e títulos.

### Notas

- `PUT /api/notas` — upsert `{ alunoId, sabadoId, criterioId, valor, observacao? }`
- `PUT /api/notas/lote` — lista desses itens
- `GET /api/trimestres/{trimestreId}/notas?sabadoId=&criterioId=` — filtros opcionais, para montar a grade

### Consultas e PDF

- `GET /api/trimestres/{trimestreId}/ranking` — `{ posicao, alunoId, numero, nome, total, porCriterio: [{ criterioId, nome, total }] }`
- `GET /api/trimestres/{trimestreId}/evolucao` — sábados em ordem e, por aluno ativo, pontos daquele sábado e acumulado
- `GET /api/trimestres/{trimestreId}/perdas` — por critério: máximo possível, obtido, perda e percentual; o primeiro item é o critério mais crítico
- `GET /api/trimestres/{trimestreId}/ranking/pdf` — Jasper, parâmetros `TITULO` (`Ranking — ` + rótulo), `TRIMESTRE` (rótulo), `TURMA`. Arquivo sugerido: `ranking-<nome>-<ano>.pdf`
- `GET /api/trimestres/{trimestreId}/sabados/{sabadoId}/lancamento/pdf` — parâmetros `TITULO`, `TRIMESTRE` (rótulo), `TURMA`, `SABADO` (título do sábado). Texto do parâmetro visível: `Sábado: `.

## Frontend

Pasta irmã `frontend/`. Angular 21, componentes standalone, signals, PrimeNG 21, Chart.js. API em `http://localhost:8080/api`. Dev server em `http://localhost:4200`.

Shell com navegação: Dashboard, Turmas, Alunos. Rotas:

| Rota | Tela |
|---|---|
| `/` | redireciona para `/dashboard` |
| `/dashboard` | evolução e perdas |
| `/turmas` | lista de turmas |
| `/turmas/:turmaId` | abas Alunos e Trimestres |
| `/turmas/:turmaId/trimestres/:trimestreId` | abas Sábados, Critérios e Ranking |
| `/turmas/:turmaId/trimestres/:trimestreId/lancamento` | grade de notas |
| `/alunos` | todos os alunos |
| `/alunos/:alunoId/historico` | histórico; query `turmaId` e `trimestreId` |

Telas:

- **Turmas:** criar, editar e excluir (confirmação). Abrir a turma.
- **Alunos da turma:** nome, data de nascimento, status. Criar, editar, inativar e excluir. Data de nascimento obrigatória, não futura.
- **Trimestres da turma:** colunas nome, ano, início, fim, status. Criar e editar. Ano obrigatório, default o ano atual, entre 2000 e 2100. Link do nome abre o trimestre.
- **Sábados:** botão “Gerar sábados” (quantidade, default 13). Lista número, título e data.
- **Critérios:** criar e editar nome, ordem e pontuação máxima.
- **Ranking:** tabela com posição, aluno, total e totais por critério. Botão para baixar o PDF.
- **Lançamento:** seletor “Sábado”. Grade com um campo numérico por aluno e critério, de 0 até o máximo. Salvar em lote. Imprimir o PDF daquele sábado, ordenado por total. Breadcrumb e subtítulo usam o rótulo do trimestre (`Lição 4 · 2026`).
- **Dashboard:** escolhe turma e trimestre (opções no rótulo `nome · ano`). Gráfico de evolução em dois modos: acumulado e por sábado. Painel do critério em que a turma mais perde pontos.
- **Alunos (geral):** lista de todas as turmas. Em cada aluno, escolher o trimestre (rótulo com ano) e abrir o histórico.
- **Histórico:** notas do aluno naquele trimestre, com o sábado, o critério, o valor e o total no subtítulo.

Textos visíveis em português, com acento: Sábado, Sábados, Trimestre, Critério. Identificadores TypeScript sem acento (`sabadoId`, `trimestreId`, `SabadoResponse`).

## Arquitetura do backend

Java 25, Spring Boot 4.0.x, Web MVC, Data JPA, Validation, Lombok, springdoc-openapi 3.x, JasperReports, MySQL 8.4. Pacote `br.com.setebit.ranking`. Clean Architecture: dependência só aponta para dentro. SOLID.

```text
br.com.setebit.ranking
├── domain
│   ├── model          Turma, Aluno, Trimestre, Sabado, Criterio, Nota  (@Data)
│   ├── exception      ResourceNotFoundException, BusinessRuleException
│   └── service        RankingCalculator, EvolucaoPontuacaoCalculator, PerdaPontuacaoCalculator
├── application
│   ├── port.in        um use case por fluxo
│   ├── port.out       um repositório por agregado
│   ├── usecase
│   ├── dto            records de resposta
│   └── mapper
├── infrastructure
│   ├── persistence    *JpaEntity (@Data, @Entity), Spring Data, adapters, JpaMapper
│   ├── report         Jasper
│   ├── config         OpenAPI, CORS
│   └── seed
└── adapters.web       controllers, records de request, exception handler
```

- `domain` não importa Spring, JPA, Web nem JDBC.
- Controller só chama `port.in`. Não injeta repositório.
- Cálculo de ranking, evolução e perda fica nos serviços de domínio, sem framework.
- Bean Validation nos requests. Regra de negócio (ano, nome repetido, nota acima do máximo, sábado de outro trimestre) vira `BusinessRuleException` e HTTP 409.
- Código Java em inglês. Mensagem de API e rótulo de tela em português.
- Testes com H2 (`ddl-auto` create-drop, seed desligado). `@WebMvcTest` usa `@MockitoBean`. Incluir teste do `RankingCalculator` (soma, ordem, empate por nome, aluno inativo de fora) e o contexto da aplicação subindo.

## Infraestrutura

`docker-compose.yml` no backend, só o MySQL:

- imagem `mysql:8.4`, container `ranking-mysql`, banco `ranking`, root/root, porta 3306, volume nomeado, healthcheck com `mysqladmin ping`.

`application-dev.yaml` (profile `dev` é o padrão):

- URL `jdbc:mysql://localhost:3306/ranking?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo`
- usuário e senha por `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`, default `root` / `root`
- `ddl-auto: update` só em dev
- `open-in-view: false`
- `ranking.seed.enabled: true` em dev, `false` em teste

CORS liberado para `localhost` e `127.0.0.1` nas portas do frontend.

Seed (só se não houver turma ativa): turma Juvenis; os 15 alunos Luísa, Júlia, Sofhia, Adrielly, Lorena, Valentina, Esther, João (número 9), Isaque, João Levi, Vitória, Manuela, João Pedro, João Lucas, João Arthur (número 22); trimestre `Lição 3` no ano corrente; 13 sábados; os 7 critérios do cartão, máximo 10, nessa ordem. Data de nascimento do seed pode ficar nula. Se já existir turma ativa, o seed não mexe no banco.

JDK 25 em `JAVA_HOME` (ex.: `C:\Program Files\Java\jdk-25`). Maven Wrapper (`mvnw.cmd`) com `.mvn/settings.xml` apontando para o Maven Central.

README do backend com: arquitetura, Compose, JDK 25, como subir, Swagger e o fluxo criar turma → alunos → trimestre → sábados → critérios → notas → ranking.

## Critérios de aceite

- [ ] CRUD de turma com soft delete e listagem só das ativas
- [ ] CRUD de aluno, inativar, excluir (some a nota junto) e `GET /api/alunos`
- [ ] Trimestre exige ano; Lição 4 de 2026 e Lição 4 de 2027 coexistem; repetir o mesmo nome no mesmo ano na mesma turma responde 409
- [ ] Gerar 13 sábados com título `Sábado 1` … `Sábado 13`; número único no trimestre
- [ ] Critérios com ordem e máximo; nota única por aluno + sábado + critério; segundo lançamento atualiza
- [ ] Nota acima do máximo ou sábado de outro trimestre é rejeitada
- [ ] Ranking soma certo, ignora inativo e desempata por nome
- [ ] Evolução traz acumulado e total do sábado; perdas ordenam o critério mais crítico primeiro
- [ ] PDF de ranking e de lançamento mostram o rótulo `nome · ano` e a palavra Sábado com acento
- [ ] Telas: dashboard, turmas, trimestre, lançamento, alunos e histórico, com o ano visível em todo lugar que se escolhe ou abre um trimestre
- [ ] Swagger 2.0 abre e exercita a API sem login
- [ ] `mvnw test` passa em H2; a API de dev sobe no MySQL do Compose
- [ ] Entidades com `@Data`, sem getter/setter manual; controllers sem JPA

## Fora desta versão

- Login, perfil de professor ou dirigente
- Importar a planilha Excel
- Flyway ou Liquibase (dev segue com `ddl-auto: update`)
- Aplicação dentro do Docker Compose (o Compose sobe só o MySQL)
- Tema bíblico, divisão missionária ou valor da oferta do 13º sábado como dado
- App mobile

## Como validar

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
docker compose up -d
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

No frontend: `npx ng serve` e percorrer dashboard → turma Juvenis → trimestre `Lição 3 · <ano>` → gerar sábados, se ainda não existirem → lançar uma nota → ver o ranking mudar → baixar os dois PDFs → abrir o histórico de um aluno.
