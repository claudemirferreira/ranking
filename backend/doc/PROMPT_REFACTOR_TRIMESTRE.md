# Prompt: Refatorar Periodo para Trimestre

Status: executado. Backend, banco, API e frontend já usam `Trimestre`.

Execute esta refatoração. Não mude regra de negócio, cálculo de ranking, critérios nem o fluxo de lançamento. Só troque o nome `Periodo` pelo nome de domínio `Trimestre`.

Contexto de domínio: `doc/PROMPT_ESCOLA_SABATINA.md`. Um trimestre da Escola Sabatina tem 13 sábados. No código isso hoje se chama período.

## Escopo

Renomear em backend, banco, API, frontend e documentação.

| Hoje | Depois |
|---|---|
| `Periodo` | `Trimestre` |
| `periodo` / `periodoId` | `trimestre` / `trimestreId` |
| tabela `periodos` | tabela `trimestres` |
| coluna `periodo_id` (confirmar o nome físico) | coluna `trimestre_id` |
| `/api/.../periodos` | `/api/.../trimestres` |
| rota `periodos/:periodoId` | rota `trimestres/:trimestreId` |
| rótulo "Período" | rótulo "Trimestre" |

Não renomear `Turma`, `Aluno`, `Encontro`, `Criterio`, `Nota`. O nome do trimestre continua livre (ex.: "Lição 3"): isso é o campo `nome`, não o tipo.

Não criar camada de compatibilidade. Backend e frontend mudam no mesmo passo. Não há cliente externo.

## Regras

- Manter Clean Architecture e SOLID. A ordem das camadas continua: domínio → portas → caso de uso → adaptadores.
- `ddl-auto: update` **não** renomeia tabela nem coluna. Se a aplicação subir antes do `ALTER`, o Hibernate cria `trimestres` vazio e deixa `periodos` para trás. Rodar o SQL de rename **antes** de subir a API com o código novo.
- Conferir o nome físico no MySQL antes do script. O unique de `encontros` está declarado como `columnNames = {"periodoId", "numero"}`; a coluna real pode ser `periodoId` ou `periodo_id`.
- Preservar dados, FKs e o unique `(trimestre, numero)` em encontros.
- Busca final sem ocorrência de `periodo` / `Periodo` em `src/` do backend e do frontend. Pode permanecer só dentro deste prompt e do histórico, se algum texto explicar a troca.

## Tarefas

### 1. Inspecionar o schema

- [ ] `SHOW CREATE TABLE periodos;`
- [ ] `SHOW CREATE TABLE encontros;`
- [ ] `SHOW CREATE TABLE criterios;`
- [ ] Anotar colunas, índices e FKs que citam período.
- [ ] Escrever o script com os nomes reais. Exemplo, ajustar se a coluna não for `periodo_id`:

```sql
RENAME TABLE periodos TO trimestres;

ALTER TABLE encontros
  DROP INDEX uk_periodo_numero,
  CHANGE COLUMN periodo_id trimestre_id BIGINT NOT NULL,
  ADD CONSTRAINT uk_trimestre_numero UNIQUE (trimestre_id, numero);

ALTER TABLE criterios
  CHANGE COLUMN periodo_id trimestre_id BIGINT NOT NULL;
```

O nome do índice e das FKs sai do `SHOW CREATE TABLE`. Não inventar.

### 2. Domínio

- [ ] `domain/model/Periodo.java` → `Trimestre.java`.
- [ ] Em `Encontro` e `Criterio`, `periodoId` → `trimestreId`, com getter e setter.

### 3. Persistência

- [ ] `PeriodoJpaEntity` → `TrimestreJpaEntity`, `@Table(name = "trimestres")`.
- [ ] `EncontroJpaEntity` e `CriterioJpaEntity`: campo e unique com `trimestreId`.
- [ ] `SpringDataPeriodoRepository` → `SpringDataTrimestreRepository`.
- [ ] `PeriodoRepositoryAdapter` e `PeriodoRepositoryPort` → `Trimestre*`.
- [ ] `JpaMapper`: mapeamento do novo tipo e de `trimestreId` em encontro e critério.
- [ ] Queries em `SpringDataNotaRepository` (`findByPeriodoId`, `findByPeriodoIdFiltered`, `findByAlunoIdAndPeriodoId`) e o mesmo contrato em `NotaRepositoryPort` / `NotaRepositoryAdapter`.

### 4. Aplicação

- [ ] `PeriodoResponse` → `TrimestreResponse`.
- [ ] `periodoId` em `EncontroResponse`, `CriterioResponse`, `HistoricoAlunoResponse` e nos demais DTOs que carregam esse id.
- [ ] `ApplicationMapper.toResponse`.
- [ ] `ManagePeriodoUseCase` e `ManagePeriodoUseCaseImpl` → `ManageTrimestreUseCase` e `ManageTrimestreUseCaseImpl`.
- [ ] Parâmetro `periodoId` nos portos de entrada e nas implementações: ranking, evolução, perda, exportação de PDF do ranking, exportação de PDF do lançamento, histórico de notas.
- [ ] `DevDataLoader`: variável e chamadas. O nome semeado continua `"Lição 3"`. Os 13 encontros continuam iguais.

### 5. API

- [ ] `PeriodoController` → `TrimestreController`.
- [ ] `PeriodoRequest` → `TrimestreRequest`.
- [ ] Rotas:
  - `GET|POST /api/turmas/{turmaId}/trimestres`
  - `GET|PUT /api/trimestres/{id}`
  - `GET|POST /api/trimestres/{id}/encontros`
  - `GET|POST /api/trimestres/{id}/criterios`
  - `GET /api/trimestres/{id}/ranking`
  - `GET /api/trimestres/{id}/ranking/pdf`
  - `GET /api/trimestres/{id}/evolucao`
  - `GET /api/trimestres/{id}/perdas`
  - `GET /api/trimestres/{id}/notas`
  - `GET /api/trimestres/{trimestreId}/encontros/{encontroId}/lancamento/pdf`
- [ ] `AlunoController`: query param do histórico `periodoId` → `trimestreId`.
- [ ] Textos de Swagger que digam "período".

### 6. Banco

- [ ] Aplicar o script da tarefa 1 no MySQL **antes** de iniciar a API.
- [ ] Subir a API com `ddl-auto: update` e conferir que não nasceu tabela `periodos` nem coluna `periodo_id` paralela.

### 7. Frontend

Pasta: `c:\work\setebit\ranking\frontend`.

- [ ] `PeriodoRequest` / `PeriodoResponse` e todo `periodoId` em `src/app/core/models/api.models.ts`.
- [ ] `periodo.service.ts` → `trimestre.service.ts`. URLs `/periodos` → `/trimestres`. Query do histórico alinhada ao backend.
- [ ] Rotas em `app.routes.ts`: `turmas/:turmaId/trimestres/:trimestreId` e `.../lancamento`.
- [ ] Pasta `features/periodos/periodo-detail/` → `features/trimestres/trimestre-detail/`. Classe `PeriodoDetail` → `TrimestreDetail`.
- [ ] Trocar injeção, sinais e links em:
  - `features/notas/lancamento/`
  - `features/dashboard/`
  - `features/alunos/alunos-list/`
  - `features/alunos/aluno-historico/`
  - `features/turmas/turma-detail/`
  - qualquer outro arquivo que o busca `periodo` ainda aponte.
- [ ] Textos visíveis: "Período" → "Trimestre", "períodos" → "trimestres", "Selecione o período" → "Selecione o trimestre". Manter "Lição" quando for o nome cadastrado do trimestre.

### 8. Documentação

- [ ] `doc/PROMPT_IMPLEMENTACAO.md`: modelo, endpoints e a palavra período quando for o tipo do sistema.
- [ ] `doc/PROMPT_ESCOLA_SABATINA.md`: a seção "Como isto vira dado no sistema" passa a dizer trimestre.
- [ ] `README.md`: exemplos de URL.

### 9. Verificação

- [ ] `rg -i "periodo" src` no backend e no frontend sem resultado de código.
- [ ] `./mvnw test` no backend.
- [ ] Subir API e frontend. Abrir uma turma, ver a lista de trimestres, gerar os 13 sábados se preciso, lançar uma nota e abrir o ranking.
- [ ] Confirmar que os registros antigos continuam ligados à mesma turma e aos mesmos encontros.

## Fora deste prompt

Não incluir ordenação de tabela, mudança de critério, nem renomear encontro para sábado. Isso fica para outra tarefa.
