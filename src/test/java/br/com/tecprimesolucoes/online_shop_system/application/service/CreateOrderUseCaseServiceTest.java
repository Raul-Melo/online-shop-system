package br.com.tecprimesolucoes.online_shop_system.application.service;

import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderRequest;
import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderItemRequest;
import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.domain.gateway.ProductGateway;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.domain.repository.OrderRepository;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.config.OrderNumberGenerator;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.InsufficientStockException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.InvalidPaymentMethodException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseServiceTest {

    @Mock
    private ProductGateway productGateway;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderNumberGenerator orderNumberGenerator;

    @InjectMocks
    private CreateOrderUseCaseService service;

    @Test
    void shouldCreateOrderAndReturnOrderNumber() {
        Product product1 = Product.restore(1L, "Mouse", "Optico", new BigDecimal("100.00"), 10, "img1");
        Product product2 = Product.restore(2L, "Teclado", "Mecanico", new BigDecimal("250.00"), 3, "img2");

        when(productGateway.findById(1L)).thenReturn(Optional.of(product1));
        when(productGateway.findById(2L)).thenReturn(Optional.of(product2));
        when(orderNumberGenerator.generate()).thenReturn("ORD-20260306130000-ABCDE123");
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderRequest request = new CreateOrderRequest(
                "Joao",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                List.of(new OrderItemRequest(1L, 2), new OrderItemRequest(2L, 1))
        );

        CreateOrderResponse response = service.execute(request);

        assertEquals("ORD-20260306130000-ABCDE123", response.numeroPedido());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals("Joao", savedOrder.getCustomerName());
        assertEquals(new BigDecimal("450.00"), savedOrder.getTotalAmount());
        assertEquals(2, savedOrder.getItems().size());
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        when(productGateway.findById(99L)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest(
                "Maria",
                "maria@email.com",
                "Rua B, 200",
                PaymentMethod.CARTAO,
                List.of(new OrderItemRequest(99L, 1))
        );

        assertThrows(ProductNotFoundException.class, () -> service.execute(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowWhenRequestedQuantityIsGreaterThanStock() {
        Product product = Product.restore(5L, "Headset", "Audio", new BigDecimal("80.00"), 2, "img5");
        when(productGateway.findById(5L)).thenReturn(Optional.of(product));

        CreateOrderRequest request = new CreateOrderRequest(
                "Ana",
                "ana@email.com",
                "Rua C, 300",
                PaymentMethod.BOLETO,
                List.of(new OrderItemRequest(5L, 3))
        );

        assertThrows(InsufficientStockException.class, () -> service.execute(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowWhenPaymentMethodIsNull() {
        CreateOrderRequest request = new CreateOrderRequest(
                "Carlos",
                "carlos@email.com",
                "Rua D, 400",
                null,
                List.of(new OrderItemRequest(1L, 1))
        );

        assertThrows(InvalidPaymentMethodException.class, () -> service.execute(request));
        verifyNoInteractions(productGateway, orderRepository, orderNumberGenerator);
    }
}
