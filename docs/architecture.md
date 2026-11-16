# Arquitetura

## Stack real
- Java, Android Studio, Gradle wrapper (Kotlin DSL, gerado pelo template "Empty Views Activity").
- minSdk 24; targetSdk/compileSdk = padrão do Android Studio no momento do scaffold (registrado em `docs/setup.md`).
- SQLite nativo via `SQLiteOpenHelper` (sem Room, sem ORM).
- UI: Activities + layouts XML, `ListView` com adapter próprio, componentes AppCompat/Material do template.
- Tema: Material 3 claro com cores próprias (`Theme.Material3.Light.NoActionBar`), sem `values-night`. Especificação em `docs/design.md`.
- Testes: JUnit 4 do template (testes locais em `app/src/test`).
- Sem dependências além das que o template já traz.

Estado: scaffold criado; `model`, `data`, `validation` e cadastro implementados. Lista e edição em andamento. Versões reais em `docs/setup.md`.

## Mapa do código
```text
app/src/main/java/br/edu/fasipe/oficina/
├── model/ServiceOrder.java            Dados de uma OS (POJO)
├── data/DatabaseHelper.java           Cria/atualiza oficina.db (SQLiteOpenHelper)
├── data/ServiceOrderDao.java          CRUD da tabela service_orders
├── validation/ServiceOrderValidator.java  Regras dos campos (Java puro)
└── ui/
    ├── MainActivity.java              Lista (consulta) + botão "+"
    ├── ServiceOrderAdapter.java       Item da lista (ArrayAdapter<ServiceOrder>)
    ├── ServiceOrderForm.java          Lê o formulário, mostra erros e abre o DatePicker (cadastro e edição)
    ├── CreateOrderActivity.java       Cadastro
    └── EditOrderActivity.java         Edição + exclusão
app/src/main/res/layout/
├── activity_main.xml                  ListView + texto de lista vazia + FAB
├── item_service_order.xml             Linha da lista
├── form_service_order.xml             Campos do formulário (incluído por cadastro e edição)
├── activity_create_order.xml          <include> do formulário + Salvar
└── activity_edit_order.xml            <include> do formulário + Salvar + Excluir
app/src/main/res/values/
├── colors.xml                         Cores do sistema de design (docs/design.md)
├── themes.xml                         Tema claro + estilos Oficina.TextInput e Oficina.Placa
├── dimens.xml                         Espaços, área de toque e altura de campo
└── strings.xml                        Textos da interface e arrays dos Spinners
app/src/main/res/drawable/
├── bg_field_outline.xml               Contorno do Spinner
├── bg_status.xml                      Fundo do chip de status
└── ic_*.xml                           Ícones (Vector Asset)
app/src/test/java/br/edu/fasipe/oficina/
├── ServiceOrderValidatorTest.java
└── ArchitectureTest.java              Fronteira de pacotes + teto de 350 linhas
```

## Fronteiras e fluxo
Fluxo: Activity lê os campos → monta `ServiceOrder` → `ServiceOrderValidator.validate` →
se houver erros, `setError` nos campos e para → senão `ServiceOrderDao` grava → Toast → `finish()`.

Navegação: `MainActivity` → `CreateOrderActivity` (FAB) ou `EditOrderActivity` (toque no item, extra
`EXTRA_ORDER_ID` com o `id` long). `MainActivity` recarrega a lista em `onResume`.
`EditOrderActivity` sem OS para o id: Toast `Ordem não encontrada` e `finish()`.

| Módulo/camada | Pode importar | Não pode importar | Regra que garante |
|---|---|---|---|
| `ui` | `model`, `data.ServiceOrderDao`, `validation`, Android UI | `android.database.*`, `data.DatabaseHelper` | `ArchitectureTest`, roda no `testDebugUnitTest` |
| `validation` | `model`, `java.*` | qualquer `android.*` | `ArchitectureTest` (precisa ser Java puro para o JUnit local) |
| `data` | `model`, `android.*` | `ui` | não verificado por teste (proibição não pedida) |

Única conexão com o banco: `DatabaseHelper`, usado só por `ServiceOrderDao`. O DAO fecha o `SQLiteDatabase`/`Cursor` que abrir.

## Dados e migrações
- Banco `oficina.db`, versão 1, tabela `service_orders` (colunas em `PRODUCT.md` → Requisitos).
- `onCreate` cria a tabela. `onUpgrade` faz `DROP TABLE IF EXISTS` e recria.
  Consequência: mudar o schema e subir a versão **apaga as OS do aparelho**. Aceito para o trabalho.
- Sem dados de exemplo (seed). Testar cadastrando pelo app.

## Decisões
| Decisão | Motivo | Alternativa considerada | Fonte/data | Quando revisar |
|---|---|---|---|---|
| Oficina mecânica como cenário | Escolha da equipe | Pets, estoque, biblioteca | brainstorm 16/11/2026 | — |
| `SQLiteOpenHelper` puro | Exigência "SQLite" e didático | Room | brainstorm 16/11/2026 | Se o app crescer |
| 3 Activities (lista, cadastro, edição) | Requisito 6 da especificação | Fragments, uma Activity só | especificação | — |
| `ListView` + `ArrayAdapter` | Mais simples para iniciantes | RecyclerView | brainstorm 16/11/2026 | Se a lista ficar lenta |
| Sistema de design próprio sobre Material 3 claro | Pedido do usuário; leitura rápida no balcão; só recursos XML, sem dependência nova | Tema padrão do template | pedido do usuário 16/11/2026 | Se o prazo apertar: aplicar só cores e tema |
| Erro de campo no `TextInputLayout` | Mensagem embaixo do campo, como no sistema de design | `EditText.setError` (balão) | pedido do usuário 16/11/2026 | — |
| `ServiceOrderForm` compartilhado pelo cadastro e pela edição | Evita copiar ~150 linhas de leitura/erro/data nas duas Activities; só cuida da tela (sem DAO, Toast ou `finish`) | Código repetido em cada Activity | 16/11/2026 | — |
| Toolbar e FAB da `MainActivity` na `feat/cadastro` | O fluxo 2 começa no "+" e o cadastro precisa de teste manual antes do merge | Esperar a `feat/lista-edicao` | 16/11/2026 | — |
| Exclusão dentro da edição, com AlertDialog | Menos telas, confirmação explícita | Toque longo na lista | brainstorm 16/11/2026 | — |
| `onUpgrade` recria a tabela | Sem necessidade de migração no trabalho | Migração com `ALTER TABLE` | brainstorm 16/11/2026 | Se houver dados reais |
| Sem formatador automático no `check` | Prazo; Android Studio formata | Spotless | brainstorm 16/11/2026 | Se houver conflitos de estilo |
| Sem config por ambiente, log e `.env` | App offline sem segredos nem servidor | — | brainstorm 16/11/2026 | Se surgir integração externa |
| Sem teste instrumentado do DAO | Entrega no mesmo dia; CRUD verificado à mão | Teste com emulador | brainstorm 16/11/2026 | Após a entrega |

## Manutenção
Atualize ao mudar pacotes, dependências, schema, contratos do DAO ou fronteiras.
