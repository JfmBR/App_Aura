package br.edu.fasipe.oficina.validation;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import br.edu.fasipe.oficina.model.ServiceOrder;

/**
 * Regras dos campos da ordem de serviço (Java puro, testável sem Android).
 * Recebe valores já normalizados pela Activity e não altera nada.
 */
public final class ServiceOrderValidator {

    private static final String PLATE_PATTERN = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$";
    private static final int MIN_YEAR = 1950;

    private ServiceOrderValidator() {
    }

    /** Maiúsculas, sem "-" e espaços; null vira "". */
    public static String normalizePlate(String plate) {
        if (plate == null) {
            return "";
        }
        return plate.replace("-", "").replaceAll("\\s", "").toUpperCase(Locale.ROOT);
    }

    /** Só os dígitos; null vira "". */
    public static String digitsOnly(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("[^0-9]", "");
    }

    /**
     * Chave = nome da coluna, valor = mensagem de erro. Mapa vazio = válido.
     * LinkedHashMap mantém a ordem do formulário (o foco vai para o primeiro erro).
     */
    public static Map<String, String> validate(ServiceOrder order, int currentYear) {
        Map<String, String> errors = new LinkedHashMap<>();

        String plate = order.getPlate();
        if (isBlank(plate)) {
            errors.put("plate", "Informe a placa");
        } else if (!plate.matches(PLATE_PATTERN)) {
            errors.put("plate", "Placa inválida (ex.: ABC1234 ou ABC1D23)");
        }

        if (isBlank(order.getVehicleModel())) {
            errors.put("vehicle_model", "Informe o modelo");
        }

        Integer year = order.getVehicleYear();
        int maxYear = currentYear + 1;
        if (year != null && (year < MIN_YEAR || year > maxYear)) {
            errors.put("vehicle_year", "Ano deve estar entre " + MIN_YEAR + " e " + maxYear);
        }

        Integer mileage = order.getMileage();
        if (mileage != null && mileage < 0) {
            errors.put("mileage", "Km não pode ser negativo");
        }

        if (isBlank(order.getCustomerName())) {
            errors.put("customer_name", "Informe o nome do cliente");
        }

        String phone = order.getCustomerPhone();
        if (isBlank(phone)) {
            errors.put("customer_phone", "Informe o telefone");
        } else if (phone.length() != 10 && phone.length() != 11) {
            errors.put("customer_phone", "Telefone deve ter 10 ou 11 dígitos");
        }

        if (isBlank(order.getServiceType())) {
            errors.put("service_type", "Selecione o tipo de serviço");
        }

        Double price = order.getPrice();
        if (price != null && price < 0) {
            errors.put("price", "Valor não pode ser negativo");
        }

        if (isBlank(order.getEntryDate())) {
            errors.put("entry_date", "Informe a data de entrada");
        }

        if (isBlank(order.getStatus())) {
            errors.put("status", "Selecione o status");
        }

        return errors;
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
