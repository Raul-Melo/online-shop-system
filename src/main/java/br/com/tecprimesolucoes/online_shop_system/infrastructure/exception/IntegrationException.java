package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class IntegrationException extends DomainException {

    public IntegrationException() {
        super(
                HttpStatus.BAD_GATEWAY,
                "EXTERNAL_API_ERROR",
                "Erro ao consumir catálogo externo de produtos."
        );
    }

    public IntegrationException(Throwable cause) {
        super(
                HttpStatus.BAD_GATEWAY,
                "EXTERNAL_API_ERROR",
                "Erro ao consumir catálogo externo de produtos.",
                cause
        );
    }
}