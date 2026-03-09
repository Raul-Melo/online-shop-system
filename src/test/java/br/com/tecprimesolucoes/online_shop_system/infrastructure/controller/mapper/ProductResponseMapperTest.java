package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper;

import br.com.tecprimesolucoes.online_shop_system.application.dto.ProductResponse;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductResponseMapperTest {

    private final ProductResponseMapper mapper = Mappers.getMapper(ProductResponseMapper.class);

    @Test
    void shouldMapDomainProductToResponse() {
        Product product = Product.restore(1L, "Mouse", "Optico", new BigDecimal("99.90"), 10, "img.png");

        ProductResponse response = mapper.toResponse(product);

        assertEquals(1L, response.id());
        assertEquals("Mouse", response.nome());
        assertEquals("Optico", response.descricao());
        assertEquals(new BigDecimal("99.90"), response.preco());
        assertEquals(10, response.estoque());
        assertEquals("img.png", response.imagem());
    }
}

