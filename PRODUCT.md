# Oficina — controle de ordens de serviço

## Problema e objetivo
Uma oficina mecânica pequena anota os veículos que entram para serviço em papel ou planilha solta.
O app registra cada ordem de serviço (OS) no próprio celular, permite consultar, editar e excluir,
e mantém os dados após fechar o app.

Contexto acadêmico: Atividade N3 da Faculdade Fasipe (prof. Ibrahin). Entrega: **16/11/2026**.
Os 12 requisitos obrigatórios estão em `Especificações do app aura +.md`. O nome do repositório
("App_Aura") não define o tema.

## Público e papéis
- Atendente da oficina: cadastra, consulta, edita e exclui OS.
- Papel único. Sem login. Um aparelho, dados locais.

## Fluxos da primeira versão
1. **Consultar:** abrir app → `MainActivity` lista as OS → vê placa, modelo, cliente, status e data.
2. **Cadastrar:** botão "+" na lista → `CreateOrderActivity` → preenche formulário → "Salvar" → Toast "Ordem salva" → volta à lista com a OS nova.
3. **Editar:** tocar numa OS da lista → `EditOrderActivity` com os dados carregados → altera → "Salvar" → Toast "Ordem atualizada" → volta à lista.
4. **Excluir:** em `EditOrderActivity` → "Excluir" → AlertDialog "Excluir esta ordem?" → "Sim" → Toast "Ordem excluída" → volta à lista. "Não" fecha o diálogo sem mudar nada.

## MVP
Em ordem de prioridade:
1. Banco SQLite + DAO com as 4 operações CRUD.
2. Validação dos campos (`ServiceOrderValidator`).
3. Cadastro (fluxo 2).
4. Listagem (fluxo 1).
5. Edição e exclusão (fluxos 3 e 4).

## Fora do escopo
Busca e filtro, login, fotos do veículo, tabela de clientes separada, várias tabelas relacionadas,
exportação/relatórios, sincronização em nuvem, publicação na Play Store.

## Requisitos

### Tabela `service_orders`
Chave primária `id INTEGER PRIMARY KEY AUTOINCREMENT` + 11 campos:

| Coluna | Tipo SQLite | Campo na tela | Obrigatório | Regra |
|---|---|---|---|---|
| `plate` | TEXT | EditText (maiúsculas) | Sim | Após normalizar (maiúsculas, sem `-` e espaços): `^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$` (ABC1234 ou ABC1D23) |
| `vehicle_model` | TEXT | EditText | Sim | Não vazio após `trim` |
| `vehicle_year` | INTEGER | EditText número | Não | Se informado: 1950 até ano atual + 1 |
| `mileage` | INTEGER | EditText número | Não | Se informado: ≥ 0 |
| `customer_name` | TEXT | EditText | Sim | Não vazio após `trim` |
| `customer_phone` | TEXT | EditText telefone | Sim | Guardar só dígitos; 10 ou 11 dígitos |
| `service_type` | TEXT | Spinner | Sim | 1º item `Selecione...` (= vazio, não é valor válido); valores: Revisão, Troca de óleo, Freios, Suspensão, Elétrica, Motor, Outro |
| `description` | TEXT | EditText multilinha | Não | Livre |
| `price` | REAL | EditText decimal | Não | Se informado: ≥ 0 |
| `entry_date` | TEXT | DatePicker | Sim | Gravada como `yyyy-MM-dd`; exibida como `dd/MM/yyyy`; padrão no cadastro = hoje |
| `status` | TEXT | Spinner | Sim | Aberta, Em andamento, Concluída, Entregue; padrão no cadastro = Aberta |

Campo opcional vazio é gravado como `NULL`.

### Quem faz o quê com os valores
- **Activity** (ao montar a `ServiceOrder`, antes de validar): aplica `normalizePlate` na placa e `digitsOnly` no telefone;
  converte opcional vazio em `null`; Spinner em `Selecione...` vira `""`. O valor normalizado é o que vai para o banco.
- **Validador**: recebe valores já normalizados; não altera nada. Placa `"--"` chega como `""` → `Informe a placa`.
  Telefone `"abc"` chega como `""` → `Informe o telefone`.
- `service_type`, `status` e `entry_date`: o validador checa só vazio. A lista de valores dos Spinners fica só em
  `strings.xml` (sem cópia em Java). O formato de `entry_date` é garantido pelo DatePicker.

### Mensagens de validação (exibidas com `setError` no campo)
| Situação | Mensagem |
|---|---|
| Placa vazia | `Informe a placa` |
| Placa fora do formato | `Placa inválida (ex.: ABC1234 ou ABC1D23)` |
| Modelo vazio | `Informe o modelo` |
| Ano fora do limite | `Ano deve estar entre 1950 e <ano atual + 1>` |
| Km negativo | `Km não pode ser negativo` |
| Cliente vazio | `Informe o nome do cliente` |
| Telefone vazio | `Informe o telefone` |
| Telefone com quantidade errada de dígitos | `Telefone deve ter 10 ou 11 dígitos` |
| Tipo de serviço vazio | `Selecione o tipo de serviço` |
| Valor negativo | `Valor não pode ser negativo` |
| Data vazia | `Informe a data de entrada` |
| Status vazio | `Selecione o status` |

### Mensagens de retorno (Toast)
`Ordem salva` · `Ordem atualizada` · `Ordem excluída` · `Erro ao salvar ordem` · `Erro ao atualizar ordem` ·
`Erro ao excluir ordem` · `Ordem não encontrada`.

### Outros
- Idioma da interface: português do Brasil.
- Dispositivo: celular Android, orientação retrato, minSdk 24.
- Dados pessoais (nome e telefone do cliente) ficam só no aparelho. Sem envio para rede.
- Acessibilidade: todo campo tem rótulo (`hint`), erros aparecem no próprio campo.

## Restrições e decisões técnicas
Java + Android Studio + SQLite (`SQLiteOpenHelper`, sem Room), Activities com layouts XML, sem internet.
Motivo: exigência da atividade e simplicidade para equipe iniciante. Detalhes em `docs/architecture.md`.

## Dúvidas abertas
Nenhuma.

## Critérios de aceite
1. Cadastrar uma OS válida mostra `Ordem salva` e a OS aparece na lista, na posição da sua data de entrada (mais recente primeiro; mesma data → cadastrada por último primeiro).
2. Tentar salvar com campo obrigatório vazio não grava e mostra a mensagem no campo.
3. Placa `abc-1d23` é aceita e gravada como `ABC1D23`; placa `AB123` é rejeitada.
4. Tocar numa OS abre a edição com todos os 11 campos preenchidos.
5. Editar e salvar mostra `Ordem atualizada` e a lista reflete a mudança.
6. Excluir pede confirmação; "Sim" remove a OS e mostra `Ordem excluída`; "Não" mantém a OS.
7. Fechar o app (inclusive forçar parada) e abrir de novo mantém todas as OS.
8. Lista vazia mostra `Nenhuma ordem cadastrada. Toque em + para criar.`
9. Botão voltar navega de cadastro/edição para a lista sem gravar.

## Próxima entrega
**Base de dados e validação** (Pessoa 1, branch `feat/banco`, criada a partir da `main` depois do merge da `docs/harness`):
scaffold do projeto + `ServiceOrder` + `DatabaseHelper` + `ServiceOrderDao` + `ServiceOrderValidator` + `ServiceOrderValidatorTest` + `ArchitectureTest`,
com `./gradlew lint testDebugUnitTest assembleDebug` passando.

Pré-requisito: Android Studio instalado. O scaffold é feito pela Pessoa 1 no assistente New Project → "Empty Views Activity"
(valores em `docs/setup.md` → Componentes), nunca escrito à mão. Ainda na `feat/banco`, a Pessoa 1 move a `MainActivity`
gerada para o pacote `ui` (Refactor → Move, que atualiza o Manifest) e apaga os dois testes de exemplo do template
(`ExampleUnitTest` e `ExampleInstrumentedTest`); o conteúdo da
`MainActivity` fica para a Pessoa 3.

### Contratos
- `model/ServiceOrder`: campos da tabela acima. `id` é `long` (0 = ainda não gravada). `vehicleYear` e `mileage` são `Integer`, `price` é `Double` (`null` = não informado). Demais campos `String`.
- `data/DatabaseHelper extends SQLiteOpenHelper`: banco `oficina.db`, versão 1. `onUpgrade` apaga e recria a tabela.
  `NOT NULL` nas 7 colunas obrigatórias (`plate`, `vehicle_model`, `customer_name`, `customer_phone`, `service_type`,
  `entry_date`, `status`). Sem `CHECK`: as regras ficam no validador.
- Erro de SQLite: o DAO captura `SQLiteException` em todos os métodos, registra `Log.e` sem dados do cliente e devolve o
  valor de falha abaixo. O app nunca fecha por erro de banco.
- `data/ServiceOrderDao` (recebe `Context`):
  - `long insert(ServiceOrder)` → id novo, ou `-1` em erro.
  - `List<ServiceOrder> findAll()` → ordenado por `entry_date DESC, id DESC`; lista vazia se não houver registros ou em erro.
  - `ServiceOrder findById(long id)` → `null` se não existir ou em erro.
  - `int update(ServiceOrder)` → linhas alteradas (1 ok, 0 = não encontrada/erro).
  - `int delete(long id)` → linhas removidas (1 ok, 0 = não encontrada/erro).
  - Nas telas: `update`/`delete` com 0 → `Erro ao atualizar ordem` / `Erro ao excluir ordem`. `Ordem não encontrada`
    só aparece quando `findById` devolve `null` ao abrir a edição.
- `validation/ServiceOrderValidator` (Java puro, sem classes Android):
  - `static String normalizePlate(String)` → maiúsculas, sem `-` e espaços; `null` vira `""`.
  - `static String digitsOnly(String)` → só dígitos; `null` vira `""`.
  - `static Map<String, String> validate(ServiceOrder order, int currentYear)` → chave = nome da coluna, valor = mensagem da tabela de validação. Mapa vazio = válido. Recebe `currentYear` para o teste não depender da data.

### Entradas e saídas esperadas (viram testes em `ServiceOrderValidatorTest`)
| Entrada | Resultado |
|---|---|
| OS com todos os obrigatórios válidos e opcionais `null` | mapa vazio |
| `plate = ""` | `plate` → `Informe a placa` |
| `plate = "AB123"` | `plate` → `Placa inválida (ex.: ABC1234 ou ABC1D23)` |
| `normalizePlate("abc-1d23")` | `"ABC1D23"` |
| `vehicleYear = 1949`, `currentYear = 2026` | `vehicle_year` → `Ano deve estar entre 1950 e 2027` |
| `vehicleYear = 2027`, `currentYear = 2026` | válido |
| `mileage = -1` | `mileage` → `Km não pode ser negativo` |
| `customerPhone = "123456789"` (9 dígitos) | `customer_phone` → `Telefone deve ter 10 ou 11 dígitos` |
| `customerPhone = "11987654321"` | válido |
| `price = -0.01` | `price` → `Valor não pode ser negativo` |
| vários campos inválidos ao mesmo tempo | uma entrada no mapa por campo inválido |

### Como provar sem dados reais
- Validador: JUnit local (`testDebugUnitTest`), sem emulador.
- DAO: nesta entrega, prova só pelo `check` (compila). Sem teste instrumentado e sem código provisório em telas.
  O CRUD é verificado à mão no emulador (critérios 1–7) quando as telas das Pessoas 2 e 3 chegarem, antes da entrega final.
- Fronteira e teto de linhas: `ArchitectureTest` (JUnit local), ver `docs/quality.md`.
