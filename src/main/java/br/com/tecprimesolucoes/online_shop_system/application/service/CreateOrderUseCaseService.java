package br.com.tecprimesolucoes.online_shop_system.application.service;

import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderRequest;
import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderResponse;
import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderItemRequest;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.CreateOrderUseCase;
import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.domain.gateway.ProductGateway;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.model.OrderItem;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.domain.repository.OrderRepository;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.config.OrderNumberGenerator;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.InsufficientStockException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.InvalidPaymentMethodException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreateOrderUseCaseService implements CreateOrderUseCase {

    private final ProductGateway productGateway;
    private final OrderRepository orderRepository;
    private final OrderNumberGenerator orderNumberGenerator;

    public CreateOrderUseCaseService(ProductGateway productGateway,
                                     OrderRepository orderRepository,
                                     OrderNumberGenerator orderNumberGenerator) {
        this.productGateway = productGateway;
        this.orderRepository = orderRepository;
        this.orderNumberGenerator = orderNumberGenerator;
    }

    @Override
    @Transactional
    public CreateOrderResponse execute(CreateOrderRequest request) {

        List<OrderItem> items = request.produtos().stream()
                .map(this::buildOrderItem)
                .toList();

        String orderNumber = orderNumberGenerator.generate();

        Order order = Order.create(
                orderNumber,
                request.nome(),
                request.email(),
                request.endereco(),
                request.formaPagamento(),
                items
        );

        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResponse(savedOrder.getId(), savedOrder.getOrderNumber());
    }


    private OrderItem buildOrderItem(OrderItemRequest itemRequest) {
        Product product = productGateway.findById(itemRequest.productId())
                .orElseThrow(() -> new ProductNotFoundException(itemRequest.productId()));

        validateStock(product, itemRequest.quantity());

        return OrderItem.create(
                product.getId(),
                product.getNome(),
                product.getPreco(),
                itemRequest.quantity()
        );
    }

    private void validateStock(Product product, Integer requestedQuantity) {
        if (requestedQuantity > product.getEstoque()) {
            throw new InsufficientStockException(
                    product.getId(),
                    requestedQuantity,
                    product.getEstoque()
            );
        }
    }
}
