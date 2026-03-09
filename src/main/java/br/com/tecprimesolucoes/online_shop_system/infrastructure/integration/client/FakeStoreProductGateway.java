package br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client;

import br.com.tecprimesolucoes.online_shop_system.domain.gateway.ProductGateway;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.mapper.ExternalProductMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FakeStoreProductGateway implements ProductGateway {

    private final FakeStoreProductClient fakeStoreProductClient;
    private final ExternalProductMapper externalProductMapper;

    public FakeStoreProductGateway(FakeStoreProductClient fakeStoreProductClient,
                                   ExternalProductMapper externalProductMapper) {
        this.fakeStoreProductClient = fakeStoreProductClient;
        this.externalProductMapper = externalProductMapper;
    }

    @Override
    public List<Product> findAll() {
        return fakeStoreProductClient.getProducts().stream()
                .map(externalProductMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return fakeStoreProductClient.getProductById(id)
                .map(externalProductMapper::toDomain);
    }
}
