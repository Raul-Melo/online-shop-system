package br.com.tecprimesolucoes.online_shop_system.application.usecase;

import br.com.tecprimesolucoes.online_shop_system.application.dto.OrderDetailsResponse;

import java.util.UUID;

public interface GetOrderByIdUseCase {
    OrderDetailsResponse execute(UUID orderId);
}