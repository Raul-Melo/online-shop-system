package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiContractsTest {

    @Test
    void shouldKeepDomainExceptionMetadata() {
        DomainException ex = new DomainException(HttpStatus.BAD_REQUEST, "DOM-001", "Regra inválida");

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("DOM-001", ex.getCode());
        assertEquals("Regra inválida", ex.getClientMessage());
    }

    @Test
    void shouldUseExpectedDefaultsForIntegrationException() {
        IntegrationException ex = new IntegrationException();

        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatus());
        assertEquals("EXTERNAL_API_ERROR", ex.getCode());
        assertEquals("Erro ao consumir catálogo externo de produtos.", ex.getClientMessage());
    }

    @Test
    void shouldExposeApiErrorFields() {
        LocalDateTime now = LocalDateTime.now();
        ApiError error = new ApiError(now, 400, "Bad Request", "VALID-001", "campo inválido", "/products");

        assertEquals(now, error.timestamp());
        assertEquals(400, error.status());
        assertEquals("Bad Request", error.error());
        assertEquals("VALID-001", error.code());
        assertEquals("campo inválido", error.message());
        assertEquals("/products", error.path());
    }
}

