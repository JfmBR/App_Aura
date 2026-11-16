# App_Aura — Oficina

Trabalho da faculdade para a Atividade N3 da Faculdade Fasipe dirigida pelo professor Ibrahin.

App Android (Java + SQLite) para uma oficina mecânica cadastrar, consultar, editar e excluir ordens de serviço, com os dados guardados no aparelho.

## Rodar
Requer Android Studio (traz JDK e Android SDK). Passo a passo em [docs/onboarding.md](docs/onboarding.md).

```bash
./gradlew installDebug
```

```bash
./gradlew lint testDebugUnitTest assembleDebug
```

No Windows use `gradlew.bat` no lugar de `./gradlew`. Sem variáveis de ambiente.

## Documentação
- [Produto e escopo](PRODUCT.md)
- [Preparação local](docs/onboarding.md)
- [Arquitetura e decisões](docs/architecture.md)
- [Design e acessibilidade](docs/design.md)
- [Qualidade](docs/quality.md)
- [Instruções para agentes](AGENTS.md)
- [Setup e evidências](docs/setup.md)
- [Especificação da atividade](Especificações%20do%20app%20aura%20+.md)

## Próxima entrega
Base de dados e validação: `ServiceOrder`, `DatabaseHelper`, `ServiceOrderDao` e `ServiceOrderValidator` com testes JUnit passando. Detalhes em [PRODUCT.md](PRODUCT.md#próxima-entrega).
