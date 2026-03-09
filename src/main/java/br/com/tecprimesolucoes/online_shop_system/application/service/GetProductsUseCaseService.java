package br.com.tecprimesolucoes.online_shop_system.application.service;

import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetProductsUseCase;
import br.com.tecprimesolucoes.online_shop_system.domain.gateway.ProductGateway;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetProductsUseCaseService implements GetProductsUseCase {

    private final ProductGateway productGateway;

    public GetProductsUseCaseService(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public List<Product> execute() {
        return productGateway.findAll();
    }
}
