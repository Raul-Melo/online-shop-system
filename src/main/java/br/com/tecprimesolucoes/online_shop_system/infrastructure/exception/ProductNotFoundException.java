package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(Long productId) {
        super(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_NOT_FOUND",
                "Produto não encontrado: " + productId
        );
    }
}
