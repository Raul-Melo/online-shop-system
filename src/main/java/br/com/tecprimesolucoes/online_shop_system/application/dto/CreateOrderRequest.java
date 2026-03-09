package br.com.tecprimesolucoes.online_shop_system.application.dto;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @NotBlank(message = "Endereço é obrigatório.")
        String endereco,

        @NotNull(message = "Forma de pagamento é obrigatória.")
        PaymentMethod formaPagamento,

        @NotEmpty(message = "Lista de produtos é obrigatória.")
        List<@Valid OrderItemRequest> produtos
) {
}