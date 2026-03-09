package br.com.tecprimesolucoes.online_shop_system.application.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        String imagem
) {
}