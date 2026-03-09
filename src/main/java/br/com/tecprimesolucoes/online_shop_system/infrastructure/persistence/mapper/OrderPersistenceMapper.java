package br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.mapper;

import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.model.OrderItem;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.entity.OrderEntity;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setOrderNumber(order.getOrderNumber());
        orderEntity.setCustomerName(order.getCustomerName());
        orderEntity.setCustomerEmail(order.getCustomerEmail());
        orderEntity.setCustomerAddress(order.getCustomerAddress());
        orderEntity.setPaymentMethod(order.getPaymentMethod());
        orderEntity.setTotalAmount(order.getTotalAmount());
        orderEntity.setStatus(order.getStatus());
        orderEntity.setCreatedAt(order.getCreatedAt());

        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(this::toItemEntity)
                .toList();

        itemEntities.forEach(orderEntity::addItem);

        return orderEntity;
    }

    public Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(this::toDomainItem)
                .toList();

        return Order.restore(
                entity.getId(),
                entity.getOrderNumber(),
                entity.getCustomerName(),
                entity.getCustomerEmail(),
                entity.getCustomerAddress(),
                entity.getPaymentMethod(),
                entity.getStatus(),
                entity.getCreatedAt(),
                items
        );
    }

    private OrderItemEntity toItemEntity(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setQuantity(item.getQuantity());
        entity.setLineTotal(item.getLineTotal());
        return entity;
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        return OrderItem.restore(
                entity.getId(),
                entity.getProductId(),
                entity.getProductName(),
                entity.getUnitPrice(),
                entity.getQuantity()
        );
    }
}