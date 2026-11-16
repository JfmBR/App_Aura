# Setup e evidências

## Contexto
- Data: 16/11/2026. Skill `project-harness` (adaptação do vibe-coding-toolkit), Claude Code.
- Máquina: Linux (Debian, kernel 6.12). **Sem JDK e sem Android SDK** no momento do brainstorm; Android Studio instalado depois, no mesmo dia.
- Branch: `docs/harness`.

## Decisões e fontes
Brainstorm com o usuário em 16/11/2026, a partir de `Especificações do app aura +.md` e do `CLAUDE.md` global do usuário.
Defaults não aplicados (config por ambiente, log, formatador): motivo em `docs/architecture.md` → Decisões.

## Componentes
| Componente | Ação e motivo | Onde | Estado e evidência |
|---|---|---|---|
| Android Studio (JDK + SDK) | Instalado pelo usuário | `~/android-studio` (JDK em `jbr`), SDK em `~/Android/Sdk` | feito: Android Studio 2026.2.2, JetBrains JDK 25.0.4, emulador `Medium_Phone_API_37.0` |
| Scaffold "Empty Views Activity" (Java, minSdk 24, pacote `br.edu.fasipe.oficina`, nome "Oficina") | Template oficial do Android Studio | Raiz do repositório | feito (commit `chore: cria projeto Android pelo template Empty Views Activity`) |
| Versões reais (AGP, Gradle, compileSdk, targetSdk) | Geradas pelo template | `gradle/libs.versions.toml`, `gradle/wrapper/gradle-wrapper.properties`, `app/build.gradle.kts` | AGP 9.5.1, Gradle 9.6.0, compileSdk 37, targetSdk 37, minSdk 24, Java 11 (source/target), Material 1.10.0 |

## Checklist de decisões
| Bloco | Item | Estado | Registrado em |
|---|---|---|---|
| A | Problema | confirmado (oficina mecânica, escolhida pelo usuário) | PRODUCT.md |
| A | Público e papéis | confirmado (atendente, papel único) | PRODUCT.md |
| A | Fluxos do MVP | deduzido (especificação, requisitos 5–12) + confirmado | PRODUCT.md |
| A | Prioridade dos fluxos | confirmado | PRODUCT.md → MVP |
| A | Fora do escopo | confirmado | PRODUCT.md |
| A | Critérios de aceite | confirmado | PRODUCT.md |
| A | Próxima entrega | confirmado | PRODUCT.md |
| B | Plataforma | deduzido (especificação: Android) | AGENTS.md |
| B | Linguagem e framework | deduzido (especificação: Java, Android Studio) | AGENTS.md |
| B | Gerenciador e runtime | confirmado (Gradle wrapper, minSdk 24) | architecture.md |
| B | Como executa | confirmado (emulador/APK) | onboarding.md |
| B | Experiência da equipe | respondido (2–3 pessoas, iniciantes) | AGENTS.md |
| C | Entidades e campos | confirmado (`service_orders`, 11 campos + id) | PRODUCT.md |
| C | Banco e onde roda | deduzido (especificação: SQLite local) | architecture.md |
| C | Migrações | confirmado (versão 1; `onUpgrade` recria) | architecture.md |
| C | Dados pessoais | confirmado (nome/telefone só no aparelho) | PRODUCT.md |
| D | Login | não se aplica (sem login, confirmado) | PRODUCT.md |
| D | Papéis | não se aplica (papel único) | PRODUCT.md |
| E | Integrações | não se aplica (sem internet, confirmado) | PRODUCT.md |
| F | Interface, idioma, tema | confirmado (pt-BR, sistema de design próprio sobre Material 3 claro, 16/11/2026) | design.md |
| F | Formato de mensagens | confirmado (setError + Toast) | PRODUCT.md |
| F | Acessibilidade | confirmado (hints, 48dp, erro no campo) | design.md |
| F | Design system | confirmado (Material do template) | design.md |
| G | Camadas | confirmado (model, data, validation, ui) | architecture.md |
| G | Fronteiras | confirmado (`ui` sem banco; `validation` sem Android) | architecture.md |
| G | Erros e validação | confirmado | PRODUCT.md, architecture.md |
| G | Config e logs | não se aplica (app offline sem segredos) | architecture.md |
| H | Tipos de teste | confirmado (JUnit validador + arquitetura; CRUD manual) | quality.md |
| H | Lint e formatador | confirmado (Android Lint; formatação manual no IDE) | quality.md |
| H | CI | respondido (sem CI) | quality.md |
| I | Publicação | confirmado (entrega = repositório GitHub + APK debug; sem Play Store) | PRODUCT.md |
| I | Custo | não se aplica (sem serviço pago) | — |
| J | Git e branches | respondido (branch por pessoa + PR; commits `tipo: descrição`) | AGENTS.md |
| J | Equipe | respondido (2–3 pessoas) | setup.md |
| J | Idiomas | deduzido (CLAUDE.md global: docs/comentários pt, nomes em inglês) | AGENTS.md |
| J | Agentes | deduzido (Claude Code) | CLAUDE.md |
| K | Entradas, saídas, erros, prova | confirmado | PRODUCT.md → Próxima entrega |

## Divisão da equipe
| Pessoa | Branch | Escopo |
|---|---|---|
| Pessoa 1 | `feat/banco` | Scaffold, `ServiceOrder`, `DatabaseHelper`, `ServiceOrderDao`, `ServiceOrderValidator`, testes. Vai primeiro: as telas dependem disso. |
| Pessoa 2 | `feat/cadastro` | `form_service_order.xml`, `strings.xml` dos campos, `CreateOrderActivity` |
| Pessoa 3 | `feat/lista-edicao` | `MainActivity`, `ServiceOrderAdapter`, `item_service_order.xml`, `EditOrderActivity` |

Decisão do usuário (16/11/2026): por falta de tempo da equipe, as 3 branches são feitas pelo usuário com o agente, em sequência.
Com 2 pessoas, a Pessoa 1 também faz `feat/lista-edicao`. `strings.xml` e `AndroidManifest.xml` são compartilhados: combinem quem edita para evitar conflito.

## Checks
| Item | Estado | Evidência |
|---|---|---|
| install | ok | Gradle Sync do Android Studio, 16/11/2026 |
| check | ok | `JAVA_HOME=~/android-studio/jbr ./gradlew lint testDebugUnitTest assembleDebug` → `BUILD SUCCESSFUL` na `feat/banco`, 16/11/2026 |
| regra de fronteira | ok | `ArchitectureTest` (4 testes) passa no `testDebugUnitTest` |
| dev sobe | ok | 16/11/2026, emulador `Medium_Phone_API_37.0` sem janela (`-memory 1536 -cores 2`, Gradle parado): critérios de aceite 1–9 executados pelo agente via adb, sem crash. Não observados: Toasts (não aparecem no dump de tela) e ordem por datas diferentes (só mesma data) |
| teste de dúvida | 2 rodadas; todas as dúvidas resolvidas | ver "Teste de dúvida" abaixo |
| smoke do harness | não executado | |

## Teste de dúvida
Rodada 1 (subagente, contexto novo, só docs): 7 dúvidas.
| Dúvida | Classe | Resolução |
|---|---|---|
| Quem faz o scaffold sem JDK/SDK | difícil de achar | PRODUCT.md → Próxima entrega: Pessoa 1, assistente do Android Studio |
| Sobras do template (`MainActivity` fora de `ui`) | nova → usuário decidiu | Pessoa 1 move para `ui` na `feat/banco` |
| Quem normaliza placa/telefone | nova → usuário decidiu | Activity normaliza; PRODUCT.md → "Quem faz o quê" |
| Spinners: validar lista? | nova → usuário decidiu | só vazio; placeholder `Selecione...` |
| Data mal formatada | nova → usuário decidiu | só vazio; DatePicker garante formato |
| `feat/banco` sai de onde | nova → usuário decidiu | da `main` após merge da `docs/harness` |
| "topo da lista" × `entry_date DESC` | contradição | critério 1 reescrito pela ordenação |

Rodada 2: 7 dúvidas.
| Dúvida | Classe | Resolução |
|---|---|---|
| Como provar o DAO sem telas | nova → usuário decidiu | só `check`; CRUD manual quando as telas chegarem |
| Retorno do DAO em erro de SQLite | nova → usuário decidiu | captura `SQLiteException`; valores de falha em PRODUCT.md |
| `NOT NULL`/`CHECK` | nova → usuário decidiu | `NOT NULL` nos 7 obrigatórios, sem `CHECK` |
| Qual teste de exemplo apagar | ambíguo | os dois |
| `docs/harness` já mergeada? | estado | não; ver Pendências |
| Mensagens no Java × `strings.xml` | contradição | exceção registrada no AGENTS.md |
| Especificação não lida | fora do escopo do teste | PRODUCT.md cobre os 12 requisitos |

Parei após 2 rodadas: as dúvidas restantes eram de texto, não de decisão.

## Memória e contexto
Contexto durável: este repositório (`AGENTS.md` + `PRODUCT.md` + `docs/`). Claude carrega via `CLAUDE.md` → `@AGENTS.md`.
Decisões duráveis vão para o documento responsável; progresso temporário vai para `docs/handoff.md`.

## Pendências e próxima entrega
1. `feat/banco` (PR #3) e `feat/cadastro` (PR #4): mergeadas.
2. `feat/lista-edicao`: PR #5 aberto; critérios 1–9 testados no emulador. Falta conferir os Toasts e a ordem por datas diferentes.
3. Depois do merge: `./gradlew assembleDebug` na `main` e entregar `app/build/outputs/apk/debug/AutoOS-debug.apk` com o link do repositório.
4. Opcional (último item de prioridade do `docs/design.md`): ícones nos chips de status.
