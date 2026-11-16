# Design e acessibilidade

## Direção
- Público: atendente de oficina, uso rápido no balcão.
- Idioma: português do Brasil. Celular, retrato.
- Visual: tema Material padrão do template do Android Studio (cores e tipografia sem personalização). Prioridade é funcionar, não estética.

## Sistema existente
- Tema: `res/values/themes.xml` gerado pelo template. Não criar tema próprio.
- Textos: `res/values/strings.xml`. Listas dos Spinners como `string-array` (`service_types` começando com `Selecione...`, `order_statuses`) com os valores de `PRODUCT.md`.

## Telas
- **Lista (`MainActivity`)**: título "Ordens de serviço"; cada item mostra placa (destaque), modelo, cliente, status e data `dd/MM/yyyy`; FloatingActionButton "+" no canto inferior direito.
- **Cadastro / Edição**: formulário rolável (`ScrollView`) com os 11 campos na ordem da tabela em `PRODUCT.md`; botão "Salvar" no fim; edição tem também "Excluir".
- Campo de data: `EditText` não editável por teclado; o toque abre `DatePickerDialog`.
- Spinner de tipo de serviço começa em `Selecione...` (1º item do array). Spinner de status começa em `Aberta`.

## Interação
- Todo campo tem `hint` com o nome do campo; obrigatórios terminam com `*` (ex.: `Placa *`).
- `inputType` adequado: `textCapCharacters` (placa), `number` (ano, km), `phone` (telefone), `numberDecimal` (valor), `textMultiLine` (descrição).
- Erro aparece no próprio campo (`setError`); o foco vai para o primeiro campo inválido.
- Botões e itens com área de toque ≥ 48dp; FAB com `contentDescription` "Nova ordem".
- Exclusão sempre confirmada por AlertDialog ("Sim" / "Não").

## Layout e estados
- Lista vazia: texto `Nenhuma ordem cadastrada. Toque em + para criar.` no lugar da lista.
- Sem estados de carregamento (operações locais e rápidas).
