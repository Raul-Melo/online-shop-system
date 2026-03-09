package br.com.tecprimesolucoes.online_shop_system.application.dto;

import java.util.UUID;

public record CreateOrderResponse(
        UUID id,
        String numeroPedido
) {
}