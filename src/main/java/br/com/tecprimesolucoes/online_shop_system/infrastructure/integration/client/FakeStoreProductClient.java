package br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client;

import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.IntegrationException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.dto.FakeStoreProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;

@Component
public class FakeStoreProductClient {

    private final RestClient restClient;
    private final String productsPath;

    public FakeStoreProductClient(RestClient.Builder restClientBuilder,
                                  @Value("${integration.fakestore.base-url}") String baseUrl,
                                  @Value("${integration.fakestore.products-path}") String productsPath) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.productsPath = productsPath;
    }

    public List<FakeStoreProductResponse> getProducts() {

        try {

            List<FakeStoreProductResponse> response = restClient.get()
                    .uri(productsPath)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            return response == null ? List.of() : response;

        } catch (RestClientException ex){
            throw new IntegrationException(ex);
        }
    }

    public Optional<FakeStoreProductResponse> getProductById(Long id) {
        try {
            FakeStoreProductResponse response = restClient.get()
                    .uri(productsPath + "/{id}", id)
                    .retrieve()
                    .body(FakeStoreProductResponse.class);

            return Optional.ofNullable(response);
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        } catch (RestClientException ex) {
            throw new IntegrationException(ex);
        }
    }
}
