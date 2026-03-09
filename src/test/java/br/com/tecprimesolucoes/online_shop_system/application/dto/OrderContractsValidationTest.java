package br.com.tecprimesolucoes.online_shop_system.application.dto;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderContractsValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldValidateCreateOrderRequestAndNestedItems() {
        CreateOrderRequest invalid = new CreateOrderRequest(
                "",
                "email-invalido",
                "",
                null,
                List.of(new OrderItemRequest(null, 0))
        );

        Set<?> violations = validator.validate(invalid);
        assertEquals(6, violations.size());
    }

    @Test
    void shouldValidateOrderItemRequestQuantityRule() {
        OrderItemRequest invalid = new OrderItemRequest(10L, 0);
        Set<?> violations = validator.validate(invalid);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldKeepOrderResponseContracts() {
        UUID createOrderId = UUID.randomUUID();
        CreateOrderResponse createResponse = new CreateOrderResponse(createOrderId, "ORD-20260306130000-ABC12345");
        assertEquals(createOrderId, createResponse.id());
        assertEquals("ORD-20260306130000-ABC12345", createResponse.numeroPedido());

        OrderItemDetailsResponse item = new OrderItemDetailsResponse(
                UUID.randomUUID(),
                1L,
                "Mouse",
                new BigDecimal("100.00"),
                2,
                new BigDecimal("200.00")
        );

        OrderDetailsResponse details = new OrderDetailsResponse(
                UUID.randomUUID(),
                "PED-001",
                "Joao",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                "CREATED",
                new BigDecimal("200.00"),
                LocalDateTime.of(2026, 3, 6, 13, 0),
                List.of(item)
        );

        assertEquals("PED-001", details.numeroPedido());
        assertEquals(1, details.itens().size());
        assertEquals("Mouse", details.itens().getFirst().nomeProduto());
        assertTrue(validator.validate(details).isEmpty());
    }
}
