package br.com.tecprimesolucoes.online_shop_system.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDetailsResponse(
        UUID id,
        Long productId,
        String nomeProduto,
        BigDecimal precoUnitario,
        Integer quantidade,
        BigDecimal subtotal
) {
}