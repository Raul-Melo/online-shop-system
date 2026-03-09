package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper;

import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderItemDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDetailsResponseMapper {

    public OrderDetailsResponse toResponse(Order order) {
        List<OrderItemDetailsResponse> itens = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new OrderDetailsResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerAddress(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itens
        );
    }

    private OrderItemDetailsResponse toItemResponse(OrderItem item) {
        return new OrderItemDetailsResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}