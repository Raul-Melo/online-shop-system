package br.com.tecprimesolucoes.online_shop_system.application.usecase;

import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderRequest;
import br.com.tecprimesolucoes.online_shop_system.application.dto.CreateOrderResponse;

public interface CreateOrderUseCase {
    CreateOrderResponse execute(CreateOrderRequest request);
}