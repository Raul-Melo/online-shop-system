package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.api;

import br.com.tecprimesolucoes.online_shop_system.application.dto.ProductResponse;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetProductsUseCase;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper.ProductResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Test
    void shouldReturnWrappedProductList() {
        GetProductsUseCase useCase = mock(GetProductsUseCase.class);
        ProductResponseMapper mapper = mock(ProductResponseMapper.class);

        Product domainProduct = Product.restore(1L, "Mouse", "Optico", new BigDecimal("99.90"), 10, "img.png");
        ProductResponse dto = new ProductResponse(1L, "Mouse", "Optico", new BigDecimal("99.90"), 10, "img.png");

        when(useCase.execute()).thenReturn(List.of(domainProduct));
        when(mapper.toResponse(domainProduct)).thenReturn(dto);

        ProductController controller = new ProductController(useCase, mapper);
        List<ProductResponse> response = controller.getProducts();

        assertEquals(List.of(dto), response);
    }
}

