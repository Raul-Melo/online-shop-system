package br.com.tecprimesolucoes.online_shop_system.domain;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.domain.gateway.ProductGateway;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.model.OrderItem;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DomainAbstractionsMockTest {

    @Test
    void shouldPlaceOrderUsingOnlyDomainInterfaces() {
        ProductGateway productGateway = mock(ProductGateway.class);
        OrderRepository orderRepository = mock(OrderRepository.class);

        Product product = Product.restore(10L, "Mouse", "Optico", new BigDecimal("99.90"), 5, "img.png");
        when(productGateway.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CheckoutDomainService service = new CheckoutDomainService(productGateway, orderRepository);
        Order created = service.placeOrder(
                "PED-001",
                "Joao",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                List.of(new ItemRequest(10L, 2))
        );

        assertEquals(new BigDecimal("199.80"), created.getTotalAmount());
        assertEquals("CREATED", created.getStatus());
        verify(productGateway).findById(10L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldFailWhenProductDoesNotExistAndNotSaveOrder() {
        ProductGateway productGateway = mock(ProductGateway.class);
        OrderRepository orderRepository = mock(OrderRepository.class);

        when(productGateway.findById(99L)).thenReturn(Optional.empty());

        CheckoutDomainService service = new CheckoutDomainService(productGateway, orderRepository);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.placeOrder(
                        "PED-002",
                        "Maria",
                        "maria@email.com",
                        "Rua B, 10",
                        PaymentMethod.CARTAO,
                        List.of(new ItemRequest(99L, 1))
                )
        );

        assertEquals("Produto não encontrado: 99", ex.getMessage());
        verify(productGateway).findById(99L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    private static final class CheckoutDomainService {
        private final ProductGateway productGateway;
        private final OrderRepository orderRepository;

        private CheckoutDomainService(ProductGateway productGateway, OrderRepository orderRepository) {
            this.productGateway = productGateway;
            this.orderRepository = orderRepository;
        }

        private Order placeOrder(String orderNumber,
                                 String customerName,
                                 String customerEmail,
                                 String customerAddress,
                                 PaymentMethod paymentMethod,
                                 List<ItemRequest> itemsRequest) {
            List<OrderItem> items = itemsRequest.stream()
                    .map(req -> productGateway.findById(req.productId())
                            .map(product -> OrderItem.create(
                                    product.getId(),
                                    product.getNome(),
                                    product.getPreco(),
                                    req.quantity()
                            ))
                            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + req.productId())))
                    .toList();

            Order order = Order.create(
                    orderNumber,
                    customerName,
                    customerEmail,
                    customerAddress,
                    paymentMethod,
                    items
            );

            return orderRepository.save(order);
        }
    }

    private record ItemRequest(Long productId, Integer quantity) {
    }
}

