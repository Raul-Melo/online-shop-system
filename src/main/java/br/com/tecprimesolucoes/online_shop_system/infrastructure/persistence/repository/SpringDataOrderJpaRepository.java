package br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.repository;

import br.com.tecprimesolucoes.online_shop_system.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = "items")
    Optional<OrderEntity> findById(UUID id);
}