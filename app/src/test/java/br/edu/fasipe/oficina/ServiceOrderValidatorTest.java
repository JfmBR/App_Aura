package br.edu.fasipe.oficina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import br.edu.fasipe.oficina.model.ServiceOrder;
import br.edu.fasipe.oficina.validation.ServiceOrderValidator;

/**
 * Um teste por linha da tabela "Entradas e saídas esperadas" do PRODUCT.md.
 */
public class ServiceOrderValidatorTest {

    private static final int CURRENT_YEAR = 2026;

    // OS com todos os obrigatórios válidos e opcionais null
    private ServiceOrder validOrder() {
        ServiceOrder order = new ServiceOrder();
        order.setPlate("ABC1D23");
        order.setVehicleModel("Gol");
        order.setCustomerName("Maria");
        order.setCustomerPhone("11987654321");
        order.setServiceType("Freios");
        order.setEntryDate("2026-11-16");
        order.setStatus("Aberta");
        return order;
    }

    private Map<String, String> validate(ServiceOrder order) {
        return ServiceOrderValidator.validate(order, CURRENT_YEAR);
    }

    @Test
    public void validOrderWithNullOptionals_returnsEmptyMap() {
        assertTrue(validate(validOrder()).isEmpty());
    }

    @Test
    public void emptyPlate_returnsRequiredMessage() {
        ServiceOrder order = validOrder();
        order.setPlate("");
        assertEquals("Informe a placa", validate(order).get("plate"));
    }

    @Test
    public void malformedPlate_returnsFormatMessage() {
        ServiceOrder order = validOrder();
        order.setPlate("AB123");
        assertEquals("Placa inválida (ex.: ABC1234 ou ABC1D23)", validate(order).get("plate"));
    }

    @Test
    public void normalizePlate_removesDashAndUppercases() {
        assertEquals("ABC1D23", ServiceOrderValidator.normalizePlate("abc-1d23"));
    }

    @Test
    public void yearBefore1950_returnsRangeMessage() {
        ServiceOrder order = validOrder();
        order.setVehicleYear(1949);
        assertEquals("Ano deve estar entre 1950 e 2027", validate(order).get("vehicle_year"));
    }

    @Test
    public void yearNextYear_isValid() {
        ServiceOrder order = validOrder();
        order.setVehicleYear(2027);
        assertTrue(validate(order).isEmpty());
    }

    @Test
    public void negativeMileage_returnsMessage() {
        ServiceOrder order = validOrder();
        order.setMileage(-1);
        assertEquals("Km não pode ser negativo", validate(order).get("mileage"));
    }

    @Test
    public void phoneWithNineDigits_returnsLengthMessage() {
        ServiceOrder order = validOrder();
        order.setCustomerPhone("123456789");
        assertEquals("Telefone deve ter 10 ou 11 dígitos", validate(order).get("customer_phone"));
    }

    @Test
    public void phoneWithElevenDigits_isValid() {
        ServiceOrder order = validOrder();
        order.setCustomerPhone("11987654321");
        assertTrue(validate(order).isEmpty());
    }

    @Test
    public void negativePrice_returnsMessage() {
        ServiceOrder order = validOrder();
        order.setPrice(-0.01);
        assertEquals("Valor não pode ser negativo", validate(order).get("price"));
    }

    @Test
    public void severalInvalidFields_returnsOneEntryPerField() {
        ServiceOrder order = validOrder();
        order.setPlate("");
        order.setVehicleModel("  ");
        order.setCustomerName("");
        order.setCustomerPhone("");
        order.setServiceType("");
        order.setEntryDate("");
        order.setStatus("");
        Map<String, String> errors = validate(order);
        assertEquals(7, errors.size());
        assertEquals("Informe o modelo", errors.get("vehicle_model"));
        assertEquals("Informe o nome do cliente", errors.get("customer_name"));
        assertEquals("Informe o telefone", errors.get("customer_phone"));
        assertEquals("Selecione o tipo de serviço", errors.get("service_type"));
        assertEquals("Informe a data de entrada", errors.get("entry_date"));
        assertEquals("Selecione o status", errors.get("status"));
    }
}
