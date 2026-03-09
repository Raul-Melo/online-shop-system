package br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client;

import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.IntegrationException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.dto.FakeStoreProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FakeStoreProductClientTest {

    @Test
    void shouldReturnProductsWhenExternalCallSucceeds() {
        RestClient.Builder builder = mock(RestClient.Builder.class);
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl("http://fake")).thenReturn(builder);
        when(builder.build()).thenReturn(restClient);
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri("/products");
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(
                List.of(new FakeStoreProductResponse(1L, "Mouse", new BigDecimal("10.00"), "desc", "cat", "img"))
        );

        FakeStoreProductClient client = new FakeStoreProductClient(builder, "http://fake", "/products");

        List<FakeStoreProductResponse> result = client.getProducts();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
    }

    @Test
    void shouldThrowIntegrationExceptionWhenGetProductsFails() {
        RestClient.Builder builder = mock(RestClient.Builder.class);
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);

        when(builder.baseUrl("http://fake")).thenReturn(builder);
        when(builder.build()).thenReturn(restClient);
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri("/products");
        when(headersSpec.retrieve()).thenThrow(new RestClientException("erro"));

        FakeStoreProductClient client = new FakeStoreProductClient(builder, "http://fake", "/products");

        assertThrows(IntegrationException.class, client::getProducts);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        RestClient.Builder builder = mock(RestClient.Builder.class);
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl("http://fake")).thenReturn(builder);
        when(builder.build()).thenReturn(restClient);
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri("/products/{id}", 99L);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(FakeStoreProductResponse.class)).thenThrow(HttpClientErrorException.NotFound.class);

        FakeStoreProductClient client = new FakeStoreProductClient(builder, "http://fake", "/products");

        Optional<FakeStoreProductResponse> result = client.getProductById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowIntegrationExceptionWhenGetProductByIdFails() {
        RestClient.Builder builder = mock(RestClient.Builder.class);
        RestClient restClient = mock(RestClient.class);
        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(builder.baseUrl("http://fake")).thenReturn(builder);
        when(builder.build()).thenReturn(restClient);
        doReturn(uriSpec).when(restClient).get();
        doReturn(headersSpec).when(uriSpec).uri("/products/{id}", 1L);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(FakeStoreProductResponse.class)).thenThrow(new RestClientException("erro"));

        FakeStoreProductClient client = new FakeStoreProductClient(builder, "http://fake", "/products");

        assertThrows(IntegrationException.class, () -> client.getProductById(1L));
    }
}
