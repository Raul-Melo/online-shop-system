package br.com.tecprimesolucoes.online_shop_system.application.service;

import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderRequest;
import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderItemRequest;
import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.domain.gateway.ProductGateway;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.config.OrderNumberGenerator;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.mapper.OrderPersistenceMapper;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.repository.JpaOrderRepository;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.repository.SpringDataOrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({
        JpaOrderRepository.class,
        OrderPersistenceMapper.class,
        CreateOrderUseCaseService.class,
        CreateOrderUseCaseServiceIntegrationTest.TestConfig.class
})
class CreateOrderUseCaseServiceIntegrationTest {

    @Autowired
    private CreateOrderUseCaseService service;

    @Autowired
    private ProductGateway productGateway;

    @Autowired
    private OrderNumberGenerator orderNumberGenerator;

    @Autowired
    private SpringDataOrderJpaRepository springDataOrderJpaRepository;

    @Test
    void shouldPersistOrderAndItemsWithRealJpaRepository() {
        Product product = Product.restore(10L, "Mouse", "Optico", new BigDecimal("100.00"), 10, "img");
        when(productGateway.findById(10L)).thenReturn(Optional.of(product));
        when(orderNumberGenerator.generate()).thenReturn("ORD-20260306130500-INT12345");

        CreateOrderRequest request = new CreateOrderRequest(
                "Cliente Integracao",
                "integracao@teste.com",
                "Rua Integracao, 123",
                PaymentMethod.PIX,
                List.of(new OrderItemRequest(10L, 2))
        );

        CreateOrderResponse response = service.execute(request);

        assertEquals("ORD-20260306130500-INT12345", response.numeroPedido());
        assertEquals(1, springDataOrderJpaRepository.count());

        var saved = springDataOrderJpaRepository.findAll().getFirst();
        assertEquals("Cliente Integracao", saved.getCustomerName());
        assertEquals(new BigDecimal("200.00"), saved.getTotalAmount());
        assertEquals(1, saved.getItems().size());
        assertEquals(10L, saved.getItems().getFirst().getProductId());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ProductGateway productGateway() {
            return mock(ProductGateway.class);
        }

        @Bean
        OrderNumberGenerator orderNumberGenerator() {
            return mock(OrderNumberGenerator.class);
        }
    }
}
