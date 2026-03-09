package br.com.tecprimesolucoes.online_shop_system.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemAndCalculateLineTotal() {
        OrderItem item = OrderItem.create(10L, "Mouse", new BigDecimal("99.90"), 3);

        assertNotNull(item.getId());
        assertEquals(10L, item.getProductId());
        assertEquals(new BigDecimal("299.70"), item.getLineTotal());
    }

    @Test
    void shouldRestoreUsingGivenId() {
        UUID id = UUID.randomUUID();
        OrderItem item = OrderItem.restore(id, 1L, "Teclado", new BigDecimal("120.00"), 2);

        assertEquals(id, item.getId());
        assertEquals(new BigDecimal("240.00"), item.getLineTotal());
    }

    @Test
    void shouldThrowWhenProductIdIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderItem.create(null, "Mouse", BigDecimal.ONE, 1)
        );
        assertEquals("Id do produto é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenProductNameIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderItem.create(1L, " ", BigDecimal.ONE, 1)
        );
        assertEquals("Nome do produto é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUnitPriceIsNegativeOrZero() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderItem.create(1L, "Mouse", new BigDecimal("-1.00"), 1)
        );
        assertEquals("Preço unitário é inválido.", ex.getMessage());

        IllegalArgumentException exZero = assertThrows(
                IllegalArgumentException.class,
                () -> OrderItem.create(1L, "Mouse", BigDecimal.ZERO, 1)
        );
        assertEquals("Preço unitário é inválido.", exZero.getMessage());
    }

    @Test
    void shouldThrowWhenQuantityIsZeroOrLess() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderItem.create(1L, "Mouse", BigDecimal.ONE, 0)
        );
        assertEquals("Quantidade deve ser maior que zero.", ex.getMessage());
    }

    @Test
    void shouldUseIdentityForEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        OrderItem i1 = OrderItem.restore(id, 1L, "A", BigDecimal.ONE, 1);
        OrderItem i2 = OrderItem.restore(id, 1L, "B", BigDecimal.TEN, 2);
        OrderItem i3 = OrderItem.restore(UUID.randomUUID(), 1L, "A", BigDecimal.ONE, 1);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
        assertNotEquals(i1, i3);
    }

    @Test
    void shouldThrowWhenRestoreIdIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderItem.restore(null, 1L, "Mouse", BigDecimal.ONE, 1)
        );

        assertEquals("Id do item é obrigatório para restauração.", ex.getMessage());
    }
}
