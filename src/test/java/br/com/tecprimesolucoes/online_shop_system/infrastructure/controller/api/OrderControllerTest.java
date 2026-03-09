package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.api;

import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderItemDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.CreateOrderUseCase;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetOrderByIdUseCase;
import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Test
    void shouldReturnOrderById() {
        CreateOrderUseCase createOrderUseCase = mock(CreateOrderUseCase.class);
        GetOrderByIdUseCase getOrderByIdUseCase = mock(GetOrderByIdUseCase.class);

        UUID orderId = UUID.randomUUID();

        OrderDetailsResponse expected = new OrderDetailsResponse(
                orderId,
                "ORD-123",
                "João",
                "joao@email.com",
                "Rua A, 100",
                PaymentMethod.PIX,
                "CREATED",
                new BigDecimal("200.00"),
                LocalDateTime.of(2026, 3, 6, 12, 0),
                List.of(new OrderItemDetailsResponse(
                        UUID.randomUUID(),
                        1L,
                        "Mouse",
                        new BigDecimal("100.00"),
                        2,
                        new BigDecimal("200.00")
                ))
        );

        when(getOrderByIdUseCase.execute(orderId)).thenReturn(expected);

        OrderController controller = new OrderController(createOrderUseCase, getOrderByIdUseCase);

        ResponseEntity<OrderDetailsResponse> response = controller.getOrderById(orderId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(orderId, response.getBody().id());
        verify(getOrderByIdUseCase).execute(orderId);
    }
}
