# Design e acessibilidade

## Direção
- Público: atendente de oficina, uso rápido no balcão.
- Idioma: português do Brasil. Celular, retrato.
- Visual: tema próprio sobre o Material 3 claro do template ("sistema de design da Oficina"). Só cor, forma e texto mudam; os componentes são os do Material Components que o template já traz. Sem dependência nova.
- Referência visual (canvas com fundamentos, componentes, XML e 3 telas): https://claude.ai/artifact/7bhKYxRsL2t9f4mr8zVx1t — privado; o dono compartilha pelo menu Share.
- Prazo curto: se faltar tempo, aplique nesta ordem: 1) cores e tema, 2) campos, 3) card e status, 4) ícones do status.

## Cores
Valores em `res/values/colors.xml`, ligados aos atributos do tema em `res/values/themes.xml`.

| Papel | Atributo do tema | Cor | Uso |
|---|---|---|---|
| Primary | `colorPrimary` / `colorOnPrimary` | `#1F4E79` / `#FFFFFF` | Barra superior, botão Salvar, foco do campo |
| Primary container | `colorPrimaryContainer` / `colorOnPrimaryContainer` | `#D3E4F7` / `#0B2A45` | Status Aberta |
| Secondary | `colorSecondary` / `colorOnSecondary` | `#B4500A` / `#FFFFFF` | Só o FAB "+" |
| Secondary container | `colorSecondaryContainer` / `colorOnSecondaryContainer` | `#FFDCC4` / `#5C2700` | Status Em andamento |
| Surface | `colorSurface`, `android:colorBackground` | `#F8F9FB` | Fundo das telas |
| On surface | `colorOnSurface` | `#1A1C1E` | Texto principal, placa |
| On surface variant | `colorOnSurfaceVariant` | `#43474E` | Texto secundário, rótulos, ícones |
| Outline | `colorOutline` | `#73777F` | Borda dos campos (nunca texto) |
| Outline variant | `colorOutlineVariant` | `#C3C7CF` | Borda dos cards, divisores |
| Error | `colorError` | `#BA1A1A` | Erro de campo, botão Excluir, "Sim" do diálogo |
| — | `@color/white` (template) | `#FFFFFF` | Fundo dos cards e do diálogo |

### Status da ordem
Fundo e texto do chip. A cor nunca vem sozinha: o chip sempre mostra o texto do status.

| Status | Fundo | Texto | Ícone (Vector Asset) |
|---|---|---|---|
| Aberta | `#D3E4F7` | `#0B2A45` | `radio_button_unchecked` |
| Em andamento | `#FFDCC4` | `#5C2700` | `build` |
| Concluída | `#CDEBD3` | `#0D3B1C` | `check` |
| Entregue | `#E2E2E6` | `#303236` | `key` |

## Tipografia
Roboto (padrão do Android), estilos do Material 3 (`?attr/textAppearance...`). Nenhum arquivo de fonte.

| Estilo | Tamanho / linha / peso | Uso |
|---|---|---|
| `TitleLarge` | 22 / 28 sp · Regular | Título da barra |
| `TextAppearance.Oficina.Placa` | 20 sp · `monospace` · bold · `letterSpacing` 0.1 | Placa no item da lista |
| `TitleMedium` | 16 / 24 sp · Medium | Modelo e ano |
| `BodyLarge` | 16 / 24 sp · Regular | Texto digitado nos campos |
| `BodyMedium` | 14 / 20 sp · Regular | Cliente no item da lista |
| `LabelLarge` | 14 / 20 sp · Medium | Botões |
| `LabelMedium` | 12 / 16 sp · Medium | Status, data, rótulo do Spinner |
| `BodySmall` | 12 / 16 sp · Regular | Mensagem de erro |

## Espaço, forma e toque
- Espaços em `res/values/dimens.xml`: `space_xs` 4dp · `space_s` 8dp · `space_m` 12dp · `space_l` 16dp (margem da tela, entre campos, dentro do card) · `space_xl` 24dp · `space_xxl` 32dp (fim do formulário) · `touch_min` 48dp · `field_height` 56dp.
- Raios: 8dp campos e chips · 12dp cards · 16dp FAB · 28dp diálogo · botões em pílula (padrão do Material 3).
- Profundidade: cards com borda 1dp `colorOutlineVariant` e sem sombra. Só o FAB tem sombra.
- Uma ação principal por tela.

## Telas
- **Lista (`MainActivity`)**: `MaterialToolbar` com título "Ordens de serviço"; `ListView` com padding 16dp e 12dp entre cards. Cada item é um `MaterialCardView`: linha 1 placa (`Oficina.Placa`) + chip de status à direita; linha 2 modelo e ano; linha 3 cliente à esquerda e data `dd/MM/yyyy` à direita. FAB "+" laranja no canto inferior direito.
- **Cadastro / Edição**: `MaterialToolbar` com seta de voltar e título "Nova ordem" / "Editar ordem"; formulário rolável (`ScrollView`) com os 11 campos na ordem da tabela em `PRODUCT.md` (Ano e Km podem ficar lado a lado); botão "Salvar" (filled, largura toda) no fim; edição tem também "Excluir" (outlined, vermelho) abaixo de "Salvar".
- Campo de data: `TextInputEditText` com `focusable="false"`; o toque abre `DatePickerDialog`; ícone de calendário no fim (`endIconMode="custom"`).
- Spinner de tipo de serviço começa em `Selecione...` (1º item do array). Spinner de status começa em `Aberta`.

## Componentes
- **Campo de texto**: `TextInputLayout` contornado (estilo `Widget.Oficina.TextInput`, raio 8dp, aplicado a todos pelo tema) com `TextInputEditText` dentro. O `hint` fica no `TextInputLayout`.
- **Lista suspensa**: `TextView` de rótulo acima (`labelFor`, `LabelMedium`, `colorOnSurfaceVariant`) + `Spinner` dentro de um `FrameLayout` com `@drawable/bg_field_outline` (borda 1dp `colorOutline`, raio 8dp, 56dp de altura).
- **Chip de status**: `TextView` com `@drawable/bg_status` (raio 8dp); o adapter troca `backgroundTint` e cor do texto pelo status (tabela acima).
- **Botões**: `MaterialButton` com altura mínima 48dp. Excluir: estilo `Widget.Material3.Button.OutlinedButton` com texto e borda `colorError`.
- **FAB**: `backgroundTint="?attr/colorSecondary"`, `tint="?attr/colorOnSecondary"`, ícone `ic_add` (Vector Asset), `contentDescription` "Nova ordem".
- **Diálogo**: `MaterialAlertDialogBuilder`, título "Excluir esta ordem?", botões "Não" e "Sim" ("Sim" em `colorError`).
- **Toast**: padrão do sistema, sem estilo próprio.

## Sistema existente
- Tema: `res/values/themes.xml` gerado pelo template, com o `parent` trocado para `Theme.Material3.Light.NoActionBar` e as cores acima. Apagar `res/values-night/themes.xml`: o app fica sempre no tema claro.
- Textos: `res/values/strings.xml`. Listas dos Spinners como `string-array` (`service_types` começando com `Selecione...`, `order_statuses`) com os valores de `PRODUCT.md`.

## Interação
- Todo campo tem rótulo com o nome do campo; obrigatórios terminam com `*` (ex.: `Placa *`). Campo de texto: `hint` no `TextInputLayout`. Spinner: `TextView` com `labelFor`.
- `inputType` adequado: `textCapCharacters` (placa), `number` (ano, km), `phone` (telefone), `numberDecimal` (valor), `textMultiLine` (descrição).
- Erro aparece no próprio campo: `TextInputLayout.setError(mensagem)` nos campos de texto e no de data; `setError(null)` para limpar. No Spinner, `((TextView) spinner.getSelectedView()).setError(mensagem)`. O foco vai para o primeiro campo inválido.
- Botões e itens com área de toque ≥ 48dp; FAB com `contentDescription` "Nova ordem"; seta de voltar com descrição padrão da `MaterialToolbar`.
- Exclusão sempre confirmada por AlertDialog ("Sim" / "Não").
- Contraste: texto ≥ 4.5:1 sobre o fundo (todas as combinações acima passam). `colorOutline` não é usado para texto.

## Layout e estados
- Lista vazia: texto `Nenhuma ordem cadastrada. Toque em + para criar.` centralizado, `BodyLarge`, `colorOnSurfaceVariant`, no lugar da lista.
- Sem estados de carregamento (operações locais e rápidas).

## Implementação (trechos)

### `res/values/colors.xml` (manter `black` e `white` do template)
```xml
<color name="color_primary">#1F4E79</color>
<color name="color_primary_container">#D3E4F7</color>
<color name="color_on_primary_container">#0B2A45</color>
<color name="color_secondary">#B4500A</color>
<color name="color_secondary_container">#FFDCC4</color>
<color name="color_on_secondary_container">#5C2700</color>
<color name="color_error">#BA1A1A</color>
<color name="color_surface">#F8F9FB</color>
<color name="color_on_surface">#1A1C1E</color>
<color name="color_on_surface_variant">#43474E</color>
<color name="color_outline">#73777F</color>
<color name="color_outline_variant">#C3C7CF</color>
<color name="status_open_bg">#D3E4F7</color>
<color name="status_open_text">#0B2A45</color>
<color name="status_in_progress_bg">#FFDCC4</color>
<color name="status_in_progress_text">#5C2700</color>
<color name="status_done_bg">#CDEBD3</color>
<color name="status_done_text">#0D3B1C</color>
<color name="status_delivered_bg">#E2E2E6</color>
<color name="status_delivered_text">#303236</color>
```

### `res/values/themes.xml`
Supõe o nome `Base.Theme.Oficina`; use o nome que o template gerou.
```xml
<style name="Base.Theme.Oficina" parent="Theme.Material3.Light.NoActionBar">
    <item name="colorPrimary">@color/color_primary</item>
    <item name="colorOnPrimary">@color/white</item>
    <item name="colorPrimaryContainer">@color/color_primary_container</item>
    <item name="colorOnPrimaryContainer">@color/color_on_primary_container</item>
    <item name="colorSecondary">@color/color_secondary</item>
    <item name="colorOnSecondary">@color/white</item>
    <item name="colorSecondaryContainer">@color/color_secondary_container</item>
    <item name="colorOnSecondaryContainer">@color/color_on_secondary_container</item>
    <item name="colorError">@color/color_error</item>
    <item name="android:colorBackground">@color/color_surface</item>
    <item name="colorSurface">@color/color_surface</item>
    <item name="colorOnSurface">@color/color_on_surface</item>
    <item name="colorOnSurfaceVariant">@color/color_on_surface_variant</item>
    <item name="colorOutline">@color/color_outline</item>
    <item name="colorOutlineVariant">@color/color_outline_variant</item>
    <item name="textInputStyle">@style/Widget.Oficina.TextInput</item>
</style>

<style name="Widget.Oficina.TextInput" parent="Widget.Material3.TextInputLayout.OutlinedBox">
    <item name="boxCornerRadiusTopStart">8dp</item>
    <item name="boxCornerRadiusTopEnd">8dp</item>
    <item name="boxCornerRadiusBottomStart">8dp</item>
    <item name="boxCornerRadiusBottomEnd">8dp</item>
</style>

<style name="TextAppearance.Oficina.Placa" parent="TextAppearance.Material3.TitleLarge">
    <item name="android:fontFamily">monospace</item>
    <item name="android:textStyle">bold</item>
    <item name="android:textSize">20sp</item>
    <item name="android:letterSpacing">0.1</item>
</style>
```

### `res/values/dimens.xml` (arquivo novo)
```xml
<resources>
    <dimen name="space_xs">4dp</dimen>
    <dimen name="space_s">8dp</dimen>
    <dimen name="space_m">12dp</dimen>
    <dimen name="space_l">16dp</dimen>
    <dimen name="space_xl">24dp</dimen>
    <dimen name="space_xxl">32dp</dimen>
    <dimen name="touch_min">48dp</dimen>
    <dimen name="field_height">56dp</dimen>
</resources>
```

### Drawables novos
```xml
<!-- res/drawable/bg_field_outline.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <stroke android:width="1dp" android:color="@color/color_outline" />
    <corners android:radius="8dp" />
</shape>

<!-- res/drawable/bg_status.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <corners android:radius="8dp" />
    <solid android:color="@color/white" />
</shape>
```
Ícones (`ic_add`, `ic_calendar` e os do status): Android Studio → New → Vector Asset → Clip Art.

### Campo de texto
```xml
<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/layoutPlate"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="@dimen/space_l"
    android:hint="@string/hint_plate">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/editPlate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="textCapCharacters" />
</com.google.android.material.textfield.TextInputLayout>
```

### Spinner
```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:labelFor="@+id/spinnerServiceType"
    android:text="@string/label_service_type"
    android:textAppearance="?attr/textAppearanceLabelMedium"
    android:textColor="?attr/colorOnSurfaceVariant" />

<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="@dimen/field_height"
    android:layout_marginTop="@dimen/space_xs"
    android:background="@drawable/bg_field_outline">

    <Spinner
        android:id="@+id/spinnerServiceType"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:entries="@array/service_types" />
</FrameLayout>
```

### Lista e card
```xml
<!-- activity_main.xml -->
<ListView
    android:id="@+id/listOrders"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/space_l"
    android:clipToPadding="false"
    android:divider="@android:color/transparent"
    android:dividerHeight="@dimen/space_m" />

<!-- item_service_order.xml (raiz) -->
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardBackgroundColor="@color/white"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeColor="@color/color_outline_variant"
    app:strokeWidth="1dp">
```

### Cor do status no `ServiceOrderAdapter`
```java
// Pinta o chip conforme o status (valores de order_statuses)
int bg = R.color.status_open_bg;
int fg = R.color.status_open_text;
switch (order.getStatus()) {
    case "Em andamento":
        bg = R.color.status_in_progress_bg;
        fg = R.color.status_in_progress_text;
        break;
    case "Concluída":
        bg = R.color.status_done_bg;
        fg = R.color.status_done_text;
        break;
    case "Entregue":
        bg = R.color.status_delivered_bg;
        fg = R.color.status_delivered_text;
        break;
}
textStatus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), bg));
textStatus.setTextColor(ContextCompat.getColor(getContext(), fg));
```

### Barra superior e FAB
```xml
<com.google.android.material.appbar.MaterialToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    android:background="?attr/colorPrimary"
    app:title="@string/title_orders"
    app:titleTextColor="?attr/colorOnPrimary"
    app:navigationIconTint="?attr/colorOnPrimary" />

<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fabNewOrder"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="@dimen/space_l"
    android:contentDescription="@string/fab_new_order"
    app:srcCompat="@drawable/ic_add"
    app:backgroundTint="?attr/colorSecondary"
    app:tint="?attr/colorOnSecondary" />
```
