package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends DomainException {

    public InsufficientStockException(Long productId, Integer requested, Integer available) {
        super(
                HttpStatus.BAD_REQUEST,
                "INSUFFICIENT_STOCK",
                "Estoque insuficiente para o produto " + productId +
                        ". Quantidade solicitada: " + requested +
                        ", disponível: " + available
        );
    }
}
