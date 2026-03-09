package br.com.tecprimesolucoes.online_shop_system.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(

        @NotNull(message = "Id do produto é obrigatório.")
        Long productId,

        @NotNull(message = "Quantidade é obrigatória.")
        @Min(value = 1, message = "Quantidade deve ser maior que zero.")
        Integer quantity
) {
}