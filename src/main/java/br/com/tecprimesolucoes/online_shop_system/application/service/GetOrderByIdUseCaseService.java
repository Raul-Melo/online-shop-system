package br.com.tecprimesolucoes.online_shop_system.application.service;

import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderDetailsResponse;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetOrderByIdUseCase;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.repository.OrderRepository;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper.OrderDetailsResponseMapper;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOrderByIdUseCaseService implements GetOrderByIdUseCase {

    private final OrderRepository orderRepository;
    private final OrderDetailsResponseMapper orderDetailsResponseMapper;

    public GetOrderByIdUseCaseService(OrderRepository orderRepository,
                                      OrderDetailsResponseMapper orderDetailsResponseMapper) {
        this.orderRepository = orderRepository;
        this.orderDetailsResponseMapper = orderDetailsResponseMapper;
    }

    @Override
    public OrderDetailsResponse execute(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderDetailsResponseMapper.toResponse(order);
    }
}