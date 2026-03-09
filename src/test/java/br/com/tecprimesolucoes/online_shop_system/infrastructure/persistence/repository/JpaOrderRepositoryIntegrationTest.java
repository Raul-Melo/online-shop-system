package br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.repository;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.model.OrderItem;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({JpaOrderRepository.class, OrderPersistenceMapper.class})
class JpaOrderRepositoryIntegrationTest {

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    @Autowired
    private SpringDataOrderJpaRepository springDataOrderJpaRepository;

    @Test
    void shouldSaveOrderAndItemsAndLoadBackById() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 3, 6, 13, 0);

        OrderItem i1 = OrderItem.restore(UUID.randomUUID(), 1L, "Mouse", new BigDecimal("100.00"), 2);
        OrderItem i2 = OrderItem.restore(UUID.randomUUID(), 2L, "Teclado", new BigDecimal("150.00"), 1);

        Order order = Order.restore(
                orderId,
                "PED-INT-001",
                "Cliente Teste",
                "cliente@teste.com",
                "Rua Teste, 123",
                PaymentMethod.PIX,
                "CREATED",
                createdAt,
                List.of(i1, i2)
        );

        Order saved = jpaOrderRepository.save(order);
        Optional<Order> loadedOpt = jpaOrderRepository.findById(saved.getId());

        assertTrue(loadedOpt.isPresent());
        Order loaded = loadedOpt.get();

        assertEquals(orderId, loaded.getId());
        assertEquals("PED-INT-001", loaded.getOrderNumber());
        assertEquals("Cliente Teste", loaded.getCustomerName());
        assertEquals(PaymentMethod.PIX, loaded.getPaymentMethod());
        assertEquals("CREATED", loaded.getStatus());
        assertEquals(createdAt, loaded.getCreatedAt());
        assertEquals(new BigDecimal("350.00"), loaded.getTotalAmount());
        assertEquals(2, loaded.getItems().size());
    }

    @Test
    void shouldReturnEmptyWhenOrderDoesNotExist() {
        Optional<Order> result = jpaOrderRepository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldLoadItemsWithEntityGraphInSpringDataRepository() {
        OrderItem item = OrderItem.restore(UUID.randomUUID(), 10L, "Fone", new BigDecimal("80.00"), 1);
        Order order = Order.restore(
                UUID.randomUUID(),
                "PED-INT-002",
                "Maria",
                "maria@teste.com",
                "Rua B, 50",
                PaymentMethod.CARTAO,
                "CREATED",
                LocalDateTime.of(2026, 3, 6, 13, 10),
                List.of(item)
        );

        Order saved = jpaOrderRepository.save(order);
        int itemsSize = springDataOrderJpaRepository.findById(saved.getId())
                .orElseThrow()
                .getItems()
                .size();

        assertEquals(1, itemsSize);
    }
}
