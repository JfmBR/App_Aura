# Qualidade

## Comandos e o que provam
| Comando | Prova | Quando rodar |
|---|---|---|
| `./gradlew lint` | Android Lint sem erros (layouts, manifest, APIs, strings) | Antes de abrir PR |
| `./gradlew testDebugUnitTest` | Validador correto; fronteira `ui`/`validation` respeitada; nenhum `.java` acima de 350 linhas | A cada mudança |
| `./gradlew assembleDebug` | Código compila e o APK é gerado | Antes de abrir PR |
| `./gradlew lint testDebugUnitTest assembleDebug` | Tudo acima (`check` do projeto) | Antes de dizer "pronto" |
| Teste manual no emulador | Critérios de aceite 1–9 de `PRODUCT.md` | Antes do merge de cada tela e antes da entrega |

## Regras base
- Android Lint com a configuração padrão do template. Não desligar regras para ficar verde.
- Teto de 350 linhas por arquivo `.java` (verificado no `ArchitectureTest`).
- Validação de todo campo do formulário pelo `ServiceOrderValidator` antes de gravar.
- `ui` sem acesso direto ao banco.
- Sem log de dados do cliente (nome, telefone) no Logcat.

## Estratégia de testes
- `ServiceOrderValidatorTest` (JUnit local): um teste por linha da tabela "Entradas e saídas esperadas" de `PRODUCT.md`.
- `ArchitectureTest` (JUnit local): lê os `.java` de `app/src/main/java` e falha se
  (a) algum arquivo de `ui/` contém `import android.database` ou `DatabaseHelper`;
  (b) algum arquivo de `validation/` contém `import android.`;
  (c) algum `.java` passa de 350 linhas.
  O diretório de trabalho dos testes locais é `app/`, então os caminhos partem de `src/main/java`.
- DAO e telas: teste manual no emulador. Não há teste instrumentado nem E2E.

## Revisão após implementar ou corrigir
Escopo pelo risco e pelo diff real. Revisão independente para mudança não trivial quando disponível (outro membro da equipe no PR).
Termina com achados resolvidos ou com destino, checks passando e riscos residuais registrados.

## Revisão de segurança
Sob demanda: revisão só leitura do repositório ou de um diff. Pontos de atenção: SQL sempre com argumentos (`?`, `ContentValues`), nunca concatenando texto do usuário.

## Dependências
Só as do template do Android Studio. Lint/build verde não significa auditoria de dependências.
