package br.com.tecprimesolucoes.online_shop_system.domain.gateway;

import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductGateway {

    List<Product> findAll();

    Optional<Product> findById(Long id);
}