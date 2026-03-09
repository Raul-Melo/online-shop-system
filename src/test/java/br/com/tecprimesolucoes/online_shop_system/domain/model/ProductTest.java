package br.com.tecprimesolucoes.online_shop_system.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void shouldCreateProductWhenDataIsValid() {
        Product product = Product.restore(
                1L,
                "Notebook",
                "Notebook gamer",
                new BigDecimal("4999.90"),
                10,
                "img.png"
        );

        assertEquals(1L, product.getId());
        assertEquals("Notebook", product.getNome());
        assertEquals(new BigDecimal("4999.90"), product.getPreco());
        assertEquals(10, product.getEstoque());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Product.restore(null, "P", "D", BigDecimal.ONE, 1, "I")
        );
        assertEquals("Id do produto é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Product.restore(1L, " ", "D", BigDecimal.ONE, 1, "I")
        );
        assertEquals("Nome do produto é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPriceIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Product.restore(1L, "Produto", "D", null, 1, "I")
        );
        assertEquals("Preço do produto é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenStockIsNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Product.restore(1L, "Produto", "D", BigDecimal.ONE, -1, "I")
        );
        assertEquals("Estoque do produto é inválido.", ex.getMessage());
    }

    @Test
    void shouldUseIdentityForEqualsAndHashCode() {
        Product p1 = Product.restore(1L, "A", "D", BigDecimal.ONE, 1, "I");
        Product p2 = Product.restore(1L, "B", "D2", BigDecimal.TEN, 5, "X");
        Product p3 = Product.restore(2L, "A", "D", BigDecimal.ONE, 1, "I");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }
}

