package br.com.tecprimesolucoes.online_shop_system.domain.repository;

import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);
}