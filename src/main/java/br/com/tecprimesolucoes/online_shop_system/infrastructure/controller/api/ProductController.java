package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.api;

import br.com.tecprimesolucoes.online_shop_system.application.dto.ProductResponse;
import br.com.tecprimesolucoes.online_shop_system.application.usecase.GetProductsUseCase;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper.ProductResponseMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final GetProductsUseCase getProductsUseCase;
    private final ProductResponseMapper productResponseMapper;

    public ProductController(GetProductsUseCase getProductsUseCase, ProductResponseMapper productResponseMapper) {
        this.getProductsUseCase = getProductsUseCase;
        this.productResponseMapper = productResponseMapper;
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return getProductsUseCase.execute().stream()
                .map(productResponseMapper::toResponse)
                .toList();
    }
}
