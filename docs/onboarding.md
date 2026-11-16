# Preparação local

## Pré-requisitos
- Android Studio estável atual (já inclui JDK e Android SDK). Download: https://developer.android.com/studio
- Um emulador (Device Manager do Android Studio, ex.: Pixel com API 34+) ou celular com depuração USB.
- Git com acesso ao repositório `JfmBR/App_Aura`.

Para rodar `./gradlew` no terminal fora do Android Studio, aponte `JAVA_HOME` para o JDK que vem com ele
(Linux: `<pasta do android-studio>/jbr`; Windows: `C:\Program Files\Android\Android Studio\jbr`).

## Primeira execução
1. Clonar o repositório e abrir a pasta no Android Studio (File → Open).
2. Aguardar o "Gradle Sync" terminar (baixa dependências).
3. Criar/escolher um emulador e clicar em Run (▶).

Sem `.env` e sem variáveis de ambiente.

## Banco de dados
- O banco `oficina.db` é criado sozinho no aparelho na primeira execução.
- Para zerar os dados: desinstalar o app do emulador ou Configurações → Apps → Oficina → Armazenamento → Limpar dados.
- Inspecionar: Android Studio → View → Tool Windows → App Inspection → Database Inspector.

## Verificar
```bash
./gradlew lint testDebugUnitTest assembleDebug
```
Resultado esperado: `BUILD SUCCESSFUL`. Esses testes rodam sem emulador.
O CRUD é verificado à mão no emulador, seguindo os critérios de aceite de `PRODUCT.md`.

## Agentes
Leia `AGENTS.md`. `CLAUDE.md` importa as mesmas instruções.

## Problemas conhecidos
Nenhum verificado ainda.
