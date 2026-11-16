package br.edu.fasipe.oficina.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import br.edu.fasipe.oficina.R;
import br.edu.fasipe.oficina.model.ServiceOrder;
import br.edu.fasipe.oficina.validation.ServiceOrderValidator;

/**
 * Campos de form_service_order.xml, usados pelo cadastro e pela edição.
 * Só cuida da tela: ler, mostrar erros e escolher data. Gravar fica com a Activity.
 */
public class ServiceOrderForm {

    // Ordem dos campos na tela: o foco vai para o primeiro inválido
    private static final String[] FIELD_ORDER = {
            "plate", "vehicle_model", "vehicle_year", "mileage", "customer_name",
            "customer_phone", "service_type", "description", "price", "entry_date", "status"
    };

    private final Activity activity;
    private final TextInputLayout layoutPlate;
    private final TextInputLayout layoutVehicleModel;
    private final TextInputLayout layoutVehicleYear;
    private final TextInputLayout layoutMileage;
    private final TextInputLayout layoutCustomerName;
    private final TextInputLayout layoutCustomerPhone;
    private final TextInputLayout layoutDescription;
    private final TextInputLayout layoutPrice;
    private final TextInputLayout layoutEntryDate;
    private final Spinner spinnerServiceType;
    private final Spinner spinnerStatus;

    // true quando o Valor tem texto que não vira número (ex.: ".")
    private boolean priceInvalid;

    public ServiceOrderForm(Activity activity) {
        this.activity = activity;
        layoutPlate = activity.findViewById(R.id.layoutPlate);
        layoutVehicleModel = activity.findViewById(R.id.layoutVehicleModel);
        layoutVehicleYear = activity.findViewById(R.id.layoutVehicleYear);
        layoutMileage = activity.findViewById(R.id.layoutMileage);
        layoutCustomerName = activity.findViewById(R.id.layoutCustomerName);
        layoutCustomerPhone = activity.findViewById(R.id.layoutCustomerPhone);
        layoutDescription = activity.findViewById(R.id.layoutDescription);
        layoutPrice = activity.findViewById(R.id.layoutPrice);
        layoutEntryDate = activity.findViewById(R.id.layoutEntryDate);
        spinnerServiceType = activity.findViewById(R.id.spinnerServiceType);
        spinnerStatus = activity.findViewById(R.id.spinnerStatus);

        layoutEntryDate.getEditText().setOnClickListener(v -> openDatePicker());
        layoutEntryDate.setEndIconOnClickListener(v -> openDatePicker());
    }

    /** Data de hoje no formato do banco (yyyy-MM-dd). */
    public static String today() {
        Calendar calendar = Calendar.getInstance();
        return toIsoDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    /** Mostra no campo uma data yyyy-MM-dd como dd/MM/yyyy. */
    public void setEntryDate(String isoDate) {
        String[] parts = isoDate.split("-");
        layoutEntryDate.getEditText().setText(parts[2] + "/" + parts[1] + "/" + parts[0]);
    }

    /** Monta a OS com os valores já normalizados, como PRODUCT.md pede. */
    public ServiceOrder readOrder() {
        ServiceOrder order = new ServiceOrder();
        order.setPlate(ServiceOrderValidator.normalizePlate(text(layoutPlate)));
        order.setVehicleModel(text(layoutVehicleModel));
        order.setVehicleYear(toInteger(text(layoutVehicleYear)));
        order.setMileage(toInteger(text(layoutMileage)));
        order.setCustomerName(text(layoutCustomerName));
        order.setCustomerPhone(ServiceOrderValidator.digitsOnly(text(layoutCustomerPhone)));
        // 1º item do Spinner é "Selecione..." = nada escolhido
        order.setServiceType(spinnerServiceType.getSelectedItemPosition() == 0 ? ""
                : (String) spinnerServiceType.getSelectedItem());
        order.setDescription(emptyToNull(text(layoutDescription)));
        order.setPrice(readPrice());
        order.setEntryDate(displayToIsoDate(text(layoutEntryDate)));
        order.setStatus((String) spinnerStatus.getSelectedItem());
        return order;
    }

    /** Mostra os erros nos campos e foca o primeiro. Devolve true se estiver tudo certo. */
    public boolean validateAndShowErrors(ServiceOrder order) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Map<String, String> errors = ServiceOrderValidator.validate(order, currentYear);
        if (priceInvalid) {
            errors.put("price", activity.getString(R.string.error_price_invalid));
        }

        View firstInvalid = null;
        for (String field : FIELD_ORDER) {
            View view = showError(field, errors.get(field));
            if (firstInvalid == null && errors.containsKey(field)) {
                firstInvalid = view;
            }
        }
        if (firstInvalid != null && !firstInvalid.requestFocus()) {
            // Spinner e data não recebem foco: só rola a tela até eles
            firstInvalid.requestRectangleOnScreen(
                    new Rect(0, 0, firstInvalid.getWidth(), firstInvalid.getHeight()));
        }
        return errors.isEmpty();
    }

    // Mensagem null limpa o erro. Devolve a View que deve receber o foco.
    private View showError(String field, String message) {
        switch (field) {
            case "plate":
                return setLayoutError(layoutPlate, message);
            case "vehicle_model":
                return setLayoutError(layoutVehicleModel, message);
            case "vehicle_year":
                return setLayoutError(layoutVehicleYear, message);
            case "mileage":
                return setLayoutError(layoutMileage, message);
            case "customer_name":
                return setLayoutError(layoutCustomerName, message);
            case "customer_phone":
                return setLayoutError(layoutCustomerPhone, message);
            case "service_type":
                return setSpinnerError(spinnerServiceType, message);
            case "description":
                return setLayoutError(layoutDescription, message);
            case "price":
                return setLayoutError(layoutPrice, message);
            case "entry_date":
                return setLayoutError(layoutEntryDate, message);
            default:
                return setSpinnerError(spinnerStatus, message);
        }
    }

    private View setLayoutError(TextInputLayout layout, String message) {
        layout.setError(message);
        return layout.getEditText();
    }

    private View setSpinnerError(Spinner spinner, String message) {
        View selected = spinner.getSelectedView();
        if (selected instanceof TextView) {
            ((TextView) selected).setError(message);
        }
        return spinner;
    }

    private Double readPrice() {
        // Teclado em pt-BR pode digitar vírgula: 12,50 vira 12.50
        String price = text(layoutPrice).replace(',', '.');
        priceInvalid = false;
        if (price.isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(price);
        } catch (NumberFormatException e) {
            priceInvalid = true;
            return null;
        }
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        String isoDate = displayToIsoDate(text(layoutEntryDate));
        if (!isoDate.isEmpty()) {
            String[] parts = isoDate.split("-");
            calendar.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1,
                    Integer.parseInt(parts[2]));
        }
        new DatePickerDialog(activity, (picker, year, month, day) -> {
            setEntryDate(toIsoDate(year, month + 1, day));
            layoutEntryDate.setError(null);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private static String toIsoDate(int year, int month, int day) {
        return String.format(Locale.ROOT, "%04d-%02d-%02d", year, month, day);
    }

    // dd/MM/yyyy -> yyyy-MM-dd; campo vazio vira ""
    private static String displayToIsoDate(String displayDate) {
        String[] parts = displayDate.split("/");
        if (parts.length != 3) {
            return "";
        }
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    private static String text(TextInputLayout layout) {
        EditText edit = layout.getEditText();
        Editable value = edit == null ? null : edit.getText();
        return value == null ? "" : value.toString().trim();
    }

    // inputType number + maxLength garantem só dígitos que cabem em int
    private static Integer toInteger(String value) {
        return value.isEmpty() ? null : Integer.valueOf(value);
    }

    private static String emptyToNull(String value) {
        return value.isEmpty() ? null : value;
    }
}
