package br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.dto;

import java.math.BigDecimal;

public record FakeStoreProductResponse(
        Long id,
        String title,
        BigDecimal price,
        String description,
        String category,
        String image
) {
}
