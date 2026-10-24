# Prompt: Refatorar Encontro para Sábado

Status: executado. Backend, banco, API e frontend já usam `Sabado`.

Execute esta refatoração. Não mude regra de negócio, cálculo de ranking, critérios nem o fluxo de lançamento. Só troque o nome `Encontro` pelo nome de domínio `Sabado`.

Contexto: `doc/PROMPT_ESCOLA_SABATINA.md`. Cada trimestre tem 13 sábados. No código isso hoje se chama encontro. O trimestre já foi renomeado (`doc/PROMPT_REFACTOR_TRIMESTRE.md`).

## Escopo

Renomear em backend, banco, API, frontend e documentação.

| Hoje | Depois |
|---|---|
| `Encontro` | `Sabado` |
| `encontro` / `encontroId` | `sabado` / `sabadoId` |
| `encontros` | `sabados` |
| tabela `encontros` | tabela `sabados` |
| `notas.encontro_id` | `notas.sabado_id` |
| `/api/trimestres/{id}/encontros` | `/api/trimestres/{id}/sabados` |
| rótulo "Encontro" / "Encontros" | "Sábado" / "Sábados" |

Identificadores ficam sem acento (`Sabado`, `sabadoId`). Texto visível ao usuário leva acento (`Sábado`, `Sábados`).

Não renomear `Turma`, `Aluno`, `Trimestre`, `Criterio`, `Nota`. `Nota` continua existindo; só o campo que aponta para o sábado muda de `encontroId` para `sabadoId`. O título gerado continua `"Sábado " + numero`. O sábado 13 continua sendo o fechamento do trimestre.

Não criar camada de compatibilidade. Backend e frontend mudam no mesmo passo. Não há cliente externo.

## Regras

- Manter Clean Architecture e SOLID.
- Parar a API na porta 8080 **antes** de salvar o código novo. Com devtools e `ddl-auto: update`, a aplicação cria a tabela `sabados` vazia e a coluna `sabado_id` ao lado de `encontro_id`, com valor 0 nas linhas antigas. Isso aconteceu na troca de período para trimestre.
- Rodar o `ALTER` **antes** de subir a API com o código novo.
- Conferir o schema com `SHOW CREATE TABLE` antes do script. Estado visto em 24/10/2026:

```text
encontros (
  id, data, numero, trimestre_id, titulo,
  UNIQUE uk_trimestre_numero (trimestre_id, numero)
)

notas (
  id, aluno_id, atualizado_em, criterio_id, encontro_id,
  lancado_em, observacao, valor,
  UNIQUE UKtqxp8vqdtejkt0q4a7cfj7up (aluno_id, encontro_id, criterio_id)
)
```

Não há FK. O `CHANGE COLUMN` do MySQL leva o índice unique junto com a coluna. Não dropar o unique das notas.

- Se a subida antecipada já tiver criado `sabados` vazia ou `sabado_id` preenchida com 0, apagar só esses objetos novos e renomear os originais, que guardam os dados.
- Busca final sem `encontro` / `Encontro` em `src/` do backend e do frontend. Pode permanecer a palavra no texto corrido de domínio que não é o tipo (por exemplo "neste encontro" numa frase do prompt de Escola Sabatina, se não estiver nomeando o modelo). No código, zero.

## Tarefas

### 1. Parar a API e inspecionar

- [ ] Encerrar o processo na porta 8080.
- [ ] `SHOW CREATE TABLE encontros;`
- [ ] `SHOW CREATE TABLE notas;`
- [ ] Anotar se já existem `sabados` ou `sabado_id`. Se existirem vazios ou zerados, removê-los antes do rename.

Script esperado, ajustar só se o `SHOW CREATE` divergir:

```sql
RENAME TABLE encontros TO sabados;

ALTER TABLE notas
  CHANGE COLUMN encontro_id sabado_id BIGINT NOT NULL;
```

Depois: `SHOW CREATE TABLE sabados; SHOW CREATE TABLE notas;` O unique das notas deve citar `sabado_id`. As linhas antigas continuam com o mesmo id de sábado.

### 2. Domínio

- [ ] `domain/model/Encontro.java` → `Sabado.java`.
- [ ] `Nota.encontroId` → `sabadoId`.
- [ ] `EvolucaoPontuacaoCalculator` e `PerdaPontuacaoCalculator`, e os testes desses cálculos e de `RankingCalculatorTest`, passam a usar `Sabado`.

### 3. Persistência

- [ ] `EncontroJpaEntity` → `SabadoJpaEntity`, `@Table(name = "sabados")`. Manter `uk_trimestre_numero` em `(trimestre_id, numero)`.
- [ ] `NotaJpaEntity`: coluna física `sabado_id`. O unique da nota é `(aluno_id, sabado_id, criterio_id)`.
- [ ] `SpringDataEncontroRepository` → `SpringDataSabadoRepository`.
- [ ] `EncontroRepositoryAdapter` e `EncontroRepositoryPort` → `Sabado*`.
- [ ] `JpaMapper`.
- [ ] Queries de `SpringDataNotaRepository` (`findByAlunoIdAndEncontroIdAndCriterioId` e os filtros por `encontroId`) e o mesmo contrato em `NotaRepositoryPort` / `NotaRepositoryAdapter`.

### 4. Aplicação

- [ ] `EncontroResponse` → `SabadoResponse`.
- [ ] `CreateEncontrosRequest` → `CreateSabadosRequest`.
- [ ] `NotaRequest`, `NotaResponse` e qualquer DTO com `encontroId`.
- [ ] `ApplicationMapper`.
- [ ] `ManageTrimestreUseCase` / `ManageTrimestreUseCaseImpl`: `createEncontros` → `createSabados`, `listEncontros` → `listSabados`. O título gravado continua `"Sábado " + numero`.
- [ ] `ManageNotaUseCase` / `ManageNotaUseCaseImpl`: parâmetro e mensagens que dizem "encontro".
- [ ] `ExportLancamentoPdfUseCase` e a implementação. O parâmetro Jasper que for o id ou o rótulo do encontro passa a sábado. O PDF de lançamento e o de ranking não podem ficar com `$P{ENCONTRO}` solto.
- [ ] Evolução e perda de pontuação.

### 5. API

- [ ] Rotas em `TrimestreController`:
  - `GET|POST /api/trimestres/{id}/sabados`
  - `GET /api/trimestres/{trimestreId}/notas?sabadoId=&criterioId=`
  - `GET /api/trimestres/{trimestreId}/sabados/{sabadoId}/lancamento/pdf`
- [ ] `NotaController`: corpo `{ alunoId, sabadoId, criterioId, valor }`.
- [ ] Swagger: "encontro" → "sábado".
- [ ] Nome de arquivo do PDF: `lancamento-trimestre-{id}-sabado-{id}.pdf`.

### 6. Banco

- [ ] Aplicar o script da tarefa 1 **antes** de iniciar a API.
- [ ] Subir a API e conferir que não nasceu tabela `encontros` nem coluna `encontro_id`.
- [ ] `SELECT id, nome FROM trimestres` e a contagem de sábados por trimestre continuam iguais (hoje: Lição 3 e Lição 4, 13 cada). As notas continuam apontando para os mesmos ids.

### 7. Frontend

Pasta: `c:\work\setebit\ranking\frontend`.

- [ ] `api.models.ts`: `EncontroResponse` → `SabadoResponse`, `CreateEncontrosRequest` → `CreateSabadosRequest`, `encontroId` → `sabadoId`.
- [ ] `trimestre.service.ts`: URLs `/encontros` → `/sabados`, query `sabadoId`.
- [ ] `features/trimestres/trimestre-detail/`: aba "Sábados", botão "Gerar sábados", diálogo e mensagens. O subtítulo deixa de dizer "Encontros".
- [ ] `features/notas/lancamento/`: rótulo "Sábado" (hoje está "Encontro (sábado)"), sinais `selectedSabadoId`, textos "Selecione um sábado".
- [ ] `features/alunos/aluno-historico/`: coluna "Sábado" e o mapa que hoje usa `encontroId` / `encontroLabel`.
- [ ] Qualquer outro arquivo que a busca `encontro` ainda aponte.

### 8. Documentação

- [ ] `doc/PROMPT_IMPLEMENTACAO.md`: modelo, unique da nota e endpoints.
- [ ] `doc/PROMPT_ESCOLA_SABATINA.md`: a seção "Como isto vira dado no sistema" chama o sábado de `Sabado`, não de encontro.
- [ ] `README.md`: `POST /api/trimestres/{id}/sabados`.

### 9. Verificação

- [ ] `rg -i "encontro" src` no backend e no frontend sem resultado de código.
- [ ] `./mvnw test` com JDK 25.
- [ ] `npx ng build --configuration=development` no frontend.
- [ ] Abrir Juvenis → Sábados, a Lição 4 com os 13 sábados, o lançamento de um sábado e o ranking.

## Fora deste prompt

Não incluir ordenação de tabela nem mudança de critério.
