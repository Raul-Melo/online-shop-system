package br.com.tecprimesolucoes.online_shop_system.domain.model;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void shouldCreateOrderWithDefaultStatusAndCalculatedTotal() {
        OrderItem i1 = OrderItem.create(1L, "Mouse", new BigDecimal("100.00"), 2);
        OrderItem i2 = OrderItem.create(2L, "Teclado", new BigDecimal("150.00"), 1);

        Order order = Order.create(
                "PED-001",
                "Joao",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                List.of(i1, i2)
        );

        assertNotNull(order.getId());
        assertNotNull(order.getCreatedAt());
        assertEquals("CREATED", order.getStatus());
        assertEquals(new BigDecimal("350.00"), order.getTotalAmount());
    }

    @Test
    void shouldReturnDefensiveCopyOfItems() {
        OrderItem item = OrderItem.create(1L, "Mouse", new BigDecimal("100.00"), 1);
        Order order = Order.create(
                "PED-001",
                "Joao",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                List.of(item)
        );

        List<OrderItem> returnedItems = order.getItems();
        returnedItems.clear();

        assertEquals(1, order.getItems().size());
    }

    @Test
    void shouldNotBeAffectedByExternalListMutationAfterCreation() {
        OrderItem i1 = OrderItem.create(1L, "Mouse", new BigDecimal("10.00"), 1);
        List<OrderItem> sourceItems = new ArrayList<>();
        sourceItems.add(i1);

        Order order = Order.create(
                "PED-001",
                "Joao",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                sourceItems
        );

        sourceItems.add(OrderItem.create(2L, "Teclado", new BigDecimal("50.00"), 1));

        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("10.00"), order.getTotalAmount());
    }

    @Test
    void shouldThrowWhenItemsIsEmpty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Order.create(
                        "PED-001",
                        "Joao",
                        "joao@email.com",
                        "Rua A, 100",
                        PaymentMethod.PIX,
                        List.of()
                )
        );

        assertEquals("Pedido deve possuir ao menos um item.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenRequiredFieldsAreInvalid() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Order.create(
                        " ",
                        "Joao",
                        "joao@email.com",
                        "Rua A, 100",
                        PaymentMethod.PIX,
                        List.of(OrderItem.create(1L, "Mouse", BigDecimal.ONE, 1))
                )
        );
        assertEquals("Número do pedido é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldUseIdentityForEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        List<OrderItem> items = List.of(OrderItem.create(1L, "Mouse", BigDecimal.ONE, 1));

        Order o1 = Order.restore(id, "PED-001", "Joao", "a@a.com", "Rua", PaymentMethod.PIX, "CREATED", null, items);
        Order o2 = Order.restore(id, "PED-002", "Maria", "b@b.com", "Rua B", PaymentMethod.BOLETO, "PAID", null, items);
        Order o3 = Order.restore(UUID.randomUUID(), "PED-001", "Joao", "a@a.com", "Rua", PaymentMethod.PIX, "CREATED", null, items);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertNotEquals(o1, o3);
    }
}

