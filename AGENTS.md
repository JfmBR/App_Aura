# Oficina (repositório App_Aura)

Regras gerais de trabalho vêm do `CLAUDE.md` global do usuário.

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
- Build: `./gradlew assembleDebug` (gera `app/build/outputs/apk/debug/AutoOS-debug.apk`)
- Verificação completa: `./gradlew lint testDebugUnitTest assembleDebug` — rode antes de declarar uma tarefa concluída.

## Git
- Uma branch por tarefa: `feat/banco`, `feat/cadastro`, `feat/lista-edicao`, `docs/...`. Toda branch nova sai da `main` atualizada.
- Merge na `main` por Pull Request no GitHub (`JfmBR/App_Aura`).

## Mapa de documentação
- Escopo: `PRODUCT.md` · Estrutura e decisões: `docs/architecture.md` · Interface: `docs/design.md`
- Preparação local: `docs/onboarding.md` · Checks: `docs/quality.md` · Evidências: `docs/setup.md`

## Convenções do projeto
- Código simples e legível (prazo curto, atividade da faculdade).
- Arquivos Gradle, `AndroidManifest.xml`, `res/values/strings.xml` e Git ficam com o coordenador.
- Pacote raiz `br.edu.fasipe.oficina`, subpacotes `model`, `data`, `validation`, `ui`.
- `ui` nunca importa `android.database.*` nem `DatabaseHelper`; usa `ServiceOrderDao`.
- Nomes de classes, métodos e variáveis em inglês; comentários em português.
- Textos da interface em `res/values/strings.xml`, em português. Exceção: mensagens de validação ficam fixas no `ServiceOrderValidator` (Java puro, testável sem Android).
- Erro de campo: `TextInputLayout.setError(mensagem)` (Spinner: `TextView` do item selecionado). Retorno de operação: `Toast`. Textos exatos em `PRODUCT.md`.
- Visual: cores, estilos e componentes do sistema de design em `docs/design.md`. Não use cor fixa em layout: use `@color/...` ou `?attr/...` do tema.
- Máximo de 350 linhas por arquivo `.java`.
