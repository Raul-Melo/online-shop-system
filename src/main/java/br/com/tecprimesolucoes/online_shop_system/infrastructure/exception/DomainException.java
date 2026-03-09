package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends RuntimeException {

    private final HttpStatus status;
    private final String code;
    private final String clientMessage;

    public DomainException(HttpStatus status, String code, String clientMessage) {
        super(clientMessage);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
    }

    public DomainException(HttpStatus status, String code, String clientMessage, Throwable cause) {
        super(clientMessage, cause);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
    }

    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
    public String getClientMessage() { return clientMessage; }
}
