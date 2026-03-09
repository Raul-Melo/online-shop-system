package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.api;

import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderRequest;
import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.CreateOrderUseCase;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetOrderByIdUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderByIdUseCase getOrderByIdUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        CreateOrderResponse response = createOrderUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsResponse> getOrderById(@PathVariable UUID id) {
        OrderDetailsResponse response = getOrderByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}