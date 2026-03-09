package br.com.tecprimesolucoes.online_shop_system.application.usecase;

import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;

import java.util.List;

public interface GetProductsUseCase {
    List<Product> execute();
}
