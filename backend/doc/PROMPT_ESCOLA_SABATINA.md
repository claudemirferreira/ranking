# Prompt: Domínio da Escola Sabatina Adventista

Use este texto como contexto de domínio ao implementar ou explicar o sistema de pontuação da salinha. A fonte de produto continua sendo `doc/PROMPT_IMPLEMENTACAO.md`. Não invente regra de pontuação: o cartão de pontos é costume local da classe; a estrutura do trimestre é da Igreja.

## O que é a Escola Sabatina

A Escola Sabatina é o estudo bíblico semanal da Igreja Adventista do Sétimo Dia. Acontece no sábado de manhã, em geral antes do culto divino, e dura cerca de uma hora a uma hora e meia.

A manhã tem duas partes:

1. **Programa geral**, com todos juntos: boas-vindas, cântico, oração, relatório de presença e oferta da semana anterior, história do Informativo Mundial das Missões e coleta.
2. **Classes** (unidades de ação), por idade, onde se estuda a lição da semana. É a parte mais longa.

Faixas publicadas pela Igreja: recém-nascidos (rol do berço), jardim da infância, primários, juvenis, adolescentes, jovens e adultos. Cada classe tem professor. A coordenação fica com o diretor da Escola Sabatina e com a secretaria, que anota presença e oferta.

Há um segundo tipo de reunião durante a semana, em casa, em pequeno grupo, para oração, companheirismo e testemunho.

A oferta semanal da Escola Sabatina é missionária. Desde 1913, essa oferta semanal sustenta a missão mundial.

## Como funciona cada trimestre

O ano tem quatro trimestres, todos com a mesma mecânica:

| Trimestre | Meses |
|---|---|
| 1º | janeiro–março |
| 2º | abril–junho |
| 3º | julho–setembro |
| 4º | outubro–dezembro |

Cada trimestre tem **13 sábados** e **13 lições**. O tema dos adultos é o mesmo no mundo inteiro; muda só o idioma. Classes infantis usam material próprio, mais narrativo. Jovens e adultos estudam o tema do trimestre em linguagem da faixa.

Dentro do tema, cada lição é uma semana. O estudo em casa começa no **sábado à tarde** (já a lição da semana seguinte) e fecha na **sexta-feira**, em geral com uma revisão. Essa revisão é a “tarefa da lição de sexta”. Cada semana traz um **verso para decorar**.

No sábado, depois do programa geral, a classe discute o que foi estudado. No auxiliar da Divisão Sul-Americana, a unidade de ação dura cerca de uma hora:

- **Companheirismo e pastoreio** (cerca de 10 minutos): boas-vindas, testemunhos, oração.
- **Minutos missionários** (cerca de 15 minutos): chamada, quem estudou a lição, pequeno grupo, estudos bíblicos, visita aos ausentes, plano missionário e oferta.
- **Lição**: discussão do estudo da semana.

### Os 13 sábados

**Sábado 1 — abertura.** Apresenta o tema, as metas do trimestre (pontualidade, estudo diário e missão) e o compromisso da contracapa da lição. Cada professor combina com a classe o que será acompanhado nos três meses.

**Sábados 2 a 12 — rotina.** Estudo diário em casa, história missionária de um país da região do trimestre, oferta e discussão da lição. Nas classes infantis e de juvenis, é neste encontro que se lançam os pontos.

**Sábado 13 — fechamento.** O programa fica mais longo e missionário. A lição do dia é resumida, a igreja revisa as metas e recolhe a **oferta do 13º sábado** (desde 1912). Do valor recolhido, **25%** vai aos projetos anunciados naquele trimestre (escola, templo, acampamento, dormitório) e **75%** segue para o fundo mundial de missões. Os projetos estão na última parte do informativo missionário.

### O que permanece e o que muda

| Permanece em todo trimestre | Muda a cada trimestre |
|---|---|
| 13 semanas e o estudo de sábado à tarde até sexta | Tema bíblico e os 13 versos |
| Programa geral e classe | Divisão do mundo em destaque no informativo |
| Oferta missionária semanal | Projetos da oferta do 13º sábado |
| Metas de estudo, presença e missão | Foco do pequeno grupo, quando a lição propõe um |

Exemplo: no 3º trimestre de 2026 (a partir de 27 de junho), os adultos estudam as cartas aos Coríntios. O informativo acompanha a Divisão Intereuropeia. A oferta de 26 de setembro ajuda projetos na Bélgica, Bulgária, Itália e Romênia. No trimestre seguinte entram outro tema e outra região; a sequência de 13 sábados recomeça.

## Cartão de pontos da classe de juvenis

O cartão é um recurso pedagógico local, comum nas divisões infantis e de juvenis no Brasil. Não é regra mundial da denominação. Cada igreja define a nota (muitas usam máximo 10 por item).

| Critério no sistema | Significado na classe |
|---|---|
| Presença | Veio à classe no sábado |
| Oração | Fez ou participou da oração |
| Tarefa da lição de sexta | Estudou e trouxe a revisão da semana |
| Decorar o verso | Decorou o versículo da lição |
| Passar lição ou informativo | Contou a lição ou entregou um informativo a alguém durante a semana |
| Comportamento | Participou com ordem durante a classe |
| Oferta | Trouxe a oferta da Escola Sabatina |

## Como isto vira dado no sistema

- **Turma**: uma faixa (ex.: Juvenis).
- **Trimestre**: o ciclo de 13 sábados (ex.: “Lição 4”), com o ano, porque o mesmo trimestre se repete todo ano, e com início e fim.
- **Sabado**: um dos 13 sábados (`numero` 1–13, título “Sábado N”). O sábado 13 é o fechamento e a oferta do 13º sábado.
- **Critério**: um item do cartão, com ordem e pontuação máxima. Os nomes acima são o conjunto usual; a igreja pode cadastrar outros.
- **Nota**: valor daquele aluno, naquele sábado, naquele critério. Na prática costuma ser 0 ou a nota máxima, e o sistema aceita qualquer valor de 0 até o máximo.
- **Total e ranking**: soma das notas do aluno no trimestre; ranking dos ativos por total decrescente.

Ao gerar sábados de um trimestre novo, o padrão é **13**. Não amarrar o código a um tema bíblico ou a uma divisão missionária: isso muda todo trimestre e fica no nome do trimestre e no material da igreja, fora do sistema.

## Fontes

- Igreja Adventista — Escola Sabatina: https://institucional.adventistas.org/pt/nossos-cultos/escola-sabatina/
- Escola Sabatina (Wikipédia): https://pt.wikipedia.org/wiki/Escola_Sabatina
- Ofertas da Escola Sabatina: https://stewardship.adventistchurch.com/sabbath-school-offerings/
- Auxiliar do 3º trimestre de 2026 (Divisão Sul-Americana): https://arquivosadventistas.org/arquivos/Escola_sabatina/2026/3-trim-2026/USEB%20-%20AUXILIAR%203%20TRIMESTRE%202026-compact.pdf
- Informativo missionário do 3º trimestre de 2026: https://arquivosadventistas.org/arquivos/Escola_sabatina/2026/3-trim-2026/INFORMATIVO%203%20TRIMESTRE%202026.pdf
