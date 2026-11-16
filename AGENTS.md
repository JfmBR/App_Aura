# Oficina (repositório App_Aura)

## Produto e stack
- Escopo, campos, mensagens e contratos: `PRODUCT.md`.
- Stack: Java, Android (minSdk 24), Gradle wrapper com Kotlin DSL, SQLite via `SQLiteOpenHelper`, Activities + layouts XML. Versões reais: `docs/setup.md`.
- Evidências do setup: `docs/setup.md`.

## Comandos canônicos
Linux/macOS usam `./gradlew`; Windows usa `gradlew.bat`. Rodar na raiz do projeto.
- Instalar: `./gradlew dependencies` (baixa dependências; a sincronização do Android Studio faz o mesmo)
- Desenvolver: rodar no emulador pelo botão Run do Android Studio, ou `./gradlew installDebug` com emulador aberto
- Lint: `./gradlew lint`
- Formatar: não se aplica: sem formatador automático; use Code → Reformat Code (Ctrl+Alt+L) do Android Studio nos arquivos que editou
- Tipos: não se aplica: o compilador Java verifica no build
- Testes: `./gradlew testDebugUnitTest`
- Build: `./gradlew assembleDebug` (gera `app/build/outputs/apk/debug/app-debug.apk`)
- Verificação completa: `./gradlew lint testDebugUnitTest assembleDebug` — rode antes de declarar uma tarefa concluída.

## Acordos de trabalho
- Leia o código e as instruções relevantes antes de editar. Declare hipóteses que afetam comportamento ou escopo.
- Faça a menor mudança completa. Reuse padrões, componentes e dependências existentes.
- Não crie abstrações, dependências ou funcionalidades não pedidas. Prazo é curto e a equipe é iniciante: código simples e legível.
- Preserve validação, tratamento de erro, segurança, acessibilidade e mudanças locais.
- Defina o critério de aceite e o comando de verificação antes de implementar. Bug: reproduza primeiro, depois prove a correção com teste.
- Não transforme hipótese em fato nem integração pendente em resultado simulado.
- Dúvida que os documentos não respondem: pergunte ao usuário, não adivinhe. Registre a resposta no documento responsável na mesma mudança.
- Item em "Dúvidas abertas" do `PRODUCT.md`: não implemente nada que dependa dele sem decisão do usuário.
- Publicar, enviar mensagens a terceiros, gerar custo e ações destrutivas exigem autorização explícita. Push só quando o usuário pedir.
- Ao mudar comportamento, comando, schema ou fronteira, atualize o documento responsável na mesma mudança.

## Git
- Nunca commitar na `main`. Uma branch por tarefa: `feat/banco`, `feat/cadastro`, `feat/lista-edicao`, `docs/...`.
- Merge na `main` por Pull Request no GitHub (`JfmBR/App_Aura`). Toda branch nova sai da `main` atualizada.
- Commits em português: `tipo: descrição curta` (ex.: `feat: adiciona validação de placa`).

## Continuidade
- Um bloqueio para só as tarefas dependentes. Conclua as independentes.
- Não repita tentativa idêntica sem evidência nova.
- Ao retomar de um handoff, confira arquivos, estado do Git, checks e processos reais. O handoff é contexto, não prova.
- Trabalho inacabado entre sessões: registre em `docs/handoff.md` (objetivo, estado verificado, evidência, pendências, bloqueios, próxima ação).

## Delegação (quando o cliente suporta subagentes)
- Delegue só quando isolamento de contexto, especialização ou paralelismo independente compensam.
- Cada tarefa delegada recebe: objetivo, contexto mínimo, arquivos permitidos, dependências, recursos, critério de aceite e checks.
- Paralelo só com arquivos disjuntos. Um escritor por arquivo. Arquivos Gradle, `AndroidManifest.xml`, `res/values/strings.xml` e Git ficam com o coordenador.
- Revisores trabalham só em leitura. O coordenador confere o diff e roda os checks depois de cada entrega.

## Revisão
- Escopo da revisão segue o risco e o diff real. Mudança não trivial recebe revisão independente quando disponível.
- Termina quando achados relevantes estão resolvidos ou com destino explícito, checks afetados passam e riscos residuais estão registrados.

## Mapa de documentação
- Escopo: `PRODUCT.md` · Estrutura e decisões: `docs/architecture.md` · Interface: `docs/design.md`
- Preparação local: `docs/onboarding.md` · Checks: `docs/quality.md` · Evidências: `docs/setup.md`

## Convenções do projeto
- Pacote raiz `br.edu.fasipe.oficina`, subpacotes `model`, `data`, `validation`, `ui`.
- `ui` nunca importa `android.database.*` nem `DatabaseHelper`; usa `ServiceOrderDao`.
- Nomes de classes, métodos e variáveis em inglês; comentários em português.
- Textos da interface em `res/values/strings.xml`, em português.
- Erro de campo: `EditText.setError(mensagem)`. Retorno de operação: `Toast`. Textos exatos em `PRODUCT.md`.
- Máximo de 350 linhas por arquivo `.java`.
