package br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.repository;

import br.com.tecprimesolucoes.online_shop_system.domain.model.Order;
import br.com.tecprimesolucoes.online_shop_system.domain.repository.OrderRepository;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.entity.OrderEntity;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderJpaRepository springDataOrderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    public JpaOrderRepository(SpringDataOrderJpaRepository springDataOrderJpaRepository,
                              OrderPersistenceMapper orderPersistenceMapper) {
        this.springDataOrderJpaRepository = springDataOrderJpaRepository;
        this.orderPersistenceMapper = orderPersistenceMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderPersistenceMapper.toEntity(order);
        OrderEntity savedEntity = springDataOrderJpaRepository.save(entity);
        return orderPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return springDataOrderJpaRepository.findById(id)
                .map(orderPersistenceMapper::toDomain);
    }
}