package br.com.tecprimesolucoes.online_shop_system.application.dto;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsResponse(
        UUID id,
        String numeroPedido,
        String nome,
        String email,
        String endereco,
        PaymentMethod formaPagamento,
        String status,
        BigDecimal total,
        LocalDateTime criadoEm,
        List<OrderItemDetailsResponse> itens
) {
}