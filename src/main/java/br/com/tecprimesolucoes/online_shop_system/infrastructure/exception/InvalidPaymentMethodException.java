package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InvalidPaymentMethodException extends DomainException {
    public InvalidPaymentMethodException() {
        super(
                HttpStatus.BAD_REQUEST,
                "PAYMENT_VALID_REQUIRED",
                "Método de pagamento não é valido."
        );
    }
}
