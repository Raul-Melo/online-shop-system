package br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.mapper;

import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.dto.FakeStoreProductResponse;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring")
public interface ExternalProductMapper {

    BigDecimal EXCHANGE_RATE = new BigDecimal("5.00");
    Integer DEFAULT_STOCK = 10;

    default Product toDomain(FakeStoreProductResponse response) {
        return Product.restore(
                response.id(),
                response.title(),
                response.description(),
                convertToBrazilianPrice(response.price()),
                DEFAULT_STOCK,
                response.image()
        );
    }

    default BigDecimal convertToBrazilianPrice(BigDecimal externalPrice) {
        if (externalPrice == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return externalPrice
                .multiply(EXCHANGE_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    default Integer simulateStock() {
        return 10;
    }
}