package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.api;

import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderItemDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.CreateOrderUseCase;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetOrderByIdUseCase;
import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerGetIntegrationTest {

    @Test
    void shouldExposeGetOrderByIdEndpoint() throws Exception {
        CreateOrderUseCase createOrderUseCase = mock(CreateOrderUseCase.class);
        GetOrderByIdUseCase getOrderByIdUseCase = mock(GetOrderByIdUseCase.class);

        UUID orderId = UUID.randomUUID();

        when(getOrderByIdUseCase.execute(orderId)).thenReturn(
                new OrderDetailsResponse(
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
                )
        );

        OrderController controller = new OrderController(createOrderUseCase, getOrderByIdUseCase);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.numeroPedido").value("ORD-123"))
                .andExpect(jsonPath("$.itens[0].productId").value(1));
    }
}
