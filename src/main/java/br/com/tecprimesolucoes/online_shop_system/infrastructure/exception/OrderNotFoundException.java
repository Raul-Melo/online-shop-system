package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(UUID orderId) {
        super(
                HttpStatus.NOT_FOUND,
                "ORDER_NOT_FOUND",
                "Pedido não encontrado: " + orderId
        );
    }
}
